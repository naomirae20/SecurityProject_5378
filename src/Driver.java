import java.util.Random;
import java.util.Scanner;

public class Driver {
    public static void displayGraph(Graph g) {
        int gridWidth = 10;
        int gridLength = 50;
        for (int i = 0; i < gridWidth + 3; i++) {
            for (int j = 0; j < gridLength + 3; j++) {
                if (i == 0 || j == 0 || i == gridWidth + 2 || j == gridLength + 2) {
                    System.out.print("*");
                    if (j == gridLength + 2) {
                        System.out.println();
                    }
                }
                else {
                    char c = ' ';
                    for (Block b : g.graph) {
                        if (j == b.x && i == b.y) {
                            c = 'O';
                        }
                    }
                    System.out.print(c);
                }
            }
        }
    }
    public static void main(String[] args) {
        Graph g = new Graph();
        Random rand = new Random();
        for (int i = 0; i < 20; i++) {
            int r1 = rand.nextInt(50) + 1;
            int r2 = rand.nextInt(10) + 1;
            g.addBlock("anything", r1, r2);
            displayGraph(g);
            System.out.println();
        }

        System.out.println("Now you can manually enter new block data: ");
        Scanner in = new Scanner(System.in);
        try{
            while (true) {
                System.out.print("Data: ");
                String data = in.nextLine();
                if (data.equalsIgnoreCase("q")) return;
                System.out.print("x: ");
                int x = Integer.parseInt(in.nextLine());
                System.out.print("y: ");
                int y = Integer.parseInt(in.nextLine());
                
                g.addBlock(data, x, y);
                displayGraph(g);
            }
        } finally{
            in.close();
        }
        
    }
}
