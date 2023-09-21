//import javax.swing.*;
//import java.awt.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//public class DynamicRandomColoredRectangles extends JPanel {
//
//    private List<ColoredRectangle> rectangles;
//
//    public DynamicRandomColoredRectangles(List<ColoredRectangle> rectangles) {
//        this.rectangles = rectangles;
//    }
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//
//        for (ColoredRectangle rect : rectangles) {
//            drawRectangleOutline(g, rect.getRectangle(), rect.getColor());
//            drawColorMarker(g, rect.getName(), rect.getColor(), rect.getRectangle().x + rect.getRectangle().width + 10,
//                    rect.getRectangle().y + rect.getRectangle().height / 2);
//        }
//    }
//
//    private void drawRectangleOutline(Graphics g, Rectangle rect, Color color) {
//        g.setColor(color);
//        g.drawRect(rect.x, rect.y, rect.width, rect.height);
//    }
//
//    private void drawColorMarker(Graphics g, String name, Color color, int x, int y) {
//        g.setColor(color);
//        g.fillRect(x, y - 5, 10, 10);
//        g.setColor(Color.BLACK);
//        g.drawString(name, x + 15, y + 5);
//    }
//
//    public static void main(String[] args) {
//        List<ColoredRectangle> rectangles = generateRandomRectangles(5);
//        MBR mbr = new MBR(
//                new ArrayList<>(List.of(50.0, 100.0)),
//                new ArrayList<>(List.of(150.0, 50.0))
//        );
//        createAndShowGUI(rectangles, mbr);
//    }
//
//    public static List<ColoredRectangle> generateRandomRectangles(int numRectangles) {
//        List<ColoredRectangle> rectangles = new ArrayList<>();
//        Random random = new Random();
//
//        for (int i = 0; i < numRectangles; i++) {
//            MBR mbr = new MBR(
//                    new ArrayList<>(List.of(random.nextDouble(), random.nextDouble())),
//                    new ArrayList<>(List.of(random.nextDouble(), random.nextDouble()))
//            );
//            Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
//
//            rectangles.add(new ColoredRectangle("Ορθογώνιο " + (i + 1), mbr.getDiastaseisB().get(0), mbr.getDiastaseisB().get(1),
//                    mbr.getDiastaseisA().get(0), mbr.getDiastaseisA().get(1), color));
//        }
//
//        return rectangles;
//    }
//
//    public static void createAndShowGUI(List<ColoredRectangle> rectangles, MBR mbr) {
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("Τυχαία Ορθογώνια με Περίγραμμα και Σημαδάκια");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(500, 400);
//
//            DynamicRandomColoredRectangles rectanglePanel = new DynamicRandomColoredRectangles(rectangles);
//            frame.add(rectanglePanel);
//
//            frame.setVisible(true);
//        });
//    }
//}
//
//class ColoredRectangle {
//    private String name;
//    private Rectangle rectangle;
//    private Color color;
//
//    public ColoredRectangle(String name, double x1, double y1, double x2, double y2, Color color) {
//        this.name = name;
//        this.rectangle = new Rectangle((int)x1, (int)y1, (int)(x2 - x1), (int)(y2 - y1));
//        this.color = color;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public Rectangle getRectangle() {
//        return rectangle;
//    }
//
//    public Color getColor() {
//        return color;
//    }
//}
//
////class MBR {
////    private ArrayList<Double> diastaseisA;
////    private ArrayList<Double> diastaseisB;
////
////    public MBR(ArrayList<Double> diastaseisA, ArrayList<Double> diastaseisB) {
////        this.diastaseisA = diastaseisA;
////        this.diastaseisB = diastaseisB;
////    }
////
////    public ArrayList<Double> getDiastaseisA() {
////        return diastaseisA;
////    }
////
////    public ArrayList<Double> getDiastaseisB() {
////        return diastaseisB;
////    }
////}
