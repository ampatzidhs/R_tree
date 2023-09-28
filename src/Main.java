import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Main {
    public static void main(String []args) throws ParserConfigurationException, IOException, SAXException {
        AllBlocks allBlocks = new AllBlocks();
        allBlocks.readFromOsmFile();
        ArrayList<Block> returned = allBlocks.readFromBinaryFile();

        CreateRTree r_tree = new CreateRTree(returned);
        r_tree.createTree();

        R_Tree actualTree = new R_Tree(r_tree.allNodes.get(0), r_tree.allNodes, r_tree.rect_id_count, r_tree.node_id_count);

        Skyline skyline = new Skyline();
        Delete delete = new Delete();
        Insert insert = new Insert();
//
//////// 1
        long startTime = System.nanoTime();
        System.out.println("Χρονος Skyline Seiriaka: ");
        skyline.seiriaka();

        System.out.println();
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;//END TIME KNN R_TREE

        Double time = (double) totalTime;
        time=time/1000000000;
        System.out.println("Χρόνος Skyline seiriaka: "+time+"sec");
        System.out.println("Skyline-->Running time is: "+totalTime+" nanoseconds");


//////// 2
        startTime = System.nanoTime();
        System.out.println("Χρονος Skyline R* Tree: ");
        skyline.createSkyline(actualTree);

        System.out.println();
        endTime   = System.nanoTime();
        totalTime = endTime - startTime;//END TIME KNN R_TREE

        time = (double) totalTime;
        time=time/1000000000;
        System.out.println("Χρόνος Skyline r tree: "+time+"sec");
        System.out.println("Skyline-R_Tree-->Running time is: "+totalTime+" nanoseconds");

//////// 3
        startTime = System.nanoTime();
        System.out.println("Delete Seiriaka otan uparxei to shmeio: ");
        LeafRecords leafRecords = new LeafRecords("delete",38.4096078, 21.9065266);
        boolean dLeaf = delete.deleteLeafMR(actualTree, actualTree.root, leafRecords);
        allBlocks.deleteFromDatafile(38.4096078, 21.9065266);

        System.out.println();
        endTime   = System.nanoTime();
        totalTime = endTime - startTime;//END TIME KNN R_TREE

        time = (double) totalTime;
        time=time/1000000000;
        System.out.println("Χρόνος Delete seiriaka: "+time+"sec");
        System.out.println("Delete-->Running time is: "+totalTime+" nanoseconds");


//////// 4
        startTime = System.nanoTime();
        System.out.println("Delete R TREE otan uparxei to shmeio: ");
        leafRecords = new LeafRecords("delete",38.4096078, 21.9065266);
        dLeaf = delete.deleteLeafMR(actualTree, actualTree.root, leafRecords);
//        allBlocks.deleteFromDatafile(38.4096078, 21.9065266);

        System.out.println();
        endTime   = System.nanoTime();
        totalTime = endTime - startTime;//END TIME KNN R_TREE

        time = (double) totalTime;
        time=time/1000000000;
        System.out.println("Χρόνος Delete seiriaka: "+time+"sec");
        System.out.println("Delete-R_Tree-->Running time is: "+totalTime+" nanoseconds");



//////// 5
        startTime = System.nanoTime();
        System.out.println("Insert όλο το δέντρο: ");
        R_Tree rTree2 = new R_Tree();
        int i=0;
        for(Block b: allBlocks.allBlocks){
            for(Record r: b.oneBlock) {
                LeafRecords leafRecords1 = new LeafRecords(r);
                if (i == 0) {
                    Nodes n = new Nodes();
                    n.setId("node0");
                    MBR mbr = new MBR();
                    mbr.id = "rect0";
                    mbr.diastaseisA.add(r.getDiastaseis().get(0));
                    mbr.diastaseisB.add(r.getDiastaseis().get(0));
                    mbr.diastaseisA.add(r.getDiastaseis().get(1));
                    mbr.diastaseisB.add(r.getDiastaseis().get(1));
                    mbr.periexomeno.add(leafRecords1);
                    n.allRectangles.add(mbr);
                    rTree2.allNodes.add(n);
                }
                else{
                    insert.doTheInsert(rTree2, leafRecords1);
                }
                i++;
            }
        }

        System.out.println();
        endTime   = System.nanoTime();
        totalTime = endTime - startTime;//END TIME KNN R_TREE

        time = (double) totalTime;
        time=time/1000000000;
        System.out.println("Χρόνος Insert όλου του δέντρου: "+time+"sec");
        System.out.println("Insert-->Running time is: "+totalTime+" nanoseconds");


//////// 6
        startTime = System.nanoTime();
        System.out.println("Χρονος Bottom-up: ");
        r_tree = new CreateRTree(returned);
        r_tree.createTree();

        actualTree = new R_Tree(r_tree.allNodes.get(0), r_tree.allNodes, r_tree.rect_id_count, r_tree.node_id_count);


        System.out.println();
        endTime   = System.nanoTime();
        totalTime = endTime - startTime;//END TIME KNN R_TREE

        time = (double) totalTime;
        time=time/1000000000;
        System.out.println("Bottom-up seiriaka: "+time+"sec");
        System.out.println("Bottom-up-R_Tree-->Running time is: "+totalTime+" nanoseconds");














//        System.out.println(sky.size());
//        System.out.println(help.size());







//
//        ArrayList<Block> returned = new ArrayList<>();
//        Block block = new Block(""+0, 0);
//        for(int i=0;i<10;i++){
//            ArrayList<Double> diast = new ArrayList<>();
//            diast.add(i*1.0);
//            diast.add((i+1)*1.0);
//            Record record = new Record(""+i, 2, diast, "name", 0);
//            block.oneBlock.add(record);
//        }
//        returned.add(block);
////
//        CreateRTree r_tree = new CreateRTree(returned);
//        r_tree.createTree();
//
//        System.out.println("HEREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
//        r_tree.printTree();
//
//
//        R_Tree actualTree = new R_Tree(r_tree.allNodes.get(0), r_tree.allNodes, r_tree.rect_id_count, r_tree.node_id_count);
//        System.out.println("===================TREE==========================");
//        actualTree.printTree();
////
//        MBR keepRect = new MBR();
//        boolean flag = false;
//        for(Nodes n: actualTree.allNodes){
//            for (MBR mbr:n.allRectangles){
//                if(!flag) {
//                    keepRect = mbr;
//                    flag = true;
//                }
//            }
//        }
//        System.out.println("Before============================= ");
//        actualTree.printTree();
//        Insert insert = new Insert();
//        double x= 20.1, y= 21.1;
//        for(int j=0;j<90;j++){
//            //int savedAt = allBlocks.addToDataFile(x, y);
//            //ArrayList<MBR> saveIt = actualTree.recursiveUntilIDoItCorrect(actualTree.root, x, y, savedAt);
//            LeafRecords leafRecords = new LeafRecords("2", x, y);
//            insert.doTheInsert(actualTree,leafRecords);
//
////            for(MBR mbr:saveIt)
////                System.out.println("TI SKATA EPISTREFEI AFTH H MALAKIA: "+ mbr.getId());
//            x+=0.1;
//            y+=0.1;
//
////            if(j==4){
////                insert.breakLeafMBR(keepRect, leafRecords, actualTree);
////            }
//        }
//        System.out.println("================TREEE=========================");
//        actualTree.printTree();











//
//        System.out.println("===========================================================");
//        for(int j=0;j<50;j++){
//            System.out.println("GINETAIIIIIIII");
//            //int savedAt = allBlocks.addToDataFile(x, y);
//            //ArrayList<MBR> saveIt = actualTree.recursiveUntilIDoItCorrect(actualTree.root, x, y, savedAt);
//            LeafRecords leafRecords = new LeafRecords(j+"", x, y);
//            insert.doTheInsert(actualTree, leafRecords);
//
//            x+=0.1;
//            y+=0.1;
//
//        }
//        System.out.println("===========================================================");
//        actualTree.printTree();





















//        MBR mbr = new MBR();
//        mbr.id = "ToAdd";
//        mbr.diastaseisA.add(-1.0);
//        mbr.diastaseisA.add(-1.0);
//        mbr.diastaseisB.add(-1.0);
//        mbr.diastaseisB.add(-1.0);
//        Nodes n = new Nodes();
//        for(Nodes nodes: actualTree.allNodes){
//            n = nodes;
//            break;
//
//        insert.correctTree(actualTree);
//        System.out.println("After======================================== ");
//        actualTree.printTree();
//        for (Nodes nodee: actualTree.allNodes){
//            System.out.println(nodee.getId());
//            for (MBR m:nodee.allRectangles){
//                System.out.println("MBR: "+ m.id);
//                for (LeafRecords l:m.periexomeno){
//                    l.printRecord();
//                }
//            }
//        }
        //insert.correctTree(actualTree);





















 //        boolean opposite = false;
 //        for (LeafRecords l: sky){
 //            opposite = false;
 //            for (LeafRecords l2: help){
 //                if(Objects.equals(l.getNode(), l2.getNode())){
 //                    opposite = true;
 //                }
 //            }
 //            if(!opposite){
 //                System.out.println("HEREEEEEEEE " + l.getNode() + " NOT WRITTEN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
 //                l.printRecord();
 //            }
 //        }






















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






/**
 System.out.println("-----------------PRINT THE FTREEEEEEEEEEEEEEEEEEE  end------------------");
 double x= 40.1, y= 23.1;
 for(int j=0;j<1000;j++){
 int savedAt = allBlocks.addToDataFile(x, y);
 ArrayList<MBR> saveIt = actualTree.recursiveUntilIDoItCorrect(actualTree.root, x, y, savedAt);
 for(MBR mbr:saveIt)
 System.out.println("TI SKATA EPISTREFEI AFTH H MALAKIA: "+ mbr.getId());
 x+=0.1;
 y+=0.1;
 }
 actualTree.printTree();

 for(Nodes n: actualTree.allNodes){
 for (MBR mbr:n.allRectangles){
 if(!mbr.getParentID().equals(n.getParentID())){
 System.out.println("Problemmmmmm");
 }
 if(!mbr.isLeafRect()){
 Nodes kids = actualTree.findKids(mbr);
 if(!kids.getParentID().equals(mbr.getId())){
 System.out.println("Other Problemmmmm");
 }
 }
 }
 }
 System.out.println("OK");
 */












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