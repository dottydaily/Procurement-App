package view;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import controller.DBConnecter;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginUiController {

    @FXML
    protected Label signInLabel;
    @FXML
    protected Label statusLabel;

    @FXML
    protected Button logInButton;
    @FXML
    protected Button signUpButton;

    @FXML
    protected JFXTextField usernameTextField;
    @FXML
    protected JFXPasswordField passwordTextField;

    private DBConnecter database = new DBConnecter();
    private ResultSet resultSet;

    @FXML
    public void initialize() {
        try {
            resultSet = database.getResultSet("SELECT * FROM user_list");
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    @FXML
    protected void handleLogInButton() {
        String filledUsername = usernameTextField.getText();
        String filledPassword = passwordTextField.getText();

        if (filledUsername.isEmpty() || filledPassword.isEmpty()) {
            statusLabel.setText("Empty Username/ID or Password. Try again.");
        }
        else {
            try {
                boolean isLoggedIn = false;
                while (resultSet.next()) {
                    if (resultSet.getString("username").equals(filledUsername)
                        && resultSet.getString("password").equals(filledPassword)) {

                        isLoggedIn = true;
                        break;
                    }
                }

                if (isLoggedIn) {
                    statusLabel.setText("Log in successful!");
                }
                else {
                    statusLabel.setText("Wrong Username/ID or Password.");
                }

                resultSet.beforeFirst();

            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace();
            }
        }
    }

    @FXML
    protected void handlePasswordField(KeyEvent e) {
        if (e.getCode().equals(KeyCode.ENTER)) {
            handleLogInButton();
        }
    }
}