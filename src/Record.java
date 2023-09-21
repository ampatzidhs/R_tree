import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Record implements Serializable {
    ArrayList<Double> diastaseis;
    int k;
    String name;
    String nodeID;//Afto opos einai
    int whereSaved;

    public Record(){
        this.nodeID = "";
        this.k = 0;
        diastaseis = new ArrayList<>();
        this.name = "";
        this.whereSaved = 0;
    }

    public Record(String id, int k, ArrayList<Double> theList, String name, int whereSaved) {
        this.nodeID = id;
        this.k = k;
        diastaseis = theList;
        this.name = name;
        this.whereSaved = whereSaved;
    }

    public Record(int k, ArrayList<Double> theList, String name, int whereSaved) {
        this.nodeID = "";
        this.k = k;
        diastaseis = theList;
        this.name = name;
        this.whereSaved = whereSaved;
    }

    public void printRecord(){
        System.out.println("-----------------RECORD-------------------");
        System.out.println(this.nodeID);
        System.out.println("Record saved in block: " + whereSaved);
        if(!this.name.equals("")){
            System.out.println(this.name);
        }
            System.out.println(diastaseis);
    }

    public void setWhereSaved(int whereSaved) {
        this.whereSaved = whereSaved;
    }

    public int getWhereSaved() {
        return whereSaved;
    }

    public ArrayList<Double> getDiastaseis() {
        return diastaseis;
    }

    public int getK() {
        return k;
    }

    public String getName() {
        return name;
    }

    public void setDiastaseis(ArrayList<Double> diastaseis) {
        this.diastaseis = diastaseis;
    }

    public void setK(int k) {
        this.k = k;
    }

    public void setName(String name) {
        this.name = name;
    }
}
