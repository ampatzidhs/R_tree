import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Interface {
    Scanner scanner = new Scanner(System.in);
    R_Tree actualTree;

    Knn knn;




    public Interface() throws ParserConfigurationException, IOException, SAXException {
        AllBlocks allBlocks = new AllBlocks();
        allBlocks.readFromOsmFile();
        ArrayList<Block> returned = allBlocks.readFromBinaryFile();


        CreateRTree r_tree = new CreateRTree(returned);
        r_tree.createTree();
        actualTree = new R_Tree(r_tree.allNodes.get(0), r_tree.allNodes, r_tree.rect_id_count, r_tree.node_id_count);

    }

    public void inter()
    {

        int input=-1;

        while(input!=10)
        {
            System.out.println("For Insert press: 1");
            System.out.println("For Delete press: 2");
            System.out.println("For range query press: 3");
            System.out.println("For Knn press: 4");
            System.out.println("For Skyline press: 5");
            System.out.println("For Exit press: 10");

            System.out.print("Enter a number: ");


            // Check if the input is an integer
            if (scanner.hasNextInt())
            {
                input = scanner.nextInt();
                System.out.println("You entered: " + input);

               if(input==1)
               {

               }
               else if(input==2)
               {

               }
               else if(input==3)
               {

               }
               else if(input==4)//Knn
               {
                   System.out.println("Knn");
                   System.out.print("Enter number for K:");
                   int k=scanner.nextInt();
                   int tmp;
                   System.out.print("To create your point press 1:");
                   System.out.print("For an uparxon point press 2");
                   tmp=scanner.nextInt();
                   LeafRecords point;
                   if(tmp==1)
                   {
                       System.out.print("Enter the x:");
                       Double x=scanner.nextDouble();
                       System.out.print("Enter the y:");
                       Double y=scanner.nextDouble();
                       point=new LeafRecords("anazitoumeno",x,y);
                       knn.x=point;

                   }
                   else
                   {
                       point=new LeafRecords("rec Knn",38.3744238,21.8528735);
                   }


                   knn=new Knn(k,actualTree,point);

                   System.out.println("Your Point is:");
                   point.printRecord();

                   knn.isTouching(actualTree,actualTree.getRoot().allRectangles);
                   knn.isInCircle(actualTree.getRoot());

                   System.out.println("--------- Knn results -------------------:");
                   knn.knnPrint();



               }
               else if(input==5)
               {

               }
















            }
            else
            {
                System.out.println("Invalid input. Please enter an integer.");
            }



        }




    }

}
