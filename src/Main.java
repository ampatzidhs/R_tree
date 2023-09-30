import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Main {
    public static void main(String []args) throws ParserConfigurationException, IOException, SAXException {
        int dementions = 2;
        ArrayList<String> dedomena = new ArrayList<>();
        dedomena.add("lat");
        dedomena.add("lon");

        ///ΑΛΛΑΓΗ PATH ΓΙΑ ΝΑ ΔΙΑΒΑΣΕΙ ΤΟ ΑΡΧΕΙΟ
        String osmFile = "C:\\Users\\zoika\\Downloads\\map (3).osm";
        AllBlocks allBlocks = new AllBlocks();
        System.out.println("Διάβασμα από αρχειο:");
        //allBlocks.readFromOsmFile(dementions, osmFile, dedomena);
        ArrayList<Block> returned = allBlocks.readFromBinaryFile();

        CreateRTree r_tree = new CreateRTree(returned);
        r_tree.createTree();

        R_Tree actualTree = new R_Tree(r_tree.allNodes.get(0), r_tree.allNodes, r_tree.rect_id_count, r_tree.node_id_count);
        actualTree.writeToFile();

        Skyline skyline = new Skyline();
        Delete delete = new Delete();
        Insert insert = new Insert();


////////ΚΝΝ
        LeafRecords point=new LeafRecords("rec Knn",38.3744238,21.8528735);
        //LeafRecords point=new LeafRecords("rec Knn",41.4635367,26.5648868);
        Knn knn=new Knn(25,actualTree,point);
        ArrayList<LeafRecords> otinanai=knn.kontinotera;

        System.out.println();
        System.out.println();

        long startTime = System.nanoTime();//////START TIME KNN WITH R_TREE
        System.out.println("--------- Knn results -------------------:");

        knn.isTouching(actualTree,actualTree.getRoot().allRectangles);
        knn.isInCircle(actualTree.getRoot());

        System.out.println();
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;//END TIME KNN R_TREE

        Double time = (double) totalTime;
        time=time/1000000000;
        System.out.println("Χρόνος Κnn με χρήση δένδρου: "+time+"sec");

        ///Σειριακά βρίσκω τους Knn
        startTime = System.nanoTime();//////START TIME KNN SEIRIAKA

        otinanai=knn.knnSeiriaka(otinanai);

        System.out.println();
        endTime   = System.nanoTime();
        totalTime = endTime - startTime;//END TIME KNN SEIRIAKA

        time = (double) totalTime;
        time=time/1000000000;
        System.out.println("Χρόνος Κnn με χρήση σειριακής αναζήτησης: "+time+"sec");

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

/////////////Code for Search.

        System.out.println("<--------------------------------Επόμενο ερώτημα-------------------------------->");


        //Δημιουργία ορθογωνίου για ερώτημα περιοχής
        MBR anazitisi=new MBR();
        anazitisi.setId("rec anazitis");
        anazitisi.diastaseisA.add(38.3573721);
        anazitisi.diastaseisA.add(21.7877221);


        anazitisi.diastaseisB.add(38.3748695);
        anazitisi.diastaseisB.add(21.8534671);


        /** //to diko tou arxeio
         anazitisi.diastaseisA.add(41.5025026);
         anazitisi.diastaseisA.add(26.4715030);//132


         anazitisi.diastaseisB.add(41.5372766);//418
         anazitisi.diastaseisB.add(26.5162474);*/

        /**     //to diko tou arxeio
         anazitisi.diastaseisA.add(41.4479275);
         anazitisi.diastaseisA.add(26.4938293);//6463


         anazitisi.diastaseisB.add(41.4978275);//δικο μου
         anazitisi.diastaseisB.add(26.4999993);*/


        Search ser=new Search(actualTree);

        ArrayList<Nodes> dummy=new ArrayList<>();
        dummy.add(actualTree.getRoot());

        startTime = System.nanoTime();//////START TIME Search with R_Tree
        ArrayList<MBR> mbrTouching=ser.anazit(actualTree,dummy,anazitisi);
        ArrayList<LeafRecords>results=ser.findMbrPeriexomeno(mbrTouching,anazitisi);

        endTime   = System.nanoTime();
        totalTime = endTime - startTime;//END TIME Search with R_Tree



        time = (double) totalTime;
        time=time/1000000000;
        System.out.println("Χρόνος για ερώτημα περιοχής με χρήση R_Tree: "+time+"sec");



        //Αποτελέσματα σε ερώτημα περιοχής(Σειριακά)

        startTime = System.nanoTime();//////START TIME Search with (Σειριακά)


        ArrayList<Record> resultsSeiriaka=ser.searchSeiriaka(anazitisi);



        endTime   = System.nanoTime();
        totalTime = endTime - startTime;//END TIME Search with R_Tree

        time = (double) totalTime;
        time=time/1000000000;
        System.out.println("Χρόνος για ερώτημα περιοχής (Σειριακά): "+time+"sec");


        int c=0;
        for(LeafRecords function:results)
        {
            System.out.println("C: " + c);
            function.printRecord();
            c++;
        }
        System.out.println("Μέσα στο παράθυρο αναζήτησης υπήρχαν: "+c+" σημεία");



        System.out.println("<--------------------------------Επόμενο ερώτημα-------------------------------->");


//////// 1
        startTime = System.nanoTime();
        System.out.println("Skyline: ");
        skyline.seiriaka();

        System.out.println();
        endTime   = System.nanoTime();
        totalTime = endTime - startTime;//END TIME KNN R_TREE

        time = (double) totalTime;
        time=time/1000000000;
        System.out.println("Χρόνος Skyline seiriaka: "+time+"sec");
        System.out.println("Skyline-->Running time is: "+totalTime+" nanoseconds");
        System.out.println();

        System.out.println("<--------------------------------Επόμενο ερώτημα-------------------------------->");


//////// 2
        startTime = System.nanoTime();
        System.out.println("Skyline R* Tree: ");
        skyline.createSkyline(actualTree);

        System.out.println();
        endTime   = System.nanoTime();
        totalTime = endTime - startTime;//END TIME KNN R_TREE

        time = (double) totalTime;
        time=time/1000000000;
        System.out.println("Χρόνος Skyline r* tree: "+time+"sec");
        System.out.println("Skyline-R_Tree-->Running time is: "+totalTime+" nanoseconds");
        System.out.println();

        System.out.println("<--------------------------------Επόμενο ερώτημα-------------------------------->");

//////// 3
        startTime = System.nanoTime();
        System.out.println("Delete Seiriaka: ");
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
        System.out.println();

        System.out.println("<--------------------------------Επόμενο ερώτημα-------------------------------->");


//////// 4
        startTime = System.nanoTime();
        System.out.println("Delete R TREΕ: ");
        leafRecords = new LeafRecords("delete",38.4096078, 21.9065266);
        dLeaf = delete.deleteLeafMR(actualTree, actualTree.root, leafRecords);
//        allBlocks.deleteFromDatafile(38.4096078, 21.9065266);

        System.out.println();
        endTime   = System.nanoTime();
        totalTime = endTime - startTime;//END TIME KNN R_TREE

        time = (double) totalTime;
        time=time/1000000000;
        System.out.println("Χρόνος Delete R*-Tree: "+time+"sec");
        System.out.println("Delete-R_Tree-->Running time is: "+totalTime+" nanoseconds");
        System.out.println();

        System.out.println("<--------------------------------Επόμενο ερώτημα-------------------------------->");


//////// 5
        startTime = System.nanoTime();
        System.out.println("Insert όλο το δέντρο: ");
        R_Tree rTree2 = new R_Tree();
        int j=0;
        for(Block b: allBlocks.allBlocks){
            for(Record r: b.oneBlock) {
                LeafRecords leafRecords1 = new LeafRecords(r);
                if (j == 0) {
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
                j++;
            }
        }

        System.out.println();
        endTime   = System.nanoTime();
        totalTime = endTime - startTime;//END TIME KNN R_TREE

        time = (double) totalTime;
        time=time/1000000000;
        rTree2.printDetails();
        System.out.println("Χρόνος Insert όλου του δέντρου: "+time+"sec");
        System.out.println("Insert-->Running time is: "+totalTime+" nanoseconds");
        System.out.println();


        System.out.println("<--------------------------------Επόμενο ερώτημα-------------------------------->");

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
        System.out.println("Bottom-up: "+time+"sec");
        System.out.println("Bottom-up-R_Tree-->Running time is: "+totalTime+" nanoseconds");
        System.out.println();




    }



}