package com.light.niepaper.Model;

import java.util.Date;

/**
 * Created by sayo on 5/20/2017.
 */

public class ChatMessageModel {
    private String messageText;
    private String messageUser;
    private String groupName;
    private String messageKey;
    private long messageTime;
    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }



    public ChatMessageModel() {
    }
    public ChatMessageModel(String messageText , String messageUser , String messageKey , String groupName){
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageKey = messageKey;
        this.groupName = groupName;
        messageTime = new Date().getTime();
    }

    public ChatMessageModel(String messageUser){
        this.messageUser = messageUser;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
