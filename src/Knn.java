import java.util.ArrayList;
import java.util.Objects;

public class Knn {
    int k;
    ArrayList<LeafRecords> kontinotera;
    R_Tree rTree;
    Double maxApostash;
    LeafRecords x;//Σημειο απο το οποιο ψαχνω τους κοντινοτερους.

    public Knn(R_Tree rTree) {
        this.rTree = rTree;
        this.k=0;
        this.kontinotera=new ArrayList<>();
        loadKontinotera();
    }

    public Knn(int k, R_Tree rTree,LeafRecords x) {
        this.k = k;
        this.rTree = rTree;
        this.x=x;
        this.kontinotera=new ArrayList<>();
        loadKontinotera();
    }


    public ArrayList<LeafRecords> isTouching(R_Tree tree,ArrayList<MBR> nextlevel)
    {
        ArrayList<Nodes> tmp=tree.getAllNodes();
        if(nextlevel.isEmpty())
        {
            return kontinotera;
        }


        if(nextlevel.get(0).isLeafRect())//Εαν ειναι φυλλα βρες την αποσταση του φυλλου με το Χ. Κρατα αποσταση την αποσταση του mbr που ειναι φυλλο σε σχεση με το σημειο Χ.
        {
            Double min=distMbrToPoint(nextlevel.get(0),x);
            MBR mbrKeep=nextlevel.get(0);//kontinotero mbr

            for(MBR mbr:nextlevel)
            {
                if(distMbrToPoint(mbr,x) < min)
                {
                    min=distMbrToPoint(mbr,x);
                    mbrKeep=mbr;
                }
            }

            //Παιρνει το περιεχομενο του φυλλου. Βλεπει την αποσταση απο το Χ. Βρισκει το μακρυτερο φυλλο απο τα kontinotera (χαζο array γεματο με τυχαια σημεια)
            //Και εαν το φυλλο ειναι μικροτερο απο το μεγαλυτερο του kontinotera κανει αντικατασταση.
            for(LeafRecords leaf:mbrKeep.getPeriexomeno())//περιεχομενο του κοντινοτερου mbr.
            {
//                System.out.println("------To mbr einai:");
//                mbrKeep.printRect();
                Double apost=distManhattan(leaf,x);
                LeafRecords maxLeaf=findMax(kontinotera,x);//Βρες το μεγαλυτερο απο τα κοντινοτερα

                Double maxApost=distManhattan(maxLeaf,x);
                if(apost<maxApost)//Εαν βρήκες καποιο ποιο κοντινο απο το μεγαλυτερο της λιστας κανε την αντικατασταση.
                {
                    if(!isIn(leaf))//εαν δεν ειναι ηδη μεσα το σημειο
                    {
                        kontinotera.remove(maxLeaf);
                        kontinotera.add(leaf);
                    }
                }

            }
            //Εδω φτιαχνω τον κυκλο με κεντρο το Χ και ακτινα το μακρυτερο απο την λιστα kontinotera...
            LeafRecords distanceLeaf = findMax(kontinotera,x);
            Double distanceCircle=distManhattan(distanceLeaf,x);

            for(MBR m:nextlevel)//Κοιταει με τα αλλα φυλλα
            {
                if(distMbrToPoint(m,x) < distanceCircle && !Objects.equals(m.getId(), mbrKeep.getId()))//Εαν καποιο αλλο Mbr(φυλλο) είναι μεσα στον κυκλο που δημιουργησα.
                {
                    for(LeafRecords leaf:m.getPeriexomeno())
                    {
                        Double perApost=distManhattan(leaf,x);
                        LeafRecords maxLeaf=findMax(kontinotera,x);//Βρες το μεγαλυτερο απο τα κοντινοτερα

                         Double maxApost=distManhattan(maxLeaf,x);
                        if(perApost<maxApost)//Εαν βρήκες καποιο ποιο κοντινο απο το μεγαλυτερο της λιστας κανε την αντικατασταση.
                        {
                            if(!isIn(leaf))//εαν δεν ειναι ηδη μεσα το σημειο
                            {
                                kontinotera.remove(maxLeaf);
                                kontinotera.add(leaf);
                            }
                        }


                    }
                }
            }

//            //Εχει κανει τον κυκλο και το παει απο την αρχη για να τσεκαρει εαν ο κυκλο τεμνει σε καποιο αλλο mbr(mbr γενικα οχι μονο τα φυλλα)
//            isInCircle(findMax(kontinotera,x),rTree.getRoot());//ενημερωνει τα kontinotera αφου εχει γινει ο κυκλος

        }

        Double tmpDist=distMbrToPoint(nextlevel.get(0),x);
        MBR tmpMbr=nextlevel.get(0);//kontinotero mbr
        ArrayList<MBR> toVisit=new ArrayList<>();
        for(MBR m:nextlevel)
        {
            Double dist =distMbrToPoint(m,x);
            if(dist<=tmpDist)
            {
                tmpDist=dist;
                tmpMbr=m;
                toVisit.add(tmpMbr);
            }


        }
        for(MBR mp:toVisit)
        {
            ArrayList<MBR> kidss=tree.findKids(mp).allRectangles;
            isTouching(tree,kidss);

        }




        return kontinotera;
    }

