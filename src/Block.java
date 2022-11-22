import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Random;

class Block
{
    final int x;
    final int y;
    final String data;
    final double timestamp = Instant.now().getEpochSecond();
    byte[][] publicKeys = new byte[32][];
    byte[][] signature = new byte[32][];

    private final byte[][] privateKeys = new byte[32][];
    private int nonce = 0;

    private byte[] hash;

    private int validators = 0;

    // set local variables
    public Block(String data, int x, int y) {
        this.data = data;
        this.x = x;
        this.y = y;
        hash = calculateHash();
    }

    // calculate SHA256 hash of (sum of all local vars)
    public byte[] calculateHash() {
        String str = x + y + timestamp + data + nonce;

        return SHA256(str.getBytes(StandardCharsets.UTF_8)); // gets SHA-256 hash in bytes
    }

    public byte[] getHash() {
        return hash;
    }

    public String getHashString() {
        return bytesToHex(hash);
    }
    
    // PoW
    void mineBlock(int difficulty) {
        assert(difficulty > 0 && difficulty < 20);
        String offset = new String(new char[difficulty]).replace("\0", "0"); // makes char array, replaces null terms with 0s, makes string of 0s

        while(!getHashString().startsWith(offset)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block mined: " + bytesToHex(hash));
    }

    public void incrementValidators() {
        validators++;
    }

    public int validatorCount() {
        return validators;
    }

    // generates signature and keys
    public void generateKeys() {
        Random rand = new Random();
        for (int i = 0; i < 32; i++) {
            privateKeys[i] = new byte[32];
            rand.nextBytes(privateKeys[i]); //generate 256-bit private keys
        }

        for (int i = 0; i < 32; i++) {
            publicKeys[i] = privateKeys[i];
            for (int j = 0; j < 256; j++) {             // hash public key 256 times
                publicKeys[i] = SHA256(publicKeys[i]);
                if (j == 255-(hash[i] & 0xff))          // grab signature when has hashed enough times
                    signature[i] = publicKeys[i];       // signature is hashed 256-N times (N is hash[i])
            }
        }
    }

    static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();     // StringBuilder to save re-allocating the String every loop
        for (byte b : bytes) {                      // Convert each byte to hex
            result.append(String.format("%02x", b));
        }
        return result.toString(); // return hash as hexadecimal string
    }

    public static byte[] SHA256(byte[] input) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");  // grabs the SHA-256 algorithm
        } catch (NoSuchAlgorithmException e) {
            System.out.println("HASHING ERROR: " + e.getMessage());
        }
        assert md != null;

        return md.digest(input); // gets SHA-256 hash in bytes
    }

    public String toString() {
        return "/----------------\\\n" +
                "At ("+x+", " +y+")\n" +
                "data: " + data + "\n" +
                "timestamp: " + timestamp + "\n" +
                "validators: " + validators + "\n" +
                "\\----------------/\n";
    }
}
