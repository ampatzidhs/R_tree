import java.util.ArrayList;

public class Insertion {
    //calculates the eukleidian diastance
    public Double distance(Double x1, Double y1, Double x2, Double y2){
        double c = Math.pow(x2-x1,2) + Math.pow(y2-y1,2);
        return Math.sqrt(c);
    }

    //We have the bottom left corner (x1,y1) and the top right (x2,y2)
    //and we will find the top left and bottom right
    public ArrayList<Double> findCorners(Double x1, Double y1, Double x2, Double y2){
        Double topLeftX = x1;
        Double topLeftY = y2;
        Double bottomRightX = x2;
        Double bottomRightY = y1;

        ArrayList<Double> toRet = new ArrayList<>();
        toRet.add(topLeftX);
        toRet.add(topLeftY);
        toRet.add(bottomRightX);
        toRet.add(bottomRightY);

        return toRet;
    }

    public Double perimeter(Double x1, Double y1, Double x2, Double y2){
        ArrayList<Double> otherCorners = findCorners(x1, y1, x2, y2);
        Double dist1 = distance(x1,y1,otherCorners.get(0),otherCorners.get(1));
        Double dist2 = distance(x1,y1,otherCorners.get(2),otherCorners.get(3));

        return 2*dist1 + 2*dist2;
    }

    public Double emvadon(Double x1, Double y1, Double x2, Double y2){
        ArrayList<Double> otherCorners = findCorners(x1, y1, x2, y2);
        Double dist1 = distance(x1,y1,otherCorners.get(0),otherCorners.get(1));
        Double dist2 = distance(x1,y1,otherCorners.get(2),otherCorners.get(3));

        return dist1*dist2;
    }

    public boolean isInRect(MBR mbr, Double x, Double y){
        if(x<mbr.getDiastaseisA().get(0) || x>mbr.getDiastaseisB().get(0)){
            return false;
        }
        if(y<mbr.getDiastaseisA().get(1) || y>mbr.getDiastaseisB().get(1)){
            return false;
        }
        return true;
    }

    public ArrayList<Double> newCorners(MBR mbr, Double x, Double y){
        ArrayList<Double> toRet = new ArrayList<>();
        if(isInRect(mbr,x,y)){
            toRet.add(mbr.getDiastaseisA().get(0));
            toRet.add(mbr.getDiastaseisA().get(1));
            toRet.add(mbr.getDiastaseisB().get(0));
            toRet.add(mbr.getDiastaseisB().get(1));
        }

        if(x<mbr.getDiastaseisA().get(0)){
            toRet.add(x);
        }
        else {
            toRet.add(mbr.getDiastaseisA().get(0));
        }

        if(y<mbr.getDiastaseisA().get(1)){
            toRet.add(y);
        }
        else {
            toRet.add(mbr.getDiastaseisA().get(1));
        }

        if(x>mbr.getDiastaseisB().get(0)){
            toRet.add(x);
        }
        else {
            toRet.add(mbr.getDiastaseisB().get(0));
        }

        if(y>mbr.getDiastaseisB().get(1)){
            toRet.add(y);
        }
        else {
            toRet.add(mbr.getDiastaseisB().get(1));
        }

        return toRet;
    }

    public String chooseRectangle(Nodes nodes, Double x, Double y){
        Double min = 100000000.0;
        System.out.println("min beggining: " + min);
        String keepMinID = "";
        Double keepMinBefore = 0.0;
        for (MBR mbr:nodes.getAllRectangles()) {
            System.out.println("mbr:: " + mbr.getId());
            if (!mbr.getId().equals("")) {


                Double epifaneia = emvadon(mbr.getDiastaseisA().get(0), mbr.getDiastaseisA().get(1), mbr.getDiastaseisB().get(0), mbr.getDiastaseisB().get(1));
                System.out.println("Emvadon: " + epifaneia);
                ArrayList<Double> newCo = newCorners(mbr, x, y);
                Double newEpifaneia = emvadon(newCo.get(0), newCo.get(1), newCo.get(2), newCo.get(3));
                Double diafora = newEpifaneia - epifaneia;
                System.out.println("Diafora - min: " + diafora + " - " + min);
                if (diafora < min) {
                    min = diafora;
                    keepMinID = mbr.getId();
                    keepMinBefore = epifaneia;
                } else if (diafora.equals(min)) {
                    if (epifaneia < keepMinBefore) {
                        min = diafora;
                        keepMinID = mbr.getId();
                        keepMinBefore = epifaneia;
                    }
                }
            }
        }
        return keepMinID;
    }


    public static void main(String []args){
//        MBR mbr = new MBR();
//        ArrayList<Double> bottomLeft = new ArrayList<>();
//        ArrayList<Double> topRight = new ArrayList<>();
//        bottomLeft.add(1.0);
//        bottomLeft.add(1.0);
//        topRight.add(3.0);
//        topRight.add(2.0);
//
//        mbr.setDiastaseisA(bottomLeft);
//        mbr.setDiastaseisB(topRight);
//
//        boolean c = isInRect(mbr, 1.0, 1.0);
//
//        System.out.println(c);
//        ArrayList<Double> l = newCorners(mbr, 1.0, 1.0);
//        if(l.isEmpty()){
//            System.out.println("IDIES AKRES");
//        }
//        for (Double d:l){
//            System.out.println(d);
//        }
//
//        MBR m2 =new MBR();
//        ArrayList<Double> tmpa = new ArrayList<>();
//        tmpa.add(l.get(0));
//        tmpa.add(l.get(1));
//        ArrayList<Double> tmpb = new ArrayList<>();
//        tmpb.add(l.get(2));
//        tmpb.add(l.get(3));
//        m2.setDiastaseisA(tmpa);
//        m2.setDiastaseisB(tmpb);
//        System.out.println("NEW EPIFANEIA: " + emvadon(mbr.getDiastaseisA().get(0), mbr.getDiastaseisA().get(1), mbr.getDiastaseisB().get(0), mbr.getDiastaseisB().get(1)) + " - " + emvadon(m2.diastaseisA.get(0), m2.diastaseisA.get(1), m2.diastaseisB.get(0), m2.diastaseisB.get(1)));


    }
}
