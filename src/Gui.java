import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Gui extends JPanel {

    private final double margin = 50.0;
    private final double block_thickness_in_pixels = 10; 
    private final double number_of_partitions = 12.0;

    private Graph graph;
    private final int total_blocks;
    private final double search_radius;
    private double max_x_coordinate_value;
    private double max_y_coordinate_value;
    private ArrayList<double[]> coordinate_pairs;

    public Gui(Graph g, double x, double y, double radius, int total_blocks) {
        this.graph = g;
        this.max_x_coordinate_value = x;
        this.max_y_coordinate_value = y;
        this.search_radius = radius;
        this.total_blocks = total_blocks;
        coordinate_pairs = new ArrayList<>();
    }

    public void add_coordinate_pair(double x, double y) {
        double[] new_pair = {x, y};
        coordinate_pairs.add(new_pair);
    }

    private void paint_tips(Graphics2D Gs, double width, double height) {
        for (int i = 0; i < graph.neighborLists.size(); i++) {
            if (graph.getNeighbors(i) != null) {
                double x1 = (coordinate_pairs.get(i)[0] / max_x_coordinate_value) * (width - 2 * margin) + margin;
                double y1 = (coordinate_pairs.get(i)[1] / max_y_coordinate_value) * (height - 2 * margin) + margin;
                for (Block b : graph.getNeighbors(i)) {
                    double x2 = (b.x / max_x_coordinate_value) * (width - 2 * margin) + margin;
                    double y2 = ((max_y_coordinate_value - b.y) / max_y_coordinate_value) * (height - 2 * margin) + margin;
                    //Gs.draw(new Line2D.Double(x1, y1, x2, y2));
                    drawArrow(Gs, x1, y1, x2, y2);
                }
            }
        }
    }

    void drawArrow(Graphics g1, double x1, double y1, double x2, double y2) { //https://stackoverflow.com/questions/4112701/drawing-a-line-with-arrow-in-java
        Graphics2D g = (Graphics2D) g1.create();

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) (Math.sqrt(dx*dx + dy*dy)-5);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);

        // Draw horizontal arrow starting in (0, 0)
        int arrowSize = 5;
        g.drawLine(0, 0, len, 0);
        g.fillPolygon(new int[] {len, len-arrowSize, len-arrowSize, len},
                new int[] {0, -arrowSize, arrowSize, 0}, 4);
    }

    private void paint_partitions(Graphics2D Gs, double width, double height) {
        Gs.setColor(Color.lightGray);
        double x_partition_length = (width - 2 * margin) / number_of_partitions;
        double y_partition_length = (height - 2 * margin) / number_of_partitions;
        for (int i = 0; i < number_of_partitions + 1; i++) {
            // X partitions
            Gs.draw(new Line2D.Double(
                (margin + (x_partition_length * i)), margin, 
                (margin + (x_partition_length * i)), (height - margin)));
            // Y partitions
            Gs.draw(new Line2D.Double(
                margin, (margin + (y_partition_length * i)), 
                (width - margin), (margin + (y_partition_length * i))));
        }
    }

    private void paint_axes(Graphics2D Gs, double width, double height) {
        Gs.setColor(Color.black);
        // X axis
        Gs.draw(new Line2D.Double(
            margin, margin, margin, (height - margin)));
        // Y axis
        Gs.draw(new Line2D.Double(
            margin, (height - margin), (width - margin), (height - margin)));
    }

    private void plot_points(Graphics2D Gs, double width, double height) {
        for (double[] pair : coordinate_pairs) {
            double x = pair[0], y = pair[1];
            double scaled_x = (
                (x / max_x_coordinate_value) * (width - 2 * margin)) + margin;
            double scaled_y = (
                (y / max_y_coordinate_value) * (height - 2 * margin)) + margin;
            double adjusted_x = scaled_x - (block_thickness_in_pixels / 2.0);
            double adjusted_y = scaled_y - (block_thickness_in_pixels / 2.0);
            if (pair == coordinate_pairs.get(coordinate_pairs.size() - 1)) {
                Gs.setColor(Color.red);
                if (coordinate_pairs.size() == total_blocks) {
                    Gs.setColor(Color.green);
                }
                paint_most_recent(Gs, width, height, scaled_x, scaled_y);
            }
            Gs.fill(new Ellipse2D.Double(adjusted_x, adjusted_y, block_thickness_in_pixels, block_thickness_in_pixels));
        }
    }

    private void paint_most_recent(Graphics2D Gs, double width, double height, double x, double y) {
        double search_radius_width = 2 * ((search_radius / max_x_coordinate_value) * (width - 2 * margin));
        double search_radius_height = 2 * ((search_radius / max_y_coordinate_value) * (height - 2 * margin));
        double srx = x - search_radius_width / 2.0;
        double sry = y - search_radius_height / 2.0;
        Gs.draw(new Ellipse2D.Double(srx, sry, search_radius_width, search_radius_height));
    }

    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D Gs = (Graphics2D) graphics;
        Gs.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
        );
        // Width and height of the JFrame
        double width = this.getWidth();
        double height = this.getHeight();

        paint_partitions(Gs, width, height);
        paint_axes(Gs, width, height);
        paint_tips(Gs, width, height);
        plot_points(Gs, width, height);

    }
}