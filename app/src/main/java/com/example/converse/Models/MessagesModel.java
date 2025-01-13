package com.example.converse.Models;

public class MessagesModel {

    String uid, message;
    Long timestamp;
    String messageId;
    String senderURL;

    public String getReceiverURL() {
        return receiverURL;
    }

    public void setReceiverURL(String receiverURL) {
        this.receiverURL = receiverURL;
    }

    public String getSenderURL() {
        return senderURL;
    }

    public void setSenderURL(String senderURL) {
        this.senderURL = senderURL;
    }

    String receiverURL;

    public MessagesModel(String uid, String message, Long timestamp) {
        this.uid = uid;
        this.message = message;
        this.timestamp = timestamp;
    }

    public MessagesModel(String uid, String message) {
        this.uid = uid;
        this.message = message;
    }


    public MessagesModel() {
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
