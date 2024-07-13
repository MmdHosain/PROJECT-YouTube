package client.controllers;

import client.models.VideoInfo;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static client.models.Main.*;

public class newCommentController implements Initializable {
    @FXML
    private TextArea newComment_textArea;
    @FXML
    private GridPane newComment_gridPane;
    @FXML
    private Circle cmntProffile_circle;

    VideoInfo videoInfo ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        newComment_textArea.setPrefRowCount(1);
        //TODO
//        String profPath = "-fx-background-image: url('')";
//        profile_button.setStyle(profPath);


        newComment_textArea.textProperty().addListener((observable, oldValue, newValue) -> {

            Text tempText = new Text(newValue);
            tempText.setFont(newComment_textArea.getFont());
            tempText.setWrappingWidth(newComment_textArea.getWidth() - newComment_textArea.getPadding().getLeft() - newComment_textArea.getPadding().getRight());

            newComment_textArea.setMinHeight(tempText.getLayoutBounds().getHeight() + 40);
        });

        try {
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
            }
            Image image = new Image(Objects.requireNonNull(getClass().getResource("../../CACHE/imageCache/profImg0.jpg")).openStream());
            cmntProffile_circle.setFill(new ImagePattern(image));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void define(VideoInfo videoInfo){
        this.videoInfo = videoInfo;
    }

    @FXML
    public void cancel_action() {
        newComment_gridPane.getChildren().clear();
    }

    @FXML
    public void comment_action() {

        String comment_text = newComment_textArea.getText();
        request.addComment(videoInfo.id,comment_text,-1);
        newComment_textArea.clear();
    }
}
