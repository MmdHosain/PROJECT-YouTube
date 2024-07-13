package client.controllers;

import client.models.VideoInfo;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import static client.models.Main.*;

public class HomeController implements Initializable {
    public static String theme = "light";
    @FXML
    private BorderPane homeMain_borderPain;
    @FXML
    private VBox postInHome_vBox;
    @FXML
    private ScrollPane post_scrollPane;
    @FXML
    private Pane toolBar_pane;
    @FXML
    private VBox toolBar_vBox;
    @FXML
    private Pane account_pane;
    @FXML
    private Pane fadeRectangle_pane;
    @FXML
    private Pane creat_pane;
    @FXML
    private Circle accountProfHome_circle1;
    @FXML
    private ScrollPane toolBar_scrollPane;
    @FXML
    private Rectangle fadeRectangle_rectangle;
    @FXML
    private Circle accountProfHome_circle;
    @FXML
    private HBox tags_hBox;
    @FXML
    private ScrollPane tags_scrollPane;
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //****************************************** PUBLIC *********************************************
        searchResult_vBox.prefWidthProperty().bind(Search_textField.widthProperty());


        toolBar_scrollPane.prefHeightProperty().bind(homeMain_borderPain.heightProperty());
        fadeRectangle_rectangle.widthProperty().bind(homeMain_borderPain.widthProperty());
        fadeRectangle_rectangle.heightProperty().bind(homeMain_borderPain.heightProperty());

        //TODO
        for (int i = 0; i < 20; i++) {
            Button button = new Button("Recommended tags" + i);
            button.setStyle("-fx-background-color: #efd6d6");
            tags_hBox.getChildren().add(button);
        }

        //________________________PROFILE PHOTO__________________________
        request.profileImg();
        if (read().getString("responseType").equals("/profileImg_accepted")) {

            byte[] profImgBytes = readFile();


            File file = new File("src/main/resources/CACHE/imageCache/profImg0.jpg");

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(profImgBytes);

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                //TODO set profile of who commenting
                Image image = new Image(Objects.requireNonNull(getClass().getResource("../../CACHE/imageCache/profImg0.jpg")).openStream());
                accountProfHome_circle.setFill(new ImagePattern(image));
                accountProfHome_circle1.setFill(new ImagePattern(image));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

//            accountProfHome_button1.
        }


        //_______________________VIDEO THUMBNAIL__________________
        request.videoList(9, new JSONArray());
        JSONObject response1 = read();
        JSONArray video_idList = response1.getJSONArray("videoIdList");
        ArrayList<VideoInfo> videoList = new ArrayList<>();

        for (int i = 0; i < video_idList.length(); i++) {

            int video_id = video_idList.getInt(i);
            request.video(video_id);
            JSONObject response2 = read();
            videoList.add(new VideoInfo(video_id, response2));
        }


        for (int i = 0; i < 3; i++) {
            HBox hBox = new HBox();
            hBox.setSpacing(10);
            hBox.setFillHeight(true);
            hBox.prefHeightProperty().bind(post_scrollPane.widthProperty());
            hBox.prefWidthProperty().bind(post_scrollPane.heightProperty());


            for (int j = 0; j < 3; j++) {
                try {
                    int pointer = i * 3 + j;

                    VideoInfo video = videoList.get(pointer);
                    request.imageFile(video.id);
                    byte[] imageBytes = readFile();


                    File file = new File("src/main/resources/CACHE/imageCache" + "/img" + (i * 3 + j) + ".jpg");

                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(imageBytes);

                    //to load fxml file
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

                    ((ImageView) ((AnchorPane) ((VBox) pane.getChildren().get(0)).getChildren().get(0)).getChildren().get(0)).fitWidthProperty().bind(post_scrollPane.widthProperty().divide(3).subtract(20));
                    ((ImageView) ((AnchorPane) ((VBox) pane.getChildren().get(0)).getChildren().get(0)).getChildren().get(0)).fitHeightProperty().bind(post_scrollPane.widthProperty().divide(3).subtract(20));
                    ((Line) ((VBox) pane.getChildren().get(0)).getChildren().get(1)).endXProperty().bind(post_scrollPane.widthProperty().divide(3).subtract(30));


                    hBox.getChildren().add(pane);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            postInHome_vBox.getChildren().add(hBox);
        }
        //_______________________________________________________________________________________________________
    }

    public static byte[] resize(File icon) {
        try {
            BufferedImage originalImage = ImageIO.read(icon);

//            originalImage= Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, 128, 153);
            //To save with original ratio uncomment next line and comment the above.
            originalImage = Scalr.resize(originalImage, 306, 256);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "jpg", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (Exception e) {
            return null;
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

    //****************************************** PRIVET *********************************************
    @FXML
    public void moveRight_action() {
        double value = tags_scrollPane.getHvalue();
        tags_scrollPane.setHvalue(value + 0.1);
    }

    @FXML
    public void moveLeft_action() {
        double value = tags_scrollPane.getHvalue();
        tags_scrollPane.setHvalue(value - 0.1);
    }
}
