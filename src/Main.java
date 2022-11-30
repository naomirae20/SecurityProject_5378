import javax.swing.JFrame;
import java.util.Scanner;
import java.util.Random;
//import java.util.LinkedList;    // maybe delete? idk

public class Main {

    // SEARCH_RADIUS MUST MATCH SEARCH_RADIUS IN GRAPH.JAVA
    private static final int search_radius = 10;
    private static final int number_of_blocks = 50;
    private static final int max_x_coordinate_value = 50;
    private static final int max_y_coordinate_value = 50;
    public static void main(String[] args) {

        // Graph init
        Graph g = new Graph();

        // Gui init
        JFrame frame = new JFrame(
            "CS4371 Computer Security: Final Project Group 15"
        );
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Gui gui = new Gui(
            g,
            max_x_coordinate_value, 
            max_y_coordinate_value, 
            search_radius,
            number_of_blocks
        );
        frame.add(gui);
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Wait for user to start the demo
        Scanner in = new Scanner(System.in);
        try{
            Random rand = new Random();
            System.out.println("Whenever you're ready! (Press Enter)");
            in.nextLine();

            // Demo begins
            for (int i = 0; i < number_of_blocks; i++) {
                int x_coordinate = rand.nextInt(max_x_coordinate_value);
                int y_coordinate = rand.nextInt(max_y_coordinate_value);
                System.out.println("Adding new point at: " + 
                                    x_coordinate + " " + y_coordinate + "...");
                gui.add_coordinate_pair(
                    x_coordinate, 
                    (max_y_coordinate_value - y_coordinate)
                );
                g.addBlock("anything", x_coordinate, y_coordinate);
                gui.repaint();
            }

            System.out.println("Now you can manually enter new block data: ");
            while (true){
                System.out.print("Data: ");
                String data = in.nextLine();
                if (data.equalsIgnoreCase("q")) return;
                System.out.print("x: ");
                int x = Integer.parseInt(in.nextLine());
                System.out.print("y: ");
                int y = Integer.parseInt(in.nextLine());
                gui.add_coordinate_pair(x, y);
                g.addBlock(data, x, y);
                gui.repaint();
            }
        } finally{
            in.close();
        }
    }
}