package client.controllers;

import client.models.VideoInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

import static client.models.Main.read;
import static client.models.Main.request;


public class PostInHomeController {
    private byte[] thumbnailByte;
    private int video_id;
    private String channel;
    private String topic;
    private int view;
    private String timeUpload;
    private VideoInfo video;

    public void define(byte[] thumbnailByte, int video_id, String topic, String chanel, int view, String timeUpload,VideoInfo video) {
        this.thumbnailByte = thumbnailByte;
        this.video_id = video_id;
        this.topic = topic;
        this.channel = chanel;
        this.view = view;
        this.timeUpload = timeUpload;
        this.video = video;

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
    @FXML
    private Line lineOnThumbnail_line;
    @FXML
    private MediaView thumbnail_mediaView;
    @FXML
    private Button thumbnail_button;
    @FXML
    private AnchorPane thumbnail_anchorPane;
    @FXML
    private Pane other_pane;
    @FXML
    private Button other_button;


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


        thumbnail_mediaView.fitWidthProperty().bind(thumbnail_imageView.fitWidthProperty());
        thumbnail_mediaView.fitHeightProperty().bind(thumbnail_imageView.fitHeightProperty().divide(1.777778));
        thumbnail_button.prefWidthProperty().bind(thumbnail_imageView.fitWidthProperty());
        thumbnail_button.prefHeightProperty().bind(thumbnail_imageView.fitHeightProperty().divide(1.777778));
//        thumbnail_anchorPane.maxWidthProperty().bind(thumbnail_imageView.fitWidthProperty().subtract(200));
//        thumbnail_anchorPane.maxHeightProperty().bind(thumbnail_imageView.fitHeightProperty().subtract(200));

    }
    public void showOther(boolean status){
        other_pane.setVisible(status);
        other_pane.setDisable(!status);
        other_button.setVisible(status);
        other_button.setDisable(!status);
    }

    @FXML
    public void goToVideoPlayer_action(ActionEvent actionEvent) {
//        String anchorPaneID = ((AnchorPane)actionEvent.getSource()).getId();
        request.add_WatchedVideo(video.id);
        read();
        try {

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("../../videoPlayer/videoPlayer.fxml")));
            BorderPane borderPane = fxmlLoader.load();

            VideoPlayerController videoPlayerController = fxmlLoader.getController();

            videoPlayerController.define(video_id, video);
            videoPlayerController.setup();

            Scene scene = new Scene(borderPane);
            if(Objects.equals(HomeController.theme, "light")) {
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../videoPlayer/VideoPlayerStyle.css")).toExternalForm());
            }else {
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../videoPlayer/VideoPlayerStyle_dark.css")).toExternalForm());
            }
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private MediaPlayer mediaPlayer;

    @FXML
    public void hideLine_action() {
        lineOnThumbnail_line.setVisible(false);
        //TODO
        File file = null;
        try {
            file = new File(getClass().getResource("../../CACHE/videoCache/video3.mp4").toURI());
            thumbnail_mediaView.setVisible(true);
            Media media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            thumbnail_mediaView.setMediaPlayer(mediaPlayer);


//            DoubleProperty mvw = thumbnail_mediaView.fitWidthProperty();
//            DoubleProperty mvh = thumbnail_mediaView.fitHeightProperty();
//            mvw.bind(Bindings.selectDouble(postInHome_anchorPane.sceneProperty(), "width"));
//            mvh.bind(Bindings.selectDouble(postInHome_anchorPane.sceneProperty(), "height"));

//            thumbnail_mediaView.setFitWidth(286);
//            thumbnail_mediaView.setFitHeight(161);
//            thumbnail_mediaView.fitHeightProperty().bind(thumbnail_imageView.fitHeightProperty());
//            thumbnail_mediaView.fitWidthProperty().bind(thumbnail_imageView.fitWidthProperty());

//            cloudinary.url().transformation(new Transformation().height(200).width(200).crop("crop")).videoTag("docs/cld_rubiks_guy");


            //            mediaPlayer.setStartTime(Duration.ZERO);
            mediaPlayer.setMute(true);
            mediaPlayer.play();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void showLine_action() {
        lineOnThumbnail_line.setVisible(true);
        mediaPlayer.stop();
        thumbnail_mediaView.setVisible(false);
    }

    @FXML
    public void channel_action(ActionEvent actionEvent) {
        try {

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("../../channel/Channel.fxml")));
            BorderPane borderPane = fxmlLoader.load();
            ChannelController channelController = fxmlLoader.getController();


            //TODO get to channel scene
            channelController.define(video);



            channelController.setup();
            Scene scene = new Scene(borderPane);
            if(HomeController.theme == "light") {
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../channel/ChannelStyle.css")).toExternalForm());
            }else{
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../channel/ChannelStyle_dark.css")).toExternalForm());
            }
                stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    public void other_action(){
        boolean status = other_pane.isVisible();
        other_pane.setVisible(!status);
        other_pane.setDisable(status);
    }

    @FXML
    public void delete_action(){
        //TODO delete post
        other_action();
    }
}
