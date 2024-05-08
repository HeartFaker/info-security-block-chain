package com.blockchain.backend.Model;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.*;

public class Participants {

    public String privateKey;
    public String publicKey;
    public String name;

    public Participants(String name) {
        generateKeys();
        this.name = name;
    }
    public HashMap<String, OutputCase> UTXOs = new HashMap<>();

    public void generateKeys() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            keyGen.initialize(ecSpec, random);
            KeyPair keyPair = keyGen.generateKeyPair();
            // 使用 Base64 编码密钥，方便之后反序列化
            privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
            publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());

        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public float getBalance() {
        float balance = 0;
        for (Map.Entry<String, OutputCase> item: BlockChain.UTXOs.entrySet()){
            OutputCase UTXO = item.getValue();
            System.out.println(UTXO);
            if(Objects.equals(publicKey, UTXO.receiver)) { //if output belongs to me ( if coins belong to me )
                UTXOs.put(UTXO.id,UTXO); //add it to our list of unspent transactions.
                balance += UTXO.value ;
            }
        }
        return balance;
    }

    public Transaction sendFunds(String receiver,float value, String info, String senderName, String toName ) {
        if(getBalance() < value) {
            return null;
        }
        ArrayList<InputCase> inputCases = new ArrayList<InputCase>();

        float total = 0;
        for (Map.Entry<String, OutputCase> item: UTXOs.entrySet()){
            OutputCase UTXO = item.getValue();
            total += UTXO.value;
            inputCases.add(new InputCase(UTXO.id));
            if(total > value) break;
        }

        Transaction newTransaction = new Transaction(publicKey, receiver , value, inputCases, info, senderName, toName);
        newTransaction.generateSignature(privateKey);
        for(InputCase inputCase : inputCases){
            UTXOs.remove(inputCase.transactionOutputId);
        }
        return newTransaction;
    }
}
