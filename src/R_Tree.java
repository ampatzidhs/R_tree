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

    public void printDetails(){
        System.out.println("Root: " + root.getId());
        System.out.println("Nodes: " + allNodes.size());
        System.out.println("Levels: " + findLevels());
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

    public int findLevels(){
        int counter = 1;
        MBR first = getFirstMBR(root);
        Nodes kids = findKids(first);
        MBR kid = getFirstMBR(kids);
        if(first.isLeafRect()){
            return counter;
        }
        counter ++;
        while (!kid.isLeafRect()&&!first.isLeafRect()){
            kids = findKids(kid);
            kid = getFirstMBR(kids);
            counter ++;
        }
        return counter;
    }

    public MBR getFirstMBR(Nodes node){
        if(node.allRectangles.isEmpty()){
            return new MBR();
        }
        return node.allRectangles.get(0);
    }

    public void removeMBR(String mbrID) {
        String keepID = "";
        MBR keepMBR = new MBR();
        for (Nodes n : allNodes) {
            for (MBR m : n.allRectangles) {
                if (m.getId().equals(mbrID)) {
                    keepID = n.getId();
                    keepMBR = m;
                }
            }
        }
        for (Nodes n : allNodes) {
            if (n.getId().equals(keepID)) {
                n.allRectangles.remove(keepMBR);
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
        toBeAdded.setParentID(node.getParentID());

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
        Nodes first = new Nodes();
//        String id = "node" + node_id_count;
//        node_id_count ++;
        first.setId(node.getId());

        Nodes second = new Nodes();
        String id = "node" + node_id_count;
        node_id_count++;
        second.setId(id);

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

        second.allRectangles.add(toBeAdded);
        String keepID = "";
        String keepID2giataalla = "";
        MBR keepMBR = new MBR();
        for (Nodes n : allNodes) {
            for (MBR m : n.allRectangles) {
                if (m.getId().equals(node.getParentID())) {
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
                n.allRectangles.remove(keepMBR);
            }
        }

        allNodes.remove(node);
        ArrayList<Nodes> twoNodes = new ArrayList<>();
        twoNodes.add(first);
        twoNodes.add(second);

        return twoNodes;
    }


    ///kaleis an xoraei to leafrecord sto mbr
    public void addToTree(LeafRecords newLeaf, MBR mbrToUse) {
        leafRecordsCount++;

        Insert insert = new Insert();

        for (Nodes node : allNodes) {
//            System.out.println("node: " + node.getId() + ": " + node.getParentID());
            for (MBR mbr : node.allRectangles) {
//                System.out.println("mbr: " + mbr.getId());
                if (mbr.getId().equals(mbrToUse.getId())) {
//                    System.out.println("MBR PERIEXOMENO BEFORE: " + mbr.periexomeno.size());
                    mbr.periexomeno.add(newLeaf);
//                    System.out.println("MBR PERIEXOMENO AFTERRRRR: " + mbr.periexomeno.size());
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
//        System.out.println("breakLeafMBR");
        Insert insert = new Insert();
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
        Insert insert = new Insert();
        if (node.getAllRectangles().isEmpty()) {
            MBR tmp = new MBR();
//            System.out.println("EMPTY NODE");
            tmp.setId("dummy");
            ArrayList<MBR> r = new ArrayList<>();
            r.add(tmp);

            return r;
        }

        if (node.getAllRectangles().get(0).isLeafRect()) {
//            System.out.println("isLeafRect");
            String idToPut = insert.chooseRectangle(node, x, y);
            MBR mbrToUse = findMBR(node, idToPut);
            LeafRecords newLeaf = new LeafRecords(mbrToUse.getId(), x, y, savedAt);
            if (mbrToUse.fits(MBR.sizeof(node), newLeaf, 1.0)) {// an xoraei to leaf sto mbr
                addToTree(newLeaf, mbrToUse);


                MBR tmp = new MBR();
                tmp.setId("dummy");
                ArrayList<MBR> r = new ArrayList<>();
                r.add(tmp);


                return r;
            } else {//an den xoraei to leaf sto mbr
                ArrayList<MBR> twoMBRs = breakLeafMBR(mbrToUse, newLeaf);

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


                    return r;
                } else {
                    ArrayList<Nodes> twoNodes = breakNode(twoMBRs.get(1), node);
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

            return toPut;
        } else {

            MBR first = toPut.get(0);
            MBR second = toPut.get(1);
            Nodes keep = new Nodes();
            Boolean flag = false;
            for (Nodes n : allNodes) {
                for (MBR mbr : n.getAllRectangles()) {
                    if (mbr.getId().equals(first.getParentID())) {
                        keep = n;
                        flag = true;
                    }
                }
            }

            addMBRToNode(first, keep);//////EDOOOOOOOOOOOOOOOOOOOOOOO !!!!!!!!!!!!!!!
            Nodes keep2 = new Nodes();
            for (Nodes n : allNodes) {
                if (n.getId().equals(keep.getId())) {
                    keep2 = n;
                }
            }

            if (first.fits(MBR.sizeof(keep), second, 1.0)) {
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
        }
        return null;
    }
}
