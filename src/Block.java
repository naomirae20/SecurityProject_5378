import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

class Block
{
    final int x;
    final int y;

    private final String data;
    private final double timestamp = Instant.now().getEpochSecond();
    private String hash;
    private int nonce = 0;
    
    // set local variables
    public Block(String data, int x, int y) {
        this.data = data;
        this.x = x;
        this.y = y;
        hash = calculateHash();
    }

    // calculate SHA256 hash of (sum of all local vars)
    public String calculateHash() {
        String str = x + y + timestamp + data + nonce;

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");  // grabs the SHA-256 algorithm
        } catch (NoSuchAlgorithmException e) {
            System.out.println("HASHING ERROR: " + e.getMessage());
        }
        assert md != null;
        byte[] hashBytes = md.digest(str.getBytes(StandardCharsets.UTF_8)); // gets SHA-256 hash in bytes

        StringBuilder result = new StringBuilder();     // StringBuilder to save re-allocating the String every loop
        for (byte b : hashBytes) {                      // Convert each byte to hex
            result.append(String.format("%02x", b));
        }
        return result.toString(); // return hash as hexadecimal string
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
