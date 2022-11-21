import java.util.ArrayList;
import java.util.LinkedList;
import java.lang.Math;
import java.util.Random;

public class Graph {
    public int difficulty = 4;
    public double searchRadius = 4.0;

    private final ArrayList<Block> graph = new ArrayList<Block>();
    private final ArrayList<LinkedList<Block>> neighborLists = new ArrayList<>();

    public Block getLatestBlock() {
        return graph.get(graph.size() - 1);
    } //may not need
    public LinkedList<Block> getNeighbors(Block block) {
        return neighborLists.get(graph.indexOf(block));
    }
    public LinkedList<Block> getNeighbors(int index) {
        return neighborLists.get(index);
    }
    public int addNeighbor(Block origin, Block target) {
        LinkedList<Block> neighbors = getNeighbors(origin);
        neighbors.add(target);
        return neighbors.size();
    } //may not need

    public LinkedList<Block> tipSelect(int x, int y) {
        if (graph.size() == 0) { return null; }
        LinkedList<Block> tips = new LinkedList<Block>();
        LinkedList<Block> possibleTips = new LinkedList<Block>();
        for (Block b : graph) {
            double distance = 
                Math.sqrt(Math.pow((x - b.x), 2) + Math.pow((y - b.y), 2));
            if ((distance <= searchRadius) && (b.validatorCount() < 2)) {
                possibleTips.add(b);
            }
        }
        if (possibleTips.size() == 0) { return null; }
        if (possibleTips.size() <= 2) {
            possibleTips.get(0).incrementValidators();
            tips.add(possibleTips.get(0));
            if (possibleTips.size() == 2) {
                possibleTips.get(1).incrementValidators();
                tips.add(possibleTips.get(1));
            }
            return tips;
        }
        Random rand = new Random();
        int r1, r2;
        r1 = r2 = rand.nextInt(possibleTips.size());
        while (r1 == r2) {
            r2 = rand.nextInt(possibleTips.size());
        }
        possibleTips.get(r1).incrementValidators();
        possibleTips.get(r2).incrementValidators();
        tips.add(possibleTips.get(r1));
        tips.add(possibleTips.get(r2));
        return tips;
    }

    public void addBlock(String data, int x, int y) {
        Block block = new Block(data, x, y);            // make new block
        LinkedList<Block> neighbors = tipSelect(x, y);  // tip select

        block.mineBlock(difficulty);                    // perform PoW

        graph.add(block);                               // add block to graph
        neighborLists.add(neighbors);                   // add neighbors to adjacency list
    }

}
