import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String []args) throws ParserConfigurationException, IOException, SAXException {
        AllBlocks allBlocks = new AllBlocks();
        allBlocks.readFromOsmFile();
        ArrayList<Block> returned = allBlocks.readFromBinaryFile();

        CreateRTree r_tree = new CreateRTree(returned);
        r_tree.createTree();


        R_Tree actualTree = new R_Tree(r_tree.allNodes.get(0), r_tree.allNodes, r_tree.rect_id_count, r_tree.node_id_count);
//      actualTree.printTree();
        System.out.println("**********************************************************");



        MBR anazitisi=new MBR();
        anazitisi.setId("rec Anazitisi -1");
        anazitisi.diastaseisA.add(38.3573721);//kato aristera
        anazitisi.diastaseisA.add(21.7877221);

        anazitisi.diastaseisB.add(38.3748695);//pano de3ia
        anazitisi.diastaseisB.add(21.8534671);

        Search ser=new Search(actualTree);

        ArrayList<Nodes> dummy=new ArrayList<>();
        dummy.add(actualTree.getRoot());

        ArrayList<MBR> result=ser.anazit(actualTree,dummy,anazitisi);

        System.out.println("$$---------final results------------$$");
        for(MBR mbr:result)
        {
            mbr.printRect();
        }



        System.out.println(".................................................");

        for(Nodes nodes: actualTree.getAllNodes())
        {
            System.out.println("...................result......................");
            for(MBR mbr:nodes.getAllRectangles())
            {

                if(mbr.isLeafRect())
                {
                    if (ser.isBetween(mbr, anazitisi))
                    {
                        mbr.printRect();
                    }
                }
            }
        }




        //ser.findLeafRec();


//        r_tree.recordToRecLeaves();
//        for(LeafRecords l: r_tree.allInLeaves){
//            l.printRecord();
//        }
        //r_tree.printAllLeafRecords();
//        r_tree.toRectangles();
        //r_tree.printAllBoundingRect();
//        r_tree.toNodes();
        //System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))");
        //System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))");
//        r_tree.printAllNodes();
        //System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))");
        //r_tree.bottomUp();

//        r_tree.bottomUp();
        //r_tree.printTree();
        //r_tree.printAllNodes();
//        System.out.println("----------------------------  CHECK RECTANGLES  ----------------------------------------------");
//        r_tree.printAllBoundingRect();

//        System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))");
//
//        for(Nodes n: r_tree.leafNodes){
//            Double minX = 10000.0, minY = 10000.0, maxX = 0.0, maxY = 0.0;
//            for(MBR mbr : n.getAllRectangles()){
//                for(LeafRecords l: mbr.getPeriexomeno()){
//                    if(l.getDiastaseis().get(0) < minX){
//                        minX = l.getDiastaseis().get(0);
//                    }
//                    if(l.getDiastaseis().get(1) < minY){
//                        minY = l.getDiastaseis().get(1);
//                    }
//                    if(l.getDiastaseis().get(0) > maxX){
//                        maxX = l.getDiastaseis().get(0);
//                    }
//                    if(l.getDiastaseis().get(1) > maxY){
//                        maxY = l.getDiastaseis().get(1);
//                    }
//                }
//            }
//            System.out.println("==============================Node: "+n.getId());
//            System.out.println("MinX MaxX: " + minX +" - " + maxX);
//            System.out.println("MinY MaxY: " + minY +" - " + maxY);
//        }
//
//        R_Tree actualTree = new R_Tree(r_tree.allNodes.get(0), r_tree.allNodes, r_tree.rect_id_count, r_tree.node_id_count);
//        actualTree.printTree();
//
//        Z_order zOrder = new Z_order();
//        Double dec1 = 2.5, dec2 = 3.6;
//        String bin1 = zOrder.decToBin(dec1);
//        Double newDec1 = zOrder.convertBinaryToDecimal(bin1);
//        System.out.println("DECIMAL: " + dec1 + "\nBINARY: " + bin1 + "\nResult Decimal: " + newDec1);
//        String bin2 = zOrder.decToBin(dec2);
//        Double newDec2 = zOrder.convertBinaryToDecimal(bin2);
//        System.out.println("DECIMAL: " + dec2 + "\nBINARY: " + bin2 + "\nResult Decimal: " + newDec2);
//        System.out.println("CONNECTED: " + zOrder.combine(bin1,bin2));




        // r_tree.printTree();


















//        System.out.println("------------------------------------------------");
//        System.out.println("------------>ALL LEAF RECORDS");
//        r_tree.printAllLeafRecords();
//
//
//        r_tree.allInLeaves=r_tree.sort();
//
//        System.out.println("------------------------------------------------");
//        System.out.println("------------>SORTED");
//        for(LeafRecords t:r_tree.allInLeaves)
//        {
//            t.printRecord();
//        }
//
//        System.out.println(r_tree.isAfxousa(r_tree.allInLeaves));


//        ArrayList<LeafRecords>tmp=r_tree.sort();
//
//        System.out.println("------------------------------------------------");
//        System.out.println("------------>SORTED");
//        for(LeafRecords t:tmp)
//        {
//            t.printRecord();
//        }
//
//        System.out.println(r_tree.isAfxousa(tmp));


    }



}
