package client.controllers;

import client.models.UserAccount;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import static client.models.Main.*;

public class LoginController {

    @FXML
    private TextField loginUsername_textField;
    @FXML
    private PasswordField loginPass_passwordField;
    @FXML
    private Label loginStatus_label;


    @FXML
    public void login_action(ActionEvent actionEvent) throws IOException {
        String username = loginUsername_textField.getText();
        String password = loginPass_passwordField.getText();
        loginStatus_label.setVisible(false);
        request.login(username, password);
        getResponse(actionEvent);

    }

    @FXML
    public void goToSignup_action(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../entry/Signup.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../entry/SignupStyle.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    public void goToHome_action(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../home/Home.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../home/HomeStyle.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void getResponse(ActionEvent actionEvent) {
        JSONObject response = read();
        if (response.get("responseType").equals("/login_accepted")) {
            loginAccepted(response, actionEvent);
        } else loginRejected();

    }

    private void loginAccepted(JSONObject response, ActionEvent actionEvent) {

//
//        userAccount = new UserAccount((String) response.get("username"), (String) response.get("password"));
//        goToHome(actionEvent);

        Object username = response.get("username");
        Object password = response.get("password");

        if (username != null && password != null) {
            userAccount = new UserAccount(username.toString(), password.toString());
            userAccount.writeRememberMe();
            goToHome_action(actionEvent);
        }
    }

    private void loginRejected() {
        loginStatus_label.setText("Username or Password is Wrong");
        loginStatus_label.setVisible(true);
    }

}
