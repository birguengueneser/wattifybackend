package ai.youki.wattifybackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionInfo {

    @JsonProperty("transactionHash")
    private String transactionHash ;
    @JsonProperty("isTransactionSuccessful")
    private boolean transactionSuccessful;

    public TransactionInfo(String transactionHash, boolean transactionSuccessful) {
        this.transactionHash = transactionHash;
        this.transactionSuccessful = transactionSuccessful;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public boolean isTransactionSuccessful() {
        return transactionSuccessful;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public void setTransactionSuccessful(boolean transactionSuccessful) {
        this.transactionSuccessful = transactionSuccessful;
    }
}
