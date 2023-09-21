import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class LeafRecords implements Serializable {
    ArrayList<Double> diastaseis;
    int whereSaved;
    String node;
    String rectangleID;
    Double zOrder;


    public LeafRecords(Record record) {
        this.diastaseis = record.diastaseis;
        this.whereSaved = record.whereSaved;
        this.node = record.nodeID;
        this.rectangleID = "";
        this.zOrder=0.0;
    }

    public LeafRecords(String rectangleID, Double x, Double y) {
        this.diastaseis = new ArrayList<>();
        this.diastaseis.add(x);
        this.diastaseis.add(y);
        this.rectangleID = rectangleID;
        this.whereSaved = -1;
        this.node = "";
        this.zOrder = -1.0;
    }

    public LeafRecords(String rectangleID, Double x, Double y, int savedAt) {
        this.diastaseis = new ArrayList<>();
        this.diastaseis.add(x);
        this.diastaseis.add(y);
        this.rectangleID = rectangleID;
        this.whereSaved = savedAt;
        this.node = "";
        this.zOrder = -1.0;
    }

    public void printRecord(){
        System.out.println("-----------------RECORD IN LEAF-------------------");
        System.out.println("Record saved in block: " + whereSaved);
        System.out.println("Node id: " + node);
        System.out.println("Is in rectangle: " + rectangleID);
        System.out.println(diastaseis);
        System.out.println(" Z-order: "+zOrder);
    }

    public void zOrderLeaf()
    {
        Z_order z = new Z_order();
        ArrayList<String> zDiastaseis = new ArrayList<>();

        for(int i=0;i< diastaseis.size();i++)
        {
            zDiastaseis.add(z.decToBin(diastaseis.get(i)));
        }


        String tmp = z.combine(zDiastaseis.get(0),zDiastaseis.get(1));
        zOrder=z.convertBinaryToDecimal(tmp);

    }


    public String getRectangleID() {
        return rectangleID;
    }

    public void setRectangleID(String rectangleID) {
        this.rectangleID = rectangleID;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public ArrayList<Double> getDiastaseis() {
        return diastaseis;
    }

    public void setDiastaseis(ArrayList<Double> diastaseis) {
        this.diastaseis = diastaseis;
    }

    public int getWhereSaved() {
        return whereSaved;
    }

    public void setWhereSaved(int whereSaved) {
        this.whereSaved = whereSaved;
    }


}
