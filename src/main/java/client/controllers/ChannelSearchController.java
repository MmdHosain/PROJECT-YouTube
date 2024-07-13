package client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import static client.models.Main.*;

public class ChannelSearchController {
    private String channel_username;
    private boolean is_subscribed;

    public void define(JSONObject response) {
        this.channel_username = response.getString("channel_username");
//        request.channelProfileImg(channel_username);
        channelName_label.setText(response.getString("channelTitle"));
        channelUserSub_label.setText("@" + channel_username + " • " + response.getInt("totalSubscribers") + " subscribers");
        channelInfo_label.setText(response.getString("channelDescription"));
        is_subscribed = response.getBoolean("is_subscribed");

        if (is_subscribed) {
            subscribe_button.setText("subscribed");
        }
    }

    @FXML
    public Button subscribe_button;
    @FXML
    private Circle channelProf_circle;
    @FXML
    private Label channelName_label;
    @FXML
    private Label channelUserSub_label;
    @FXML
    private Label channelInfo_label;

    public void setup() throws IOException {

        request.channelProfileImg(channel_username);
        JSONObject response = read();
        byte[] videoBytes = readFile();

        File file = new File("src/main/resources/CACHE/imageCache/channelProf0.jpg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(videoBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image image = new Image(Objects.requireNonNull(getClass().getResource("../../CACHE/imageCache/channelProf0.jpg")).openStream());
        channelProf_circle.setFill(new ImagePattern(image));

    }

    @FXML
    public void subscribe_action() {

        if (!is_subscribed) {
            request.subscribeChannel(channel_username);
            JSONObject response = read();
            if (response.getString("responseType").equals("/subscribeChannel_accepted")) ;
            {
                System.out.println("/subscribeChannel_accepted");
                subscribe_button.setText("subscribed");
                is_subscribed = true;
            }
        } else {
            request.unsubscribeChannel(channel_username);
            JSONObject response = read();
            if (response.getString("responseType").equals("/unsubscribeChannel_accepted")) ;
            {
                System.out.println("/unsubscribeChannel_accepted");
                subscribe_button.setText("subscribe");
                is_subscribed = false;
            }
        }
    }
}
