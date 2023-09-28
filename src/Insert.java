import java.io.IOException;
import java.util.ArrayList;

public class Insert {
    MBR brokeChildNodeAt;
    Nodes brokeLeafMBRAt;//pou tha mpei to leaf mbr
    MBR addToLeafMBR;//to adddddddd
    Nodes deleteNode;//poio node tha ginei diagrafh, oste na diagrafei sosta o goneas

    //calculates the eukleidian diastance

    public Insert(){
        brokeChildNodeAt = new MBR();
        brokeLeafMBRAt = new Nodes();
        deleteNode = new Nodes();
        addToLeafMBR = new MBR();
    }

    public boolean isIn(Nodes node, MBR mbr){
        for(MBR m: node.allRectangles){
            if(m.getId().equals(mbr.getId())){
                return true;
            }
        }
        return false;
    }

    public void doTheInsert(R_Tree rTree,  LeafRecords leafRecords) throws IOException {
        MBR forTheNext = new MBR();
        for(MBR mbr: rTree.root.allRectangles) {
            Nodes kidss = findKids(mbr, rTree);
            String idToPut = chooseRectangle(kidss, leafRecords.getDiastaseis().get(0), leafRecords.getDiastaseis().get(1));

            for (MBR m : kidss.getAllRectangles()) {
                if (idToPut.equals(m.getId())) {
                    forTheNext = m;
                }
            }
        }

        insertLeafRec(rTree, forTheNext, leafRecords);
        System.out.println("TREEE AFTER INSERT ");
        rTree.printTree();

        boolean flag = false;
        if(!brokeChildNodeAt.getId().equals("")){
            flag = true;
        }
        if(!brokeLeafMBRAt.getId().equals("")){
            flag = true;
        }
        if(!addToLeafMBR.getId().equals("")){
            flag = true;
        }
        if(!deleteNode.getId().equals("")){
            flag = true;
        }
        System.out.println("KAI AFTO GINETAI"+flag);

        while(flag){
            correctTree(rTree);
            flag = false;
            if(!brokeChildNodeAt.getId().equals("")){
                flag = true;
            }
            if(!brokeLeafMBRAt.getId().equals("")){
                flag = true;
            }
            if(!addToLeafMBR.getId().equals("")){
                flag = true;
            }
            if(!deleteNode.getId().equals("")){
                flag = true;
            }
        }
    }

    public void correctTree(R_Tree rTree){
        if(!deleteNode.getId().equals("")){
            deleteN(rTree);
            giaRiza(rTree);
        }
        if(!brokeLeafMBRAt.getId().equals("")){////exei spasei leaf mbr
            if(!addToLeafMBR.getId().equals("")){////xorage sto node
                //////prepei na spaseis to node kai na baleis to mbr
                breakNode(addToLeafMBR, brokeLeafMBRAt, rTree);
            }
            else{
                //////apla tsekare ron gonea gia diastaseis
                correctParDiast(rTree);
            }
            brokeLeafMBRAt = new Nodes();
        }
        if(!brokeChildNodeAt.getId().equals("")){
            makeParent(rTree);
        }

    }

    public void makeParent(R_Tree rTree){
        MBR second = new MBR();
        ArrayList<Nodes> twoNodes = new ArrayList<>();
        for (Nodes n: rTree.allNodes){
            if(n.getParentID().equals(brokeChildNodeAt.getId())){
                twoNodes.add(n);
            }
        }
        if(twoNodes.size()>=2) {
            String id = "rect" + rTree.rect_id_count;
            rTree.rect_id_count++;
            second.setId(id);

            second.setChildID(twoNodes.get(1).getId());
            second.createMBRFromNode(twoNodes.get(1));

            for (Nodes n : rTree.allNodes) {
                boolean flag = false;
                for (MBR m : n.allRectangles) {
                    if (m.getId().equals(brokeChildNodeAt.getId())) {
                        flag = true;
                    }
                }
                if (flag) {
                    if(!isIn(n, second)) {
                        n.allRectangles.add(second);
                    }
                }

                if (n.getId().equals(twoNodes.get(1).getId())) {
                    n.setParentID(second.getId());
                }
            }
        }
        brokeChildNodeAt = new MBR();
    }

