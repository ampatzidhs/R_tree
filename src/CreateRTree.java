import java.io.*;
import java.util.ArrayList;
//Step one: Read data from datafile (readFromBinaryFile)
// and save them in the arraylist toPutToTheTree (size fileDataSize)  ***DONE***

//Step 2: From ArrayList<Block> transfer all the records in ArrayList<LeafRecords>
//It doesn't matter that they are not going to be in the same spots, we were
//going to change them anyway (recordToRecLeaves) ***DONE***

//Step 3: Perform a z-order to the data  ***DONE***

//Step 4: Put them in bounding rectangles (70% space) ***DONE***

//Step 5: Continue this, until you reach the root ***DONE***

public class CreateRTree {//DIABAZEI TO DATAFILE KAI PERNNAEI TA DEDOMENA SOSTA STO INDEX FILE
    ArrayList<Block> toPutToTheTree;//DATA RIGHT FROM THE DATAFILE/ χύμα τα δεδομένα από το αρχείο
    int fileDataSize;
    ArrayList<LeafRecords> allInLeaves; //NOW YOU HAVE TO ORGANIZE THEM AND /χύμα τα δεδομένα από το αρχείο αλλα σαν φυλλα
    ///// PUT THEM IN BOUNDING RECTANGLES

    ArrayList<LeafRecords> organized;

    ArrayList<MBR> allRectangles; //οργανομένα σε MBR τα δεδομένα από το αρχείο

    ArrayList<Nodes> leafNodes;/// Nodes περιέχει MBR, που το MBR περιέχουν τα σημεία.

    ArrayList<Nodes> allNodes;
    int rect_id_count;
    int node_id_count;



    public CreateRTree(ArrayList<Block> toPutToTheTree) {
        this.toPutToTheTree = toPutToTheTree;
        this.fileDataSize = 0;
        this.allInLeaves = new ArrayList<>();
        this.allRectangles = new ArrayList<>();
        this.organized = new ArrayList<>();
        this.rect_id_count = 0;
        this.node_id_count = 0;
        this.leafNodes = new ArrayList<>();
        this.allNodes = new ArrayList<>();
    }

    public void createTree() throws IOException {
        recordToRecLeaves();
        allInLeaves=sort();
        toRectangles();
        toNodes();
        bottomUp();
        setAllParentIDs();
    }

    public void recordToRecLeaves(){
        int count = 0;
        for(Block block: toPutToTheTree){
            for(Record record: block.getOneBlock()){
                LeafRecords leafRecords = new LeafRecords(record);
                allInLeaves.add(leafRecords);
                if(record.nodeID == 477238000){
                    count ++;
                }
            }
        }
//        if(count == 1){
//            System.out.println("COUNT OKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
//        }
//        else if(count == 0){
//            System.out.println("COUNT 0000000000000000000000000000000000000000000000");
//        }
//        else{
//            System.out.println("COUNT TOOOOO BIGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG" + count);
//        }
    }

    public void printAllLeafRecords(){
        System.out.println("[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[");
        for(LeafRecords leafRecords: allInLeaves){
            leafRecords.printRecord();
        }
    }

    public ArrayList<LeafRecords> sort()
    {
        for(LeafRecords leaf:allInLeaves)
        {
            leaf.zOrderLeaf();
        }

        return mergeSort(allInLeaves);
    }


    public ArrayList<LeafRecords> mergeSort(ArrayList<LeafRecords> arr) {
        if (arr.size() <= 1) {
            return arr;
        }

        int mid = arr.size() / 2;
        ArrayList<LeafRecords> left = new ArrayList<>(arr.subList(0, mid));
        ArrayList<LeafRecords> right = new ArrayList<>(arr.subList(mid, arr.size()));

        mergeSort(left);
        mergeSort(right);

        merge(arr, left, right);
        return arr;
    }

    private void merge(ArrayList<LeafRecords> arr, ArrayList<LeafRecords> left, ArrayList<LeafRecords> right) {
        int i = 0, j = 0, k = 0;

        while (i < left.size() && j < right.size()) {
            if (left.get(i).zOrder <= right.get(j).zOrder) {
                arr.set(k++, left.get(i++));
            } else {
                arr.set(k++, right.get(j++));
            }
        }

        while (i < left.size()) {
            arr.set(k++, left.get(i++));
        }

        while (j < right.size()) {
            arr.set(k++, right.get(j++));
        }

    }


    public boolean isAfxousa(ArrayList<LeafRecords> allLeaves){
        Double min = allLeaves.get(0).zOrder;
        for(LeafRecords l: allLeaves){
            if(l.zOrder < min){
                return false;
            }
            min = l.zOrder;
        }
        return true;
    }


