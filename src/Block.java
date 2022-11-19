import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

class Block
{
    private final double timestamp = Instant.now().getEpochSecond();
    private final int index;
    private final String data;
    private final String previousHash;
    private String hash;
    private int nonce = 0;
    
    // set local variables
    public Block(int index, String data, String previousHash) {
        this.index = index;
        this.data = data;
        this.previousHash = previousHash;
    }

    // calculate SHA256 hash of (sum of all local vars)
    public String calculateHash() {
        String str = index + timestamp + data + previousHash + nonce;

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("ERROR: Error with SHA-256 hashing. " + e.getMessage());
        }
        assert md != null;
        byte[] hashBytes = md.digest(str.getBytes(StandardCharsets.UTF_8)); // get hash in bytes

        StringBuilder result = new StringBuilder();     // StringBuilder to save re-allocating the String every loop
        for (byte b : hashBytes) {                      // Convert each byte to hex
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
    
    // PoW
    void mineBlock(int difficulty) {
        assert(difficulty > 0 && difficulty < 20);
        String offset = new String(new char[difficulty]).replace("\0", "0"); // makes char array, replaces null terms with 0s, makes string of 0s

        while(!hash.startsWith(offset)) {
            nonce++;
            hash = calculateHash();
        }

        System.out.println("Block mined: " + hash);
    }
  
}
