package client.controllers;

import client.models.VideoInfo;
import javafx.animation.*;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import static client.models.Main.*;

public class VideoPlayerController {
    private File file;
    private VideoInfo videoInfo;

    private int commentNumber;
    private boolean is_subscribed;
    private boolean is_liked;
    private boolean is_disliked;
    private int video_id;
    private VideoInfo video;

    public void define(int video_id, VideoInfo video) { //like a constructor
        this.video_id = video_id;
        this.video = video;
        request.videoFile(video_id);
        byte[] videoBytes = readFile();

        File file = new File("src/main/resources/CACHE/videoCache" + "/video" + video_id + ".mp4");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(videoBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.file = file;
        request.video(video_id);
        JSONObject response2 = read();
        videoInfo = new VideoInfo(video_id, response2);
        is_liked = videoInfo.is_liked;
        is_disliked = videoInfo.is_disliked;
        like_button.setText(String.valueOf(videoInfo.getTotal_likes()));


        request.channel(videoInfo.channel_username);
        JSONObject response = read();

        channelName_label.setText(response.getString("channelTitle"));
        totalSubscribers_label.setText(response.getInt("totalSubscribers") + " subscribers");
        video_lable.setText(videoInfo.getTitle());
        videoDiscription_label.setText(videoInfo.getTitle_body());
        is_subscribed = response.getBoolean("is_subscribed");
        if (is_subscribed) {
            subscribe_button.setText("subscribed");
        }
    }

    @FXML
    private Label videoDiscription_label;
    @FXML
    public Label totalSubscribers_label;
    @FXML
    public Label commentCount;
    @FXML
    private Label channelName_label;
    @FXML
    private MediaPlayer mediaPlayer;
    @FXML
    private Pane toolBar_pane;
    @FXML
    private VBox toolBar_vBox;
    @FXML
    private VBox videoComments_vBox;
    @FXML
    private VBox recommendVideo_vBox;
    @FXML
    private MediaView video_mediaView;
    @FXML
    private Slider videoTimeline_slider;
    @FXML
    private ScrollPane videoPlayer_scrollPane;
    @FXML
    private BorderPane homeMain_borderPain;
    @FXML
    private VBox postInHome_vBox;
    @FXML
    private ScrollPane post_scrollPane;
    @FXML
    private Pane account_pane;
    @FXML
    private Pane fadeRectangle_pane;
    @FXML
    private Pane creat_pane;
    @FXML
    private Button accountProfHome_button;
    @FXML
    private ScrollPane toolBar_scrollPane;
    @FXML
    private Rectangle fadeRectangle_rectangle;
    @FXML
    private Button accountProfHome_button1;
    @FXML
    private HBox tags_hBox;
    @FXML
    private ScrollPane tags_scrollPane;
    @FXML
    private VBox videoController_vBox;
    @FXML
    private Button playAndPause_button;
    @FXML
    private Button mute_button;
    @FXML
    private Label duration_label;
    @FXML
    private VBox video_vBox;
    @FXML
    private VBox videoPlayer_vBox;
    @FXML
    private Circle videoProf_circle;
    @FXML
    private Button next_button;
    @FXML
    private Button fullScream_button;
    @FXML
    private Slider volume_slider;
    @FXML
    private Button speed_button;
    @FXML
    private Label video_lable;
    @FXML
    private Button subscribe_button;
    @FXML
    private Button like_button;
    @FXML
    private Button dislike_button;
    @FXML
    private Pane other_pane;
    @FXML
    private Circle accountProfHome_circle1;
    @FXML
    private Circle accountProfHome_circle;
    @FXML
    private VBox searchResult_vBox;
    @FXML
    private TextField Search_textField;
    @FXML
    private Pane searchResult_pane;
    @FXML
    private GridPane tags_gridPane;
    @FXML
    private Pane notification_pane;
    @FXML
    private VBox notification_vBox;
    @FXML
    private Circle notification_circle;

    public void setup() throws IOException {

        request.commentList(videoInfo.id);
        JSONObject CLresponse = read();
        JSONArray commentIdList = CLresponse.getJSONArray("commentIdList");
        commentNumber = CLresponse.getInt("commentCount");
        commentCount.setText(commentNumber + " comments");


        for (int i = 0; i < commentNumber; i++) {
            try {

                FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("../../videoPlayer/Comment.fxml")));
                AnchorPane anchorPane = fxmlLoader.load();
                anchorPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../videoPlayer/CommentStyle.css")).toExternalForm());


                int comment_id = commentIdList.getInt(i);


                CommentController commentController = fxmlLoader.getController();
                commentController.define(comment_id);
                commentController.setup();

                videoComments_vBox.getChildren().add(anchorPane);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        try {
            request.channelProfileImg(videoInfo.channel_username);
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
            videoProf_circle.setFill(new ImagePattern(image));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //______________________________________________________________________________


        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("../../videoPlayer/newComment.fxml")));
            GridPane gridPane = fxmlLoader.load();
            gridPane.getChildren().remove(((GridPane) gridPane.getChildren().get(2)).getChildren().remove(1));
            gridPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../videoPlayer/newCommentStyle.css")).toExternalForm());
            newCommentController newCommentController = fxmlLoader.getController();
            videoPlayer_vBox.getChildren().add(5, gridPane);

            newCommentController.define(videoInfo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        Media media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        video_mediaView.setMediaPlayer(mediaPlayer);
        video_mediaView.fitWidthProperty().bind(videoPlayer_scrollPane.widthProperty().subtract(349));
        video_mediaView.fitHeightProperty().bind(videoPlayer_scrollPane.widthProperty().subtract(349));


        mediaPlayer.totalDurationProperty().addListener((obs, oldDuration, newDuration) ->
                videoTimeline_slider.setMax(newDuration.toSeconds())
        );

        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (!videoTimeline_slider.isValueChanging()) {
                videoTimeline_slider.setValue(newTime.toSeconds());

                String totalTimeStr = mediaPlayer.getTotalDuration().toString();
                String currentTimeStr = mediaPlayer.getCurrentTime().toString();
                double totalDuration = Double.parseDouble(totalTimeStr.substring(0, totalTimeStr.length() - 3));
                System.out.println(media.getDuration());
                double currentDuration = Double.parseDouble(currentTimeStr.substring(0, currentTimeStr.length() - 3));
                System.out.println(mediaPlayer.getCurrentTime());
                double percent = currentDuration / totalDuration * 100;
                System.out.println(percent);
                videoTimeline_slider.setStyle("-fx-background-color: linear-gradient(to right, green 0%, green " + percent + "%, gray " + percent + "%, gray 100%);");
            }
        });


