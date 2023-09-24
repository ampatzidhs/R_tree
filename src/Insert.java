import java.util.ArrayList;

public class Insert {
    MBR brokeChildNodeAt;
    Nodes brokeLeafMBRAt;

    //calculates the eukleidian diastance

    public Insert(){
        brokeChildNodeAt = new MBR();
        brokeLeafMBRAt = new Nodes();
    }
    public Double distance(Double x1, Double y1, Double x2, Double y2){
        double c = Math.pow(x2-x1,2) + Math.pow(y2-y1,2);
        return Math.sqrt(c);
    }

    //We have the bottom left corner (x1,y1) and the top right (x2,y2)
    //and we will find the top left and bottom right
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
        System.out.println("min beggining: " + min);
        String keepMinID = "";
        Double keepMinBefore = 0.0;
        for (MBR mbr:nodes.getAllRectangles()) {
            System.out.println("mbr:: " + mbr.getId());
            if (!mbr.getId().equals("")) {


                Double epifaneia = emvadon(mbr.getDiastaseisA().get(0), mbr.getDiastaseisA().get(1), mbr.getDiastaseisB().get(0), mbr.getDiastaseisB().get(1));
                System.out.println("Emvadon: " + epifaneia);
                ArrayList<Double> newCo = newCorners(mbr, x, y);
                Double newEpifaneia = emvadon(newCo.get(0), newCo.get(1), newCo.get(2), newCo.get(3));
                Double diafora = newEpifaneia - epifaneia;
                System.out.println("Diafora - min: " + diafora + " - " + min);
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
        Insertion insert = new Insertion();
        System.out.println("MBR TO USE PARENT ID: " + mbrToUse.getParentID());

        for (Nodes node : rTree.allNodes) {
            System.out.println("node: " + node.getId() + ": " + node.getParentID());
            for (MBR mbr : node.allRectangles) {
                System.out.println("mbr: " + mbr.getId());
                if (mbr.getId().equals(mbrToUse.getId())) {
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

    public ArrayList<MBR> breakLeafMBR(MBR mbr, LeafRecords newLeaf, R_Tree rTree){
        System.out.println("breakLeafMBR");
        MBR first = new MBR();
        MBR second = new MBR();

        first.setId(mbr.getId());
        String id = "rect" + rTree.rect_id_count;
        rTree.rect_id_count++;
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

        String idToPut = chooseRectangle(tmp, newLeaf.getDiastaseis().get(0), newLeaf.getDiastaseis().get(1));
        if (first.getId().equals(idToPut)) {
            first.periexomeno.add(newLeaf);
        } else {
            second.periexomeno.add(newLeaf);
        }

        first.setNewDiastaseis();
        second.setNewDiastaseis();

        ArrayList<MBR> twoMBRs = new ArrayList<>();
        twoMBRs.add(first);
        twoMBRs.add(second);

        for (Nodes n: rTree.allNodes){
            boolean flag = false;
            for(MBR m: n.allRectangles){
                if(m.getId().equals(mbr.getId())){
                    flag = true;
                }
            }
            if(flag){
                n.allRectangles.remove(mbr);
                n.allRectangles.add(first);
                n.allRectangles.add(second);
                brokeLeafMBRAt = n;
            }

        }

        return twoMBRs;
    }

    public void breakNode(MBR toBeAdded, Nodes node, R_Tree rTree){
        Nodes first = new Nodes();
        first.setId(node.getId());

        Nodes second = new Nodes();
        String id = "node" + rTree.node_id_count;
        rTree.node_id_count++;
        second.setId(id);

        int size = node.allRectangles.size();
        int middle = node.allRectangles.size() / 2+1;
        ArrayList<MBR> forFirst = new ArrayList<>();
        ArrayList<MBR> forSecond = new ArrayList<>();
        System.out.println("MIDDLE: " + middle);

        for (int i = 0; i < middle; i++) {
            forFirst.add(node.allRectangles.get(i));
        }
        for (int i = middle; i < size - 1; i++) {
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

        first.setParentID(keepParentID);
        second.setParentID(keepParentID);
        for (MBR mbr:first.allRectangles){
            mbr.setParentID(keepParentID);
        }
        for (MBR mbr:second.allRectangles){
            mbr.setParentID(keepParentID);
        }

        ///Den exo diagrapsei ton gonea

        rTree.allNodes.add(first);
        rTree.allNodes.add(second);

        for (Nodes n: rTree.allNodes){
            for (MBR mbr: n.allRectangles){
                if(mbr.getId().equals(first.getParentID())){
                    brokeChildNodeAt = mbr;
                }
            }
        }
    }
}
