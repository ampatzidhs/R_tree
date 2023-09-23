import org.xml.sax.SAXException;

import javax.media.j3d.Leaf;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
//4:30
public class Main {
    public static void main(String []args) throws ParserConfigurationException, IOException, SAXException {
        AllBlocks allBlocks = new AllBlocks();
        allBlocks.readFromOsmFile();
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
        LeafRecords point=new LeafRecords("rec Knn",38.3744238,21.8528735);

        Knn knn=new Knn(3,actualTree,point);
        ArrayList<LeafRecords> otinanai=knn.kontinotera;

        //<node id="73050076" visible="true" version="9" changeset="97872157" timestamp="2021-01-21T04:40:31Z" user="BatsmanMapsman" uid="8794020" lat="38.3744238" lon="21.8528735"/>

        System.out.println("LeafRecord Point :");
        point.printRecord();
        System.out.println("----------------------------:");

        System.out.println("---------knn oti nanai-------------------:");
        knn.knnPrint();
        System.out.println("----------------------------:");

        knn.isTouching(actualTree,actualTree.getRoot().allRectangles);
        knn.isInCircle(actualTree.getRoot());
        knn.knnPrint();



        for(Nodes n: actualTree.getAllNodes())
        {
            if(n.getAllRectangles().get(0).isLeafRect())
            {
                for(MBR mbr :n.getAllRectangles())
                {
                    for(LeafRecords leaf:mbr.getPeriexomeno())
                    {
                       Double apostLeaf= knn.dummy.distance(leaf.getDiastaseis().get(0),leaf.getDiastaseis().get(1),knn.x.getDiastaseis().get(0),knn.x.getDiastaseis().get(1));
                       LeafRecords makri=knn.findMax(otinanai,knn.x);
                       Double apostOtinanai= knn.dummy.distance(makri.getDiastaseis().get(0),makri.getDiastaseis().get(1),knn.x.getDiastaseis().get(0),knn.x.getDiastaseis().get(1));

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
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println("OTI NANAI KNN results");
        for(LeafRecords l:otinanai)
        {
            l.printRecord();
        }




    }



}
