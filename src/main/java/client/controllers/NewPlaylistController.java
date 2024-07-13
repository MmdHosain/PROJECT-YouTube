package client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;

import static client.models.Main.*;

public class NewPlaylistController implements Initializable {
    @FXML
    private ComboBox<String> pripub_comboBox;
    @FXML
    private TextArea title_textArea;
    @FXML
    private TextArea description_textArea;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pripub_comboBox.getItems().setAll("Public", "Privet");
    }

    @FXML
    public void close_action(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void creat_action() {
        String title = title_textArea.getText();
        String description = description_textArea.getText();

        request.createPlaylist(title,description,userAccount.channel_username);
        JSONObject response = read();
        UploadFileController.chosenPlaylist = response.getString("title");
    }
}
