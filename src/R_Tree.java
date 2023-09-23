import java.io.*;
import java.util.ArrayList;

public class R_Tree {
    Nodes root;
    ArrayList<Nodes> allNodes;
    int rect_id_count;
    int node_id_count;

    int leafRecordsCount;

    public R_Tree(){
        root = new Nodes();
        allNodes = new ArrayList<>();
        rect_id_count = 0;
        node_id_count = 0;
        leafRecordsCount = 0;
    }

    public R_Tree(Nodes root, ArrayList<Nodes> allNodes, int rect_id_count, int node_id_count) {
        this.root = root;
        this.allNodes = allNodes;
        this.rect_id_count = rect_id_count;
        this.node_id_count = node_id_count;
        leafRecordsCount = 0;
    }

    public void printTree() {
        System.out.println("Root: " + root.getId());
        System.out.println("Rectangle count: " + rect_id_count);
        System.out.println("Node count: " + node_id_count);
        for (Nodes n : allNodes) {
            n.printNodes();
        }
    }


    public void removeMBR(String mbrID) {
        String keepID = "";
        MBR keepMBR = new MBR();
        for (Nodes n : allNodes) {
            for (MBR m : n.allRectangles) {
                if (m.getId().equals(mbrID)) {
                    System.out.println("RE PAIZEI KAI A BGEI TORA");
                    keepID = n.getId();
                    keepMBR = m;
                }
            }
        }
        for (Nodes n : allNodes) {
            if (n.getId().equals(keepID)) {
                n.printNodes();
                n.allRectangles.remove(keepMBR);
                System.out.println("TI ALLO NA KANO DHLADH PXIA");
                n.printNodes();
            }
        }
    }

    public MBR findMBR(Nodes node, String mbrID) {
        for (MBR m : node.getAllRectangles()) {
            if (m.getId().equals(mbrID)) {
                return m;
            }
        }
        return new MBR();
    }

    //////////!!!!!!!!!!!!!!!!!!!!!!!____________________
    //kaleis an xoraei to mbr sto node
    public boolean addMBRToNode(MBR toBeAdded, Nodes node) throws IOException {
        System.out.println("addMBRToNode " + node.getId() + " !!!!!!!!!!!!!!!!!");

        if (node.getId().equals("")) {
            System.out.println("EDO RE FILE RE GAMOTO NODE 15555555555555555");//OUTE AFTO
            System.out.println("::: " + toBeAdded.getId());
        }
        toBeAdded.setParentID(node.getParentID());
        if (toBeAdded.getId().equals("rect50")) {
            System.out.println("NODE ID: " + node.getId());
            System.out.println("rect50 ADDDDDDDDDDDDDD" + " parent: " + toBeAdded.parentID + " node id: " + node.getId());
        }

        for (Nodes n : allNodes) {
            if (n.getId().equals(node.getId())) {
                n.allRectangles.add(toBeAdded);
                return true;
            }
        }
        return false;
    }


