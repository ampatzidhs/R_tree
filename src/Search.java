import java.awt.*;
import java.util.ArrayList;

public class Search {
    R_Tree rTree;


    public Search(R_Tree rTree) {
        this.rTree = rTree;
    }

    public void findLeafRec(Nodes nodes,MBR anazitisi)
    {
        if(nodes.getAllRectangles().get(0).isLeafRect())
        {
            return;
        }



    }


    public ArrayList<MBR> anazit(R_Tree tree,ArrayList<Nodes> nextlevel,MBR anazitisi)
    {
        ArrayList<MBR> result= new ArrayList<>();
        if(nextlevel.get(0).getAllRectangles().get(0).isLeafRect())
        {
            for(Nodes n:nextlevel)
            {
                for(MBR m:n.getAllRectangles())
                {
                    if(isBetween(m,anazitisi))
                    {
                        result.add(m);
                    }
                }
            }
            return result;
        }

        //Nodes nodes=tree.getRoot();
        //ArrayList<MBR> level = nodes.getAllRectangles();
        ArrayList<MBR> level = new ArrayList<>();
        for(Nodes nodes:nextlevel)
        {
            for(MBR m: nodes.getAllRectangles())
            {
                level.add(m);
            }
        }

        ArrayList<Nodes> kids = new ArrayList<>();
        for(MBR m:level)
        {
            System.out.println("-------Kids are:");
            m.printRect();
            if(isBetween(m,anazitisi))
            {
                kids.add(tree.findKids(m));
                System.out.println("Το αναζητουμενο σημειο επικαλύπτει καποιο σημείο του δενδρου !");
            }
            else
            {
                System.out.println("Το αναζητουμενο σημειο δεν επικαλύπτει καποιο σημείο του δενδρου.");
            }

            System.out.println("-----------------------------------------------");

        }


        System.out.println("<------------Next_Level----");
        for(Nodes n:kids)
        {
            n.printNodes();
        }

        System.out.println("##--------- results in the functions!------------##");
        for(MBR mbr:result)
        {
            mbr.printRect();
        }

        result=anazit(tree,kids,anazitisi);

        return result;
    }


    public boolean isBetween(MBR dentoy,MBR anazitoymenoy)
    {


        if(anazitoymenoy.diastaseisA.get(0) >= dentoy.diastaseisA.get(0)  && anazitoymenoy.diastaseisA.get(0) <= dentoy.diastaseisB.get(0))//Αναζητουμενο κατω αριστερα γωνια Χ > Χ.ΔΕΝΔΡΟΥ(ΑΡΙΣΤΕΡΑ)     ΚΑΙ Χ < Χ.ΔΕΝΔΡΟΥ(ΔΕΞΙΑ)
        {
            if(anazitoymenoy.diastaseisA.get(1) >= dentoy.diastaseisA.get(1)  && anazitoymenoy.diastaseisA.get(1) <= dentoy.diastaseisB.get(1))//Αναζητουμενο κατω αριστερα γωνια Υ > Υ.ΔΕΝΔΡΟΥ(ΑΡΙΣΤΕΡΑ)     ΚΑΙ Υ < Υ.ΔΕΝΔΡΟΥ(ΔΕΞΙΑ)
                return true;
        }


        if(anazitoymenoy.diastaseisB.get(0) >= dentoy.diastaseisA.get(0)  && anazitoymenoy.diastaseisB.get(0) <= dentoy.diastaseisB.get(0))//Αναζητουμενο πανω δεξιας γωνια Χ > Χ.ΔΕΝΔΡΟΥ(ΑΡΙΣΤΕΡΑ)     ΚΑΙ Χ < Χ.ΔΕΝΔΡΟΥ(ΔΕΞΙΑ)
        {
            if(anazitoymenoy.diastaseisB.get(1) >= dentoy.diastaseisA.get(1)  && anazitoymenoy.diastaseisB.get(1) <= dentoy.diastaseisB.get(1))//Αναζητουμενο πανω δεξιας γωνια Υ > Υ.ΔΕΝΔΡΟΥ(ΑΡΙΣΤΕΡΑ)     ΚΑΙ Υ < Υ.ΔΕΝΔΡΟΥ(ΔΕΞΙΑ)
                return true;
        }





        //System.out.println("Δεν ικανοποιηθηκε καμια συνθηκη");
        return false;



    }

//    public void epikalici(MBR anazitoumeno)
//    {
//        System.out.println("----------aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa----------");
//        System.out.println("Το ορθογώνιο προς αναζήτηση είναι:");
//        anazitoumeno.printRect();
//
//        ArrayList<ColoredRectangle> allColorRectangles =new ArrayList<>();
//        for(int i=0;i<5;i++)
//        {
//            for(MBR m:rTree.allNodes.get(i).allRectangles)
//            {
//                ColoredRectangle col=new ColoredRectangle("rec"+i,m.diastaseisA.get(0),m.diastaseisA.get(1),m.diastaseisB.get(0),m.diastaseisB.get(1), Color.blue);
//                allColorRectangles.add(col);
//            }
////            ColoredRectangle col=new ColoredRectangle(rTree.allNodes.get(i));
//
//        }
//
//        DynamicRandomColoredRectangles vis=new DynamicRandomColoredRectangles(allColorRectangles);
//       vis.createAndShowGUI(allColorRectangles);
//
////        ArrayList<ColoredRectangle> rectangles = generateRandomRectangles(5, minBRList);
////        createAndShowGUI(rectangles);
//    }
//




}
