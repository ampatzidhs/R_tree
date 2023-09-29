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

public class AllBlocks {
    ArrayList<Block> allBlocks;
    int amountOfBlocks;

    public AllBlocks(){
        allBlocks = new ArrayList<>();
        amountOfBlocks = 0;
    }

    public void readFromOsmFile() throws ParserConfigurationException, IOException, SAXException {
//   File inputFile = new File("C:\\Users\\ampat\\Desktop\\papadopoulos askisi\\map.osm");
        File inputFile = new File("C:\\Users\\zoika\\Downloads\\map.osm (1)\\map.osm");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("node");

        ArrayList<Double> keepData;//ZOI GIA NA KANV TIS KLASEIS
        String keepName="";
        String keepId;
        int k=2;//KAI AFTA DIKA MOY

        Node node = null;

        ArrayList<Record> arrayList = new ArrayList<>();//Gia ta xrhsima data meta
        Block aBlock = new Block("block0", amountOfBlocks);
        amountOfBlocks ++;
        aBlock.setOneBlock(arrayList);
        allBlocks.add(aBlock);
        writeToFile(allBlocks);

        aBlock = new Block("block1", amountOfBlocks);
        int sumOfBlock = 0;
         //nodeList.getLength()
        for (int i = 0; i < nodeList.getLength(); i++){//na to valo gia ola meta
            ArrayList<Object> myArrayList = new ArrayList<Object>();
            keepData = new ArrayList<>();
            keepName = "";

            node = nodeList.item(i);
            String value =  node.getAttributes().getNamedItem("id").getNodeValue();
            keepId = value;
            value =  node.getAttributes().getNamedItem("lat").getNodeValue();
            keepData.add(Double.parseDouble(value));
            value =  node.getAttributes().getNamedItem("lon").getNodeValue();
            keepData.add(Double.parseDouble(value));

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                NodeList tagList = element.getElementsByTagName("tag");
                for (int j = 0; j < tagList.getLength(); j++) {
                    Node tagNode = tagList.item(j);
                    if (tagNode.getAttributes().getNamedItem("k").getNodeValue().equals("name")) {
                        String name = tagNode.getAttributes().getNamedItem("v").getNodeValue();
//                        System.out.println("Tag Key: " + tagNode.getAttributes().getNamedItem("k").getNodeValue() + ", Tag Value: " + tagNode.getAttributes().getNamedItem("v").getNodeValue());
                        myArrayList.add(name);
                        //
                        keepName = name;
                    }
                }
            }

            String key = node.getAttributes().getNamedItem("id").getNodeValue();
            //
            Record record = new Record(keepId, k, keepData, keepName, amountOfBlocks);
            if(aBlock.fits(sumOfBlock, record)){
//                System.out.println("Record added");
//                record.printRecord();
                aBlock.oneBlock.add(record);
                sumOfBlock += sizeof(record);
            }
            else {
                writeToFile(allBlocks);
                amountOfBlocks ++;
                record.setWhereSaved(amountOfBlocks);
                String id = "block" + amountOfBlocks;
//                System.out.println("BLOCK" + amountOfBlocks + "--------------------------------------------------------------");
                aBlock = new Block(id, amountOfBlocks);
                aBlock.oneBlock.add(record);
                allBlocks.add(aBlock);
//                System.out.println("Record added");
//                record.printRecord();
                sumOfBlock = sizeof(record);
            }
        }
//        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    }

    public void writeToFile(ArrayList<Block> b) throws IOException {
        ////////WRITE/////////////////
        FileOutputStream fileOut = new FileOutputStream("datafile.txt");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);

        out.writeObject(b);

        out.close();
        fileOut.close();
    }

    public ArrayList<Block> readFromBinaryFile() {
        try (FileInputStream fileIn = new FileInputStream("datafile.txt");
             BufferedInputStream bufIn = new BufferedInputStream(fileIn, 8 * 1024);
             ObjectInputStream objIn = new ObjectInputStream(bufIn)) {

            while (true) {
                try {
                    ArrayList<Block> arrayList = (ArrayList<Block>) objIn.readObject();
                    this.allBlocks.addAll(arrayList);
                    amountOfBlocks = allBlocks.size();

                    return allBlocks;
                } catch (EOFException e) {
                    break; // End of file reached, exit the loop
                }
            }

            // Print the data in the ArrayList
//            for(Block b:allBlocks){
//                b.printBlock();
//            }//to kano sxolio gia na to emfaniszo apo ta blockrectangles
/////EXO SXOLIO
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int addToDataFile(Double x, Double y) throws IOException {
        ArrayList<Double> data = new ArrayList<>();
        data.add(x);
        data.add(y);
        Record r = new Record(2, data, "", 0);
        int savedAt = 0;
        for (Block b: allBlocks){
            if(b.fits(sizeof(b), r)){
                r.setWhereSaved(amountOfBlocks);
//                System.out.println("Record added");
//                r.printRecord();
                b.oneBlock.add(r);
                savedAt = b.whereSaved;
            }
            else{
                amountOfBlocks ++;
                r.setWhereSaved(amountOfBlocks);
                savedAt = amountOfBlocks;
                String id = "block" + amountOfBlocks;
                Block aBlock = new Block(id, amountOfBlocks);
                aBlock.oneBlock.add(r);
//                System.out.println("Record added");
//                r.printRecord();
            }
        }
        writeToFile(allBlocks);
        return savedAt;
    }

    public void deleteFromDatafile(Double x, Double y) throws IOException {
        Record keep = new Record();
        boolean flag= false;
        for (Block b:allBlocks){
            for(Record r:b.getOneBlock()){
                if(r.diastaseis.get(0).equals(x)&&r.diastaseis.get(1).equals(y)){
                    keep = r;
                    flag = true;
                }
            }
            if(flag){
                b.oneBlock.remove(keep);
                flag = false;
//                System.out.println("Deleted from datafile ");
            }
        }
        writeToFile(allBlocks);
    }

    public static int sizeof(Object obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.flush();
        objectOutputStream.close();
        return byteArrayOutputStream.toByteArray().length;
    }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        AllBlocks allBlocks = new AllBlocks();
        allBlocks.readFromOsmFile();
        ArrayList<Block> returned = allBlocks.readFromBinaryFile();
    }
}