    public boolean isInCircle(Nodes nextlevel)//1η φορα το nextlevel θα ειναι το root
    {
        LeafRecords makritero=findMax(kontinotera,x);
        //range Circle
        Double maxDist=distManhattan(makritero,x);
        if(nextlevel.getAllRectangles().get(0).isLeafRect())
        {
            for(MBR mbr:nextlevel.getAllRectangles()){
                for(LeafRecords leaf: mbr.getPeriexomeno())
                {
                    Double leafDist=distManhattan(leaf,x);
                    if(leafDist<maxDist)
                    {
                        if(!isIn(leaf))//εαν δεν ειναι ηδη μεσα το σημειο
                        {
                            kontinotera.remove(findMax(kontinotera,x));
                            kontinotera.add(leaf);
                        }
                    }

                }
            }

            return true;
        }


        //Nodes root = rTree.getRoot();
        for(MBR m: nextlevel.getAllRectangles())
        {
            Double apostMbr=distMbrToPoint(m,x);
            if(apostMbr<maxDist)///εαν ειναι μεσα στον κυκλο
            {
                Nodes kids=rTree.findKids(m);
                return isInCircle(kids);
            }
            else
            {
                return false;
            }
        }
        return false;

    }



    /**
     * Βαλε τυχαια leafrecord στην λιστα kontinotera τα οποια μετα θα τα αλλαζει με τα οντως κοντινοτερα του.
     */
    public void loadKontinotera()
    {
        int p=0;
        for(Nodes nodes:rTree.getAllNodes())
        {
            for(MBR mbr:nodes.getAllRectangles())
            {
                for(LeafRecords leaf:mbr.getPeriexomeno())
                {
                    if(p<k)
                    {
                        kontinotera.add(leaf);
                        p++;
                    }
                }
            }
        }
    }

    public boolean isIn(LeafRecords x)
    {
        for(LeafRecords leaf:kontinotera)
        {
            if(Objects.equals(leaf.getDiastaseis().get(0), x.diastaseis.get(0)) && (Objects.equals(leaf.getDiastaseis().get(1), x.diastaseis.get(1))))
            {
                return true;
            }

        }
        return false;
    }

    /**
     *Εστω Χ το σημείο που ψαχνουμε τους γειτονες του. Βαζω τυχαια το 1ο σημειο στο maxleaf και στο Max την αποστασση του απο το Χ.
     * Συγκρινε το με ολα τα αλλα και βρες το max.
     * TO DO:Πρεπει να το κανω να βρισκει το Κ μακρυτερο, και οχι το μακρυτερο γενικα.
     */
    public LeafRecords findMax(ArrayList<LeafRecords> k,LeafRecords x)
    {
        LeafRecords maxleaf= k.get(0);
        //Double max=dummy.distance(maxleaf.getDiastaseis().get(0),maxleaf.getDiastaseis().get(1),x.getDiastaseis().get(0),x.getDiastaseis().get(1));
        Double max=distManhattan(maxleaf,x);
        for(LeafRecords leaf:k)
        {
           // Double tmp=dummy.distance(leaf.getDiastaseis().get(0),leaf.getDiastaseis().get(1),x.getDiastaseis().get(0),x.getDiastaseis().get(1));
            Double tmp=distManhattan(leaf,x);
            if(tmp>max)
            {
                max=tmp;
                maxleaf=leaf;
            }
        }

        return maxleaf;
    }


