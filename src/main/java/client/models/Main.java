package client.models;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONObject;


import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class Main extends Application {


    public static BufferedReader reader;
    public static BufferedWriter writer;
    public static DataInputStream dataReader;
    public static DataOutputStream dataWriter;
    public static UserAccount userAccount;
    public static Request request;
    private static Socket socket;



    public static void main(String[] args) {
        request = new Request();
        userAccount = new UserAccount();
        socket_init();
        launch(args);

    }

    @Override
    public void start(Stage stage) throws IOException {
        String css;
        Parent root;

        if(userAccount.autoLogin()){
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../home/Home.fxml")));
            css = Objects.requireNonNull(this.getClass().getResource("../../home/HomeStyle.css")).toExternalForm();
        }
        else {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../entry/Login.fxml")));
            css = Objects.requireNonNull(this.getClass().getResource("../../entry/LoginStyle.css")).toExternalForm();
        }
        Scene scene = new Scene(root);
        scene.getStylesheets().add(css);
        stage.setTitle("YouTube");
        stage.setScene(scene);
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("../../entry/youtube-icon.png")));
        stage.getIcons().add(icon);
        stage.show();
    }

    private static void socket_init() {
        try{
            socket = new Socket("localhost", 4444);

            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            dataReader = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dataWriter = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static JSONObject read(){
        JSONObject response = new JSONObject();
        try {
            String line = reader.readLine();
            response = new JSONObject(line);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.put("respondType", "/error");
        return response;
    }
    public static void saveFile(byte[] bytes,String path){

        try {

            File file = new File(path);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);

        }catch (IOException e){
            e.printStackTrace();
        }

    }
    public static byte[] readFile(){

        try {
            DataInputStream dataIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            long fileLength = dataIn.readLong();

            byte[] bytes = new byte[(int) fileLength];
            int bytesRead;
            int totalBytesRead = 0;
            while (totalBytesRead < fileLength) {
                bytesRead = dataIn.read(bytes, totalBytesRead, bytes.length - totalBytesRead);
                if (bytesRead == -1) { // End of stream
                    break;
                }
                totalBytesRead += bytesRead;
            }

            return bytes;
        }catch (IOException e){
            e.printStackTrace();
        }

        return new byte[0];
    }
    public static void write(JSONObject request){
        try {
            String requestToSend =  request.toString();
            writer.write(requestToSend);
            writer.newLine();
            writer.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void uploadFile(File file) {

        try {

            DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            FileInputStream fileInputStream= new FileInputStream(file);

            long length = file.length();
            dataOut.writeLong(length);
            dataOut.flush();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                dataOut.write(buffer, 0, bytesRead);
            }
            dataOut.flush();

            System.out.println("file has been sent");


        } catch (IOException e) {
            e.printStackTrace();
        }



    }

}





