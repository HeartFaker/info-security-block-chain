package com.blockchain.backend.Service;

import com.blockchain.backend.Model.*;
import org.springframework.stereotype.Service;

import java.security.Security;
import java.util.*;

@Service
public class BlockService {
    public static HashMap<String, Participants> participantsHashMap = new HashMap<>();
    public BlockChain blockChain = new BlockChain();
    public List<Transaction> allTransactions = new ArrayList<>();
    public List<Map<String, Object>> products = new ArrayList<>();
    public Block createGenesis(){
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Participants participantsA = new Participants("genesis");
        Participants coinbase = new Participants("coinbase");
        participantsHashMap.put("coinbase", coinbase);
        participantsHashMap.put("genesis", participantsA);
        Transaction genesisTransaction = new Transaction(coinbase.publicKey, participantsA.publicKey,100f, new ArrayList<>(), "This is Genesis block", "coinbase", "genesis");
        genesisTransaction.generateSignature(coinbase.privateKey);
        genesisTransaction.transactionId = "genesis";
        Block genesis = new Block("0");
        genesis.addTransaction(genesisTransaction);
        allTransactions.add(genesisTransaction);
        blockChain.addBlock(genesis);
        return genesis;
    }

    public Block addBlock(String info, String fromName, String toName, float value){
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Participants participantA = participantsHashMap.getOrDefault(fromName, new Participants(fromName));
        Participants participantB = participantsHashMap.get(toName);
        if (participantB == null) {
            // 为了模拟交易场景，新建账户，系统自动发100币
            participantB = new Participants(toName);
            Participants coinbase = new Participants("coinbase");
            Transaction genesisTransaction = new Transaction(coinbase.publicKey, participantB.publicKey,100f, new ArrayList<>(), "coinbase auto send money", "coinbase", toName);
            genesisTransaction.generateSignature(coinbase.privateKey);
            genesisTransaction.transactionId = "coinbase auto send money";
            genesisTransaction.outputCases.add(new OutputCase(genesisTransaction.receiver, genesisTransaction.value, genesisTransaction.transactionId));
            BlockChain.UTXOs.put(genesisTransaction.outputCases.get(0).id, genesisTransaction.outputCases.get(0)); //its important to store our first transaction in the UTXOs list.
            Block newB = new Block(blockChain.getLastBlockHash());
            newB.addTransaction(genesisTransaction);
            allTransactions.add(genesisTransaction);
            blockChain.addBlock(newB);
            System.out.println(participantB.getBalance());
        }
        Block newBlock = new Block(blockChain.getLastBlockHash());
        System.out.println(value);
        if(value==0f){
            Transaction sendFunds = participantB.sendFunds(participantA.publicKey, value, info, fromName, toName);
            newBlock.addTransaction(sendFunds);
            allTransactions.add(sendFunds);
        }else{
            Transaction sendFunds = participantB.sendFunds(participantA.publicKey, value, info, toName, fromName);
            newBlock.addTransaction(sendFunds);
            allTransactions.add(sendFunds);
        }
        participantsHashMap.put(fromName, participantA);
        participantsHashMap.put(toName, participantB);
        blockChain.addBlock(newBlock);
        return newBlock;
    }

    public boolean checkChainValid(){
        return blockChain.isChainValid();
    }

    public float queryBalance(String name){
        Participants participant = participantsHashMap.get(name);
        if(participant == null){
            return -1;
        }
        return participant.getBalance();
    }

    public List<Map<String, Object>> queryParticipant(){
        Set<String> keys = participantsHashMap.keySet();
        List<Map<String, Object>> participantList = new ArrayList<>();
        // 遍历并输出所有键
        for (String key : keys) {
            Map<String, Object> participant = new HashMap<>();
            participant.put("name", key); // Add participant name
            participant.put("balance", participantsHashMap.get(key).getBalance()); // Add participant balance
            participant.put("privateKey", participantsHashMap.get(key).privateKey);
            participant.put("publicKey", participantsHashMap.get(key).publicKey);
            participantList.add(participant); // Add participant object to the list
        }
        return participantList;
    }

    public ArrayList<Block> getBlockChain(){
        return BlockChain.blockchain;
    }

    public List<Transaction> queryTransactions(){
        return allTransactions;
    }

    public List<Map<String, Object>> addProducts(String name) {
        Map<String, Object> product = new HashMap<>();
        product.put("name", name);
        product.put("date", new Date());
        products.add(product);
        return products;
    }
}
