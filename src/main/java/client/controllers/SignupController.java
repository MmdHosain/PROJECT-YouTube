package client.controllers;

import client.models.Request;
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

public class SignupController {

    @FXML
    private TextField signupUsername_textField;
    @FXML
    private PasswordField signupPass_passwordField;
    @FXML
    private PasswordField signupConfirmPass_passwordField;
    @FXML
    private Label signupStatus_label;


    @FXML
    public void signup_action(ActionEvent actionEvent) throws IOException {
        String username = signupUsername_textField.getText();
        String password = signupPass_passwordField.getText();
        String passwordConfirm = signupConfirmPass_passwordField.getText();
        //TODO name input
        //TODO email input
        //TODO number input

        signupStatus_label.setVisible(false); //to show the errors
        if(password.equals(passwordConfirm)){
            request.signUp(username,password,"","","");
            getResponse(actionEvent);
        }
        else {
            signupStatus_label.setText("passwords doesn't match");
            signupStatus_label.setVisible(true);
        }


    }

    private void getResponse(ActionEvent actionEvent) {
        JSONObject response = read();
        if(response.get("responseType").equals("/signUp_accepted")){
            signUpAccepted(response,actionEvent);
        }
        else SignUpRejected();

    }

    private void signUpAccepted(JSONObject response,ActionEvent actionEvent) {
        userAccount = new UserAccount((String) response.get("username"),(String) response.get("password"));
        goToHome(actionEvent);

    }
    private void SignUpRejected() {
        signupStatus_label.setText("this Username has already been chosen by another user ");
        signupStatus_label.setVisible(true);

    }
    public void goToHome(ActionEvent actionEvent) {
        try {

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../home/Home.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../home/HomeStyle.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @FXML
    public void backToLogin_action(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../entry/Login.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../entry/LoginStyle.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();

    }
}
