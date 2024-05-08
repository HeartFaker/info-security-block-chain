package com.blockchain.backend.Model;

import com.blockchain.backend.Util.SHAUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Block {
    public String hash;
    public String previousHash;
    public String merkleRoot;
    public ArrayList<Transaction> transactions = new ArrayList<>();
    public long timeStamp;
    public int nonce;

    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash() {
        return SHAUtil.SHA256(previousHash + timeStamp + nonce + merkleRoot);
    }

    public void mineBlock(int difficulty) {
        merkleRoot = MerkleRoot(transactions);
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
    }

    public static String MerkleRoot(ArrayList<Transaction> transactions) {
        if (transactions.isEmpty()) {
            return "";
        }
        List<String> treeLayer = new ArrayList<>();
        for (Transaction transaction : transactions) {
            treeLayer.add(transaction.transactionId);
        }
        while (treeLayer.size() > 1) {
            List<String> newTreeLayer = new ArrayList<>();
            int i = 0;
            while (i < treeLayer.size()) {
                // Combine two adjacent transaction IDs and hash them
                String left = treeLayer.get(i);
                String right = (i + 1 < treeLayer.size()) ? treeLayer.get(i + 1) : left; // Duplicate the last node if necessary
                newTreeLayer.add(SHAUtil.SHA256(left + right));
                i += 2;
            }
            treeLayer = newTreeLayer;
        }
        return treeLayer.get(0); // Return the root of the tree
    }

    public void addTransaction(Transaction transaction) {
        if (transaction == null) return;
        if(!(transaction.sender == null || transaction.receiver == null)){
            if ((!Objects.equals(previousHash, "0"))) {
                if ((!transaction.processTransaction())) {
                    return;
                }
            }
        }
        transactions.add(transaction);
    }
}
