import java.io.IOException;
import java.util.ArrayList;

public class Delete {
    Insert insertion;//Για να μπορω να χρησιμοποιω συναρτησεις οπως η isInRect
    Nodes deletedFrom;
    Double nodeSize50;
    boolean deleted;

    public Delete(){
        insertion = new Insert();
        deletedFrom = new Nodes();
        nodeSize50 = 16*1024*1.0;
        deleted = false;
    }

    public boolean correctTree(R_Tree rTree) throws IOException {
        if(deletedFrom.getId().equals("")){
            deleted = true;
            return true;
        }

        ///An diagrafhke alla to from oxi adeio
        boolean ok = underTheLimit(rTree);
        if(ok){
            return true;
        }
        ////kalese thn allh;
        combineWithASibling(rTree);
        if (deletedFrom.getId().equals(""))
            return true;

        correctTree(rTree);
        return false;
    }


    public boolean combineWithASibling(R_Tree rTree) throws IOException {
        MBR leftSiblingMom = findLeftSibling(rTree, deletedFrom.parentID);
        Nodes leftSibling  = rTree.findKids(leftSiblingMom);

        boolean combined = combineTwoNodes(rTree,leftSibling);
        if(combined){
            return true;
        }

        MBR rightSiblingMom = findRightSibling(rTree, deletedFrom.getParentID());
        Nodes rightSibling = rTree.findKids(rightSiblingMom);

        combined = combineTwoNodes(rTree, rightSibling);
        if(combined){
            return true;
        }

        Nodes upperNode = new Nodes();
        for (Nodes n : rTree.allNodes){
            for (MBR m: n.allRectangles){
                if(m.getId().equals(deletedFrom.getParentID())){
                    upperNode = n;
                }
            }
        }
        if(Nodes.sizeof(upperNode)<nodeSize50)
            deletedFrom = upperNode;
        else
            deletedFrom = new Nodes();

        if(deletedFrom.getId().equals("")){
            deletedFrom = new Nodes();
            return true;
        }
        return false;
    }


