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

        if(nextlevel.get(0).isLeafRect())
        {
            Double min=apostasi(nextlevel.get(0),x);
            MBR mbrKeep=nextlevel.get(0);//kontinotero mbr

            for(MBR mbr:nextlevel)
            {
              if(apostasi(mbr,x) < min)
              {
                min=apostasi(mbr,x);
                mbrKeep=mbr;
              }
            }

            for(LeafRecords leaf:mbrKeep.getPeriexomeno())
            {
                Double apost=dummy.distance(leaf.getDiastaseis().get(0),leaf.getDiastaseis().get(1),x.getDiastaseis().get(0),x.getDiastaseis().get(1));
                LeafRecords max=findMax(kontinotera,x);

                Double maxApost=dummy.distance(max.getDiastaseis().get(0),max.getDiastaseis().get(1),x.getDiastaseis().get(0),x.getDiastaseis().get(1));
                if(apost<maxApost)
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
////////////////////exei brei to 1o mbr poy temnei kai exei parei kai ta shmeia toy sto arraylist kontinotera kai meta dimioyrgei ton kyklo
// kai tsekarei ean temnei kapoio allo orthogonio.
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
