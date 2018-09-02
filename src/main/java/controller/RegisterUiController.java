package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import model.DBConnecter;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class RegisterUiController {
    @FXML
    protected JFXTextField firstNameTextField;
    private String firstName;
    @FXML
    protected JFXTextField lastNameTextField;
    private String lastName;
    @FXML
    protected JFXTextField usernameTextField;
    private String username;
    @FXML
    protected JFXPasswordField passwordField;
    private String password;
    @FXML
    protected JFXPasswordField confirmPasswordField;
    private String confirmPassword;
    @FXML
    protected JFXTextField emailTextField;
    private String email;
    @FXML
    protected JFXTextField phoneNumberTextField;
    private String phoneNumber;

    @FXML
    protected Label statusLabel;

    @FXML
    protected JFXButton backButton;
    @FXML
    protected JFXButton createButton;

    @FXML
    protected ArrayList<Node> fxmlList;

    private DBConnecter database = new DBConnecter();


    @FXML
    protected void initialize() {
        fxmlList = new ArrayList<>(8);
        fxmlList.add(firstNameTextField);
        fxmlList.add(lastNameTextField);
        fxmlList.add(usernameTextField);
        fxmlList.add(passwordField);
        fxmlList.add(confirmPasswordField);
        fxmlList.add(emailTextField);
        fxmlList.add(phoneNumberTextField);
        fxmlList.add(createButton);
    }

    @FXML
    protected void handleBackButton(ActionEvent e) {
        PageSwapper.swapPage(e, "LoginUi.fxml");
    }

    @FXML
    protected void handleCreateButton(ActionEvent e) {
        if (updateData()) {
            for (Node n : fxmlList) {
                n.setDisable(true);
            }
            statusLabel.setText("Register Complete. Please Back to login menu.");
        }

    }

    // helper method
    private String copyWithChecking(String words, char check, String status, boolean isName) {
        for (char c : words.toCharArray()) {
            if (c == check) {
                statusLabel.setText(status);
                return null;
            }

            if (isName) {
                if (words.matches("\\W")) {
                    statusLabel.setText("Name can't have any special characters!");
                    return null;
                }

                words = words.toUpperCase();
            }
        }

        return words;
    }
    private String copyPhoneNumber(String words, char check, String status) {
        if (copyWithChecking(words, check, status, false) == null) {
            return null;
        }

        return words.replaceAll("\\D", "");
    }

    // helper method
    private boolean updateData() {

        if (firstNameTextField.getText().isEmpty() || lastNameTextField.getText().isEmpty() ||
            usernameTextField.getText().isEmpty() || passwordField.getText().isEmpty() ||
            confirmPasswordField.getText().isEmpty() || emailTextField.getText().isEmpty() ||
            phoneNumberTextField.getText().isEmpty()) {
            statusLabel.setText("Please fill all information!");
            return false;
        }

        firstName = copyWithChecking(firstNameTextField.getText(), ' ',
                "First Name cannot have any space!", true);
        if (firstName == null) return false;

        lastName = copyWithChecking(lastNameTextField.getText(), ' ',
                "Last Name cannot have any space!", true);
        if (lastName == null) return false;

        username = copyWithChecking(usernameTextField.getText(), ' ',
                "Username cannot have any space!", false);
        if (username == null) return false;

        if (passwordField.getText().equals(confirmPasswordField.getText())) {
            password = passwordField.getText();
            confirmPassword = confirmPasswordField.getText();
        }
        else {
            statusLabel.setText("Password is not matching!");
            return false;
        }

        email = copyWithChecking(emailTextField.getText(), ' ',
                "E-mail address cannot have any space!", false);
        if (email == null) return false;

        phoneNumber = copyPhoneNumber(phoneNumberTextField.getText(), ' ',
                "Phone number cannot have any space!");
        if (phoneNumber == null) return false;


        database.insertUserData(firstName, lastName, username, password, email, phoneNumber);
        System.out.println("DONE UPDATE DATA!");

        System.out.println(firstName);
        System.out.println(lastName);
        System.out.println(username);
        System.out.println(password);
        System.out.println(confirmPassword);
        System.out.println(email);
        System.out.println(phoneNumber);

        return true;
    }
}