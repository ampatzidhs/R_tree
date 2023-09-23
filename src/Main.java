import org.xml.sax.SAXException;

import javax.media.j3d.Leaf;
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
        LeafRecords point=new LeafRecords("rec Knn",38.3573721,21.7877221);
        Knn knn=new Knn(3,actualTree,point);
        System.out.println("LeafRecord Point :");
        point.printRecord();
        System.out.println("----------------------------:");

        System.out.println("---------knn oti nanai-------------------:");
        knn.knnPrint();
        System.out.println("----------------------------:");

        knn.isTouching(actualTree,actualTree.getRoot().allRectangles);
        knn.isInCircle(actualTree.getRoot());
        knn.knnPrint();








    }



}
