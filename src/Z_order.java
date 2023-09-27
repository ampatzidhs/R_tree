public class Z_order {


    public String decToBin(double dec) {
        long intPart = (long) dec;
        double fracPart = dec - intPart;

        String intBinary = Long.toBinaryString(intPart);
        String fracBinary = "";

        while (fracPart > 0 && fracBinary.length() < 32) {
            fracPart *= 2;
            if (fracPart >= 1) {
                fracBinary += '1';
                fracPart -= 1;
            } else {
                fracBinary += '0';
            }
        }
        String toRet = "" + intBinary;
        if(fracBinary.length() > 0){
            toRet += "." + fracBinary;
        }
        return toRet;
    }

    public String combine(String x, String y){
        String[] partsX = x.split("\\.");
        String intPartX = partsX[0];
        String fracPartX = partsX.length > 1 ? partsX[1] : "0";

        String[] partsY = y.split("\\.");
        String intPartY = partsY[0];
        String fracPartY = partsY.length > 1 ? partsY[1] : "0";

        String res = "";
        String extra = "";
        if (intPartX.length() > intPartY.length()) {
            extra = "";
            for(int i=intPartY.length();i<intPartX.length();i++){
                extra += "0";
            }
            intPartY = extra + intPartY;
           // System.out.println(y + " after transition: " + intPartY);
        }
        else if(intPartY.length() > intPartX.length()){
            extra = "";
            for(int i=intPartX.length();i<intPartY.length();i++){
                extra += "0";
            }
            intPartX = extra + intPartX;
            //System.out.println(x + " after transition: " + intPartX);
        }

        for(int i=0;i<intPartX.length();i++){
            res += intPartX.charAt(i);
            res += intPartY.charAt(i);
        }

        res += ".";

        if (fracPartX.length() > fracPartY.length()) {
            extra = "";
            for(int i=fracPartY.length();i<fracPartX.length();i++){
                extra += "0";
            }
            fracPartY = extra + fracPartY;
        }
        else if(fracPartY.length() > fracPartX.length()){
            extra = "";
            for(int i=fracPartX.length();i<fracPartY.length();i++){
                extra += "0";
            }
            fracPartX = extra + fracPartX;
        }

        for(int i=0;i<fracPartX.length();i++){
            res += fracPartX.charAt(i);
            res += fracPartY.charAt(i);
        }
        return res;
    }

    public double convertBinaryToDecimal(String binary) {
        String[] parts = binary.split("\\.");
        String intPart = parts[0];
        String fracPart = parts.length > 1 ? parts[1] : "0";

        double intDecimal = 0;
        double fracDecimal = 0;

        for (int i = 0; i < intPart.length(); i++) {
            if (intPart.charAt(i) == '1') {
                intDecimal += Math.pow(2, intPart.length() - 1 - i);
            }
        }

        for (int i = 0; i < fracPart.length(); i++) {
            if (fracPart.charAt(i) == '1') {
                fracDecimal += Math.pow(2, -(i + 1));
            }
        }

        return intDecimal + fracDecimal;
    }
}
