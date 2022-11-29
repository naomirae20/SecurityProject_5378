import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.lang.Math;
import java.util.Random;

public class Graph
{
    public int difficulty = 4;  // arbitrary
    public double searchRadius = 10.0;

    public final ArrayList<Block> graph = new ArrayList<>();
    public final ArrayList<LinkedList<Block>> neighborLists = new ArrayList<>();

    public Block addBlock(String data, int x, int y) {
        Block block = new Block(data, x, y);            // make new block
        LinkedList<Block> neighbors = tipSelect(x, y);  // tip select

        verifySignatures(neighbors);                    // tip validation of W-OTS signatures

        block.mineBlock(difficulty);                    // perform PoW
        block.generateKeys();                           // generate W-OTS signature and keys

        graph.add(block);                               // add block to graph
        neighborLists.add(neighbors);                   // add neighbors to adjacency list

        return block;
    }

    public LinkedList<Block> getNeighbors(Block block) {
        return neighborLists.get(graph.indexOf(block));
    }
    public LinkedList<Block> getNeighbors(int index) {
        return neighborLists.get(index);
    }

    public LinkedList<Block> tipSelect(int x, int y) {
        if (graph.size() == 0) { return null; }
        LinkedList<Block> tips = new LinkedList<>();
        LinkedList<Block> possibleTips = new LinkedList<>();
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

    public boolean verifySignatures(LinkedList<Block> blocks) {
        if (blocks == null) return true;
        for (Block block : blocks) {
            if (!validateSignature(block))
                return false;   // a signature didn't match
        }
        return true;    // all signatures match
    }
    public boolean validateSignature(Block block) {
        byte[][] sig = new byte[32][];
        for (int i = 0; i < block.signature.length; i++)
            sig[i] = block.signature[i].clone();                    // make local copy of signature

        for (int i = 0; i < 32; i++) {                              // for each 256-bit value
            for (int j = 0; j < (block.getHash()[i] & 0xff); j++) { // hash sig N times to get publicKey
                sig[i] = Block.SHA256(sig[i]);
            }
            if (!Arrays.equals(sig[i], block.publicKeys[i])) {      // check sig matches publicKey
                System.out.println("\nWARNING: Validation failed on this block: \n" + block);
                return false;
            }
        }
        System.out.println("Validation succeeded on this block: \n" + block);
        return true; // signature matches
    }
}
