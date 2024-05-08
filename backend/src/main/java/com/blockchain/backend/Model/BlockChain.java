package com.blockchain.backend.Model;

import com.blockchain.backend.lib.configData;
import java.util.ArrayList;
import java.util.HashMap;

public class BlockChain {
    public static ArrayList<Block> blockchain = new ArrayList<>();
    public static HashMap<String, OutputCase> UTXOs = new HashMap<>();
    public String getLastBlockHash(){
        return blockchain.get(blockchain.size() - 1).hash;
    }
    public boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        for(int i=1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);
            if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
                return false;
            }
            if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
                return false;
            }
        }
        return true;
    }
    public void addBlock(Block newBlock) {
        newBlock.mineBlock(configData.difficulty);
        blockchain.add(newBlock);
    }
}
