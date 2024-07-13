package client.controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
//import org.bytedeco.javacv.FFmpegFrameGrabber;
//import org.bytedeco.javacv.Frame;
//import org.bytedeco.javacv.Java2DFrameConverter;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static client.models.Main.*;


public class UploadFileController implements Initializable {

    private String filePath;
    private File fileToUpload ;
    private boolean childAbuse;
    public static String chosenPlaylist;
    @FXML
    private AnchorPane uploadVideo_anchorPain;
    @FXML
    private ImageView upload_imageView;
    @FXML
    private Label selectedFile_label;
    @FXML
    private ScrollPane details_scrollPane;
    @FXML
    private Pane selectFile_pane;
    @FXML
    private Pane details_pane;
    @FXML
    private Label videoPath_label;
    @FXML
    private RadioButton childYes_radioButton;
    @FXML
    private RadioButton childNo_radioButton;
    @FXML
    private RadioButton private_RadiButton;
    @FXML
    private RadioButton public_radioButton;
    @FXML
    private MediaView video_mediaView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1));
        fadeTransition.setNode(uploadVideo_anchorPain);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1));
        scaleTransition.setNode(uploadVideo_anchorPain);
        scaleTransition.setCycleCount(1);
        uploadVideo_anchorPain.setScaleX(0.1);
        uploadVideo_anchorPain.setScaleY(0.1);
        uploadVideo_anchorPain.setScaleZ(0.1);

        scaleTransition.setToX(1);
        scaleTransition.setToY(1);
        scaleTransition.setToZ(1);