    public void giaRiza(R_Tree rTree){
        Nodes root = new Nodes();
        String id = "node" + rTree.node_id_count;
        rTree.node_id_count++;
        root.setId(id);
        ArrayList<Nodes> allFromRoot = new ArrayList<>();

        for(Nodes n: rTree.allNodes){
            if(n.getParentID().equals("")){
                allFromRoot.add(n);
            }
        }

        for(Nodes node: allFromRoot){
            MBR mbr = new MBR();
            id = "rect" + rTree.rect_id_count;
            rTree.rect_id_count++;
            mbr.setId(id);
            mbr.setParentID("");
            mbr.setChildID(node.getId());

            mbr.createMBRFromNode(node);

            if(!isIn(root, mbr)) {
                root.allRectangles.add(mbr);
            }
            for(Nodes n: rTree.allNodes){
                if(n.getId().equals(node.getId())){
                    n.setParentID(mbr.getId());
                    for(MBR mbr1: n.allRectangles){
                        mbr1.setParentID(mbr.getId());
                    }
                }
            }
        }


        root.setParentID("");
        rTree.allNodes.add(root);
        rTree.root = root;
    }

    public void correctParDiast(R_Tree rTree){
        for(Nodes n: rTree.allNodes){
            for (MBR m:n.allRectangles){
                if(m.getId().equals(brokeLeafMBRAt.getParentID())){
                    m.setNewDiastaseis();
                }
            }
            if(!n.getParentID().equals("")){
                brokeLeafMBRAt = n;
            }
            else{
                brokeLeafMBRAt = new Nodes();
            }
        }
    }

    public Double distance(Double x1, Double y1, Double x2, Double y2){
        double c = Math.pow(x2-x1,2) + Math.pow(y2-y1,2);
        return Math.sqrt(c);
    }

    public void deleteN(R_Tree rTree){
        if(!deleteNode.getId().equals("")){
            Nodes keepN= new Nodes();
            for(Nodes n: rTree.allNodes){
                if(deleteNode.getId().equals(n.getId())){
                    keepN = n;
                }
            }
            rTree.allNodes.remove(keepN);
        }
        deleteNode = new Nodes();
    }

    //We have the bottom left corner (x1,y1) and the top right (x2,y2)
    //and we will find the top left and bottom right

    public void insertLeafRec(R_Tree rTree, MBR mbr, LeafRecords leafRecords) throws IOException {
        if(mbr.isLeafRect()){
            boolean flag = false;
            for(Nodes n: rTree.allNodes){
                flag = false;
                for(MBR m: n.allRectangles){
                    if(m.getId().equals(mbr.getId())){
                        flag = true;
                    }
                }
                if(flag){
                    if(mbr.fits(Nodes.sizeof(n), leafRecords, 1.0)){
                        addToTree(leafRecords, mbr, rTree);
                    }
                    else{
                        breakLeafMBR(mbr, leafRecords, rTree);
                    }
                }
            }
            return;
        }

        Nodes kidss = findKids(mbr, rTree);
        String idToPut = chooseRectangle(kidss, leafRecords.getDiastaseis().get(0), leafRecords.getDiastaseis().get(1));

        MBR forTheNext = new MBR();


        for (MBR m : kidss.getAllRectangles()) {
            if (idToPut.equals(m.getId())) {
                forTheNext = m;
            }
        }

        insertLeafRec(rTree, forTheNext, leafRecords);

    }
    public ArrayList<Double> findCorners(Double x1, Double y1, Double x2, Double y2){
        Double topLeftX = x1;
        Double topLeftY = y2;
        Double bottomRightX = x2;
        Double bottomRightY = y1;

        ArrayList<Double> toRet = new ArrayList<>();
        toRet.add(topLeftX);
        toRet.add(topLeftY);
        toRet.add(bottomRightX);
        toRet.add(bottomRightY);

        return toRet;
    }

    public Double perimeter(Double x1, Double y1, Double x2, Double y2){
        ArrayList<Double> otherCorners = findCorners(x1, y1, x2, y2);
        Double dist1 = distance(x1,y1,otherCorners.get(0),otherCorners.get(1));
        Double dist2 = distance(x1,y1,otherCorners.get(2),otherCorners.get(3));

        return 2*dist1 + 2*dist2;
    }

    public Double emvadon(Double x1, Double y1, Double x2, Double y2){
        ArrayList<Double> otherCorners = findCorners(x1, y1, x2, y2);
        Double dist1 = distance(x1,y1,otherCorners.get(0),otherCorners.get(1));
        Double dist2 = distance(x1,y1,otherCorners.get(2),otherCorners.get(3));

        return dist1*dist2;
    }

