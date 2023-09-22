import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Main {
    public static void main(String []args) throws ParserConfigurationException, IOException, SAXException {
        AllBlocks allBlocks = new AllBlocks();
//        allBlocks.readFromOsmFile();
        ArrayList<Block> returned = allBlocks.readFromBinaryFile();


        CreateRTree r_tree = new CreateRTree(returned);
        r_tree.createTree();

        System.out.println("**********************************************************");
        MBR anazitisi=new MBR();
        anazitisi.diastaseisA.add(38.3573721);//kato aristera
        anazitisi.diastaseisA.add(21.7877221);

        anazitisi.diastaseisB.add(38.3748695);
        anazitisi.diastaseisB.add(21.8534671);

//        Search ser=new Search(anazitisi);
//        ser.epikalici(r_tree);

        R_Tree actualTree = new R_Tree(r_tree.allNodes.get(0), r_tree.allNodes, r_tree.rect_id_count, r_tree.node_id_count);

        actualTree.printTree();
//        Nodes keep = new Nodes();
//        for(Nodes n: r_tree.getLeafNodes()){
//            keep = n;
//        }
//        System.out.println("KEEP ID: " +keep.getId());
//
//        Delete delete = new Delete();
//        delete.deletedFrom = keep;
//        delete.correctTree(actualTree);
//        actualTree.printTree();
//        Insertion insert = new Insertion();
//        System.out.println(" insert " + insert.chooseRectangle(actualTree.root, 39.0, 22.0));
//        System.out.println("--------------------------------------------------------------------------------");

        Skyline skyline = new Skyline();
        ArrayList<LeafRecords> sky = skyline.createSkyline(actualTree);
        System.out.println("------------------------------SKYLINE-----------------------------------------");
        for (LeafRecords l: sky){
            l.printRecord();
        }
        System.out.println("...............................");
//        actualTree.printTree();
        ArrayList<LeafRecords> help = new ArrayList<>();
        System.out.println("----------------------AFTER------------------------------");
        int counter = 0;
        for (LeafRecords l1:r_tree.allInLeaves){
            boolean notDom = true;
            for(LeafRecords l2: r_tree.allInLeaves){
                if(l1.getNode() != l2.getNode()) {
                    if (skyline.isDominated(l1, l2)) {
                        notDom = false;
                    }
                }
            }
            if(notDom){
                counter ++;
                boolean alreadythere = false;
                for(LeafRecords k: help){
                    if(Objects.equals(k.getNode(), l1.getNode())){
                        alreadythere = true;
                    }
                }
                if(!alreadythere){
                    System.out.println("NOT DOMINATED ID "+ l1.getNode());
                    System.out.println("l1: " + l1.getDiastaseis().get(0) +" "+ l1.getDiastaseis().get(1));
                    help.add(l1);
                }
                boolean exists = false;
                for(LeafRecords l: sky){
                    if(l1.getNode() == l.getNode()){
                        exists = true;
                    }
                }
                if(!exists){
                    System.out.println("HEREEEEEEEE " + l1.getNode() + "NOT WRITTEN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                }


            }

        }
        boolean opposite = false;
        for (LeafRecords l: sky){
            opposite = false;
            for (LeafRecords l2: help){
                if(l.getNode() == l2.getNode()){
                    opposite = true;
                }
            }
            if(!opposite){
                System.out.println("HEREEEEEEEE " + l.getNode() + " NOT WRITTEN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                l.printRecord();
            }
        }
        System.out.println(sky.size());
        System.out.println(help.size());
//
//
//
//
//        System.out.println("Size of MBR " + MBR.sizeof(actualTree.root.allRectangles.get(0)));
//        System.out.println("Size of node: " + Nodes.sizeof(actualTree.root));
//
//        System.out.println("LEAF RECT COUNT: " + actualTree.leafRecordsCount);
//        System.out.println("-----------------PRINT THE FTREEEEEEEEEEEEEEEEEEE------------------");
//        actualTree.printTree();
//
//
//        int c = 0;
//        for (Nodes n: actualTree.allNodes){
//            for (MBR mbr:n.allRectangles){
//                for (LeafRecords leafRecords:mbr.periexomeno){
//                    System.out.println("Saved at: "+leafRecords.whereSaved);
//                    leafRecords.printRecord();
//                    c ++;
//                }
//            }
//        }
//
//        Nodes keepN = new Nodes();
//        int count10 = 0;
//        for (Nodes n:actualTree.allNodes){
//            if(n.getId().equals("node10")){
//                keepN = n;
//                for(MBR mbr:n.allRectangles){
//                    for (LeafRecords l :mbr.periexomeno){
//                        count10 ++;
//                    }
//                }
//            }
//        }
//        System.out.println("::::::::::::::::::::::::::::::::::::::::::");
//        actualTree.printTree();
//        LeafRecords leafRecords = new LeafRecords("", 38.4096078, 21.9065266);
//        //Delete delete = new Delete();
//        boolean dLeaf = delete.deleteLeafMR(actualTree, actualTree.root, leafRecords);
//        allBlocks.deleteFromDatafile(38.4096078, 21.9065266);
//        System.out.println(dLeaf);
//
//
//        int cAfter = 0;
//        for (Nodes n: actualTree.allNodes){
//            for (MBR mbr:n.allRectangles){
//                for (LeafRecords leafRecords1:mbr.periexomeno){
//                    cAfter ++;
//                }
//            }
//        }
//
//        keepN.printNodes();
//        System.out.println("c before: " + c + " c after: "+ cAfter);
//
//        System.out.println(delete.correctTree(actualTree));
//
//        int count11 = 0;
//        for (Nodes n:actualTree.allNodes){
//            if(n.getId().equals("node10")){
//                for(MBR mbr:n.allRectangles){
//                    for (LeafRecords l :mbr.periexomeno){
//                        count11 ++;
//                    }
//                }
//            }
//        }
//        System.out.println(count10 + " ==== " + count11);
//        System.out.println("==============================================");
//        actualTree.printTree();
//
//        int i=0;
//        for (Block block:allBlocks.allBlocks){
//            for (Record record:block.getOneBlock()){
//                LeafRecords l = new LeafRecords(record);
//                i++;
//                delete.deleteLeafMR(actualTree, actualTree.root, l);
//                delete.correctTree(actualTree);
//                if(i==200)
//                    break;
//            }
//        }
//
//        boolean flag = false;
//        for (Nodes n: actualTree.allNodes){
//            if(n.getId().equals("node12")){
//                System.out.println("Node 12 still in, parent: " + n.getParentID());
//                flag = true;
//            }
//        }
//
//        actualTree.printTree();
//        System.out.println("R TREE ALL NODES SIZE: " +actualTree.allNodes.size());
//
//
//        actualTree.writeToFile();
//        actualTree = new R_Tree();
//        System.out.println("TRRRRREEEE NOWWWWWW");
//        actualTree.printTree();
//        actualTree.readFromBinaryFile();
//        System.out.println("TREEE AFTER =====================");
//        actualTree.printTree();
//        System.out.println("R TREE ALL NODES SIZE: " +actualTree.allNodes.size());
//
//
//        System.out.println("-----------------PRINT THE FTREEEEEEEEEEEEEEEEEEE  end------------------");
//        double x= 40.1, y= 23.1;
//        for(int j=0;j<10;j++){
//            int savedAt = allBlocks.addToDataFile(x, y);
//            ArrayList<MBR> saveIt = actualTree.recursiveUntilIDoItCorrect(actualTree.root, x, y, savedAt);
//            for(MBR mbr:saveIt)
//                System.out.println("TI SKATA EPISTREFEI AFTH H MALAKIA: "+ mbr.getId());
//            x+=0.1;
//            y+=0.1;
//        }















//
//        System.out.println("-----------------PRINT THE FTREEEEEEEEEEEEEEEEEEE------------------");
//        actualTree.printTree();
//        System.out.println("LEAF RECT COUNT: " + actualTree.leafRecordsCount);
//







//
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
