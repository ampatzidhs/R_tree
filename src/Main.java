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


        long startTime = System.nanoTime();//////START TIME

        LeafRecords point=new LeafRecords("rec Knn",38.3744238,21.8528735);
        Knn knn=new Knn(5,actualTree,point);


        ArrayList<LeafRecords> otinanai=knn.kontinotera;

       System.out.println();


        System.out.println("LeafRecord Point :");
        point.printRecord();

        System.out.println();


//        System.out.println("---------Αρχικές τίμες του Knn:");
//        knn.knnPrint();




        System.out.println();



        System.out.println("--------- Knn results -------------------:");

        knn.isTouching(actualTree,actualTree.getRoot().allRectangles);
        knn.isInCircle(actualTree.getRoot());

        knn.knnPrint();
















        ///Σειριακά
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
        System.out.println();
        System.out.println();
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println("Double loop for check up");
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

        System.out.println();
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;

        System.out.println("Running time is: "+totalTime+" nanoseconds");

        Double time = (double) totalTime;
        time=time/1000000000;
        System.out.println("Running time is: "+time+"sec");
    }


}
