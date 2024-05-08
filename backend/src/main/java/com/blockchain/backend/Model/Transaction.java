package com.blockchain.backend.Model;

import com.blockchain.backend.Util.SHAUtil;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;

public class Transaction {

    public String transactionId;
    public String sender;
    public String receiver;
    public String senderName;
    public String receiverName;
    public float value;
    public String info;
    public byte[] signature;

    public ArrayList<InputCase> inputCases = new ArrayList<>();
    public ArrayList<OutputCase> outputCases = new ArrayList<>();

    public Transaction(String from, String to, float value, ArrayList<InputCase> inputCases, String info, String senderName, String receiverName) {
        this.sender = from;
        this.receiver = to;
        this.value = value;
        this.inputCases = inputCases;
        this.info = info;
        this.senderName = senderName;
        this.receiverName = receiverName;
    }
    public static PrivateKey parsePrivateKeyFromString(String input){
        PrivateKey privateKey = null;
        try {
            // 将Base64编码的字符串解码为字节数组
            byte[] privateBytes = Base64.getDecoder().decode(input);

            // 将字节数组转换回PrivateKey
            PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(privateBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("ECDSA");
            privateKey = keyFactory.generatePrivate(keySpecPrivate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return privateKey;
    }
    public static PublicKey parsePublickeyFromString(String input){
        PublicKey publicKey = null;
        try {
            // 将Base64编码的字符串解码为字节数组
            byte[] publicBytes = Base64.getDecoder().decode(input);

            // 将字节数组转换回PublicKey
            X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(publicBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("ECDSA");
            publicKey = keyFactory.generatePublic(keySpecPublic);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return publicKey;
    }
    public static byte[] applyECDSA(PrivateKey privateKey, String input) {
        try {
            Signature signature = Signature.getInstance("ECDSA", "BC");
            signature.initSign(privateKey);
            signature.update(input.getBytes());
            return signature.sign();
        } catch (Exception e) {
            throw new RuntimeException("Error applying ECDSA Signature: " + e.getMessage(), e);
        }
    }
    public static boolean verifyECDSA(PublicKey publicKey, String data, byte[] signature) {
        try {
            Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            return ecdsaVerify.verify(signature);
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    public boolean processTransaction() {
        // 验证交易签名是否有效
        if (!verifyECDSA(parsePublickeyFromString(sender), sender + receiver + value, signature)) {
            System.out.println("sig fail");
            return false;
        }
        // 为每个输入找到对应的UTXO
        for (InputCase input : inputCases) {
            input.UTXO = BlockChain.UTXOs.get(input.transactionOutputId);
            if (input.UTXO == null) {
                System.out.println("UTXO fail");
                return false; // 如果任何输入没有找到有效的UTXO，则交易无效
            }
        }
        // 计算交易的输入值和输出值，检查是否平衡
        float totalinputCasesValue = getinputCasesValue();
        float balance = totalinputCasesValue - value;
        if (balance < 0) {
            return false; // 如果输出值大于输入值，交易无效
        }
        // 创建交易的输出
        outputCases.add(new OutputCase(receiver, value, SHAUtil.SHA256(sender + receiver + value))); // 接收者的输出
        if (balance > 0) {
            outputCases.add(new OutputCase(sender, balance, SHAUtil.SHA256(sender + receiver + value))); // 发送者的找零
        }
        // 更新UTXO池
        updateUTXOPool();
        return true;
    }
    private void updateUTXOPool() {
        // 添加新的输出到UTXO池
        for (OutputCase output : outputCases) {
            BlockChain.UTXOs.put(output.id, output);
        }
        // 从UTXO池中移除已经使用的输入
        for (InputCase input : inputCases) {
            if (input.UTXO != null) {
                BlockChain.UTXOs.remove(input.UTXO.id);
            }
        }
    }
    public float getinputCasesValue() {
        float total = 0;
        for(InputCase i : inputCases) {
            if(i.UTXO == null) continue;
            total += i.UTXO.value;
        }
        return total;
    }
    public void generateSignature(String privateKey) {
        PrivateKey pk = parsePrivateKeyFromString(privateKey);
        signature = applyECDSA(pk,sender + receiver + value);
    }
}
