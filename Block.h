#include "openssl/sha.h"

class Block
{
  private:
  
    int index;
    double time;
    std::string data;
    unsigned char* previousHash;
    int nonce;
  
  public:
    
    // set local variables
    Block (int _index, double _time, std::string _data, unsigned char* _previousHash);
    
    // calculate SHA256 hash of (sum of all local vars)
    unsigned char* calculateHash();
    
    // PoW
    void mineBlock(unsigned int difficulty);
  
}