    //kaleis an den xoraei to mbr sto node
    public ArrayList<Nodes> breakNode(MBR toBeAdded, Nodes node) {
        System.out.println("breakNode" + node.getId());

        if (node.getId().equals("node16")) {
            System.out.println("EDO RE FILE RE GAMOTO NODE 15555555555555555");//OUTE AFTO
        }


        Nodes first = new Nodes();
//        String id = "node" + node_id_count;
//        node_id_count ++;
        first.setId(node.getId());
        System.out.println("ΦΙΡΣΤ ΣΕΤ ΙΔ: " + first.getId());


        Nodes second = new Nodes();
        String id = "node" + node_id_count;
        node_id_count++;
        second.setId(id);

        System.out.println("σεκοντ ΣΕΤ ΙΔ: " + second.getId());
        int size = node.allRectangles.size();
        int middle = node.allRectangles.size() / 2;
        ArrayList<MBR> forFirst = new ArrayList<>();
        ArrayList<MBR> forSecond = new ArrayList<>();

        for (int i = 0; i < middle; i++) {
            forFirst.add(node.allRectangles.get(i));
        }
        for (int i = middle; i < size - 1; i++) {
            forSecond.add(node.allRectangles.get(i));
        }

        first.setAllRectangles(forFirst);
        second.setAllRectangles(forSecond);

//        first.setParentID(node.getParentID());
//        second.setParentID(node.getParentID());


        if (node.getId().equals("node8")) {
            System.out.println("ANTE NA DOUME VRADIATIKA");
            System.out.println(first.getId() + " + " + second.getId());
            node.printNodes();
            System.out.println("-----------------------");
        }

        second.allRectangles.add(toBeAdded);
        String keepID = "";
        String keepID2giataalla = "";
        MBR keepMBR = new MBR();
        for (Nodes n : allNodes) {
            for (MBR m : n.allRectangles) {
                if (m.getId().equals(node.getParentID())) {
                    System.out.println("RE PAIZEI KAI A BGEI TORA");
                    keepID2giataalla = n.getParentID();
                    keepID = n.getId();
                    keepMBR = m;
                }
            }
        }

        first.setParentID(keepID2giataalla);
        second.setParentID(keepID2giataalla);
        for (MBR m1 : first.allRectangles) {
            m1.setParentID(keepID2giataalla);
        }
        for (MBR m1 : second.allRectangles) {
            m1.setParentID(keepID2giataalla);
        }
        for (Nodes n : allNodes) {
            if (n.getId().equals(keepID)) {
                n.printNodes();
                n.allRectangles.remove(keepMBR);
                System.out.println(first.getParentID());
                System.out.println("TI ALLO NA KANO DHLADH PXIA");
                n.printNodes();
            }
        }

        allNodes.remove(node);
        System.out.println("NODE ID: " + node.getId() + "REMOVEDDDDDDDDDDDDDDDXDDD" + node.getParentID());

        ArrayList<Nodes> twoNodes = new ArrayList<>();
        twoNodes.add(first);
        twoNodes.add(second);

        return twoNodes;
    }


    ///kaleis an xoraei to leafrecord sto mbr
    public void addToTree(LeafRecords newLeaf, MBR mbrToUse) {
        leafRecordsCount++;

        System.out.println("addToTree");
        Insertion insert = new Insertion();
        System.out.println("MBR TO USE PARENT ID: " + mbrToUse.getParentID());
        if (mbrToUse.getId().equals("")) {
            System.out.println("EDO RE FILE RE GAMOTO 2");//OUTE AFTO
        }

        for (Nodes node : allNodes) {
            System.out.println("node: " + node.getId() + ": " + node.getParentID());
            for (MBR mbr : node.allRectangles) {
                System.out.println("mbr: " + mbr.getId());
                if (mbr.getId().equals(mbrToUse.getId())) {
                    System.out.println("HERE YES 11111111111111111111111111");
                    if (mbr.getId().equals("rect42")) {
                        System.out.println("YEESSSSSS BUT IT DOESNT CHANGEEEEEEEE");
                    }
                    System.out.println("MBR PERIEXOMENO BEFORE: " + mbr.periexomeno.size());
                    mbr.periexomeno.add(newLeaf);
                    System.out.println("MBR PERIEXOMENO AFTERRRRR: " + mbr.periexomeno.size());
                    ArrayList<Double> newDiast = insert.newCorners(mbr, newLeaf.getDiastaseis().get(0), newLeaf.getDiastaseis().get(1));

                    ArrayList<Double> allDiastA = new ArrayList<>();
                    allDiastA.add(newDiast.get(0));
                    allDiastA.add(newDiast.get(1));

                    ArrayList<Double> allDiastB = new ArrayList<>();
                    allDiastB.add(newDiast.get(2));
                    allDiastB.add(newDiast.get(3));

                    mbr.setDiastaseisA(allDiastA);
                    mbr.setDiastaseisB(allDiastB);
                }
            }
        }
    }


