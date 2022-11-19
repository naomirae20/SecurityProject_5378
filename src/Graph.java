import java.util.ArrayList;
import java.util.LinkedList;

public class Graph {
    public int difficulty = 4;

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
        //TODO tip selection
        return null;
    }

    public void addBlock(String data, int x, int y) {
        Block block = new Block(data, x, y);            // make new block
        LinkedList<Block> neighbors = tipSelect(x, y);  // tip select

        block.mineBlock(difficulty);                    // perform PoW

        graph.add(block);                               // add block to graph
        neighborLists.add(neighbors);                   // add neighbors to adjacency list
    }

}
