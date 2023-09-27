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

        int leafCount=0;
        for(Nodes n:allNodes){
            for (MBR m:n.allRectangles){
                for(LeafRecords l :m.periexomeno){
                    leafCount++;
                }
            }
        }
        System.out.println("Leaf Count: " + leafCount);
        for (Nodes n:allNodes){
            n.printNodes();
        }
    }

//    public void printTreeMeLevels(Nodes root){
//        if(root.allRectangles.get(0).isLeafRect()){
//            System.out.println("---------------------------------------------");
//            for(MBR m: root.allRectangles){
//                Nodes p = findKids(m);
//                if(!p.getId().equals("")) {
//                    mbr.printNodes();
//                }
//            }
//            return;
//        }
//        System.out.println("---------------------------------------------");
//        ArrayList<Nodes> nextLevel = new ArrayList<>();
//        for(MBR mbr:root.allRectangles){
//
//            Nodes p = findKids(mbr);
//            if(!p.getId().equals("")) {
//                mbr.printNodes();
//                nextLevel.add(p);
//            }
//        }
//
//        for (Nodes n : nextLevel){
//            printTreeMeLevels(n);
//        }
//
//    }


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
        //System.out.println(toRet.getId());
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
