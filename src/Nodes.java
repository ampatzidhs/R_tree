import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
/**Κλάση Nodes, αναπαραστά έναν κόμβο του δένδρου που στην ουσία είναι ένα ορθογώνιο που περιέχει αλλα ορθογώνια.
 * MBR που περιέχει αλλα MBR.
 * */
public class Nodes implements Serializable {
    ArrayList<MBR> allRectangles;
    String parentID;
    String id;//node0, node1, ...

    public Nodes(){
        allRectangles = new ArrayList<>();
        parentID = "";
        id = "";
    }

    public Nodes(ArrayList<MBR> allRectangles, String parentID, String id) {
        this.allRectangles = allRectangles;
        this.parentID = parentID;
        this.id = id;
    }

    public ArrayList<Double> getFirstRect(){
        return allRectangles.get(0).getDiastaseisA();
    }

    public ArrayList<Double> getLastRect(){
        int size = allRectangles.size();
        return allRectangles.get(size-1).getDiastaseisB();
    }

    public void printNodes()
    {

        System.out.println("Node----> " + id);
        System.out.println("Parent id: " + parentID);

        for(MBR m: allRectangles)
        {
            m.printRect();
        }

    }

    public ArrayList<Double> findMins()///kato aristera gonia
    {
        if(allRectangles.size()>=1) {
            ArrayList<Double> mins = new ArrayList<>();

            Double minX = 1000000.0;
            Double minY = 1000000.0;


            for (MBR m : allRectangles) {
                if (m.getDiastaseisA().get(0) < minX) {
                    minX = m.getDiastaseisA().get(0);
                }
                if (m.getDiastaseisA().get(1) < minY) {
                    minY = m.getDiastaseisA().get(1);
                }
            }

            mins.add(minX);
            mins.add(minY);

            return mins;
        }
        ArrayList<Double> d = new ArrayList<>();
        d.add(-1.0);
        d.add(-1.0);
        return d;
    }


    public ArrayList<Double> findMaxs()///pano dexia gonia
    {
        if(allRectangles.size()>=1) {
            ArrayList<Double> maxs = new ArrayList<>();

            Double maxX = -1.0;
            Double maxY = -1.0;


            for (MBR m : allRectangles) {
                if (m.getDiastaseisB().get(0) > maxX) {
                    maxX = m.getDiastaseisB().get(0);
                }
                if (m.getDiastaseisB().get(1) > maxY) {
                    maxY = m.getDiastaseisB().get(1);
                }
            }

            maxs.add(maxX);
            maxs.add(maxY);

            return maxs;
        }
        ArrayList<Double> d = new ArrayList<>();
        d.add(-1.0);
        d.add(-1.0);
        return d;
    }


    public ArrayList<MBR> getAllRectangles() {
        return allRectangles;
    }

    public void setAllRectangles(ArrayList<MBR> allRectangles) {
        this.allRectangles = allRectangles;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    public boolean fits(int sumOfBlock , MBR r, double percent) throws IOException {
        double value = 32*1024;
        value = value * percent;
        if(sumOfBlock + sizeof(r) > value){
            return false;
        }
        return true;
    }
}
