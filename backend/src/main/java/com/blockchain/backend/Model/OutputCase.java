package com.blockchain.backend.Model;

import com.blockchain.backend.Util.SHAUtil;

public class OutputCase {

    public String id;
    public String receiver; //also known as the new owner of these coins.
    public float value; //the amount of coins they own
    public String parentTransactionId; //the id of the transaction this output was created in

    //Constructor
    public OutputCase(String receiver, float value, String parentTransactionId) {
        this.receiver = receiver;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = SHAUtil.SHA256(receiver+value+parentTransactionId);
    }
}
