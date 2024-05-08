package com.blockchain.backend.Model;

public class InputCase {
    public String transactionOutputId;
    public OutputCase UTXO;

    public InputCase(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }
}
