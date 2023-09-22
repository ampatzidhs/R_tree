import java.util.ArrayList;
import java.util.Objects;

public class Knn {
    int k;
    ArrayList<LeafRecords> kontinotera;
    R_Tree rTree;
    Insertion dummy;
    Double maxApostash;

    public Knn(R_Tree rTree) {
        this.rTree = rTree;
        this.k=0;
        loadKontinotera();
    }

    public Knn(int k, R_Tree rTree) {
        this.k = k;
        this.rTree = rTree;
        loadKontinotera();
    }


    public ArrayList<LeafRecords> isTouching(R_Tree tree,ArrayList<MBR> nextlevel,LeafRecords x)
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
                LeafRecords max=findMax(kontinotera,x);//Βρες το μεγαλυτερο απο τα κοντινοτερα

                Double maxApost=dummy.distance(max.getDiastaseis().get(0),max.getDiastaseis().get(1),x.getDiastaseis().get(0),x.getDiastaseis().get(1));//Αποσταση το μεγαλύτερο απο την λιστα με το Χ
                if(apost<maxApost)//Εαν βρήκες καποιο ποιο κοντινο απο το μεγαλυτερο της λιστας κανε την αντικατασταση.
                {
                    kontinotera.add(leaf);
                    kontinotera.remove(max);
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


            //Εδω φτιαχνω τον κυκλο με κεντρο το Χ και ακτινα το μακρυτερο απο την λιστα kontinotera..
            LeafRecords distanceLeaf = findMax(kontinotera,x);
            Double distanceCircle = dummy.distance(distanceLeaf.getDiastaseis().get(0),distanceLeaf.getDiastaseis().get(1),x.getDiastaseis().get(0),x.getDiastaseis().get(1));

            for(MBR m:nextlevel)
            {
                if(apostasi(m,x) < distanceCircle && !Objects.equals(m.getId(), mbrKeep.getId()))
                {

                }
            }

        }





        return kontinotera;
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


    public void makeCircle(R_Tree tree,LeafRecords x)
    {
        for(Nodes node:tree.getAllNodes())
        {
            for(MBR mbr: node.getAllRectangles())
            {

            }
        }




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