    public void printAllBoundingRect(){
        System.out.println("________________________________________________________________________________");
        for(MBR b: allRectangles){
            b.printRect();
            System.out.println("__________________________ periexomeno  ______________________________________________________");
            for(LeafRecords r:b.getPeriexomeno())
            {
                r.printRecord();
            }
        }
    }

    public void printAllNodes(){
        System.out.println("----------------------------ALL NODES---------------------------");
        for(Nodes n:leafNodes){
            System.out.println("-----"+n.getId()+"-----+++++++++++++++++++++++++");
            for(MBR boundingRectangle:n.allRectangles){
                System.out.println(":::");
                boundingRectangle.printRect();
            }
        }
    }

    public void toRectangles() throws IOException {//παίρνει τα φυλλα και τα βαζει σε ορθογωνια.
        MBR boundingRectangle = new MBR();
        boundingRectangle.setId("rect0");
        rect_id_count = 1;
        int sumOfRect = 0;

        for(Double d: allInLeaves.get(0).getDiastaseis()){
            boundingRectangle.diastaseisA.add(d);
            boundingRectangle.diastaseisB.add(d);
        }





        for(LeafRecords leaf: allInLeaves){
            if(boundingRectangle.fits(sumOfRect, leaf, 0.7)){///note!
                //System.out.println("Record added");
                //leaf.printRecord();


                 if(boundingRectangle.diastaseisA.get(0)>leaf.getDiastaseis().get(0))
                 {
                     boundingRectangle.changeDiastaseisA(0,leaf.getDiastaseis().get(0));
                 }
                if(boundingRectangle.diastaseisA.get(1)>leaf.getDiastaseis().get(1))
                {
                    boundingRectangle.changeDiastaseisA(1,leaf.getDiastaseis().get(1));
                }
                if(boundingRectangle.diastaseisB.get(0)<leaf.getDiastaseis().get(0))
                {
                    boundingRectangle.changeDiastaseisB(0,leaf.getDiastaseis().get(0));
                }
                if(boundingRectangle.diastaseisB.get(1)<leaf.getDiastaseis().get(1))
                {
                    boundingRectangle.changeDiastaseisB(1,leaf.getDiastaseis().get(1));
                }


                boundingRectangle.periexomeno.add(leaf);
                leaf.setRectangleID(boundingRectangle.getId());

                sumOfRect += sizeof(leaf);
            }
            else{
                allRectangles.add(boundingRectangle);
                boundingRectangle = new MBR();
                String sid = "rect" + rect_id_count;
                boundingRectangle.setId(sid);
                rect_id_count ++;
                sumOfRect = 0;
                for(Double d: leaf.getDiastaseis()){
                    boundingRectangle.diastaseisA.add(d);
                    boundingRectangle.diastaseisB.add(d);
                }
                boundingRectangle.periexomeno.add(leaf);
                leaf.setRectangleID(boundingRectangle.getId());
                organized.add(leaf);
                sumOfRect += sizeof(leaf);
            }
        }
        if(sumOfRect != 0){
            allRectangles.add(boundingRectangle);
        }
    }


    //PROSOXH TO ADDED EINAI KAI AXRHSTOO KAI KAKO. TO XRHSIMOPOIV MONO GIA NA BALV TA REKT SE DIAFORETIKA
    //NODES VGALTO
    public void toNodes() throws IOException {///Κάνει(συνχωνευει πολλα mbr) ολα τα boundingRectangles -->Nodes
        int added=0;
        Nodes nodes = new Nodes();
        String sid = "node" + node_id_count;
        nodes.setId(sid);
        node_id_count ++;
        int sumOfNode = 0;
        for(MBR boundingRectangle: allRectangles){
            if(nodes.fits(sumOfNode, boundingRectangle, 0.7)&&added<=2){
                //System.out.println("Bounding added");
                boundingRectangle.printRect();
                nodes.allRectangles.add(boundingRectangle);
                sumOfNode += sizeof(boundingRectangle);
                added++;
            }
            else{
                leafNodes.add(nodes);
                nodes = new Nodes();
                sid = "node" + node_id_count;
                nodes.setId(sid);
                node_id_count ++;
                sumOfNode = 0;
                nodes.allRectangles.add(boundingRectangle);
                sumOfNode += sizeof(boundingRectangle);
                added = 1;
            }
        }
        if(sumOfNode != 0){
            leafNodes.add(nodes);
        }
    }


