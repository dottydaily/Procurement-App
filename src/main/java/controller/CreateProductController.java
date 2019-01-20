package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import model.DBConnecter;
import model.DataChecker;
import model.PageManager;
import model.Product;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class CreateProductController extends Observable {

    @FXML
    protected JFXTextField productNameTextField;
    @FXML
    protected JFXTextField pricePerPieceTextField;
    @FXML
    protected JFXTextField amountTextField;

    @FXML
    protected JFXButton createButton;

    @FXML
    protected Label timeLabel;

    private DBConnecter database = DBConnecter.getInstance();
    private ArrayList<Observer> observers = new ArrayList<>();

    public CreateProductController(CreatePRController controller) {
        addObserver(controller);
    }

    @FXML
    protected void initialize() {
        PageManager.setClockInView(timeLabel);
    }

    @FXML
    protected void handleCreateButton(ActionEvent e) {
        if (productNameTextField.getText().isEmpty() || pricePerPieceTextField.getText().isEmpty()
                || amountTextField.getText().isEmpty()) {
            PageManager.newAlert("Create product error!"
                    , "Please fill all of information.", Alert.AlertType.ERROR);
        } else if (pricePerPieceTextField.getText().matches("\\d+") &&
                    pricePerPieceTextField.getText().matches("\\d+")){
            String productName = productNameTextField.getText();
            String pricePerEach = DataChecker.copyNumber(pricePerPieceTextField.getText(), ' ');
            String amount = DataChecker.copyNumber(amountTextField.getText(), ' ');

            if (productName == null || pricePerEach == null || amount == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                PageManager.newAlert("Create product error!"
                        , "Invalid information.", Alert.AlertType.ERROR);
            }
            else {
                Product product = database.insertProductData(productName, pricePerEach, amount);
                System.out.println("DONE UPDATE DATA!");

                System.out.println(product.getName());
                System.out.println(product.getPricePerEachAsInt());
                System.out.println(product.getQuantityAsInt());

                PageManager.newAlert("Register complete!"
                        , "Register product complete! You can add it again.", Alert.AlertType.INFORMATION);

                notifyObservers(product);
            }
        } else {
            PageManager.newAlert("Create product error!"
                    , "Price or amount must be number.", Alert.AlertType.ERROR);
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
