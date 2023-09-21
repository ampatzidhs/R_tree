public class MBRAndMindist {
    MBR mbr;
    Double minDist;

    public MBRAndMindist(MBR mbr, Double minDist) {
        this.mbr = mbr;
        this.minDist = minDist;
    }

    public MBRAndMindist() {
        mbr = new MBR();
        minDist = 0.0;
    }

    public MBR getMbr() {
        return mbr;
    }

    public void setMbr(MBR mbr) {
        this.mbr = mbr;
    }

    public Double getMinDist() {
        return minDist;
    }

    public void setMinDist(Double minDist) {
        this.minDist = minDist;
    }
}
