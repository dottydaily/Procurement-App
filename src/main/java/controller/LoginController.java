package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.util.Duration;
import model.DBConnecter;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.PageManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LoginController {

    @FXML
    protected Label signInLabel;
    @FXML
    protected Label statusLabel;

    @FXML
    protected JFXButton logInButton;
    @FXML
    protected JFXButton signUpButton;

    @FXML
    protected JFXTextField usernameTextField;
    @FXML
    protected JFXPasswordField passwordTextField;

    @FXML
    protected Label timeLabel;

    private DBConnecter database = DBConnecter.getInstance();
    private ResultSet resultSet;

    @FXML
    public void initialize() {
        try {
            resultSet = database.getResultSet("SELECT * FROM user_list");

            while (resultSet.next()) {
                System.out.println(resultSet.getString("username"));
                System.out.println(resultSet.getString("password"));
            }

            resultSet.beforeFirst();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        PageManager.setClockInView(timeLabel);
    }

    @FXML
    protected void handleLogInButton(ActionEvent e) {
        String filledUsername = usernameTextField.getText();
        String filledPassword = passwordTextField.getText();

        System.out.printf("User have log in by --> username: %s, password: %s\n"
                            , filledUsername, filledPassword);

        if (filledUsername.isEmpty() || filledPassword.isEmpty()) {
            statusLabel.setText("Empty Username/ID or Password. Try again.");
        }
        else {
            System.out.println("Searching user name");
            try {
                boolean isLoggedIn = false;
                while (resultSet.next()) {
                    String checkingUsername = resultSet.getString("username");
                    String checkingPassword = resultSet.getString("password");
                    System.out.printf("compare with --> username: %s, password: %s\n"
                            , checkingUsername, checkingPassword);

                    if (checkingUsername.equals(filledUsername) && checkingPassword.equals(filledPassword)) {
                        isLoggedIn = true;
                        break;
                    }
                }

                resultSet.beforeFirst();

                if (isLoggedIn) {
                    statusLabel.setText("Log in successful!");
//                    PageManager.swapPage(logInButton.getScene(), "SelectMenuView.fxml");
                    PageManager.swapPage(e, "SelectMenuView.fxml");
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
    protected void handleSignUpButton(ActionEvent e) {
        PageManager.swapPage(e, "RegisterView.fxml");
    }
}