    public boolean isInRect(MBR mbr, Double x, Double y){
        if(x<mbr.getDiastaseisA().get(0) || x>mbr.getDiastaseisB().get(0)){
            return false;
        }
        if(y<mbr.getDiastaseisA().get(1) || y>mbr.getDiastaseisB().get(1)){
            return false;
        }
        return true;
    }

    public ArrayList<Double> newCorners(MBR mbr, Double x, Double y){
        ArrayList<Double> toRet = new ArrayList<>();
        if(isInRect(mbr,x,y)){
            toRet.add(mbr.getDiastaseisA().get(0));
            toRet.add(mbr.getDiastaseisA().get(1));
            toRet.add(mbr.getDiastaseisB().get(0));
            toRet.add(mbr.getDiastaseisB().get(1));
        }

        if(x<mbr.getDiastaseisA().get(0)){
            toRet.add(x);
        }
        else {
            toRet.add(mbr.getDiastaseisA().get(0));
        }

        if(y<mbr.getDiastaseisA().get(1)){
            toRet.add(y);
        }
        else {
            toRet.add(mbr.getDiastaseisA().get(1));
        }

        if(x>mbr.getDiastaseisB().get(0)){
            toRet.add(x);
        }
        else {
            toRet.add(mbr.getDiastaseisB().get(0));
        }

        if(y>mbr.getDiastaseisB().get(1)){
            toRet.add(y);
        }
        else {
            toRet.add(mbr.getDiastaseisB().get(1));
        }

        return toRet;
    }

    public String chooseRectangle(Nodes nodes, Double x, Double y){
        Double min = 100000000.0;
        String keepMinID = "";
        Double keepMinBefore = 0.0;
        for (MBR mbr:nodes.getAllRectangles()) {
            if (!mbr.getId().equals("")) {


                Double epifaneia = emvadon(mbr.getDiastaseisA().get(0), mbr.getDiastaseisA().get(1), mbr.getDiastaseisB().get(0), mbr.getDiastaseisB().get(1));
                ArrayList<Double> newCo = newCorners(mbr, x, y);
                Double newEpifaneia = emvadon(newCo.get(0), newCo.get(1), newCo.get(2), newCo.get(3));
                Double diafora = newEpifaneia - epifaneia;
                if (diafora < min) {
                    min = diafora;
                    keepMinID = mbr.getId();
                    keepMinBefore = epifaneia;
                } else if (diafora.equals(min)) {
                    if (epifaneia < keepMinBefore) {
                        min = diafora;
                        keepMinID = mbr.getId();
                        keepMinBefore = epifaneia;
                    }
                }
            }
        }
        return keepMinID;
    }

    public void addToTree(LeafRecords newLeaf, MBR mbrToUse, R_Tree rTree) {
        System.out.println("addToTree");

        for (Nodes node : rTree.allNodes) {
            for (MBR mbr : node.allRectangles) {
                if (mbr.getId().equals(mbrToUse.getId())) {
                    System.out.println("MBR PERIEXOMENO BEFORE: " + mbr.periexomeno.size());
                    mbr.periexomeno.add(newLeaf);
                    ArrayList<Double> newDiast = newCorners(mbr, newLeaf.getDiastaseis().get(0), newLeaf.getDiastaseis().get(1));

                    ArrayList<Double> allDiastA = new ArrayList<>();
                    allDiastA.add(newDiast.get(0));
                    allDiastA.add(newDiast.get(1));

                    ArrayList<Double> allDiastB = new ArrayList<>();
                    allDiastB.add(newDiast.get(2));
                    allDiastB.add(newDiast.get(3));

                    mbr.setDiastaseisA(allDiastA);
                    mbr.setDiastaseisB(allDiastB);

                    System.out.println("MBR PERIEXOMENO AFTERRRRR: " + mbr.periexomeno.size());
                }
            }
        }
    }



