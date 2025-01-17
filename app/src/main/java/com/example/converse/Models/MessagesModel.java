package com.example.converse.Models;

public class MessagesModel {

    private String uid, message, messageId, senderURL, receiverURL, senderId, receiverId;
    private Long timestamp, scheduledTimestamp;
    private String status;

    public MessagesModel(String uid, String message, Long timestamp) {
        this.uid = uid;
        this.message = message;
        this.timestamp = timestamp;
    }

    public MessagesModel(String messageId, String message, String senderId, String receiverId, long timestamp) {
        this.messageId = messageId;
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
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

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
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

    public Long getScheduledTimestamp() {
        return scheduledTimestamp;
    }

    public void setScheduledTimestamp(Long scheduledTimestamp) {
        this.scheduledTimestamp = scheduledTimestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSenderURL() {
        return senderURL;
    }

    public void setSenderURL(String senderURL) {
        this.senderURL = senderURL;
    }

    public String getReceiverURL() {
        return receiverURL;
    }

    public void setReceiverURL(String receiverURL) {
        this.receiverURL = receiverURL;
    }
}
