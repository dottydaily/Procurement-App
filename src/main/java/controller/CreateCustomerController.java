package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Customer;
import model.DBConnecter;
import model.DataChecker;
import model.PageManager;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class CreateCustomerController extends Observable {
    @FXML
    protected JFXButton createButton;

    @FXML
    protected JFXTextField firstNameTextField;
    @FXML
    protected JFXTextField lastNameTextField;
    @FXML
    protected JFXTextField emailTextField;
    @FXML
    protected JFXTextField phoneNumberTextField;
    @FXML
    protected JFXTextArea addressTextField;
    @FXML
    protected Label timeLabel;

    private DBConnecter database = DBConnecter.getInstance();
    private CreatePRController createPRController;
    private ArrayList<Observer> observers = new ArrayList<>();

    public CreateCustomerController(CreatePRController controller) {
        addObserver(controller);
    }

    @FXML
    protected void initialize() {
        PageManager.setClockInView(timeLabel);
    }

    @FXML
    protected void handleCreateButton(ActionEvent e) {
        if (firstNameTextField.getText().isEmpty() || lastNameTextField.getText().isEmpty()
         || emailTextField.getText().isEmpty() || phoneNumberTextField.getText().isEmpty()
         || addressTextField.getText().isEmpty()) {
            PageManager.newAlert("Create customer error!"
                    , "Please fill all of information.", Alert.AlertType.ERROR);
        } else {
            String firstName = DataChecker.copyWithChecking(firstNameTextField.getText(), ' ', true);
            String lastName = DataChecker.copyWithChecking(lastNameTextField.getText(), ' ', true);
            String email = DataChecker.copyWithChecking(emailTextField.getText(), ' ', false);
            String phoneNumber = DataChecker.copyNumber(phoneNumberTextField.getText(), ' ');
            String address = addressTextField.getText();

            if (firstName == null || lastName == null || email == null || phoneNumber == null || address == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                PageManager.newAlert("Create customer error!"
                        , "Invalid information.", Alert.AlertType.ERROR);
            }
            else {
                Customer customer = database.insertCustomerData(firstName, lastName, email, address
                        , "Good", phoneNumber, "5000");
                System.out.println("DONE UPDATE DATA!");

                System.out.println(firstName);
                System.out.println(lastName);
                System.out.println(email);
                System.out.println(phoneNumber);
                System.out.println(address);

                PageManager.newAlert("Register complete!"
                        , "Register customer complete! back to previous page.", Alert.AlertType.INFORMATION);

                Stage stage = (Stage) createButton.getScene().getWindow();
                stage.close();

                notifyObservers(customer);
            }
        }
    }

    @Override
    public synchronized void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void notifyObservers(Object object) {
        for (Observer o: observers) {
            o.update(this, object);
        }
    }
}
