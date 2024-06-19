package client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;


public class PostInHomeController {
    private byte[] thumbnailByte;
    private int video_id;
    private String channel;
    private String topic;
    private String view;
    private String timeUpload;

    public void define(byte[] thumbnailByte,int video_id, String topic, String chanel, String view, String timeUpload) {
        this.thumbnailByte = thumbnailByte;
        this.video_id = video_id;
        this.topic = topic;
        this.channel = chanel;
        this.view = view;
        this.timeUpload = timeUpload;
    }

    @FXML
    private AnchorPane postInHome_anchorPane;
    @FXML
    private ImageView thumbnail_imageView;
    @FXML
    private Label videoTopic_label;
    @FXML
    private Hyperlink chanel_hyperlink;
    @FXML
    private Label timeAndView_label;


    public void setup() {
        ByteArrayInputStream bis = new ByteArrayInputStream(thumbnailByte);
        Image image = new Image(bis);
        thumbnail_imageView.setImage(image);
        try {
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        videoTopic_label.setText(topic);
        chanel_hyperlink.setText(channel);
        timeAndView_label.setText(view + " • " + timeUpload);
    }


    public void goToVideoPlayer_action(ActionEvent actionEvent) throws IOException {
//        String anchorPaneID = ((AnchorPane)actionEvent.getSource()).getId();


        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("../../videoPlayer/videoPlayer.fxml")));
        BorderPane borderPane = fxmlLoader.load();
        VideoPlayerController videoPlayerController = fxmlLoader.getController();


        videoPlayerController.define(video_id);

        videoPlayerController.setup();
        Scene scene = new Scene(borderPane);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../videoPlayer/VideoPlayerStyle.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }




}