    //Επεστρεψε το μεγαλυτερο Χ του ορθογωνιου (πανω αριστερα γωνια το Χ)
    public Double findMaxX(MBR mbr)
    {
        return mbr.diastaseisB.get(0);
    }

    //Επεστρεψε το μικροτερο Χ του ορθογωνιου (κατω αριστερα γωνια το Χ)
    public Double findMinX(MBR mbr)
    {
        return mbr.diastaseisA.get(0);
    }

    //Επεστρεψε το μεγαλυτερο Χ του ορθογωνιου (πανω αριστερα γωνια το Χ)
    public Double findMaxY(MBR mbr)
    {
        return mbr.diastaseisB.get(1);
    }

    //Επεστρεψε το μικροτερο Χ του ορθογωνιου (κατω αριστερα γωνια το Χ)
    public Double findMinY(MBR mbr)
    {
        return mbr.diastaseisA.get(1);
    }

    //Εάν το Χ του σημείου μου είναι ανάμεασα στα Χ του ορθογωνίου(ανάμεσα στο μικρότερο και στο μεγαλύτερο του ορθογωνίου)
    public boolean isXbetween(LeafRecords x,MBR mbr)
    {
        Double min=findMinX(mbr);
        Double max=findMaxX(mbr);

        Double pointX=x.diastaseis.get(0);



        if(pointX>min && pointX<max)
        {
            return true;
        }

        return false;
    }

    //Εάν το Y του σημείου μου είναι ανάμεασα στα Y του ορθογωνίου(ανάμεσα στο μικρότερο και στο μεγαλύτερο του ορθογωνίου)
    public boolean isYbetween(LeafRecords x,MBR mbr)
    {
        Double min=findMinY(mbr);
        Double max=findMaxY(mbr);

        Double pointY=x.diastaseis.get(1);



        if(pointY>min && pointY<max)
        {
            return true;
        }

        return false;
    }