    //Kaleis ean den mporouse to node na parei apo aderfi
    public boolean combineTwoNodes(R_Tree rTree, Nodes leftSibling){
        ArrayList<MBR> toBeAdded = new ArrayList<>();

        if(leftSibling.getId().equals(deletedFrom.getId())&&leftSibling.getId().equals(rTree.root.getId())){
            deletedFrom = new Nodes();
            return true;
        }
        if(!leftSibling.getId().equals("")){
            for (Nodes nodes: rTree.allNodes){
                if(nodes.getId().equals(deletedFrom.getId())){
                    for(MBR mbr:nodes.allRectangles){
                        toBeAdded.add(mbr);
                    }
                }
            }
            for (Nodes nodes: rTree.allNodes){
                if(nodes.getId().equals(leftSibling.getId())){
                    for (MBR m:toBeAdded){
                        m.setParentID(nodes.getParentID());
                        nodes.allRectangles.add(m);
                        System.out.println("nodes parent: " + nodes.getParentID() +  " m parents: " + m.getParentID());
                    }
                }
            }
            System.out.println("r tree all nodes size before : " + rTree.allNodes.size());

            rTree.allNodes.remove(deletedFrom);
            System.out.println("Remove node: " + deletedFrom.getId() + "from Tree");
            System.out.println("r tree all nodes size after : " + rTree.allNodes.size());
            String keepMbrID = deletedFrom.parentID;
            MBR keepMBR = new MBR();
            deletedFrom = new Nodes();
            boolean flag = false;
            for(Nodes n: rTree.allNodes){
                for (MBR m: n.allRectangles){
                    if(m.getId().equals(keepMbrID)){
                        deletedFrom = n;
                        keepMBR = m;
                        flag = true;
                    }
                }
                if (flag){
                    System.out.println("DELETE MBR: " +keepMBR.getId() + "from node: " + n.getId()+"deletedFrom:" + deletedFrom.getId());
                    n.printNodes();
                    n.allRectangles.remove(keepMBR);
                    System.out.println("Remove: " + keepMBR.getId() + "from : " + n.getId()+"deletedFrom:" + deletedFrom.getId());
                    n.printNodes();
                    flag = false;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean deleteLeafMR(R_Tree rTree, Nodes root, LeafRecords leafRecords) throws IOException {
        deleted = false;
        if(root.getAllRectangles().isEmpty()){
            return false;
        }
        if (root.getAllRectangles().get(0).isLeafRect()){
            LeafRecords keep = new LeafRecords("",0.0,0.0);
            boolean flag = false;
            for (MBR mbr:root.allRectangles){
                for (LeafRecords l:mbr.periexomeno){
                    if(l.getDiastaseis().get(0).equals(leafRecords.getDiastaseis().get(0))&&l.getDiastaseis().get(1).equals(leafRecords.getDiastaseis().get(1))){
                        System.out.println("Exists will be deleted");
                        l.printRecord();
                        flag = true;
                        keep = l;
                    }
                }
                if(flag){
                    mbr.periexomeno.remove(keep);
                    if(Nodes.sizeof(root)< nodeSize50){
                        deletedFrom = root;
                    }
                    return true;
                }
            }
            return false;
        }
        ArrayList<MBR> mbrToVisit = new ArrayList<>();
        for (MBR mbr: root.allRectangles){
            if(insertion.isInRect(mbr, leafRecords.getDiastaseis().get(0), leafRecords.getDiastaseis().get(1))){
                mbrToVisit.add(mbr);
            }
        }
        for(MBR mbr:mbrToVisit){
            Nodes kid = rTree.findKids(mbr);
            if(kid.getId().equals("")){
                return false;
            }
            boolean deleted = deleteLeafMR(rTree, kid, leafRecords);
            if(deleted){
                return true;
            }
        }
        return false;
    }

    public boolean underTheLimit(R_Tree rTree) throws IOException
    {
        if(deletedFrom.getId().equals("")){
            return false;
        }

        MBR leftSiblingMom = findLeftSibling(rTree, deletedFrom.parentID);
        MBR rightSiblingMom = findRightSibling(rTree, deletedFrom.parentID);
        System.out.println("Sibling parents: "+ leftSiblingMom.getId() +" - " + rightSiblingMom.getId());

        Nodes leftSibling  = rTree.findKids(leftSiblingMom);
        Nodes rightSibling = rTree.findKids(rightSiblingMom);
        if(Nodes.sizeof(leftSibling)>nodeSize50){
            System.out.println("left sibling: " + leftSibling.getId() + " node: "+ deletedFrom.getId() + "right sibling: " + rightSibling.getId());
            MBR lastMBR = new MBR();
            for (MBR mbr: leftSibling.allRectangles){
                lastMBR = mbr;
            }

            for(Nodes n: rTree.allNodes){
                System.out.println("size: "+n.allRectangles.size());
                if(n.getId().equals(leftSibling.getId())){
                    n.allRectangles.remove(lastMBR);
                    System.out.println("Remove: " + lastMBR.getId() + "from : " + n.getId()+"deletedFrom:" + deletedFrom.getId());
                }
                System.out.println("size after: "+n.allRectangles.size());
            }

            String deletedFromKeepID = deletedFrom.getId();
            for(Nodes n: rTree.allNodes){
                if(n.getId().equals(deletedFromKeepID)){

                    if(!isIn(n, lastMBR)) {
                        lastMBR.parentID = n.getParentID();
                        n.allRectangles.add(lastMBR);
                    }
                    deletedFrom = new Nodes();
                    System.out.println("last mbr: ");
                    lastMBR.printRect();

                    return true;
                }
            }
            deletedFromKeepID = "";
        }
        if(Nodes.sizeof(rightSibling)>nodeSize50){

            System.out.println("mphke 1");
            MBR lastMBR = new MBR();
            for (MBR mbr: rightSibling.allRectangles){
                lastMBR = mbr;
            }

            for(Nodes n: rTree.allNodes){
                if(n.getId().equals(rightSibling.getId())){
                    System.out.println("Remove: " + lastMBR.getId() + "from : " + n.getId()+"deletedFrom:" + deletedFrom.getId());
                    n.allRectangles.remove(lastMBR);
                }
            }

            String deletedFromKeepID = deletedFrom.getId();
            for(Nodes n: rTree.allNodes){
                if(n.getId().equals(deletedFromKeepID)){
                    if(!isIn(n, lastMBR)) {
                        lastMBR.parentID = n.getParentID();
                        n.allRectangles.add(lastMBR);
                    }
                    deletedFrom = new Nodes();
                    return true;
                }
            }
            deletedFromKeepID = "";


        }
        return false;
    }

    public boolean isIn(Nodes nodes, MBR mbr){
        for (MBR m: nodes.allRectangles){
            if (m.getId().equals(mbr.getId()))
                return true;
        }
        return false;
    }

    public MBR findLeftSibling(R_Tree rTree, String parentID){
        MBR keep = new MBR();
        for (Nodes n: rTree.allNodes){
            keep = new MBR();
            for(MBR m:n.allRectangles){
                if(m.getId().equals(parentID)){
                    return keep;
                }
                keep = m;
            }
        }
        return keep;
    }

    public MBR findRightSibling(R_Tree rTree, String parentID){
        MBR keep = new MBR();

        for (Nodes n: rTree.allNodes){
            boolean found = false;
            for(MBR m:n.allRectangles){
                if(found){
                    return m;
                }
                if(m.getId().equals(parentID)){
                    found = true;
                }
            }
        }
        return keep;
    }
}