    public ArrayList<Nodes> bottomUpRecursive(ArrayList<Nodes> input) throws IOException {
        Nodes nodePoyFtiaxno = new Nodes();
        String sid= "node" + node_id_count;
        nodePoyFtiaxno.setId(sid);
        node_id_count++;

        ArrayList<Nodes> upLevel = new ArrayList<>();
        int added=0;
        int sumOfNodes=0;

        if(input.size()==1)
        {
            return new ArrayList<>();
        }

        for(Nodes nodes:input)
        {
            MBR mbrUP=new MBR();
            mbrUP.setDiastaseisA(nodes.findMins());
            mbrUP.setDiastaseisB(nodes.findMaxs());
            sid="rect"+rect_id_count;
            rect_id_count++;
            mbrUP.setId(sid);
            mbrUP.setChildID(nodes.getId());
            nodes.setParentID(mbrUP.getId());

            if(mbrUP.fits(sumOfNodes,mbrUP,0.7) && added<=2)
            {
                //System.out.println("MBR added");
                //mbrUP.printRect();
                nodePoyFtiaxno.allRectangles.add(mbrUP);
                sumOfNodes+=sizeof(mbrUP);
                added++;
            }
            else
            {
                upLevel.add(nodePoyFtiaxno);//βαζω το node που εινα γεματο απο πριν επειδη γεμισε και φτιαχνω ενα καινουργιο
                nodePoyFtiaxno= new Nodes();
                sid= "node" + node_id_count;
                nodePoyFtiaxno.setId(sid);
                node_id_count++;
                sumOfNodes=0;
                nodePoyFtiaxno.allRectangles.add(mbrUP);
                sumOfNodes+=sizeof(mbrUP);
                added=1;
            }


        }
        if(sumOfNodes!=0)//βαζω το τελευταιο node επειδη δεν εχει γεμισε αλλα τελειωσαν αυτα που επρεπε να μπουν
        {
            upLevel.add(nodePoyFtiaxno);
        }

       ArrayList<Nodes> tmp=bottomUpRecursive(upLevel);

        for(Nodes t:tmp)
        {
            allNodes.add(t);
        }
        return upLevel;
    }


    public void bottomUp() throws IOException {
        ArrayList<Nodes> nodes_plus= bottomUpRecursive(leafNodes);

        for(Nodes n:allNodes){
            if(n.getParentID().isEmpty())
            {
                n.setParentID("");
            }
        }

        for(Nodes n : nodes_plus) {
           allNodes.add(n);
        }
        for(Nodes n : leafNodes) {
            allNodes.add(n);
        }
    }

    public void printTree()
    {
        System.out.println("----------------  Tree  -------------------");
        for(Nodes n :allNodes)
        {
            n.printNodes();
        }


    }


    public void setAllParentIDs(){
        for(Nodes n: allNodes){
            for (MBR mbr: n.getAllRectangles()){
                mbr.setParentID(n.getParentID());
            }
        }
    }


    public void writeToIndexFile(ArrayList<Block> b) throws IOException {
        ////////WRITE/////////////////
        FileOutputStream fileOut = new FileOutputStream("indexfile.txt");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);

        out.writeObject(b);

        out.close();
        fileOut.close();
    }


    public static int sizeof(Object obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.flush();
        objectOutputStream.close();
        return byteArrayOutputStream.toByteArray().length;
    }

    public ArrayList<Block> getToPutToTheTree() {
        return toPutToTheTree;
    }

    public void setToPutToTheTree(ArrayList<Block> toPutToTheTree) {
        this.toPutToTheTree = toPutToTheTree;
    }

    public int getFileDataSize() {
        return fileDataSize;
    }

    public void setFileDataSize(int fileDataSize) {
        this.fileDataSize = fileDataSize;
    }

    public ArrayList<LeafRecords> getAllInLeaves() {
        return allInLeaves;
    }

    public void setAllInLeaves(ArrayList<LeafRecords> allInLeaves) {
        this.allInLeaves = allInLeaves;
    }

    public ArrayList<LeafRecords> getOrganized() {
        return organized;
    }

    public void setOrganized(ArrayList<LeafRecords> organized) {
        this.organized = organized;
    }

    public ArrayList<MBR> getAllRectangles() {
        return allRectangles;
    }

    public void setAllRectangles(ArrayList<MBR> allRectangles) {
        this.allRectangles = allRectangles;
    }

    public ArrayList<Nodes> getLeafNodes() {
        return leafNodes;
    }

    public void setLeafNodes(ArrayList<Nodes> leafNodes) {
        this.leafNodes = leafNodes;
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

    public static void main(String []args){
//        Don't use those
//        CreateRTree r_tree = new CreateRTree(new ArrayList<>());
//        r_tree.readFromBinaryFile();
//        r_tree.recordToRecLeaves();
//        r_tree.printAllLeafRecords();
    }
}