    //Απόσταση ενός σημείο από ένα ορθογώνιο με 9 περιπτώσεις
    public Double distMbrToPoint(MBR mbr,LeafRecords x)
    {
        Double apostasi=-1.0;
        Double pointX=x.diastaseis.get(0);
        Double pointY=x.diastaseis.get(1);

        //Σημείο του ορθογωνίου που θα παρούμε την απόσταση.
        LeafRecords pointOfMbr=new LeafRecords("rect",1.0,1.0);

        if(pointX<findMinX(mbr))//Έαν το Χ του σημείου μας είναι πιο μικρό από το μικρότερο Χ του ορθογωνίου. Το σημείο είναι στα αρίστερα του ορθογωνίου.
        {
            if(pointY<findMinY(mbr))//Το σημέιο έχει μικρότερο Χ και Υ.
            {
                //Δημιουργώ ένα σημείο που θα αναπαραστεί την κάτω αρίστερα γωνία.
                pointOfMbr.diastaseis.set(0,findMinX(mbr));
                pointOfMbr.diastaseis.set(1,findMinY(mbr));
                apostasi=distManhattan(x,pointOfMbr);

            }
            else if(isYbetween(x,mbr))//Το σημέιο έχει μικρότερο Χ αλλα το Υ του είναι ανάμεσα σε αυτά του ορθογωνίου.
            {
                //Δημιουργώ ένα σημείο του ορθογωνίου που θα αναπαραστεί κάποιο με το μικρότερο Χ. και Υ ανάμεσα του ορθωγωνίου .
                pointOfMbr.diastaseis.set(0,findMinX(mbr));//ελάχιστο Χ
                pointOfMbr.diastaseis.set(1,x.diastaseis.get(1)); //Υ ίδιο με το σημείο (γιατί είναι ανάμεσα στα όρια)
                apostasi=distManhattan(x,pointOfMbr);

            }
            else if(pointY>findMaxY(mbr))//Το σημέιο έχει μικρότερο Χ αλλα μεγαλύτερο Υ.
            {
                //Δημιουργώ ένα σημείο που θα αναπαραστεί την πάνω αρίστερα γωνία.
                pointOfMbr.diastaseis.set(0,findMinX(mbr));//Ελάχιστο Χ
                pointOfMbr.diastaseis.set(1,findMaxY(mbr));//Μέγιστο Υ
                apostasi=distManhattan(x,pointOfMbr);
            }
        }
        else if(isXbetween(x,mbr))//Έαν το Χ του σημείου μας είναι ανάμεσα στα Χ του ορθογωνίου.
        {
            if(pointY<findMinY(mbr))//Εαν το Χ του σημείου είναι ανάμεσα αλλα το Υ είναι μικρότερο (του ορθογωνίου) α32
            {
                //Δημιουργώ ένα σημείο του ορθογωνίου που θα αναπαραστεί κάποιο με το μικρότερο Υ. και Χ ανάμεσα του ορθωγωνίου .
                pointOfMbr.diastaseis.set(0,x.diastaseis.get(0));//Χ το ίδιο με το σημείο (γιατί είναι ανάμεσα στα όρια)
                pointOfMbr.diastaseis.set(1,findMinY(mbr));//Ελάχιστο Y
                apostasi=distManhattan(x,pointOfMbr);
            }
            else if(isYbetween(x,mbr))//Έαν το Χ και το Υ του σημείου είναι ανάμεσα σε αυτά του ορθογωνίου (πρακτικά να είναι μέσα στο ορθογώνιο) α22
            {
                apostasi=0.0;//Είναι μέσα στο ορθογώνιο.
            }
            else if(pointY>findMaxY(mbr))//Εαν το Χ του σημείου είναι ανάμεσα αλλα το Υ είναι μεγαλύτερο (του ορθογωνίου) α12
            {
                //Δημιουργώ ένα σημείο του ορθογωνίου που θα αναπαραστεί κάποιο με το μεγαλύτερο Υ. και Χ ανάμεσα στα ορια του ορθωγωνίου .
                pointOfMbr.diastaseis.set(0,x.diastaseis.get(0));//Χ το ίδιο με το σημείο (γιατί είναι ανάμεσα στα όρια)
                pointOfMbr.diastaseis.set(1,findMaxY(mbr));//Μέγιστο Y.
                apostasi=distManhattan(x,pointOfMbr);
            }
        }
        else if(pointX>findMaxX(mbr))//Έαν το Χ του σημείου μας είναι πιο μεγάλο από το μεγαλύτερο Χ του ορθογωνίου. Το σημείο είναι στα δεξία του ορθογωνίου.
        {
            if(pointY<findMinY(mbr))//Το σημέιο έχει μεγαλύτερο Χ και μικρότερο Υ.
            {
                //Δημιουργώ ένα σημείο που θα αναπαραστεί την κάτω δεξία γωνία .
                pointOfMbr.diastaseis.set(0,findMaxX(mbr));//Μέγιστο Χ
                pointOfMbr.diastaseis.set(1,findMinY(mbr));//Ελάχιστο Y
                apostasi=distManhattan(x,pointOfMbr);
            }
            else if(isYbetween(x,mbr))//Το σημέιο έχει μεγαλύτερο Χ αλλα το Υ είναι ανάμεσα σε αύτα του ορθογωνίου.
            {
                //Δημιουργώ ένα σημείο του ορθογωνίου που θα αναπαραστεί κάποιο με το μεγαλύτερο X. και Y ανάμεσα του ορθωγωνίου .
                pointOfMbr.diastaseis.set(0,findMaxX(mbr));//Μέγιστο Χ
                pointOfMbr.diastaseis.set(1,x.diastaseis.get(1));//Y το ίδιο με το σημείο (γιατί είναι ανάμεσα στα όρια)
                apostasi=distManhattan(x,pointOfMbr);
            }
            else if(pointY>findMaxY(mbr))//Το σημέιο έχει μεγαλύτερο Χ και Υ.
            {
                //Δημιουργώ ένα σημείο που θα αναπαραστεί την πάνω δεξία γωνία .
                pointOfMbr.diastaseis.set(0,findMaxX(mbr));//Μέγιστο Χ
                pointOfMbr.diastaseis.set(1,findMaxY(mbr));//Μέγιστο Y
                apostasi=distManhattan(x,pointOfMbr);
            }
        }






        return apostasi;
    }


