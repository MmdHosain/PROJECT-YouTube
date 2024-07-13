package client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import static client.models.Main.*;

public class CommentController {
    int comment_id;
    String username;
    public void define(int comment_id) {
        this.comment_id = comment_id;
        request.comment(comment_id);
        JSONObject response = read();
        if (response.getInt("repliedTo")==-1){

            username = response.getString("senderName");
            cmntChannelUsername_hyperLink.setText(response.getString("senderName"));
            cmntText_label.setText(response.getString("text"));
            whenCommented_label.setText(response.getString("senderName"));
            cmntChannelUsername_hyperLink.setText(response.getString("creationTime"));
        }
    }

    @FXML
    private Hyperlink cmntChannelUsername_hyperLink;
    @FXML
    private Label cmntText_label;
    @FXML
    private Button like_button;
    @FXML
    private VBox videoComment_vBox;
    @FXML
    private Label whenCommented_label;
    @FXML
    private Circle commentProf_circle;

    public void setup() {



        try {
            request.channelProfileImg(username);
            JSONObject response = read();
            byte[] videoBytes = readFile();

            File file = new File("src/main/resources/CACHE/imageCache/comment.jpg");
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(videoBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Image image = new Image(Objects.requireNonNull(getClass().getResource("../../CACHE/imageCache/comment.jpg")).openStream());
            commentProf_circle.setFill(new ImagePattern(image));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void likeComment_action() {
        //TODO
        like_button.setText("20k");
    }

    @FXML
    public void dislikeComment_button() {
        //TODO
        like_button.setText("20k");
    }

    @FXML
    public void replyComment_action() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("../../videoPlayer/newComment.fxml")));
            GridPane gridPane = fxmlLoader.load();
            ((Button)((GridPane) gridPane.getChildren().get(3)).getChildren().get(2)).setText("Reply");
            gridPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../videoPlayer/newCommentStyle.css")).toExternalForm());
            videoComment_vBox.getChildren().add(3, gridPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void showCommentReplies_action(){
        //TODO show comment replies
    }
}
