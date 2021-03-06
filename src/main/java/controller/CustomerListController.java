package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Customer;
import model.DBConnecter;
import model.PageManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class CustomerListController extends Observable {

    @FXML
    protected TableView<Customer> customerTableView = new TableView<>();
    @FXML
    protected TableColumn idTableViewColumn;
    @FXML
    protected TableColumn<Customer, String> firstNameTableViewColumn;
    @FXML
    protected TableColumn<Customer, String> lastNameTableViewColumn;
    @FXML
    protected TableColumn<Customer, String> emailTableViewColumn;
    @FXML
    protected TableColumn<Customer, String> addressTableViewColumn;
    @FXML
    protected TableColumn<Customer, String> telTableViewColumn;
    @FXML
    protected TableColumn<Customer, String> statusTableViewColumn;
    @FXML
    protected TableColumn<Customer, Integer> limitTableViewColumn;

    @FXML
    protected Label timeLabel;

    private DBConnecter database = DBConnecter.getInstance();
    private ResultSet resultSet;
    private CreatePRController createPRController;
    private ArrayList<Observer> observers = new ArrayList<>();
    private ObservableList<Customer> customers;

    public CustomerListController(CreatePRController controller) {
        this.createPRController = controller;
//        initialize();
    }

    @FXML
    protected void initialize() {
        try {
            resultSet = database.getResultSet("SELECT * FROM customer_list");
            customers = getCustomerList(resultSet);
        } catch (SQLException sqlE) {
            System.out.println("Cannot query from customer_list.");
            sqlE.printStackTrace();
        }

        PageManager.setClockInView(timeLabel);

        addObserver(createPRController);
        idTableViewColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("id"));
        firstNameTableViewColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameTableViewColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailTableViewColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressTableViewColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        statusTableViewColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        telTableViewColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        limitTableViewColumn.setCellValueFactory(new PropertyValueFactory<>("limit"));
        limitTableViewColumn.setStyle(" -fx-alignment: CENTER-RIGHT;");

        customerTableView.setItems(customers);
    }

    private ObservableList<Customer> getCustomerList(ResultSet resultSet) {
        ObservableList<Customer> list = FXCollections.observableArrayList();

        try {
            while (resultSet.next()) {
                String statusString = "Bad";

                if (resultSet.getBoolean(6)) {
                    statusString = "Good";
                }

                Customer customer = new Customer(resultSet.getString(2), resultSet.getString(3),
                        resultSet.getString(4), resultSet.getString(5),
                        statusString, resultSet.getString(7),
                        Integer.parseInt(resultSet.getString(8)));
                customer.setId(resultSet.getString(1));

                list.add(customer);
            }
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }

        return list;
    }

    @FXML
    public void clickCustomer(MouseEvent event) {
        if (event.getClickCount() == 2) {
            notifyObservers(customerTableView.getSelectionModel().getSelectedItem());
            Stage stage = (Stage) customerTableView.getScene().getWindow();
            stage.close();
        }
    }

    @Override
    public synchronized void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void notifyObservers(Object object) {
        for (Observer o : observers) {
            o.update(this, object);
        }
    }
}
