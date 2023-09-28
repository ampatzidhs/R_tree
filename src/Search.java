import java.awt.*;
import java.util.ArrayList;

public class Search {
    R_Tree rTree;


    public Search(R_Tree rTree) {
        this.rTree = rTree;
    }

    public ArrayList<LeafRecords> findMbrPeriexomeno(ArrayList<MBR> input,MBR anazitisi)///anazitisi einai to or8ogonio kai pernoyme ta rec pou einai mesa se auto
    {
        ArrayList<LeafRecords> results=new ArrayList<>();

        for(MBR mbr:input)
        {
           for(LeafRecords rec:mbr.getPeriexomeno())
           {

                   Double x=rec.diastaseis.get(0);
                   Double y=rec.diastaseis.get(1);

                   if(x>anazitisi.diastaseisA.get(0)&&x<anazitisi.diastaseisB.get(0))
                   {
                       if(y>anazitisi.diastaseisA.get(1)&&y<anazitisi.diastaseisB.get(1))
                       {
                           results.add(rec);
                       }
                   }

           }

        }

        return results;

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




public ArrayList<Record> searchSeiriaka(MBR anazitisi)
{
    AllBlocks allBlocks=new AllBlocks();
    ArrayList<Record> results=new ArrayList<>();


    ArrayList<Block> blocks=allBlocks.readFromBinaryFile();
    System.out.println();
    System.out.println("Αποτελέσματα σε ερώτημα περιοχής(Σειριακά)");
    for(Block bl:blocks)
    {
        for(Record rec:bl.getOneBlock())
        {
            Double x=rec.diastaseis.get(0);
            Double y=rec.diastaseis.get(1);

            if(x>anazitisi.diastaseisA.get(0)&&x<anazitisi.diastaseisB.get(0))
            {
                if(y>anazitisi.diastaseisA.get(1)&&y<anazitisi.diastaseisB.get(1))
                {
                    results.add(rec);
                }
            }
        }
    }





    for (Record r:results)
    {
        r.printRecord();
    }
    return results;

}





}