    public void breakLeafMBR(MBR mbr, LeafRecords newLeaf, R_Tree rTree) throws IOException {
        MBR first = new MBR();
        MBR second = new MBR();

        String id = "rect" + rTree.rect_id_count;
        rTree.rect_id_count++;
        first.setId(id);
        id = "rect" + rTree.rect_id_count;
        rTree.rect_id_count++;
        second.setId(id);

        int size = mbr.periexomeno.size();
        int middle = size / 2;////thimisou to
        ArrayList<LeafRecords> forTheFirst = new ArrayList<>();
        ArrayList<LeafRecords> forTheSecond = new ArrayList<>();

        for (int i = 0; i < middle; i++) {
            forTheFirst.add(mbr.periexomeno.get(i));
        }
        for (int i = middle; i < size - 1; i++) {
            forTheSecond.add(mbr.periexomeno.get(i));
        }

        first.setPeriexomeno(forTheFirst);
        second.setPeriexomeno(forTheSecond);

        first.setParentID(mbr.getParentID());
        second.setParentID(mbr.getParentID());

        first.setNewDiastaseis();
        second.setNewDiastaseis();

        Nodes tmp = new Nodes();
        tmp.allRectangles.add(first);
        tmp.allRectangles.add(second);

        String idToPut = chooseRectangle(tmp, newLeaf.getDiastaseis().get(0), newLeaf.getDiastaseis().get(1));
        if (first.getId().equals(idToPut)) {
            first.periexomeno.add(newLeaf);
        } else {
            second.periexomeno.add(newLeaf);
        }

        first.setNewDiastaseis();
        second.setNewDiastaseis();



        for (Nodes n: rTree.allNodes){
            boolean flag = false;
            for(MBR m: n.allRectangles){
                if(m.getId().equals(mbr.getId())){
                    flag = true;
                }
            }
            if(flag){
                n.allRectangles.remove(mbr);
                if(!isIn(n, first))
                    n.allRectangles.add(first);
                if(n.fits(Nodes.sizeof(n), second, 1.0)){
                    if(!isIn(n, second))
                        n.allRectangles.add(second);
                    addToLeafMBR = new MBR();
                }
                else {
                    addToLeafMBR = second;//krataei to mbr pou prepei na mpei
                    brokeLeafMBRAt = n;//krataei pou tha empaine kanonika to mbr
                }
            }

        }

    }

    public void breakNode(MBR toBeAdded, Nodes node, R_Tree rTree){
        Nodes first = new Nodes();
        String id = "node" + rTree.node_id_count;
        rTree.node_id_count++;
        first.setId(id);

        Nodes second = new Nodes();
        id = "node" + rTree.node_id_count;
        rTree.node_id_count++;
        second.setId(id);

        int size = node.allRectangles.size();
        int middle = node.allRectangles.size() / 2+1;
        ArrayList<MBR> forFirst = new ArrayList<>();
        ArrayList<MBR> forSecond = new ArrayList<>();

        for (int i = 0; i < middle; i++) {
            forFirst.add(node.allRectangles.get(i));
        }
        for (int i = middle+1; i < size - 1; i++) {
            forSecond.add(node.allRectangles.get(i));
        }

        first.setAllRectangles(forFirst);
        second.setAllRectangles(forSecond);

        second.allRectangles.add(toBeAdded);

        String keepID = "";
        String keepParentID = "";
        MBR keepMBR = new MBR();
        for (Nodes n: rTree.allNodes){
            for (MBR mbr:n.allRectangles){
                if (mbr.id.equals(node.getParentID())){
                    keepParentID = n.getParentID();
                    keepID = n.getId();
                    keepMBR = mbr;
                }
            }
        }

        first.setParentID(toBeAdded.getParentID());
        second.setParentID(toBeAdded.getParentID());
        for (MBR mbr:first.allRectangles){
            mbr.setParentID(toBeAdded.getParentID());
        }
        for (MBR mbr:second.allRectangles){
            mbr.setParentID(toBeAdded.getParentID());
        }

        for (Nodes n: rTree.allNodes){
            if(n.getId().equals(node.getId())){
                deleteNode = n;
            }
        }

        rTree.allNodes.add(first);
        rTree.allNodes.add(second);


        brokeChildNodeAt = new MBR();

        for (Nodes n: rTree.allNodes){
            for (MBR mbr: n.allRectangles){
                if(mbr.getId().equals(first.getParentID())){
                    brokeChildNodeAt = mbr;
                }
            }
        }
        if(!brokeChildNodeAt.getParentID().equals("")){
            brokeChildNodeAt = new MBR();
            deleteNode = new Nodes();
        }

        brokeLeafMBRAt = new Nodes();
        addToLeafMBR = new MBR();
    }


    public Nodes findKids(MBR mbr, R_Tree rTree) {
        Nodes toRet = new Nodes();
        boolean flag = false;
        for (Nodes n : rTree.allNodes) {
            if (n.getParentID().equals(mbr.getId())) {
                toRet = n;
                flag = true;
            }
        }
        return toRet;
    }
}
