import java.util.ArrayList;
import java.util.Objects;

public class Skyline {
    ArrayList<MBRAndMindist> allMBRMindist;
    ArrayList<LeafRecords> skyline;
    ArrayList<String> ids;
    boolean flag;

    public Skyline() {
        this.skyline = new ArrayList<>();
        this.allMBRMindist = new ArrayList<>();
        this.ids = new ArrayList<>();
        this.flag = false;
    }

    public ArrayList<LeafRecords> createSkyline(R_Tree rTree){
        calculateBBS(rTree);
        return skyline;
    }
    public ArrayList<MBRAndMindist> getAllMBRMindist() {
        return allMBRMindist;
    }

    public void setAllMBRMindist(ArrayList<MBRAndMindist> allMBRMindist) {
        this.allMBRMindist = allMBRMindist;
    }

    public ArrayList<LeafRecords> getSkyline() {
        return skyline;
    }

    public void setSkyline(ArrayList<LeafRecords> skyline) {
        this.skyline = skyline;
    }

//    public boolean checked(){
//
//    }

    public boolean notInSkyline(LeafRecords l)
    {
        for (LeafRecords all: skyline){
            if(Objects.equals(l.getNode(), all.getNode())){
                return false;
            }
        }
        return true;
    }

    public Double calculateMinDist(MBR mbr){
        return mbr.getDiastaseisA().get(0)+mbr.getDiastaseisA().get(1);
    }

    //a is dominated by b
    public boolean isDominated(LeafRecords a, LeafRecords b){
        if(Objects.equals(b.getDiastaseis().get(0), a.getDiastaseis().get(0)) && Objects.equals(b.getDiastaseis().get(1), a.getDiastaseis().get(1))){
            return false;
        }
        if(b.getDiastaseis().get(0) <= a.getDiastaseis().get(0) && b.getDiastaseis().get(1) <= a.getDiastaseis().get(1)){
            return true;
        }
        return false;
    }

    public MBR getMin(){
        Double min = allMBRMindist.get(0).getMinDist();
        MBR keepM = allMBRMindist.get(0).getMbr();
        for(MBRAndMindist n: allMBRMindist){
            if(n.getMinDist()<min){
                min = n.getMinDist();
                keepM = n.getMbr();
            }
        }
        return keepM;
    }

    public void calculateBBS(R_Tree rTree){
        for(MBR mbr: rTree.getRoot().getAllRectangles()){
            Double minDist = calculateMinDist(mbr);
            MBRAndMindist nodeA = new MBRAndMindist(mbr, minDist);
            allMBRMindist.add(nodeA);
        }
        MBR min = getMin();
        MBRAndMindist keep = new MBRAndMindist();
        for(MBRAndMindist m: allMBRMindist){
            if(m.getMbr().getId().equals(min.getId())){
                keep = m;
            }
        }
        allMBRMindist.remove(keep);
        bBBSRecursively(rTree, min);
    }

    public boolean seen(String id){
        for (String s: ids){
            if(s.equals(id)){
                return true;
            }
        }
        ids.add(id);
        return false;
    }

    public void bBBSRecursively(R_Tree rTree, MBR min){
//        if(seen(min.getId())){
//            flag = true;
//        }
//        if (flag){
//            System.out.println("PROBLEM WITH ID "+ min.getId());
//            return;
//        }
        System.out.println("Called for MBR: "+ min.getId());
        System.out.println("heap: ");
        if(allMBRMindist.isEmpty()){
            return;
        }
        for (MBRAndMindist m: allMBRMindist){
            System.out.println(m.getMbr().id);
        }

        if(min.isLeafRect()){
            boolean isInSkyline;
            for(LeafRecords l: min.getPeriexomeno()){
                isInSkyline = true;
                for(LeafRecords check: min.getPeriexomeno()){
                    if(!Objects.equals(l.getNode(), check.getNode())){
                        boolean dominated = isDominated(l, check);
                        if(dominated){
                            isInSkyline = false;
                        }
                    }
                }
                if (isInSkyline){
                    if(!skyline.isEmpty()){
                        for (LeafRecords leafRecords:skyline){
                            if(isDominated(l, leafRecords)){
                                isInSkyline = false;
                            }
                            if(!notInSkyline(l)){
                                isInSkyline = false;
                            }
                        }
                    }
                    if(isInSkyline) {
                        skyline.add(l);
                        boolean notKeep = false;
                        ArrayList<LeafRecords> throwAway = new ArrayList<>();
                        for(LeafRecords leafRecords: skyline){
                            notKeep = false;
                            if(isDominated(leafRecords, l)){
                                notKeep = true;
                                throwAway.add(leafRecords);
                            }
                        }
                        if(throwAway.size()!=0){
                            for(LeafRecords leafToLeave: throwAway){
                                skyline.remove(leafToLeave);
                            }
                        }
                    }
                }
            }
            MBR min2 = getMin();
            ArrayList<MBRAndMindist> k = new ArrayList<>();
            MBRAndMindist keep = new MBRAndMindist();
            int count=0;
            for(MBRAndMindist m: allMBRMindist){
                if(m.getMbr().getId().equals(min2.getId())){
                    k.add(m);
                    keep = m;
                    count ++;
                }
            }
            System.out.println("COUNT  : "+ count);
//            for (MBRAndMindist mbrAndMindist: k){
//                allMBRMindist.remove(mbrAndMindist);
//            }
            allMBRMindist.remove(keep);
            bBBSRecursively(rTree, min2);
        }

        for(Nodes nodes: rTree.getAllNodes()){

            if(nodes.getParentID().equals(min.getId())){
                for (MBR mbr: nodes.getAllRectangles()){
                    Double minDist = calculateMinDist(mbr);
                    MBRAndMindist nodea = new MBRAndMindist(mbr, minDist);
                    allMBRMindist.add(nodea);
                }
                MBR min2 = getMin();
                MBRAndMindist keep = new MBRAndMindist();
                for(MBRAndMindist m: allMBRMindist){
                    if(m.getMbr().getId().equals(min2.getId())){
                        keep = m;
                    }
                }
                allMBRMindist.remove(keep);
                bBBSRecursively(rTree, min2);
            }
        }
    }
}
