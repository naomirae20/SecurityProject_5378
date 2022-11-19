class Block
{
    int index;
    double time;
    String data;
    String previousHash;
    int nonce;
    
    // set local variables
    public Block(int index, double time, String data, String previousHash, int nonce) {
      this.index = index;
      this.time = time;
      this.data = data;
      this.previousHash = previousHash;
      this.nonce = nonce;
    }
    
    // calculate SHA256 hash of (sum of all local vars)
    public String calculateHash() {
      return null; //TODO
    }
    
    // PoW
    void mineBlock(int difficulty) {
      //TODO
    }
  
}
