import java.io.*;
import java.util.ArrayList;

public class MBR implements Serializable{
    String id;//rect0, rect1, ...
    ArrayList<Double> diastaseisA;///Χαρακτηριστηκά κάτω αριστερά γωνία.
    ArrayList<Double> diastaseisB;///Χαρακτηριστηκά πάνω δεξία γωνία.
    ArrayList<LeafRecords> periexomeno;///Λίστα με ολα τα παιδία του ορθογωνίου ( οταν είναι σημεία).

    String parentID;
    String childID;


    public MBR() {
        id = "";
        parentID = "";
        childID = "";
        diastaseisA = new ArrayList<>();
        diastaseisB = new ArrayList<>();
        periexomeno = new ArrayList<>();
//        toPutToTheTree = new ArrayList<>();
    }

    public void copy(MBR mbr){
        this.id = mbr.getId();
        this.diastaseisA = mbr.getDiastaseisA();
        this.diastaseisB = mbr.getDiastaseisB();
        this.periexomeno = getPeriexomeno();
        this.parentID = mbr.getParentID();
        this.childID = mbr.getChildID();
    }

    public MBR(String id, int level, ArrayList<Double> diastaseisA, ArrayList<Double> diastaseisB, String parentID, String childID, ArrayList<LeafRecords> periexomeno) {
        this.id = id;
        this.diastaseisA = diastaseisA;
        this.diastaseisB = diastaseisB;
        this.parentID = parentID;
        this.childID = childID;
        this.periexomeno = periexomeno;
    }

    public void makeDiastaseisNew(){
        diastaseisA = new ArrayList<>();
        diastaseisB = new ArrayList<>();
    }

    public void createMBRFromNode(Nodes node){
        setDiastaseisA(node.findMins());
        setDiastaseisB(node.findMaxs());
    }

    public Boolean isLeafRect(){
        return !periexomeno.isEmpty();
    }

    public void printRect(){
        System.out.println("Rectangle id::::::::::" + id);
        System.out.println("Parent id: " + parentID);
        for(Double d: diastaseisA){
            System.out.println("A: "+ d);
        }
        for(Double d: diastaseisB){
            System.out.println("B: "+ d);
        }
    }

    public void changeDiastaseisA(int index, double newDiastash)//index είναι ποιο θες να αλλάξεις πχ index=0 θα αλλαξεις το Χ.
    {
        int size=diastaseisA.size();
        ArrayList<Double> tmp = new ArrayList<>();
        for(int i=0;i<size;i++)
        {
            if(i!=index)
            {
                tmp.add(diastaseisA.get(i));
            }
            else
            {
                tmp.add(newDiastash);
            }
        }

        setDiastaseisA(tmp);

    }


    public void changeDiastaseisB(int index, double newDiastash)
    {
        int size = diastaseisB.size();
        ArrayList<Double> tmp = new ArrayList<>();
        for(int i=0;i<size;i++)
        {
            if(i!=index)
            {
                tmp.add(diastaseisB.get(i));
            }
            else
            {
                tmp.add(newDiastash);
            }
        }

        setDiastaseisB(tmp);

    }

    public void setNewDiastaseis(){
        if(!periexomeno.isEmpty()){
            Double minX = periexomeno.get(0).getDiastaseis().get(0);
            Double minY = periexomeno.get(0).getDiastaseis().get(1);
            Double maxX = periexomeno.get(0).getDiastaseis().get(0);
            Double maxY = periexomeno.get(0).getDiastaseis().get(1);

            for(LeafRecords l: periexomeno){
                if(l.getDiastaseis().get(0) < minX){
                    minX = l.getDiastaseis().get(0);
                }
                if(l.getDiastaseis().get(0) > maxX){
                    maxX = l.getDiastaseis().get(0);
                }
                if (l.getDiastaseis().get(1) < minY){
                    minY = l.getDiastaseis().get(1);
                }
                if(l.getDiastaseis().get(1) > maxY){
                    maxY = l.getDiastaseis().get(1);
                }
            }

            ArrayList<Double> mins = new ArrayList<>();
            ArrayList<Double> maxs = new ArrayList<>();

            mins.add(minX);
            mins.add(minY);

            maxs.add(maxX);
            maxs.add(maxY);

            diastaseisA = mins;
            diastaseisB = maxs;
        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<LeafRecords> getPeriexomeno() {
        return periexomeno;
    }

    public void setPeriexomeno(ArrayList<LeafRecords> periexomeno) {
        this.periexomeno = periexomeno;
    }

    public ArrayList<Double> getDiastaseisA() {
        return diastaseisA;
    }

    public void setDiastaseisA(ArrayList<Double> diastaseisA) {
        this.diastaseisA = diastaseisA;
    }

    public ArrayList<Double> getDiastaseisB() {
        return diastaseisB;
    }

    public void setDiastaseisB(ArrayList<Double> diastaseisB) {
        this.diastaseisB = diastaseisB;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getChildID() {
        return childID;
    }

    public void setChildID(String childID) {
        this.childID = childID;
    }

    public static int sizeof(Object obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.flush();
        objectOutputStream.close();
        return byteArrayOutputStream.toByteArray().length;
    }

    //For function fits, we use the percent, which is a number between 0 and 1, 0 being
    //0% and 1 being 100%
    //We use that, so that if we want to create the tree bottom-up, we only fill
    //the rectangles 70%, although if we want to do an insertion we use it 100%
    //So, in the bottom-up approach, we use as percent 0.7 and in the insertion, we use 1
    public boolean fits(int sumOfMBR ,LeafRecords r, double percent) throws IOException {
        double value = 32*1024;
        value = value * percent;
        if(sumOfMBR + sizeof(r) > value){
            return false;
        }
        return true;
    }

    public boolean fits(int sumOfNodes ,MBR r, double percent) throws IOException {
        double value = 32*1024;
        value = value * percent;
        if(sumOfNodes + sizeof(r) > value){
            return false;
        }
        return true;
    }

}
