import java.util.ArrayList;
import java.util.Objects;

public class Knn {
    int k;
    ArrayList<LeafRecords> kontinotera;
    R_Tree rTree;
    Insertion dummy;
    Double maxApostash;
    LeafRecords x;//Σημειο απο το οποιο ψαχνω τους κοντινοτερους.

    public Knn(R_Tree rTree) {
        this.rTree = rTree;
        this.k=0;
        this.kontinotera=new ArrayList<>();
        loadKontinotera();
        this.dummy= new Insertion();
    }

    public Knn(int k, R_Tree rTree,LeafRecords x) {
        this.k = k;
        this.rTree = rTree;
        this.x=x;
        this.kontinotera=new ArrayList<>();
        loadKontinotera();
        this.dummy= new Insertion();
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
            Double min=apostasi(nextlevel.get(0),x);
            MBR mbrKeep=nextlevel.get(0);//kontinotero mbr

            for(MBR mbr:nextlevel)//Βρισκει το κοντινοτερο mbr στο Χ  ---> μαλλον θα πρεπει να το κανω να βρισκει το Κ κοντινοτερο!!!!
            {
                if(apostasi(mbr,x) < min)
                {
                    min=apostasi(mbr,x);
                    mbrKeep=mbr;
                }
            }

            //Παιρνει το περιεχομενο του φυλλου. Βλεπει την αποσταση απο το Χ. Βρισκει το μακρυτερο φυλλο απο τα kontinotera (χαζο array γεματο με τυχαια σημεια)
            //Και εαν το φυλλο ειναι μικροτερο απο το μεγαλυτερο του κοντινοτερα κανει αντικατασταση.
            for(LeafRecords leaf:mbrKeep.getPeriexomeno())//περιεχομενο του κοντινοτερου mbr.
            {
                Double apost=dummy.distance(leaf.getDiastaseis().get(0),leaf.getDiastaseis().get(1),x.getDiastaseis().get(0),x.getDiastaseis().get(1));//Απόσταση τιυ φυλλου με το Χ.
                LeafRecords maxLeaf=findMax(kontinotera,x);//Βρες το μεγαλυτερο απο τα κοντινοτερα

                Double maxApost=dummy.distance(maxLeaf.getDiastaseis().get(0),maxLeaf.getDiastaseis().get(1),x.getDiastaseis().get(0),x.getDiastaseis().get(1));//Αποσταση το μεγαλύτερο απο την λιστα με το Χ
                if(apost<maxApost)//Εαν βρήκες καποιο ποιο κοντινο απο το μεγαλυτερο της λιστας κανε την αντικατασταση.
                {
                    if(!isIn(leaf))//εαν δεν ειναι ηδη μεσα το σημειο
                    {
                        kontinotera.remove(maxLeaf);
                        kontinotera.add(leaf);
                    }
                }


//                for(LeafRecords l:kontinotera)
//                {
//                    if(dummy.distance(l.getDiastaseis().get(0),l.getDiastaseis().get(1),x.getDiastaseis().get(0),x.getDiastaseis().get(1)) > apost)
//                    {
//                        kontinotera.add(leaf);
//                        LeafRecords tp=findMax(kontinotera,x);
//                        kontinotera.remove(tp);
//                    }
//                }
            }
/**exei brei to 1o mbr poy temnei kai exei parei kai ta shmeia toy sto arraylist kontinotera kai meta dimioyrgei ton kyklo
 kai tsekarei ean temnei kapoio allo orthogonio.d*/


            //Εδω φτιαχνω τον κυκλο με κεντρο το Χ και ακτινα το μακρυτερο απο την λιστα kontinotera...
            LeafRecords distanceLeaf = findMax(kontinotera,x);
            Double distanceCircle = dummy.distance(distanceLeaf.getDiastaseis().get(0),distanceLeaf.getDiastaseis().get(1),x.getDiastaseis().get(0),x.getDiastaseis().get(1));

            for(MBR m:nextlevel)//Κοιταει με τα αλλα φυλλα
            {
                //note:!!!  να μην ειναι κανενα απο αυτα που εχει εξετασει ηδη
                if(apostasi(m,x) < distanceCircle && !Objects.equals(m.getId(), mbrKeep.getId()))//Εαν καποιο αλλο Mbr(φυλλο) είναι μεσα στον κυκλο που δημιουργησα.
                {
                    for(LeafRecords leaf:m.getPeriexomeno())
                    {
                        Double perApost=dummy.distance(leaf.getDiastaseis().get(0),leaf.getDiastaseis().get(1),x.getDiastaseis().get(0),x.getDiastaseis().get(1));//Απόσταση τιυ φυλλου με το Χ.
                        LeafRecords maxLeaf=findMax(kontinotera,x);//Βρες το μεγαλυτερο απο τα κοντινοτερα

                        Double maxApost=dummy.distance(maxLeaf.getDiastaseis().get(0),maxLeaf.getDiastaseis().get(1),x.getDiastaseis().get(0),x.getDiastaseis().get(1));//Αποσταση το μεγαλύτερο απο την λιστα με το Χ
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

        Double tmpDist=apostasi(nextlevel.get(0),x);
        MBR tmpMbr=nextlevel.get(0);//kontinotero mbr
        for(MBR m:nextlevel)
        {
            Double dist =apostasi(m,x);
            if(dist<tmpDist)
            {
                tmpDist=dist;
                tmpMbr=m;
            }


        }
        ArrayList<MBR> kidss=tree.findKids(tmpMbr).allRectangles;
        isTouching(tree,kidss);



        return kontinotera;
    }

    public boolean isInCircle(Nodes nextlevel)//1η φορα το nextlevel θα ειναι το root
    {
        LeafRecords makritero=findMax(kontinotera,x);
        //range Circle
        Double maxDist=dummy.distance(makritero.getDiastaseis().get(0),makritero.getDiastaseis().get(1),x.getDiastaseis().get(0),x.getDiastaseis().get(1));

        if(nextlevel.getAllRectangles().get(0).isLeafRect())
        {
            for(MBR mbr:nextlevel.getAllRectangles()){
                for(LeafRecords leaf: mbr.getPeriexomeno())
                {
                    Double leafDist=dummy.distance(leaf.getDiastaseis().get(0),leaf.getDiastaseis().get(1),x.getDiastaseis().get(0),x.getDiastaseis().get(1));
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
            Double apostMbr=apostasi(m,x);
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
        Double max=dummy.distance(maxleaf.getDiastaseis().get(0),maxleaf.getDiastaseis().get(1),x.getDiastaseis().get(0),x.getDiastaseis().get(1));

        for(LeafRecords leaf:k)
        {
            Double tmp=dummy.distance(leaf.getDiastaseis().get(0),leaf.getDiastaseis().get(1),x.getDiastaseis().get(0),x.getDiastaseis().get(1));
            if(tmp>max)
            {
                max=tmp;
                maxleaf=leaf;
            }
        }

        return maxleaf;
    }




    /**
     * Εστω οτι το σημειο που ψαχνω τους γειτονες του ειναι το Χ. Για να βρω το κοντινοτερο ορθογωνιο του, θα παω να βαλω το Χ
     * μεσα σε ένα ορθογωνιο και θα δω ποσο μεγαλωσε η περιμετρος του. Επειτα θα επαναλαβω για καθε ορθογωνιο. Αυτο που μεγαλωσε
     * πιο λιγο ειναι και το κοντινοτερο του.
     */
    public double apostasi(MBR data,LeafRecords x)
    {
        ArrayList<Double> newMbr=dummy.newCorners(data,x.getDiastaseis().get(0),x.getDiastaseis().get(1));

        Double newPerimetros= dummy.perimeter(newMbr.get(0),newMbr.get(1),newMbr.get(2),newMbr.get(3));
        Double oldPerimetow = dummy.perimeter(data.diastaseisA.get(0),data.diastaseisA.get(1),data.diastaseisB.get(0),data.diastaseisB.get(1));


        return (newPerimetros-oldPerimetow)/2;


    }



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
            Double apo= dummy.distance(leaf.getDiastaseis().get(0),leaf.getDiastaseis().get(1),x.getDiastaseis().get(0),x.getDiastaseis().get(1));
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