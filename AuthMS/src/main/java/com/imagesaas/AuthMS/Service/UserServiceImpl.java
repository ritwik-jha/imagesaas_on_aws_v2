package com.imagesaas.AuthMS.Service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.imagesaas.AuthMS.Entity.ResponseVO;
import com.imagesaas.AuthMS.Entity.Users;
import com.imagesaas.AuthMS.Entity.UsersDto;
import com.imagesaas.AuthMS.Utils.JWTUtil;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Component
@Slf4j
public class UserServiceImpl implements UserService {

    @Value("${aws.region}")
    private String region;

    @Value("${aws.profile}")
    private String profile;

    @Autowired
    JWTUtil jwtUtil;

    private DynamoDbClient getClient(){
        return DynamoDbClient.builder().credentialsProvider(ProfileCredentialsProvider.create(profile)).region(Region.of(region)).build();
    }
    
    public ResponseVO registerUser(UsersDto user){
        ResponseVO res = new ResponseVO(false, null, user);

        try {
            DynamoDbEnhancedClient dynamoDbEnhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(getClient()).build();
            DynamoDbTable<Users> userTable = dynamoDbEnhancedClient.table("UserTable", TableSchema.fromBean(Users.class));

            Key key = Key.builder().partitionValue(user.getEmail()).build();

            Users currUser = userTable.getItem(key);

            if(currUser != null){
                res.setMessage("User already exists");
                res.setObj(currUser);

                return res;
            }

            Users newUser = new Users(user.getEmail(), user.getPassword());

            newUser.setFolderName(newUser.getEmail() + "_" +Instant.now().toEpochMilli());

            try{
                userTable.putItem(newUser);
                res.setMessage("User registered");
                res.setStatus(true);
                res.setObj(newUser);
            }
            catch(Exception e){
                res.setMessage("Error while putting to db: " + e.getMessage());
            }
        } catch (Exception e) {
            res.setMessage("Error while processing: " + e.getMessage());
        }

        return res;
    }

    public ResponseVO loginUser(UsersDto user){
        ResponseVO res = new ResponseVO(false, null, null);

        try {
            DynamoDbEnhancedClient dynamoDbEnhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(getClient()).build();
            DynamoDbTable<Users> userTable = dynamoDbEnhancedClient.table("UserTable", TableSchema.fromBean(Users.class));

            Key key = Key.builder().partitionValue(user.getEmail()).build();

            Users currUser = userTable.getItem(key);

            if(currUser == null){
                res.setMessage("no such user");
                return res;
            }

            if(!currUser.getPassword().equals(user.getPassword().strip())){
                res.setMessage("Wrong password");
                return res;
            }

            String jwtToken = jwtUtil.generateJwtToken(currUser.getEmail());
            
            res.setObj(jwtToken);
            res.setMessage("Login Successfull, jwt token generated");
            res.setStatus(true);

        } catch (Exception e) {
            res.setMessage("Error while processing login request: " + e.getMessage());
        }

        return res;
    }
}