        videoTimeline_slider.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            mediaPlayer.pause();
            playAndPause_button.setStyle("-fx-shape: 'M 12,26 18.5,22 18.5,14 12,10 z M 18.5,22 25,18 25,18 18.5,14 z'");
        });
//        mediaPlayer.setRate(2);
        videoTimeline_slider.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            mediaPlayer.seek(Duration.seconds(videoTimeline_slider.getValue()));
            playAndPause_button.setStyle("-fx-shape: 'M 12,26 16,26 16,10 12,10 z M 21,26 25,26 25,10 21,10'");
            mediaPlayer.play();
        });


        mediaPlayer.setOnReady(() -> {
            Duration totalDuration = media.getDuration();
            Duration currentDuration = mediaPlayer.getCurrentTime();
            String totalDurationStr = formatTime(totalDuration);
            duration_label.setText(formatTime(currentDuration) + " / " + totalDurationStr);
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(1), actionEvent ->
                    {
                        Duration currentDuration1 = mediaPlayer.getCurrentTime();
                        duration_label.setText(formatTime(currentDuration1) + " / " + totalDurationStr);
                    })
            );
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        });


    }

    private String formatTime(Duration time) {
        long seconds = (long) time.toSeconds();
        long minutes = seconds / 60;
        long hours = minutes / 60;
        if (hours != 0) {
            return String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60);
        } else {
            return String.format("%02d:%02d", minutes % 60, seconds % 60);

        }
    }

    @FXML
    public void toolBar_action() {
        boolean show = toolBar_pane.isVisible();


        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), toolBar_pane);
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), fadeRectangle_pane);
        if (!show) {
            toolBar_pane.setLayoutX(-200);
            translateTransition.setToX(200);
            toolBar_pane.setVisible(!show);
            toolBar_vBox.setVisible(show);
            fadeRectangle_pane.setVisible(!show);

            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
        } else {
            toolBar_pane.setLayoutX(-200);
            translateTransition.setToX(-400);
            translateTransition.setOnFinished(actionEvent -> {
                toolBar_pane.setVisible(!show);
                toolBar_vBox.setVisible(show);
                fadeRectangle_pane.setVisible(!show);
            });

            fadeTransition.setFromValue(1);
            fadeTransition.setToValue(0);
        }
        fadeTransition.play();
        translateTransition.play();
    }

    @FXML
    public void account_action() {
        account_pane.setVisible(true);
        fadeRectangle_pane.setVisible(true);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), account_pane);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);

        FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), fadeRectangle_pane);
        fadeTransition1.setFromValue(0);
        fadeTransition1.setToValue(1);

        fadeTransition.play();
        fadeTransition1.play();
    }

    @FXML
    public void notification_action() {
        fadeRectangle_pane.setVisible(true);
        notification_pane.setVisible(true);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), notification_pane);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);

        FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), fadeRectangle_pane);
        fadeTransition1.setFromValue(0);
        fadeTransition1.setToValue(1);

        fadeTransition.play();
        fadeTransition1.play();
    }

    @FXML
    public void create_action() {
        fadeRectangle_pane.setVisible(true);
        creat_pane.setVisible(true);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), creat_pane);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);

        FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), fadeRectangle_pane);
        fadeTransition1.setFromValue(0);
        fadeTransition1.setToValue(1);

        fadeTransition.play();
        fadeTransition1.play();
    }

    @FXML
    public void closeBars() {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), fadeRectangle_pane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(actionEvent -> {
            fadeRectangle_pane.setVisible(false);
        });
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), toolBar_pane);
        toolBar_pane.setLayoutX(-200);
        translateTransition.setToX(-400);
        translateTransition.setOnFinished(actionEvent -> {
            toolBar_pane.setVisible(false);
            toolBar_vBox.setVisible(true);
        });
        fadeTransition.play();
        translateTransition.play();

        account_pane.setVisible(false);
        creat_pane.setVisible(false);
        notification_pane.setVisible(false);
    }

    @FXML
    public void uploadVideo_action() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../creat/UploadFile.fxml")));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        String css = null;
        if (Objects.equals(HomeController.theme, "light")) {
            css = Objects.requireNonNull(this.getClass().getResource("../../creat/UploadFileStyle.css")).toExternalForm();
        } else {
            css = Objects.requireNonNull(this.getClass().getResource("../../creat/UploadFileStyle_dark.css")).toExternalForm();

        }
        scene.getStylesheets().add(css);
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Upload file");
        stage.setScene(scene);
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("../../creat/uploadImage.jpg")));
        stage.getIcons().add(icon);
        stage.show();
    }

    @FXML
    public void refresh_action(ActionEvent actionEvent) {
        //        String anchorPaneID = ((AnchorPane)actionEvent.getSource()).getId();
        request.add_WatchedVideo(video.id);
        read();
        try {
            mediaPlayer.stop();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("../../videoPlayer/videoPlayer.fxml")));
            BorderPane borderPane = fxmlLoader.load();

            VideoPlayerController videoPlayerController = fxmlLoader.getController();

            videoPlayerController.define(video_id, video);
            videoPlayerController.setup();

            Scene scene = new Scene(borderPane);
            if (Objects.equals(HomeController.theme, "light")) {
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../videoPlayer/VideoPlayerStyle.css")).toExternalForm());
            } else {
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../videoPlayer/VideoPlayerStyle_dark.css")).toExternalForm());
            }
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void home_action(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../home/Home.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            if (Objects.equals(HomeController.theme, "light")) {
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../home/HomeStyle.css")).toExternalForm());
            } else {
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../home/HomeStyle_dark.css")).toExternalForm());
            }
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void myChannel_action(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../creat/MyChannel.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            if (Objects.equals(HomeController.theme, "light")) {
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../creat/MyChannelStyle.css")).toExternalForm());
            } else {
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../creat/MyChannelStyle_dark.css")).toExternalForm());
            }
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    public void signOut_action(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../entry/Login.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../entry/LoginStyle.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void search_action() {
        boolean status = searchResult_pane.isVisible();
        searchResult_pane.setVisible(!status);
        searchResult_pane.setDisable(status);
        tags_gridPane.setVisible(status);
        tags_gridPane.setDisable(!status);
    }

    @FXML
    public void searchRecommend_action() {
        //TODO add recommended searches to searchResult_vBox by REGEX


        searchResult_vBox.getChildren().clear();
        for (int i = 0; i < 5; i++) {
            Label label = new Label("result test");
            label.setStyle("-fx-padding: 0 10 0 10; -fx-text-fill: #f36666; -fx-font-size: 14");
            searchResult_vBox.getChildren().add(label);
        }
        searchResult_vBox.setVisible(false);
    }

    @FXML
    public void searchResult_action() throws IOException {
        searchResult_vBox.setVisible(false);
        postInHome_vBox.getChildren().clear();
        Label label = new Label("Search result:");
        label.setStyle("-fx-text-fill: #d50101; -fx-font-size: 20; -fx-font-weight: bold;");
        postInHome_vBox.getChildren().add(label);

        request.search(Search_textField.getText());
        JSONObject response = read();
        JSONArray videoResult = response.getJSONArray("videoResult");
        JSONArray channelResult = response.getJSONArray("channelResult");
        JSONObject topChannel = channelResult.getJSONObject(0);

        ArrayList<JSONObject> videoList = new ArrayList<>();
        ArrayList<VideoInfo> videoInfoList = new ArrayList<>();
        for (int i = 0; i < videoResult.length(); i++) {
            videoList.add(videoResult.getJSONObject(i));
            int video_id = videoList.get(i).getInt("id");
            request.video(video_id);
            JSONObject response2 = read();
            videoInfoList.add(new VideoInfo(video_id, response2));
        }
        if (topChannel.getInt("distance") < 2) {
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("../../home/ChannelSearch.fxml")));
            GridPane pane = fxmlLoader.load();
            if (Objects.equals(HomeController.theme, "light")) {
                pane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../home/ChannelSearchStyle.css")).toExternalForm());
            } else {
                pane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../home/ChannelSearchStyle_dark.css")).toExternalForm());

            }
            ChannelSearchController channelSearchController = fxmlLoader.getController();
            request.channel(topChannel.getString("channel_username"));

            channelSearchController.define(read());
            channelSearchController.setup();
            postInHome_vBox.getChildren().add(pane);

        }


        HBox hBox0 = new HBox();
        hBox0.setSpacing(10);
        for (int j = 0; j < 4; j++) {
            try {
                int pointer = j;
                request.ChannelVideoList(topChannel.getString("channel_username"));
                response = read();
                JSONArray video_idList = response.getJSONArray("videoIdList");
                ArrayList<VideoInfo> videoInfoList1 = new ArrayList<>();
                for (int k = 0; k < video_idList.length(); k++) {
                    int video_id = video_idList.getInt(k);
                    request.video(video_id);
                    JSONObject response2 = read();
                    videoInfoList1.add(new VideoInfo(video_id, response2));
                }


                VideoInfo video = videoInfoList1.get(pointer);
                request.imageFile(video.id);
                byte[] imageBytes = readFile();


                File file = new File("src/main/resources/CACHE/imageCache" + "/img" + (j) + ".jpg");

                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(imageBytes);


                FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("../../home/PostInHome.fxml")));
                AnchorPane pane = fxmlLoader.load();
                if (Objects.equals(HomeController.theme, "light")) {
                    pane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../home/PostInHomeStyle.css")).toExternalForm());
                } else {
                    pane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../home/PostInHomeStyle_dark.css")).toExternalForm());

                }
                pane.setId(String.valueOf(pointer));

                PostInHomeController postInHomeController = fxmlLoader.getController();
                postInHomeController.define(imageBytes, video.id, video.getTitle(), video.getChannel_name(), video.getTotal_view(), video.getCreation_time(), video);
                postInHomeController.setup();
                postInHomeController.showOther(false);

                ((ImageView) ((AnchorPane) ((VBox) pane.getChildren().get(0)).getChildren().get(0)).getChildren().get(0)).fitWidthProperty().bind(post_scrollPane.widthProperty().divide(4).subtract(30));
                ((ImageView) ((AnchorPane) ((VBox) pane.getChildren().get(0)).getChildren().get(0)).getChildren().get(0)).fitHeightProperty().bind(post_scrollPane.widthProperty().divide(4).subtract(30));
                ((Line) ((VBox) pane.getChildren().get(0)).getChildren().get(1)).endXProperty().bind(post_scrollPane.widthProperty().divide(4).subtract(30));


                hBox0.getChildren().add(pane);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        postInHome_vBox.getChildren().add(hBox0);


        for (int i = 1; i < 3; i++) {
            HBox hBox = new HBox();
            hBox.setSpacing(10);
            for (int j = 0; j < 4; j++) {
                try {
                    int pointer = i * 4 + j;

                    VideoInfo video = videoInfoList.get(pointer);
                    request.imageFile(video.id);
                    byte[] imageBytes = readFile();


                    File file = new File("src/main/resources/CACHE/imageCache" + "/img" + (pointer) + ".jpg");

                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(imageBytes);


                    FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("../../home/PostInHome.fxml")));
                    AnchorPane pane = fxmlLoader.load();
                    if (Objects.equals(HomeController.theme, "light")) {
                        pane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../home/PostInHomeStyle.css")).toExternalForm());
                    } else {
                        pane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../home/PostInHomeStyle_dark.css")).toExternalForm());

                    }
                    pane.setId(String.valueOf(pointer));

                    PostInHomeController postInHomeController = fxmlLoader.getController();
                    postInHomeController.define(imageBytes, video.id, video.getTitle(), video.getChannel_name(), video.getTotal_view(), video.getCreation_time(), video);
                    postInHomeController.setup();

                    ((ImageView) ((AnchorPane) ((VBox) pane.getChildren().get(0)).getChildren().get(0)).getChildren().get(0)).fitWidthProperty().bind(post_scrollPane.widthProperty().divide(4).subtract(30));
                    ((ImageView) ((AnchorPane) ((VBox) pane.getChildren().get(0)).getChildren().get(0)).getChildren().get(0)).fitHeightProperty().bind(post_scrollPane.widthProperty().divide(4).subtract(30));
                    ((Line) ((VBox) pane.getChildren().get(0)).getChildren().get(1)).endXProperty().bind(post_scrollPane.widthProperty().divide(4).subtract(30));


                    hBox.getChildren().add(pane);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            postInHome_vBox.getChildren().add(hBox);
        }
    }

    @FXML
    public void subscriptions_action() {
        postInHome_vBox.getChildren().clear();
        Label label = new Label("ُSubscriptions:");
        label.setStyle("-fx-text-fill: #d50101; -fx-font-size: 24; -fx-font-weight: bold;");
        postInHome_vBox.getChildren().add(label);
        try {
            //TODO show the subscribed channels

            postInHome_vBox.getChildren().clear();
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("../../home/ChannelSearch.fxml")));
            GridPane pane = fxmlLoader.load();
            if (HomeController.theme == "light") {
                pane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../home/ChannelSearchStyle.css")).toExternalForm());
            } else {
                pane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../home/ChannelSearchStyle_dark.css")).toExternalForm());

            }
            ChannelSearchController channelSearchController = fxmlLoader.getController();


            channelSearchController.define(read());
            channelSearchController.setup();
            postInHome_vBox.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void history_action() {
        postInHome_vBox.getChildren().clear();
        Label label = new Label("History:");
        label.setStyle("-fx-text-fill: #d50101; -fx-font-size: 24; -fx-font-weight: bold;");
        postInHome_vBox.getChildren().add(label);

        request.historyVideoList();
        JSONObject response = read();
        JSONArray video_idList = response.getJSONArray("videoIdList");
        ArrayList<VideoInfo> videoInfoList = new ArrayList<>();
        for (int k = 0; k < video_idList.length(); k++) {
            int video_id = video_idList.getInt(k);
            request.video(video_id);
            JSONObject response2 = read();
            videoInfoList.add(new VideoInfo(video_id, response2));
        }
        for (int i = 0; i < videoInfoList.size(); i++) {

            try {
                VideoInfo video = videoInfoList.get(i);
                request.imageFile(video.id);
                byte[] imageBytes = readFile();


                File file = new File("src/main/resources/CACHE/imageCache" + "/img" + (i) + ".jpg");

                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(imageBytes);


                FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("../../home/PostInHome.fxml")));
                AnchorPane pane = fxmlLoader.load();
                if (Objects.equals(HomeController.theme, "light")) {
                    pane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../home/PostInHomeStyle.css")).toExternalForm());
                } else {
                    pane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../home/PostInHomeStyle_dark.css")).toExternalForm());

                }


                PostInHomeController postInHomeController = fxmlLoader.getController();
                postInHomeController.define(imageBytes, video.id, video.getTitle(), video.getChannel_name(), video.getTotal_view(), video.getCreation_time(), video);
                postInHomeController.setup();

                postInHomeController.showOther(false);
                ((ImageView) ((AnchorPane) ((VBox) pane.getChildren().get(0)).getChildren().get(0)).getChildren().get(0)).fitWidthProperty().bind(post_scrollPane.widthProperty().divide(4).subtract(30));
                ((ImageView) ((AnchorPane) ((VBox) pane.getChildren().get(0)).getChildren().get(0)).getChildren().get(0)).fitHeightProperty().bind(post_scrollPane.widthProperty().divide(4).subtract(30));
                ((Line) ((VBox) pane.getChildren().get(0)).getChildren().get(1)).endXProperty().bind(post_scrollPane.widthProperty().divide(4).subtract(30));
                postInHome_vBox.getChildren().add(pane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void watchLater_action() {

        postInHome_vBox.getChildren().clear();
        Label label = new Label("Your watch later list:");
        label.setStyle("-fx-text-fill: #d50101; -fx-font-size: 24; -fx-font-weight: bold;");
        postInHome_vBox.getChildren().add(label);

        request.watchLaterVideoList();
        JSONObject response = read();
        JSONArray video_idList = response.getJSONArray("videoIdList");
        ArrayList<VideoInfo> videoInfoList = new ArrayList<>();
        for (int k = 0; k < video_idList.length(); k++) {
            int video_id = video_idList.getInt(k);
            request.video(video_id);
            JSONObject response2 = read();
            videoInfoList.add(new VideoInfo(video_id, response2));
        }
        for (int i = 0; i < videoInfoList.size(); i++) {

            try {
                VideoInfo video = videoInfoList.get(i);
                request.imageFile(video.id);
                byte[] imageBytes = readFile();


                File file = new File("src/main/resources/CACHE/imageCache" + "/img" + (i) + ".jpg");

                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(imageBytes);


                FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("../../home/PostInHome.fxml")));
                AnchorPane pane = fxmlLoader.load();
                if (Objects.equals(HomeController.theme, "light")) {
                    pane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../home/PostInHomeStyle.css")).toExternalForm());
                } else {
                    pane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../home/PostInHomeStyle_dark.css")).toExternalForm());

                }

                PostInHomeController postInHomeController = fxmlLoader.getController();
                postInHomeController.define(imageBytes, video.id, video.getTitle(), video.getChannel_name(), video.getTotal_view(), video.getCreation_time(), video);
                postInHomeController.setup();

                postInHomeController.showOther(false);
                ((ImageView) ((AnchorPane) ((VBox) pane.getChildren().get(0)).getChildren().get(0)).getChildren().get(0)).fitWidthProperty().bind(post_scrollPane.widthProperty().divide(4).subtract(30));
                ((ImageView) ((AnchorPane) ((VBox) pane.getChildren().get(0)).getChildren().get(0)).getChildren().get(0)).fitHeightProperty().bind(post_scrollPane.widthProperty().divide(4).subtract(30));
                ((Line) ((VBox) pane.getChildren().get(0)).getChildren().get(1)).endXProperty().bind(post_scrollPane.widthProperty().divide(4).subtract(30));
                postInHome_vBox.getChildren().add(pane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void likedVideos_action() {

        postInHome_vBox.getChildren().clear();
        Label label = new Label("Your liked video list:");
        label.setStyle("-fx-text-fill: #d50101; -fx-font-size: 24; -fx-font-weight: bold;");
        postInHome_vBox.getChildren().add(label);

        request.likedVideoList();
        JSONObject response = read();
        JSONArray video_idList = response.getJSONArray("videoIdList");
        ArrayList<VideoInfo> videoInfoList = new ArrayList<>();
        for (int k = 0; k < video_idList.length(); k++) {
            int video_id = video_idList.getInt(k);
            request.video(video_id);
            JSONObject response2 = read();
            videoInfoList.add(new VideoInfo(video_id, response2));
        }
        for (int i = 0; i < videoInfoList.size(); i++) {

            try {
                VideoInfo video = videoInfoList.get(i);
                request.imageFile(video.id);
                byte[] imageBytes = readFile();


                File file = new File("src/main/resources/CACHE/imageCache" + "/img" + (i) + ".jpg");

                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(imageBytes);


                FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("../../home/PostInHome.fxml")));
                AnchorPane pane = fxmlLoader.load();
                if (Objects.equals(HomeController.theme, "light")) {
                    pane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../home/PostInHomeStyle.css")).toExternalForm());
                } else {
                    pane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../home/PostInHomeStyle_dark.css")).toExternalForm());

                }

                PostInHomeController postInHomeController = fxmlLoader.getController();
                postInHomeController.define(imageBytes, video.id, video.getTitle(), video.getChannel_name(), video.getTotal_view(), video.getCreation_time(), video);
                postInHomeController.setup();

                postInHomeController.showOther(false);
                ((ImageView) ((AnchorPane) ((VBox) pane.getChildren().get(0)).getChildren().get(0)).getChildren().get(0)).fitWidthProperty().bind(post_scrollPane.widthProperty().divide(4).subtract(30));
                ((ImageView) ((AnchorPane) ((VBox) pane.getChildren().get(0)).getChildren().get(0)).getChildren().get(0)).fitHeightProperty().bind(post_scrollPane.widthProperty().divide(4).subtract(30));
                ((Line) ((VBox) pane.getChildren().get(0)).getChildren().get(1)).endXProperty().bind(post_scrollPane.widthProperty().divide(4).subtract(30));
                postInHome_vBox.getChildren().add(pane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void settings_action() {

    }

    @FXML
    public void theme_action() {
        if (Objects.equals(HomeController.theme, "light")) {
            HomeController.theme = "dark";
        } else {
            HomeController.theme = "light";
        }
        closeBars();
    }

    //_______________________________________________________________________________________
    //________________________________________________PRIVET____________________________________________________
    //__________________________________________________________________________________________________________
    @FXML
    public void on_like(ActionEvent actionEvent) {
        if (!is_liked) {
            request.likeVideo(videoInfo.id);
            JSONObject response = read();


            if (response.getString("responseType").equals("/likeVideo_accepted")) {
                System.out.println("/likeVideo_accepted");
                //todo show video is liked
                is_liked = true;
            } else {
                System.out.println(response.getString("responseType"));
            }
        } else {
            request.remove_likedVideo(videoInfo.id);
            JSONObject response = read();
            if (response.getString("responseType").equals("/remove_likedVideo_rejected")) {
                System.out.println("/remove_likedVideo");
                //todo show video is not liked
                is_liked = false;
            } else {
                System.out.println(response.getString("responseType"));
            }
        }
    }

    @FXML
    public void on_dislike(ActionEvent actionEvent) {
        if (!is_disliked) {
            request.dislikeVideo(videoInfo.id);
            JSONObject response = read();


            if (response.getString("responseType").equals("/dislikeVideo_accepted")) {
                System.out.println("/dislikeVideo_accepted");
                //todo show video is disliked
                is_disliked = true;
            } else {
                System.out.println(response.getString("responseType"));
            }
        } else {
            request.remove_dislikedVideo(videoInfo.id);
            JSONObject response = read();
            if (response.getString("responseType").equals("/remove_dislikedVideo_rejected")) {
                System.out.println("/remove_dislikedVideo");
                //todo show video is not disliked
                is_disliked = false;
            } else {
                System.out.println(response.getString("responseType"));
            }
        }
    }

    @FXML
    public void on_subscribe(ActionEvent actionEvent) {

        if (!is_subscribed) {
            request.subscribeChannel(videoInfo.channel_username);
            JSONObject response = read();
            if (response.getString("responseType").equals("/subscribeChannel_accepted")) ;
            {
                System.out.println("/subscribeChannel_accepted");
                subscribe_button.setText("subscribed");
                is_subscribed = true;
            }
        } else {
            request.unsubscribeChannel(videoInfo.channel_username);
            JSONObject response = read();
            if (response.getString("responseType").equals("/unsubscribeChannel_accepted")) ;
            {
                System.out.println("/unsubscribeChannel_accepted");
                subscribe_button.setText("subscribe");
                is_subscribed = false;
            }
        }
    }


    private Timeline delayTimeline = new Timeline(
            new KeyFrame(Duration.seconds(3), event ->
            {
                videoController_vBox.setVisible(true);
                FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), videoController_vBox);
                fadeTransition1.setFromValue(1);
                fadeTransition1.setToValue(0);
                fadeTransition1.setOnFinished(actionEvent ->
                        videoController_vBox.setVisible(false)
                );
                fadeTransition1.play();
            }
            ));

    @FXML
    public void showVideoController_action() {


        if (!videoController_vBox.isVisible()) {
            videoController_vBox.setVisible(true);
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), videoController_vBox);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
            fadeTransition.play();
            delayTimeline.play();
        } else {
            delayTimeline.playFrom(Duration.seconds(0));
        }


    }

    @FXML
    public void playPauseVide_action() {
        if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
            mediaPlayer.pause();
            playAndPause_button.setStyle("-fx-shape: 'M 12,26 18.5,22 18.5,14 12,10 z M 18.5,22 25,18 25,18 18.5,14 z'");
        } else {
            playAndPause_button.setStyle("-fx-shape: 'M 12,26 16,26 16,10 12,10 z M 21,26 25,26 25,10 21,10'");
            mediaPlayer.play();
        }
    }

    @FXML
    public void next_action() {
        //TODO
        //start the next video for video player

    }

    @FXML
    public void muteUnmute_action() {
        if (mediaPlayer.isMute()) {
            mediaPlayer.setMute(false);
            mute_button.setStyle("-fx-shape: 'M8,21 L12,21 L17,26 L17,10 L12,15 L8,15 L8,21 Z M19,14 L19,22 C20.48,21.32 21.5,19.77 21.5,18 C21.5,16.26 20.48,14.74 19,14 ZM19,11.29 C21.89,12.15 24,14.83 24,18 C24,21.17 21.89,23.85 19,24.71 L19,26.77 C23.01,25.86 26,22.28 26,18 C26,13.72 23.01,10.14 19,9.23 L19,11.29 Z'");
        } else {
            mediaPlayer.setMute(true);
            mute_button.setStyle("-fx-shape: 'm 21.48,17.98 c 0,-1.77 -1.02,-3.29 -2.5,-4.03 v 2.21 l 2.45,2.45 c .03,-0.2 .05,-0.41 .05,-0.63 z m 2.5,0 c 0,.94 -0.2,1.82 -0.54,2.64 l 1.51,1.51 c .66,-1.24 1.03,-2.65 1.03,-4.15 0,-4.28 -2.99,-7.86 -7,-8.76 v 2.05 c 2.89,.86 5,3.54 5,6.71 z M 9.25,8.98 l -1.27,1.26 4.72,4.73 H 7.98 v 6 H 11.98 l 5,5 v -6.73 l 4.25,4.25 c -0.67,.52 -1.42,.93 -2.25,1.18 v 2.06 c 1.38,-0.31 2.63,-0.95 3.69,-1.81 l 2.04,2.05 1.27,-1.27 -9,-9 -7.72,-7.72 z m 7.72,.99 -2.09,2.08 2.09,2.09 V 9.98 z'");
        }
    }

    private int i = 1;

    @FXML
    public void speed_action() {
        if (i == 0) {
            mediaPlayer.setRate(1);
            i = (i + 1) % 3;
        } else if (i == 1) {
            mediaPlayer.setRate(2);
            i = (i + 1) % 3;
        } else if (i == 2) {
            mediaPlayer.setRate(0.5);
            i = (i + 1) % 3;
        }
    }

    @FXML
    public void fullScreen_action(ActionEvent actionEvent) {
        Pane pane = new Pane(video_vBox);
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    @FXML
    public void other_action() {
        boolean status = other_pane.isVisible();
        other_pane.setVisible(!status);
        other_pane.setDisable(status);
    }

    @FXML
    public void download_action() {
        //TODO

        other_action();
    }

    @FXML
    public void clip_action() {
        //TODO

        other_action();

    }

    @FXML
    public void save_action() {
        request.add_WatchLaterVideo(videoInfo.id);
        read();
        other_action();

    }

    @FXML
    public void report_action() {
        //TODO

        other_action();
    }
}