    //kaleis an den xoraei to leaf record sto mbr
    public ArrayList<MBR> breakLeafMBR(MBR mbr, LeafRecords newLeaf) {
        if (mbr.getId().equals("rect40")) {
            System.out.println("EDO MPAINEI GENIKAA ");
        }
        System.out.println("breakLeafMBR");
        Insertion insert = new Insertion();
        MBR first = new MBR();
        MBR second = new MBR();
//        String id = "rect" + rect_id_count;
//        rect_id_count ++;
        first.setId(mbr.getId());
        String id = "rect" + rect_id_count;
        rect_id_count++;
        second.setId(id);

        int size = mbr.periexomeno.size();
        int middle = size / 2;
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


        String idToPut = insert.chooseRectangle(tmp, newLeaf.getDiastaseis().get(0), newLeaf.getDiastaseis().get(1));
        if (first.getId().equals(idToPut)) {
            first.periexomeno.add(newLeaf);
        } else {
            second.periexomeno.add(newLeaf);
        }

//        String keepID = "";
//        for(Nodes n: allNodes){
//            for(MBR mbr1:n.allRectangles){
//                if(mbr1.getId().equals(first.getParentID())){
//                    keepID = n.getId();
//                }
//            }
//        }
//        for (Nodes n: allNodes){
//            if(n.getId().equals(keepID)){
//                n.allRectangles.remove(mbr);
//                System.out.println("ΓΙΝΕΤΑΙΙΙΙΙΙΙΙΙΙΙΙΙΙΙΙΙΙΙΙΙΙΙΙΙΙΙΙΙΙΙΙΙ");
//            }
//        }
        ArrayList<MBR> twoMBRs = new ArrayList<>();
        twoMBRs.add(first);
        twoMBRs.add(second);

        return twoMBRs;
    }

