package client.models;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static client.models.Main.*;

public class UserAccount {

    public String username;
    public String password;
    public String channel_username;

    public UserAccount(String username,String password){
        this.username = username;
        this.password = password;
//        request.getChannelUsername(username);
//        JSONObject response = read();
//        this.channel_username = response.getString("channel_username");
          this.channel_username = "channel1";
    }
    public UserAccount(){
        username = "";
        password = "";
    }

    public void writeRememberMe(){

        try {

            File file = new File("src/main/resources/CACHE/rememberMe.txt");
            FileWriter fos = new FileWriter(file) ;
            JSONObject data = new JSONObject();
            data.put("isLoggedIn",true);
            data.put("username",username);
            data.put("password",password);

            fos.write(data.toString());
            fos.flush();


        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public boolean autoLogin() throws RuntimeException{

        try {

            String content = new String(Files.readAllBytes(Paths.get("src/main/resources/CACHE/rememberMe.txt")));
            JSONObject data = null;
            if (!content.equals("")) {

                data = new JSONObject(content);
                if (data.getBoolean("isLoggedIn")){
                    request.login(data.getString("username"),data.getString("password"));

                    String responseType = read().getString("responseType");
                    if(responseType.equals("/login_accepted")){
                        userAccount = new UserAccount(data.getString("username"),data.getString("password"));
//                        username = data.getString("username");
//                        password = data.getString("password");
                        return true;
                    }
                }
                else {
                    username = "";
                    password = "";
                    return false;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }


        return false;
    }

}
