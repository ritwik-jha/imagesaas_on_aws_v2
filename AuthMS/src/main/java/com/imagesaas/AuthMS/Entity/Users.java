package com.imagesaas.AuthMS.Entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class Users {
    private String email;
    private String password;
    private String folderName;

    public Users(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Users(){}

    public Users(String email, String password, String folderName) {
        this.email = email;
        this.password = password;
        this.folderName = folderName;
    }

    @DynamoDbPartitionKey
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    
}