//        scaleTransition.setByY(50);

        scaleTransition.play();
        fadeTransition.play();
        //________________________________________
        if (childYes_radioButton.isArmed()) {
            childNo_radioButton.disarm();
        } else if (childNo_radioButton.isArmed()) {
            childNo_radioButton.disarm();
        }

        if (public_radioButton.isArmed()) {
            private_RadiButton.disarm();
        } else if (private_RadiButton.isArmed()) {
            public_radioButton.disarm();
        }

        //_________________________________________
        request.getPlaylistList(userAccount.channel_username);
        JSONObject response = read();
        JSONArray list = response.getJSONArray("playlistList");
        for (int i = 0; i < list.length(); i++) {
            selectPlaylist_comboBox.getItems().add(list.getString(i));
        }
    }

    @FXML
    public void selectFile_action(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a File");
        FileChooser.ExtensionFilter mp4Filter = new FileChooser.ExtensionFilter("MP4 Files", "*.mp4");
        fileChooser.getExtensionFilters().addAll(mp4Filter);
        File selectedFile = fileChooser.showOpenDialog(stage);

        filePath = selectedFile.getAbsolutePath();
        selectedFile_label.setText("SELECTED FILE:\n" + filePath);

//        try {

//            Image uploadGif = new Image(Objects.requireNonNull(getClass().getResource("../../create/uploadGif.gif")).openStream());
//            Image uploadImage2 = new Image(Objects.requireNonNull(getClass().getResource("../../create/uploadImage2.jpg")).openStream());

//            upload_imageView.setImage(uploadGif);

//            Timeline delayTimeline = new Timeline(new KeyFrame(Duration.seconds(2.566667), event -> upload_imageView.setImage(uploadImage2)));
//            delayTimeline.play();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }


    }

    @FXML
    public void close_action(ActionEvent actionEvent) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1));
        fadeTransition.setNode(uploadVideo_anchorPain);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(actionEvent1 -> {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        });

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1));
        scaleTransition.setNode(uploadVideo_anchorPain);
        scaleTransition.setCycleCount(1);
        uploadVideo_anchorPain.setScaleX(1);
        uploadVideo_anchorPain.setScaleY(1);
        uploadVideo_anchorPain.setScaleZ(1);
        scaleTransition.setToX(0.1);
        scaleTransition.setToY(0.1);
        scaleTransition.setToZ(0.1);

        scaleTransition.play();
        fadeTransition.play();
    }

    @FXML
    private TextField videoTitle_textField;
    @FXML
    private TextArea description_textArea;
    @FXML
    private ComboBox<String> selectPlaylist_comboBox;
    @FXML
    private TextArea addTag_textArea;

    @FXML
    public void next_action() {

//        String videoLength = null;
//        String videoSize = null;
        videoPath_label.setText("Video path: " + filePath);


        //__________________________________________
        fileToUpload = new File(filePath);
        Media media = new Media(fileToUpload.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        video_mediaView.setMediaPlayer(mediaPlayer);
        video_mediaView.fitWidthProperty().set(230);
        video_mediaView.fitHeightProperty().set(129.4);
        video_mediaView.minWidth(230);
        video_mediaView.maxWidth(230);
        video_mediaView.minHeight(129.4);
        video_mediaView.maxHeight(129.4);
        mediaPlayer.setMute(true);
        mediaPlayer.play();

        if (!Objects.equals(filePath, "")) {
            FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5));
            fadeTransition1.setNode(selectFile_pane);
            fadeTransition1.setFromValue(1);
            fadeTransition1.setToValue(0);

            FadeTransition fadeTransition2 = new FadeTransition(Duration.seconds(1));
            fadeTransition2.setNode(details_pane);
            fadeTransition2.setFromValue(0);
            fadeTransition2.setToValue(1);

            selectFile_pane.setVisible(true);
            details_pane.setVisible(true);
            TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(1));
            translateTransition1.setNode(selectFile_pane);
            translateTransition1.setToX(-750);


            details_pane.setLayoutX(750);
            TranslateTransition translateTransition2 = new TranslateTransition(Duration.seconds(1));
            translateTransition2.setNode(details_pane);
            translateTransition2.setToX(-750);
            translateTransition1.setOnFinished(actionEvent -> {
                selectFile_pane.setVisible(false);
            });
            fadeTransition1.play();
            fadeTransition2.play();
            translateTransition1.play();
            translateTransition2.play();
        } else {
            selectedFile_label.setText("You didn't select a file!");
        }
    }

    @FXML
    public void refresh_action(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../create/UploadFile.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        String css = Objects.requireNonNull(this.getClass().getResource("../../create/UploadFileStyle.css")).toExternalForm();
        scene.getStylesheets().add(css);
        scene.setFill(Color.TRANSPARENT);
//        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Upload file");
        stage.setScene(scene);
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("../../create/uploadImage.jpg")));
        stage.getIcons().add(icon);
        stage.show();
    }

    @FXML
    private Label status_label;

    @FXML
    public void upload_action(ActionEvent actionEvent) {
        boolean okayToUpload = true;

        String videoTitle = videoTitle_textField.getText();
        if (videoTitle == null) {
            status_label.setText("Video title required");
//            okayToUpload = false;
        }

        String videoDescription = description_textArea.getText();
        String videoTags = addTag_textArea.getText();

        if (childNo_radioButton.isArmed()) {
            //child abuse is on
        } else if (childNo_radioButton.isArmed()) {
            //child abuse is off
        } else {
            status_label.setText("Audience button didn't check");
//            okayToUpload = false;
        }

        if(private_RadiButton.isArmed()){
            //privet
        } else if (public_radioButton.isArmed()) {
            //public
        }else {
            status_label.setText("Visibility button didn't check");
//            okayToUpload = false;
        }


        if (okayToUpload) {

            String[] hashtags = videoTags.split(" ");

            JSONArray tags = new JSONArray();
            for (String hashtag : hashtags) {
                tags.put(hashtag);
            }
            request.addVideo(userAccount.channel_username,videoTitle,videoDescription,tags,fileToUpload,chosenPlaylist);


            //________________________________
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1));
            fadeTransition.setNode(uploadVideo_anchorPain);
            fadeTransition.setFromValue(1);
            fadeTransition.setToValue(0);
            fadeTransition.setOnFinished(actionEvent1 -> {
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.close();
            });

            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1));
            scaleTransition.setNode(uploadVideo_anchorPain);
            scaleTransition.setCycleCount(1);
            uploadVideo_anchorPain.setScaleX(1);
            uploadVideo_anchorPain.setScaleY(1);
            uploadVideo_anchorPain.setScaleZ(1);
            scaleTransition.setToX(0.1);
            scaleTransition.setToY(0.1);
            scaleTransition.setToZ(0.1);

            scaleTransition.play();
            fadeTransition.play();

        }


    }

    @FXML
    public void back_action() {
        selectFile_pane.setVisible(true);
        details_pane.setVisible(true);

        FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(1));
        fadeTransition1.setNode(selectFile_pane);
        fadeTransition1.setFromValue(0);
        fadeTransition1.setToValue(1);

        FadeTransition fadeTransition2 = new FadeTransition(Duration.seconds(0.5));
        fadeTransition2.setNode(details_pane);
        fadeTransition2.setFromValue(1);
        fadeTransition2.setToValue(0);

        selectFile_pane.setLayoutX(-750);
        TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(1));
        translateTransition1.setNode(selectFile_pane);
        translateTransition1.setToX(750);


        details_pane.setLayoutX(750);
        TranslateTransition translateTransition2 = new TranslateTransition(Duration.seconds(1));
        translateTransition2.setNode(details_pane);
        translateTransition2.setToX(750);
        translateTransition1.setOnFinished(actionEvent -> {
            details_pane.setVisible(false);
        });
        fadeTransition1.play();
        fadeTransition2.play();
        translateTransition1.play();
        translateTransition2.play();
    }

    @FXML
    public void newPlaylist_action() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../creat/NewPlaylist.fxml")));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        String css = Objects.requireNonNull(this.getClass().getResource("../../creat/NewPlaylistStyle.css")).toExternalForm();
        scene.getStylesheets().add(css);
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("New playlist");
        stage.setScene(scene);
        stage.show();
    }


    private void randomThumbnail(int num){
//        File videoFile = new File("path/to/your/video.mp4"); // Replace with your video file path
//        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(videoFile);
//        frameGrabber.start();
//
//        int totalFrames = frameGrabber.getLengthInFrames();
//        int randomFrameIndex = (int) (Math.random() * totalFrames); // Generate a random frame index
//
//        frameGrabber.setFrameNumber(randomFrameIndex);
//        Frame frame = frameGrabber.grab();
//        Java2DFrameConverter converter = new Java2DFrameConverter();
//        BufferedImage bufferedImage = converter.convert(frame);
//
//        // Save the frame as an image (e.g., PNG)
//        File outputImageFile = new File("random_frame.png");
//        ImageIO.write(bufferedImage, "png", outputImageFile);
//
//        frameGrabber.stop();
//        System.out.println("Random frame saved as: " + outputImageFile.getAbsolutePath());
    }

}
