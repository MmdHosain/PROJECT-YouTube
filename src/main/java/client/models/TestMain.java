package client.models;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class TestMain extends Application {

    @Override
    public void start(Stage stage) throws IOException {
//        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../entry/Login.fxml")));
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../home/Home.fxml")));
//        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../videoPlayer/videoPlayer.fxml")));

        Scene scene = new Scene(root);

//        String css = Objects.requireNonNull(this.getClass().getResource("../../entry/LoginStyle.css")).toExternalForm();
        String css = Objects.requireNonNull(this.getClass().getResource("../../home/HomeStyle.css")).toExternalForm();
//        String css = Objects.requireNonNull(this.getClass().getResource("../../videoPlayer/VideoPlayerStyle.css")).toExternalForm();
        scene.getStylesheets().add(css);

        stage.setTitle("YouTube");
        stage.setScene(scene);

        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("../../entry/youtube-icon.png")));
        stage.getIcons().add(icon);
        stage.show();
    }
}