    public Double distManhattan(LeafRecords a,LeafRecords b)
    {
        Double aX=a.diastaseis.get(0);
        Double aY=a.diastaseis.get(1);

        Double bX=b.diastaseis.get(0);
        Double bY=b.diastaseis.get(1);


        return Math.abs(aX - bX) + Math.abs(aY - bY);
    }





    /**
     * Εστω οτι το σημειο που ψαχνω τους γειτονες του ειναι το Χ. Για να βρω το κοντινοτερο ορθογωνιο του, θα παω να βαλω το Χ
     * μεσα σε ένα ορθογωνιο και θα δω ποσο μεγαλωσε η περιμετρος του. Επειτα θα επαναλαβω για καθε ορθογωνιο. Αυτο που μεγαλωσε
     * πιο λιγο ειναι και το κοντινοτερο του.
     */
   /** public double apostasi(MBR data,LeafRecords x)
    {
        ArrayList<Double> newMbr=dummy.newCorners(data,x.getDiastaseis().get(0),x.getDiastaseis().get(1));

        Double newPerimetros= dummy.perimeter(newMbr.get(0),newMbr.get(1),newMbr.get(2),newMbr.get(3));
        Double oldPerimetow = dummy.perimeter(data.diastaseisA.get(0),data.diastaseisA.get(1),data.diastaseisB.get(0),data.diastaseisB.get(1));


        return (newPerimetros-oldPerimetow)/2;


    }
*/


    public void knnPrint()
    {
        System.out.println("-----------------------------------------------");
        System.out.println("Knn of point");
        int i=1;
        for(LeafRecords leaf:kontinotera)
        {
            System.out.println("K:"+i);
            i++;
            leaf.printRecord();
            //Double apo= dummy.distance(leaf.getDiastaseis().get(0),leaf.getDiastaseis().get(1),x.getDiastaseis().get(0),x.getDiastaseis().get(1));
            Double apo=distManhattan(leaf,x);
            System.out.println("----Distance between leaf and Point:"+apo);
        }
    }



    public boolean otinanaiIsIn(LeafRecords x,ArrayList<LeafRecords> otinanai)
    {
        for(LeafRecords leaf:otinanai)
        {
            if(Objects.equals(leaf.getDiastaseis().get(0), x.diastaseis.get(0)) && (Objects.equals(leaf.getDiastaseis().get(1), x.diastaseis.get(1))))
            {
                return true;
            }

        }
        return false;
    }


    public ArrayList<LeafRecords> knnSeiriaka(ArrayList<LeafRecords> otinanai)
    {
        AllBlocks allBlock=new AllBlocks();
        ArrayList<Block> blocks=allBlock.readFromBinaryFile();


        //Απο τα αρχειο τα διαβασα και τα εκανα απο Records--->LeafRecods
        ArrayList<LeafRecords> tmp=new ArrayList<>();
        for(Block bl:blocks)
        {
            for(Record record:bl.getOneBlock())
            {
               LeafRecords n=new LeafRecords(record);
               tmp.add(n);
            }
        }


        for(LeafRecords leaf:tmp)
        {
            Double apostLeaf=distManhattan(leaf,x);
            LeafRecords makri=findMax(otinanai,x);
            Double apostOtinanai=distManhattan(makri,x);
            if(apostLeaf<apostOtinanai)
            {
                if(!otinanaiIsIn(leaf,otinanai))
                {
                    otinanai.remove(makri);
                    otinanai.add(leaf);
                }
            }
        }



        return otinanai;




    }














    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public ArrayList<LeafRecords> getKontinotera() {
        return kontinotera;
    }

    public void setKontinotera(ArrayList<LeafRecords> kontinotera) {
        this.kontinotera = kontinotera;
    }

    public R_Tree getrTree() {
        return rTree;
    }

    public void setrTree(R_Tree rTree) {
        this.rTree = rTree;
    }
}
