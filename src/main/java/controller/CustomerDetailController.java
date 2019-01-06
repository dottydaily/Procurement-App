package controller;

import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.Customer;

public class CustomerDetailController {

    @FXML
    protected Label firstNameLabel;
    @FXML
    protected Label lastNameLabel;
    @FXML
    protected Label emailLabel;
    @FXML
    protected Label phoneNumberLabel;
    @FXML
    protected Label creditStatusLabel;
    @FXML
    protected JFXTextArea addressTextArea;

    private CreatePRController createPRController;
    private CreateQuotationController createQuotationController;
    private CreatePOController createPOController;
    private AcceptPOController acceptPOController;

    public CustomerDetailController(CreatePRController controller) {
        createPRController = controller;
    }

    public CustomerDetailController(CreateQuotationController controller) {
        createQuotationController = controller;
    }

    public CustomerDetailController(CreatePOController controller) {
        createPOController = controller;
    }

    public CustomerDetailController(AcceptPOController controller) { acceptPOController = controller; }

    @FXML
    public void initialize() {
        Customer customer;
        if (createPRController != null) {
            customer = createPRController.getSelectCustomer();
        }
        else if (createQuotationController != null){
            customer = createQuotationController.getSelectedCustomer();
        }
        else if (createPOController != null){
            customer = createPOController.getSelectedCustomer();
        }
        else {
            customer = acceptPOController.getCustomerFromPoDetail();
        }
        firstNameLabel.setText(customer.getFirstName());
        lastNameLabel.setText(customer.getLastName());
        emailLabel.setText(customer.getEmail());
        phoneNumberLabel.setText(customer.getPhoneNumber());
        creditStatusLabel.setText(customer.getStatus());
        addressTextArea.setText(customer.getAddress());
    }
}
