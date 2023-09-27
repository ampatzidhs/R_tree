import org.xml.sax.SAXException;

import javax.media.j3d.Leaf;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
//4:30
public class Main {
    public static void main(String []args) throws ParserConfigurationException, IOException, SAXException {
        //Διάβασμα αρχείου.
        AllBlocks allBlocks = new AllBlocks();
        //allBlocks.readFromOsmFile();
        ArrayList<Block> returned = allBlocks.readFromBinaryFile();

        //Δημιουργία δένδρου.
        CreateRTree r_tree = new CreateRTree(returned);
        r_tree.createTree();
        R_Tree actualTree = new R_Tree(r_tree.allNodes.get(0), r_tree.allNodes, r_tree.rect_id_count, r_tree.node_id_count);
        //actualTree.printTree();




        LeafRecords point=new LeafRecords("rec Knn",38.3744238,21.8528735);
        Knn knn=new Knn(5,actualTree,point);


        ArrayList<LeafRecords> otinanai=knn.kontinotera;

        System.out.println();
        System.out.println("Σημείο από το οποίο ψάχνω τους γείτονες του:");
        point.printRecord();



/**
       System.out.println("---------Αρχικές τίμες του Knn:");
        knn.knnPrint();

*/


        System.out.println();


        long startTime = System.nanoTime();//////START TIME KNN WITH R_TREE
        System.out.println("--------- Knn results -------------------:");

        knn.isTouching(actualTree,actualTree.getRoot().allRectangles);
        knn.isInCircle(actualTree.getRoot());

        //Εμφανίζει το περιεχόμενο του knn.
        //knn.knnPrint();



        System.out.println();
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;//END TIME KNN R_TREE

        System.out.println("Knn-R_Tree-->Running time is: "+totalTime+" nanoseconds");

        Double time = (double) totalTime;
        time=time/1000000000;
        System.out.println("Χρόνος Κnn με χρήση δένδρου: "+time+"sec");









         startTime = System.nanoTime();//////START TIME KNN SEIRIAKA


        ///Σειριακά βρίσκω τους Knn
        for(Nodes n: actualTree.getAllNodes())
        {
            if(n.getAllRectangles().get(0).isLeafRect())
            {
                for(MBR mbr :n.getAllRectangles())
                {
                    for(LeafRecords leaf:mbr.getPeriexomeno())
                    {
//                        if(mbr.getId().equals("rect6"))
//                        {
//                            System.out.println("parent of rect6: "+mbr.getParentID());
//                        }
                        Double apostLeaf=knn.distManhattan(leaf,knn.x);
                        LeafRecords makri=knn.findMax(otinanai,knn.x);
                        Double apostOtinanai=knn.distManhattan(makri,knn.x);
                        if(apostLeaf<apostOtinanai)
                        {
                            if(!knn.otinanaiIsIn(leaf,otinanai))
                            {
                                otinanai.remove(makri);
                                otinanai.add(leaf);
                            }
                        }
                    }
                }
            }
        }


        System.out.println();
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println("Double loop for check up");
        /**
        //Περιεχόμενο του σειριακού Knn
        for(LeafRecords l:otinanai)
        {
            l.printRecord();
            Double apo=knn.distManhattan(l,knn.x);
            System.out.println("----Distance between leaf and Point:"+apo);
        }


        //για επιβεβαίωση εαν είναι ίδια
        int counter=0;
        for(LeafRecords leaf:knn.getKontinotera())
        {
            for(LeafRecords l:otinanai)
            {
                if(Objects.equals(leaf.getDiastaseis().get(0), l.getDiastaseis().get(0)))
                {
                    if(Objects.equals(leaf.getDiastaseis().get(1), l.getDiastaseis().get(1)))
                    {
                        counter++;
                    }
                }

            }
        }

        System.out.println("Counter is: "+counter);
 */

        System.out.println();
        endTime   = System.nanoTime();
        totalTime = endTime - startTime;//END TIME KNN SEIRIAKA

        System.out.println("Running time is: "+totalTime+" nanoseconds");

        time = (double) totalTime;
        time=time/1000000000;
        System.out.println("Χρόνος Κnn με χρήση σειριακής αναζήτησης: "+time+"sec");

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Code for Search.
        System.out.println("<--------------------------------Επόμενο ερώτημα-------------------------------->");


        //Δημιουργία ορθογωνίου για ερώτημα περιοχής
        MBR anazitisi=new MBR();
        anazitisi.setId("rec anazitis");
        anazitisi.diastaseisA.add(51.56172);
        anazitisi.diastaseisA.add(-0.1408816);


        anazitisi.diastaseisB.add(51.5669868);
        anazitisi.diastaseisB.add(-0.1323188);


        Search ser=new Search(actualTree);

        ArrayList<Nodes> dummy=new ArrayList<>();
        dummy.add(actualTree.getRoot());

        startTime = System.nanoTime();//////START TIME Search with R_Tree
        ArrayList<MBR> result=ser.anazit(actualTree,dummy,anazitisi);


        System.out.println("Αποτελέσματα για το ερώτημα περιοχής");
        for(MBR mbr:result)
        {
            mbr.printRect();
        }






        endTime   = System.nanoTime();
        totalTime = endTime - startTime;//END TIME Search with R_Tree

        System.out.println("Running time is: "+totalTime+" nanoseconds");

        time = (double) totalTime;
        time=time/1000000000;
        System.out.println("Χρόνος για ερώτημα περιοχής με χρήση R_Tree: "+time+"sec");



        //Αποτελέσματα σε ερώτημα περιοχής(Σειριακά)

        startTime = System.nanoTime();//////START TIME Search with (Σειριακά)


        ArrayList<MBR> resultsSeiriaka=ser.searchSeiriaka(anazitisi);



        endTime   = System.nanoTime();
        totalTime = endTime - startTime;//END TIME Search with R_Tree

        System.out.println("Running time is: "+totalTime+" nanoseconds");

        time = (double) totalTime;
        time=time/1000000000;
        System.out.println("Χρόνος για ερώτημα περιοχής (Σειριακά): "+time+"sec");


        int  counter=0;
        for(MBR function:result)
        {
          counter++;
        }
        System.out.println("Μέσα στην function υπήρχαν: "+counter+" Mbr");

        counter=0;
        ///Για επιβεβαίωση
        for(MBR function:result)
        {
            for(MBR seiriaka:resultsSeiriaka)
            {
                if(function.diastaseisA.get(0)==seiriaka.diastaseisA.get(0) && function.diastaseisA.get(1)==seiriaka.diastaseisA.get(1) )
                {
                    if(function.diastaseisB.get(0)==seiriaka.diastaseisB.get(0) && function.diastaseisB.get(1)==seiriaka.diastaseisB.get(1))
                    {
                        counter++;
                    }
                }
            }
        }

        System.out.println("τα "+counter+" ηταν ιδια");

    }


}
