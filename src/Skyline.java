import java.util.ArrayList;
import java.util.Objects;

public class Skyline {
    ArrayList<MBRAndMindist> allMBRMindist;
    ArrayList<LeafRecords> skyline;
    ArrayList<String> ids;
    boolean flag;
    boolean otherFlag;

    public Skyline() {
        this.skyline = new ArrayList<>();
        this.allMBRMindist = new ArrayList<>();
        this.ids = new ArrayList<>();
        this.flag = false;
        this.otherFlag = false;
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
//        for (MBRAndMindist m:allMBRMindist){
//            System.out.println("IN MBRAND MINDIST before: "+m.getMbr().getId());
//        }

        for (MBR mbr : rTree.getRoot().getAllRectangles()) {
            Double minDist = calculateMinDist(mbr);
            MBRAndMindist nodeA = new MBRAndMindist(mbr, minDist);
            allMBRMindist.add(nodeA);
            //ids.add(nodeA.getMbr().getId());
        }


        while(!allMBRMindist.isEmpty()) {
            MBR min = getMin();
            MBRAndMindist keep = new MBRAndMindist();
            for (MBRAndMindist m : allMBRMindist) {
                if (m.getMbr().getId().equals(min.getId())) {
                    keep = m;
//                    System.out.println("Happened");
                }
            }
            allMBRMindist.remove(keep);
//            for (MBRAndMindist m : allMBRMindist) {
//                System.out.println("IN MBRAND MINDIST: " + m.getMbr().getId());
//            }
            bBBSRecursively(rTree, min);
            //calculateBBS(rTree);
        }
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

//        System.out.println("Called for MBR: "+ min.getId());
 //       System.out.println("heap: ");

//        for (MBRAndMindist m: allMBRMindist){
//            System.out.println(m.getMbr().id);
//        }

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
        }

        for(Nodes nodes: rTree.getAllNodes()){

            if(nodes.getParentID().equals(min.getId())){
//                System.out.println("Vrike ton gonea");
                for (MBR mbr: nodes.getAllRectangles()){
                    Double minDist = calculateMinDist(mbr);
                    MBRAndMindist nodea = new MBRAndMindist(mbr, minDist);
                    allMBRMindist.add(nodea);
                    //ids.add(mbr.getId());
                }
            }
        }
    }

    public void seiriaka(){
        AllBlocks allBlock=new AllBlocks();
        ArrayList<Block> blocks=allBlock.readFromBinaryFile();
        ArrayList<LeafRecords> inLeaves = new ArrayList<>();
        ArrayList<LeafRecords> help = new ArrayList<>();

        for(Block b:blocks){
            for(Record rec: b.oneBlock){
                LeafRecords leaf = new LeafRecords(rec);
                inLeaves.add(leaf);
            }
        }

        for (LeafRecords l1:inLeaves){
            boolean notDom = true;
            for(LeafRecords l2: inLeaves){
                if(!Objects.equals(l1.getNode(), l2.getNode())) {
                    if (isDominated(l1, l2)) {
                        notDom = false;
                    }
                }
            }
            if(notDom){
                boolean alreadythere = false;
                for(LeafRecords k: help){
                    if(Objects.equals(k.getNode(), l1.getNode())){
                        alreadythere = true;
                    }
                }
                if(!alreadythere){
//                    System.out.println("NOT DOMINATED ID "+ l1.getNode());
//                    System.out.println("l1: " + l1.getDiastaseis().get(0) +" "+ l1.getDiastaseis().get(1));
                    help.add(l1);
                }
//                boolean exists = false;
//                for(LeafRecords l: sky){
//                    if(Objects.equals(l1.getNode(), l.getNode())){
//                        exists = true;
//                    }
//                }
//                if(!exists){
//                    System.out.println("HEREEEEEEEE " + l1.getNode() + "NOT WRITTEN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                }


            }
        }
    }
}