    public ArrayList<MBR> recursiveUntilIDoItCorrect(Nodes node, Double x, Double y, int savedAt) throws IOException {
        Insertion insert = new Insertion();
        if (node.getAllRectangles().isEmpty()) {
            MBR tmp = new MBR();
            System.out.println("EMPTY NODE");
            tmp.setId("dummy");
            ArrayList<MBR> r = new ArrayList<>();
            r.add(tmp);

            return r;
        }

        if (node.getAllRectangles().get(0).isLeafRect()) {
            System.out.println("isLeafRect");
            String idToPut = insert.chooseRectangle(node, x, y);
            MBR mbrToUse = findMBR(node, idToPut);
            LeafRecords newLeaf = new LeafRecords(mbrToUse.getId(), x, y, savedAt);
            if (mbrToUse.fits(MBR.sizeof(node), newLeaf, 1.0)) {// an xoraei to leaf sto mbr
                addToTree(newLeaf, mbrToUse);


                MBR tmp = new MBR();
                tmp.setId("dummy");
                ArrayList<MBR> r = new ArrayList<>();
                r.add(tmp);

                for (MBR m : r) {
                    System.out.println(m.id + " parent id: " + m.parentID);
                    if (m.getId().equals("rect50")) {
                        System.out.println("rect50 HEREEEEEEEEEEEEEEEEEEEE 405");
                        System.out.println(m.getParentID());
                    }
                }

                return r;
            } else {//an den xoraei to leaf sto mbr
                ArrayList<MBR> twoMBRs = breakLeafMBR(mbrToUse, newLeaf);
////////////////////DING DING DING DING DING DING DING DING DING DING DING DING DING DING
                if (twoMBRs.get(0).getId().equals("rect31") || twoMBRs.get(1).getId().equals("rect31")) {
                    System.out.println("sHERE2");
                    System.out.println("FIRST ");
                    twoMBRs.get(0).printRect();
                    System.out.println("SECOND ");
                    twoMBRs.get(1).printRect();
                    if (mbrToUse.getParentID().equals("rect42")) {
                        System.out.println("DING DING DING DING DING DING DING " + mbrToUse.getId() + " parent " + mbrToUse.getParentID());
                    }
                }

                if (node.getId().equals("node14")) {
                    System.out.println("HERE BROOOOOOOOOOOOOOOOOOOOOOOOO 22324232");
                }
                addMBRToNode(twoMBRs.get(0), node);//to 1o panta xoraei
                Nodes keep = new Nodes();
                for (Nodes n : allNodes) {
                    if (n.getId().equals(node.getId())) {
                        keep = n;
                    }
                }
                if (twoMBRs.get(1).fits(MBR.sizeof(node), twoMBRs.get(1), 1.0)) {///an xoraei to mbr sto node
                    addMBRToNode(twoMBRs.get(1), node);

                    ///gia na to dv kai na ananeoso tis syntetagmenes toy mbr
                    MBR tmp = new MBR();
                    tmp.setId("change");
                    tmp.setChildID(node.getId());
                    ArrayList<MBR> r = new ArrayList<>();
                    r.add(tmp);

                    for (MBR m : r) {
                        System.out.println(m.id + " parent id: " + m.parentID);
                        if (m.getId().equals("rect50")) {
                            System.out.println("rect50 HEREEEEEEEEEEEEEEEEEEEE 429");
                            System.out.println(m.getParentID());
                        }
                    }

                    return r;
                } else {
                    if (twoMBRs.get(0).getId().equals("rect31") || twoMBRs.get(1).getId().equals("rect31")) {
                        System.out.println("sHERE");
                        System.out.println("FIRST ");
                        twoMBRs.get(0).printRect();
                        System.out.println("SECOND ");
                        twoMBRs.get(1).printRect();
                    }
                    ArrayList<Nodes> twoNodes = breakNode(twoMBRs.get(1), node);

                    if (twoNodes.get(0).getId().equals("node19") || twoNodes.get(1).getId().equals("node19")) {
                        System.out.println("HEREEEEEEEEEEEEEEEEE22222------------------");
                        System.out.println("NODE 19 PARENT: " + twoNodes.get(0).getParentID() + "node parent: " + twoNodes.get(1).getParentID());

                        System.out.println("s1");

                    }
//                    allNodes.add(twoNodes.get(0));
//                    allNodes.add(twoNodes.get(1));

                    MBR forTheFirst = new MBR();
//                    forTheFirst.setId(twoNodes.get(0).getParentID());


                    MBR forTheSecond = new MBR();
                    String id = "rect" + rect_id_count;
                    rect_id_count++;
                    forTheSecond.setId(id);

                    //
                    id = "rect" + rect_id_count;
                    rect_id_count++;
                    forTheFirst.setId(id);

//                    forTheFirst.parentID = twoNodes.get(0).getParentID();
//                    forTheSecond.parentID = twoNodes.get(0).getParentID();
////////////////////Λοιπον θα γραψω το προβλημα για να μην το επαναλαβω
                    //Εδω πέρα φιλενάδα, έβαζες λάθος parent id στο parent id το απο πάνω κόμβου ενω έπρεπε να το βαλεις στο ιδ?
//                    forTheFirst.parentID = twoNodes.get(0).getParentID();
//                    forTheSecond.parentID = twoNodes.get(1).getParentID();

                    twoNodes.get(0).setParentID(forTheFirst.getId());
                    for (MBR m : twoNodes.get(0).allRectangles) {
                        m.setParentID(twoNodes.get(0).getParentID());
                    }
                    twoNodes.get(1).setParentID(forTheSecond.getId());
                    for (MBR m : twoNodes.get(1).allRectangles) {
                        m.setParentID(twoNodes.get(1).getParentID());
                    }

                    allNodes.add(twoNodes.get(0));
                    allNodes.add(twoNodes.get(1));

                    forTheFirst.createMBRFromNode(twoNodes.get(0));
                    forTheSecond.createMBRFromNode(twoNodes.get(1));

                    ArrayList<MBR> toRet = new ArrayList<>();
                    toRet.add(forTheFirst);
                    toRet.add(forTheSecond);

                    if (twoNodes.get(0).getId().equals("node14") || twoNodes.get(1).getId().equals("node14")) {
                        System.out.println("HEREEEEEEEEEEEEEEEEE222223------------------");
                        System.out.println("NODE 16 PARENT: " + twoNodes.get(0).getParentID() + "node parent: " + twoNodes.get(1).getParentID());
                        twoNodes.get(0).printNodes();
                        System.out.println("''''''''''''''''''''''''''''''");
                        twoNodes.get(1).printNodes();

                        System.out.println("s2");

                    }


                    for (MBR m : toRet) {
                        System.out.println(m.id + " parent id: " + m.parentID);
                        if (twoNodes.get(0).getId().equals("node19") || twoNodes.get(1).getId().equals("node19")) {
                            System.out.println(m.getParentID() + "node id: " + twoNodes.get(0).getId() + " " + twoNodes.get(1).getId());
                            System.out.println("HEREE33333333333333--------------");
                            forTheFirst.printRect();
                            System.out.println("---s---");
                            forTheSecond.printRect();

                        }
                    }


                    return toRet;

                }

            }
        }
        String idToPut = insert.chooseRectangle(node, x, y);
        Nodes forTheNext = new Nodes();

        for (MBR m : node.getAllRectangles()) {
            if (idToPut.equals(m.getId())) {
                forTheNext = findKids(m);
            }
        }

        ArrayList<MBR> toPut = recursiveUntilIDoItCorrect(forTheNext, x, y, savedAt);

        if (toPut.isEmpty() || toPut.get(0).getId().equals("dummy")) {
            return new ArrayList<>();
        }
        if (toPut.get(0).getId().equals("change")) {
            if (node.getId().equals("node16")) {
                System.out.println("14444444444444444444444444444444444444444444444444");
                System.out.println("CHILD ID: " + toPut.get(0).getChildID());
                node.printNodes();
            }
            String keepID = "";
            for (Nodes n : allNodes) {
                for (MBR m : n.allRectangles) {
                    if (m.getChildID().equals(toPut.get(0).getChildID())) {
                        MBR tmp = new MBR();
                        tmp.createMBRFromNode(n);
                        m.setDiastaseisA(tmp.getDiastaseisA());
                        m.setDiastaseisB(tmp.getDiastaseisB());
                        keepID = m.getId();
                    }
                }
            }
            toPut.get(0).setChildID(keepID);

            if (node.getId().equals("node16")) {
                System.out.println("14444444444444444444444444444444444444444444444444");
                System.out.println("CHILD ID: " + toPut.get(0).getChildID());
                node.printNodes();
            }

            return toPut;
        } else {

            MBR first = toPut.get(0);
            MBR second = toPut.get(1);
            Nodes keep = new Nodes();
            Boolean flag = false;
            for (Nodes n : allNodes) {
                for (MBR mbr : n.getAllRectangles()) {
                    if (mbr.getId().equals(first.getParentID())) {
                        System.out.println("NAI RE POYSTH");
                        keep = n;
                        flag = true;
                    }
                }
            }

            if (keep.getId().equals("")) {
                System.out.println("SOS SOS SOS SOS SOS SOS SOS SOS SOS SOS");
                System.out.println("mbr.getId().equals(first.getParentID()):  " + first.getParentID());
            }
            addMBRToNode(first, keep);//////EDOOOOOOOOOOOOOOOOOOOOOOO !!!!!!!!!!!!!!!
            Nodes keep2 = new Nodes();
            for (Nodes n : allNodes) {
                if (n.getId().equals(keep.getId())) {
                    keep2 = n;
                }
            }

            if (first.fits(MBR.sizeof(keep), second, 1.0)) {
                if (node.getId().equals("node14") || first.getId().equals("node14") || second.getId().equals("node14")) {
                    System.out.println("EDO RE FILE RE GAMOTO NODE 15555555555555555999");//OUTE AFTO
                    System.out.println("Second id: " + second.getId());
                }
                if (second.getId().equals("rect50")) {
                    System.out.println("EDO DEN XERO TI FASH: " + keep2.allRectangles.size());
                }
                addMBRToNode(second, keep);
                //removeMBR();

                MBR tmp = new MBR();
                tmp.setId("change");
                tmp.setChildID(node.getId());
                ArrayList<MBR> r = new ArrayList<>();
                r.add(tmp);

                return r;
            } else {
                ArrayList<Nodes> twoNodes = breakNode(second, keep);
                allNodes.add(twoNodes.get(0));
                allNodes.add(twoNodes.get(1));


                MBR forTheFirst = new MBR();
                forTheFirst.setId(twoNodes.get(0).getParentID());
                MBR forTheSecond = new MBR();
                String id = "rect" + rect_id_count;
                rect_id_count++;
                forTheSecond.setId(id);

                forTheFirst.createMBRFromNode(twoNodes.get(0));
                forTheSecond.createMBRFromNode(twoNodes.get(1));

                ArrayList<MBR> toRet = new ArrayList<>();
                toRet.add(forTheFirst);
                toRet.add(forTheSecond);

                for (MBR m : toPut) {
                    System.out.println(m.id + " parent id: " + m.parentID);
                    if (m.getId().equals("rect50")) {
                        System.out.println("rect50 HEREEEEEEEEEEEEEEEEEEEE 536");
                        System.out.println(m.getParentID());
                    }
                }

                return toRet;
            }
        }
    }

//    public MBR insertRecursive(Nodes node, Double x, Double y) throws IOException {
//        Insertion insert = new Insertion();
//        if(node.getAllRectangles().isEmpty()){
//            MBR tmp = new MBR();
//            tmp.setId("dummy");
//            return tmp;
//        }
//        if(node.getAllRectangles().get(0).isLeafRect()){///////an einai fyllo
//            String idToPut = insert.chooseRectangle(node, x, y);
//            MBR mbrToUse = findMBR(node, idToPut);
//            LeafRecords newLeaf = new LeafRecords(mbrToUse.getId(), x, y);
//            if(mbrToUse.fits(MBR.sizeof(node), newLeaf, 1.0)){/////// an xoraei
//                if(node.getId().equals("node14")){
//                    System.out.println("node14 xoraei mpainei");
//                }
//
//                mbrToUse.periexomeno.add(newLeaf);
//                ArrayList<Double> newDiast = insert.newCorners(mbrToUse, x, y);
//                mbrToUse.makeDiastaseisNew();
//                mbrToUse.diastaseisA.add(newDiast.get(0));
//                mbrToUse.diastaseisA.add(newDiast.get(1));
//                mbrToUse.diastaseisB.add(newDiast.get(2));
//                mbrToUse.diastaseisB.add(newDiast.get(3));
//                for(Nodes nodes: allNodes){
//                    if(nodes.getId().equals(node.getId())){
//                        for(MBR mbr: nodes.allRectangles){
//                            if(mbr.getId().equals(mbrToUse.getId())){
//                                mbr.copy(mbrToUse);
//                            }
//                        }
//                    }
//                }
//
//                MBR dummy = new MBR();
//                dummy.setId("dummy");
//                return dummy;//OTAN DEN EXEI GINEI SPLIT
//            }
//            else{//////////den xoraei
//                if(node.getId().equals("node14")){
//                    System.out.println("node14 den xoraei to fullo mpainei");
//                }
//                System.out.println("TO FYLLOO DEN XORAEIIIIIIIIIIIIIIIIII");
//                MBR toBeAdded = new MBR();
//                String id = "rect" + rect_id_count;
//                rect_id_count ++;
//                toBeAdded.setId(id);
//                int size = mbrToUse.periexomeno.size();
//
//                int middle = size / 2;
//                ArrayList<LeafRecords> forTheFirst = new ArrayList<>();
//                ArrayList<LeafRecords> forTheNew = new ArrayList<>();
//                for(int i=0;i<middle; i++){
//                    forTheFirst.add(mbrToUse.periexomeno.get(i));
//                }
//                for (int i=middle;i<size-1;i++){
//                    forTheNew.add(mbrToUse.periexomeno.get(i));
//                }
//
//                mbrToUse.setPeriexomeno(forTheFirst);
//                toBeAdded.setPeriexomeno(forTheNew);
//
//                mbrToUse.setNewDiastaseis();
//                toBeAdded.setNewDiastaseis();///////////////ta spame se 2 rects
//
//                if(toBeAdded.fits(MBR.sizeof(node), toBeAdded, 1.0)&&node.allRectangles.size()<=4){//////prosthetoume to mbr sto node AN XORAEI
//                    if(node.getId().equals("node14")){
//                        System.out.println("node14 new mbr xoraei mpainei rect count: " + node.allRectangles.size());
//                    }
//                    toBeAdded.setParentID(node.getId());
//                    node.allRectangles.add(toBeAdded);
//
//                    for(Nodes nodes: allNodes){
//                        if(nodes.getId().equals(node.getId())){
//                            for(MBR mbr: nodes.allRectangles){
//                                if(mbr.getId().equals(toBeAdded.getId())){
//                                    mbr.copy(mbrToUse);
//                                }
//                            }
//                        }
//                    }
//
//                    MBR tmp = new MBR();
//                    tmp.setId("change"); /// gia na xero meta na allaxo tis diastaseis tou pano mbr
//                    tmp.setChildID(node.getId());
//                    return tmp;
//                }
//                else{/// an to mbr den xoraei sto
//                    if(node.getId().equals("node14")){
//                        System.out.println("node14 new mbr denn xoraei mpainei rect count: " + node.allRectangles.size());
//                    }
//                    System.out.println("TO MBR DEN XORAEIIIIIIIIIIIIIIIIII");
//                    Nodes nodeToBeAdded = new Nodes();
//                    id = "node" + node_id_count;
//                    node_id_count ++;
//                    nodeToBeAdded.setId(id);
//                    if(id.equals("node19")){
//                        System.out.println("EDO ARXIKOPOIHTHIKEEEEEEEEEEEEEEEEEE");
//                    }
//
//                    size = node.allRectangles.size();
//                    middle = node.allRectangles.size() / 2;
//                    ArrayList<MBR> forFirstNodes = new ArrayList<>();
//                    ArrayList<MBR> mbrToBeAdded = new ArrayList<>();
//                    System.out.println("SIZE" + size);
//                    System.out.println("MIDDLE" + middle);
//                    for(int i=0;i<middle; i++){
//                        forFirstNodes.add(node.allRectangles.get(i));
//                    }
//                    for (int i=middle;i<size-1;i++){
//                        mbrToBeAdded.add(node.allRectangles.get(i));
//                        System.out.println("MPHKE " + node.allRectangles.get(i).getId());
//                    }
//
//
//                    node.setAllRectangles(forFirstNodes);
//                    nodeToBeAdded.setAllRectangles(mbrToBeAdded);
//
//                    for(Nodes nodes: allNodes){
//                        if(nodes.getId().equals(node.getId())){
//                            for(MBR mbr: nodes.allRectangles){
//                                if(mbr.getId().equals(toBeAdded.getId())){
//                                    mbr.copy(mbrToUse);
//                                }
//                            }
//                        }
//                    }
//
//                    MBR mbrToRet = new MBR();
//                    id = "rect" + rect_id_count;
//                    rect_id_count ++;
//                    mbrToRet.setId(id);
//                    mbrToRet.setChildID(nodeToBeAdded.getId());
//                    nodeToBeAdded.setParentID(mbrToRet.getId());
//
//                    mbrToRet.setDiastaseisA(nodeToBeAdded.findMins());
//                    mbrToRet.setDiastaseisB(nodeToBeAdded.findMaxs());
//                    allNodes.add(nodeToBeAdded);
//
//                    return mbrToRet;
//                }
//            }
//        }
//        else {
////            Nodes keepN = new Nodes();
////            keepN.setId(node.getId());
////            keepN.setParentID(node.getParentID());
////            keepN.setAllRectangles(node.getAllRectangles());
//            String idToPut = insert.chooseRectangle(node, x, y);
//            Nodes forTheNext = new Nodes();
//
//            for (MBR m : node.getAllRectangles()) {
//                if (idToPut.equals(m.getId())) {
//                    forTheNext = findKids(m);
//                    System.out.println("IT GETS IN HEREEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
//                    if(m.getChildID().equals("")){
//                        System.out.println("HERE ID=S THE PROBLEMMMMMMMMMMMMMMMMM");
//                    }
//                }
//            }
//
//
//            MBR toPut = insertRecursive(forTheNext, x, y);
//            if(toPut.getId().equals(""))
//            {
//                System.out.println("PROBLEMMMMMMMMMMMMMMMMMMMMMMMMMMM");
//            }
//            if(toPut.getId().equals("dummy")){
//                return toPut;
//            }
//            else if(toPut.getId().equals("change")){
//                String keepID = "";
//                for(Nodes n: allNodes){
//                    if(n.getId().equals(toPut.getChildID())){
//                        keepID = n.getParentID();
//                    }
//                }
//
//                for(Nodes n: allNodes){
//                    for(MBR mbr:n.getAllRectangles()){
//                        if(mbr.getId().equals(keepID)){
//                            mbr.setNewDiastaseis();
//                            toPut.setChildID(n.getId());
//                        }
//                    }
//                }
//                return toPut;
//            }
//
//            if (toPut.fits(MBR.sizeof(node), toPut, 1.0)&&node.allRectangles.size()<=4) {
//                if(node.getId().equals("node14")){
//                    System.out.println("node14 sthn anadromh? new mbr xoraei mpainei rect count: " + node.allRectangles.size());
//                }
//                toPut.setParentID(node.getId());
//                node.allRectangles.add(toPut);
////                            n.allRectangles.add(toPut);
//                MBR tmp = new MBR();
//                tmp.setId("change"); /// gia na xero meta na allaxo tis diastaseis tou pano mbr
//                tmp.setChildID(node.getId());
//
//                return tmp;
//            }
//            else{
//                if(node.getId().equals("node14")){
//                    System.out.println("node14 new mbr den xoraei sthn anadromh mpainei rect count: " + node.allRectangles.size());
//                }
//                System.out.println("TO NODE DEN XORAEIIIIIIIIIIIIIIIIII");
//                Nodes nodeToBeAdded = new Nodes();
//                String id = "node" + node_id_count;
//                node_id_count++;
//
//                if(id.equals("node19")){
//                    System.out.println("EDO ARXIKOPOIHTHIKEEEEEEEEEEEEEEEEEE 22222222");
//                }
//                nodeToBeAdded.setId(id);
//                int size = node.allRectangles.size();
//                int middle = node.allRectangles.size() / 2;
//                ArrayList<MBR> forFirstNodes = new ArrayList<>();
//                ArrayList<MBR> mbrToBeAdded = new ArrayList<>();
//                for (int i = 0; i < middle; i++) {
//                    forFirstNodes.add(node.allRectangles.get(i));
//                }
//                for (int i = middle; i <= size - 1; i++) {
//                    mbrToBeAdded.add(node.allRectangles.get(i));
//                }
//
//                //n.setAllRectangles(forFirstNodes);
//                nodeToBeAdded.setAllRectangles(mbrToBeAdded);
//
//                MBR mbrToRet = new MBR();
//                id = "rect" + rect_id_count;
//                rect_id_count++;
//                mbrToRet.setId(id);
//                mbrToRet.setChildID(nodeToBeAdded.getId());
//                nodeToBeAdded.setParentID(mbrToRet.getId());
//
//                mbrToRet.setDiastaseisA(nodeToBeAdded.findMins());
//                mbrToRet.setDiastaseisB(nodeToBeAdded.findMaxs());
//                allNodes.add(nodeToBeAdded);
//
//                return mbrToRet;
//            }
//        }
//    }

