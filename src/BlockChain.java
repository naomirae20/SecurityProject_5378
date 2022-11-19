import java.util.ArrayList;

public class BlockChain {
    private final ArrayList<Block> chain = new ArrayList<Block>();

    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }
}
