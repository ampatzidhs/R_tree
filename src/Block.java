import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;

public class Block implements Serializable{
    byte[] bufferSize = new byte[32*1024];
    ArrayList<Record> oneBlock;
    String blockID;
    int whereSaved;

    public void setWhereSaved(int whereSaved) {
        this.whereSaved = whereSaved;
    }

    public int getWhereSaved() {
        return whereSaved;
    }

    public Block() {
        this.oneBlock = new ArrayList<>();
    }

    public Block(ArrayList<Record> oneBlock, String blockID, int whereSaved) {
        this.oneBlock = oneBlock;
        this.blockID = blockID;
        this.whereSaved = whereSaved;
    }

    public Block(String blockID, int whereSaved) {
        this.blockID = blockID;
        this.oneBlock = new ArrayList<>();
        this.whereSaved = whereSaved;
    }

    public void setOneBlock(ArrayList<Record> oneBlock) {
        this.oneBlock = oneBlock;
    }

    public void setBlockID(String blockID) {
        this.blockID = blockID;
    }

    public void printBlock(){
        System.out.println("-------------BLOCK-------------" +  blockID + "*******************************");
        for (Record record:oneBlock){
            System.out.println("RECORD:");
            record.printRecord();
        }
    }


    public static int sizeof(Object obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.flush();
        objectOutputStream.close();
        return byteArrayOutputStream.toByteArray().length;
    }

    public boolean fits(int sumOfBlock ,Record r) throws IOException {
        if(sumOfBlock + sizeof(r) > 32*1024){
            return false;
        }
        return true;
    }

    public byte[] getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(byte[] bufferSize) {
        this.bufferSize = bufferSize;
    }

    public ArrayList<Record> getOneBlock() {
        return oneBlock;
    }

    public String getBlockID() {
        return blockID;
    }

    public static void main(String[] args) throws IOException {
//        Block block = new Block();
//        block.readFromOsmFile();
//        block.writeToFile();
//        block.readFromBinaryFile();
//        //block.printBlock();

    }
}