    public Nodes findKids(MBR mbr) {
        Nodes toRet = new Nodes();
        boolean flag = false;
        for (Nodes n : allNodes) {
            if (n.getParentID().equals(mbr.getId())) {
                toRet = n;
                flag = true;
            }
        }
        //System.out.println("FLAGGGGGGGGGGGGG " + flag);
        System.out.println(toRet.getId());
        return toRet;
    }

    public Nodes getRoot() {
        return root;
    }

    public void setRoot(Nodes root) {
        this.root = root;
    }

    public ArrayList<Nodes> getAllNodes() {
        return allNodes;
    }

    public void setAllNodes(ArrayList<Nodes> allNodes) {
        this.allNodes = allNodes;
    }

    public int getRect_id_count() {
        return rect_id_count;
    }

    public void setRect_id_count(int rect_id_count) {
        this.rect_id_count = rect_id_count;
    }

    public int getNode_id_count() {
        return node_id_count;
    }

    public void setNode_id_count(int node_id_count) {
        this.node_id_count = node_id_count;
    }

    public void writeToFile() throws IOException {
        ////////WRITE/////////////////
        FileOutputStream fileOut = new FileOutputStream("indexfile.txt");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);

        out.writeObject(allNodes);

        out.close();
        fileOut.close();
    }

    public ArrayList<Nodes> readFromBinaryFile() throws IOException {
        try (FileInputStream fileIn = new FileInputStream("indexfile.txt");
             BufferedInputStream bufIn = new BufferedInputStream(fileIn, 8 * 1024);
             ObjectInputStream objIn = new ObjectInputStream(bufIn)) {

            while (true) {
                try {
                    ArrayList<Nodes> arrayList = (ArrayList<Nodes>) objIn.readObject();
                    this.allNodes.addAll(arrayList);
                    return allNodes;
                } catch (EOFException e) {
                    break; // End of file reached, exit the loop
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            // Print the data in the ArrayList
            for (Nodes n : allNodes) {
                n.printNodes();
//            }
            }
        }
        return null;
    }
}
