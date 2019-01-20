package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class QuotationListController extends Observable {

    @FXML
    protected TableView<QuotationDetail> quotationTableView;
    @FXML
    protected TableColumn<Quotation, String> quotationIdTableColumn;
    @FXML
    protected TableColumn<Quotation, String> pRIdTableColumn;
    @FXML
    protected TableColumn<Quotation, String> dateTableColumn;
    @FXML
    protected TableColumn<Quotation, String> customerIdTableColumn;
    @FXML
    protected TableColumn<Quotation, String> firstNameTableColumn;
    @FXML
    protected TableColumn<Quotation, String> lastNameTableColumn;
    @FXML
    protected TableColumn<Quotation, Integer> totalCostTableColumn;
    @FXML
    protected Label descriptionLabel;
    @FXML
    protected Label timeLabel;

    @FXML
    protected JFXTextField customerNameTextField;
    @FXML
    protected JFXTextField priceStartTextField;
    @FXML
    protected JFXTextField priceEndTextField;
    @FXML
    protected JFXDatePicker startDatePicker;
    @FXML
    protected JFXDatePicker endDatePicker;
    @FXML
    protected JFXButton searchButton;

    @FXML
    protected JFXButton backButton;

    private DBConnecter database = DBConnecter.getInstance();
    private Object previousController;
    private ArrayList<Observer> observers = new ArrayList<>();
    private ObservableList<QuotationDetail> quotationDetails;

    public QuotationListController(Object previousController) {
        this.previousController = previousController;

        if (this.previousController instanceof CreatePOController) {
            addObserver((CreatePOController) this.previousController);
        }
    }

    @FXML
    protected void initialize() {
        if (previousController instanceof CreatePOController) {
            backButton.setText("Close");
        } else if (previousController instanceof SelectMenuController) {
            descriptionLabel.setOpacity(0);
        }

        PageManager.setClockInView(timeLabel);

        quotationIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("quotationId"));
        pRIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseRequestId"));
        dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        customerIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        firstNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        totalCostTableColumn.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
    }

    private ObservableList<QuotationDetail> getQuotationDetailList(String additionalQuery) {
        ObservableList<QuotationDetail> list = FXCollections.observableArrayList();

        try {
            String query = "SELECT\n" +
                    "    quotation_list.quotation_id,\n" +
                    "    quotation_list.pr_id,\n" +
                    "    quotation_list.date,\n" +
                    "    quotation_list.customer_id,\n" +
                    "    customer_list.customer_firstname,\n" +
                    "    customer_list.customer_lastname,\n" +
                    "    quotation_list.total_cost\n" +
                    "FROM\n" +
                    "    quotation_list\n" +
                    "INNER JOIN customer_list ON quotation_list.customer_id = customer_list.customer_id\n";

            if (previousController instanceof CreatePOController) {
                query = query +
                        "AND quotation_list.quotation_status = \"Incomplete\"\n" +
                        additionalQuery + "\n" +
                        "GROUP BY quotation_list.quotation_id";
            } else if (previousController instanceof SelectMenuController) {
                query = query +
                        additionalQuery + "\n" +
                        "GROUP BY quotation_list.quotation_id";
            }

            ResultSet quotationResultSet = database.getResultSet(query);
            while (quotationResultSet.next()) {

                QuotationDetail quotationDetail = new QuotationDetail(quotationResultSet.getInt(1)
                        , quotationResultSet.getInt(2), quotationResultSet.getString(3)
                        , quotationResultSet.getInt(4), quotationResultSet.getString(5)
                        , quotationResultSet.getString(6), quotationResultSet.getInt(7));
                list.add(quotationDetail);
            }
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }

        return list;
    }

    @FXML
    public void clickQuotationDetail(MouseEvent event) {
        if (event.getClickCount() == 2 && previousController instanceof CreatePOController) {

            if (database.hasValueInTable("po", "quotation_id"
                    , quotationTableView.getSelectionModel().getSelectedItem().getQuotationId())) {
                PageManager.newAlert("Found an exist PO with this Quotation!"
                        , "Already have Purchase Order with this Quotation!\n" +
                                "Please select another quotation"
                        , Alert.AlertType.ERROR);
            } else {
                notifyObservers(quotationTableView.getSelectionModel().getSelectedItem());
                Stage stage = (Stage) quotationTableView.getScene().getWindow();
                stage.close();
            }
        }
    }

    @FXML
    public void handleBackButton(ActionEvent e) {
        if (previousController instanceof SelectMenuController) {
            PageManager.swapPage(e, "SelectMenuView.fxml");
        } else {
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    protected void handleSearchButton(ActionEvent e) {
        String additionQuery = "WHERE customer_list.customer_firstname LIKE '%%'\n";

        if (!customerNameTextField.getText().isEmpty()) {
            String[] fullName;
            if (customerNameTextField.getText().matches(".*\\s.*")) {
                fullName = customerNameTextField.getText().split("\\s");
                additionQuery = String.format("WHERE (customer_list.customer_firstname LIKE '%%%s%%'\n" +
                        "AND customer_list.customer_lastname LIKE '%%%s%%')\n", fullName[0], fullName[1]);
            } else {
                String name = customerNameTextField.getText();
                additionQuery = String.format("WHERE (customer_list.customer_firstname LIKE '%%%s%%'\n" +
                        "OR customer_list.customer_lastname LIKE '%%%s%%')\n", name, name);
            }
        }

        int startPrice = 0;
        int endPrice = 50000;
        if (!priceStartTextField.getText().isEmpty()) {
            if (priceStartTextField.getText().matches("\\d+")) {
                startPrice = Integer.parseInt(priceStartTextField.getText());
            }

            if (startPrice > endPrice || startPrice < 0) {
                startPrice = 0;
                priceStartTextField.clear();
            }
        }
        if (!priceEndTextField.getText().isEmpty()) {
            if (priceEndTextField.getText().matches("\\d+")) {
                endPrice = Integer.parseInt(priceEndTextField.getText());
            }

            if (endPrice < startPrice || endPrice > 50000) {
                endPrice = 50000;
                priceEndTextField.clear();
            }
        }
        additionQuery += String.format("AND quotation_list.total_cost BETWEEN %d AND %d\n", startPrice, endPrice);
//
        DateTimeFormatter dateFormatForSQL = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate startDate = LocalDate.of(2017, 1, 1);
        LocalDate endDate = LocalDate.of(9999, 12, 31);
        if (startDatePicker.getValue() != null) {
            if (!startDatePicker.getValue().isAfter(endDate)) {
                startDate = startDatePicker.getValue();
            } else {
                startDatePicker.setValue(LocalDate.of(2017, 1, 1));
            }
        }
        if (endDatePicker.getValue() != null) {
            if (!endDatePicker.getValue().isBefore(startDate)) {
                endDate = endDatePicker.getValue();
            } else {
                endDatePicker.setValue(LocalDate.of(9999, 12, 31));
            }
        }
        System.out.println(startDate.toString() + " <---> " + endDate.toString());
        additionQuery += String.format("AND quotation_list.date BETWEEN '%s' AND '%s'\n", startDate.format(dateFormatForSQL), endDate.format(dateFormatForSQL));

        System.out.println("Additional Query : " + additionQuery);

        quotationDetails = getQuotationDetailList(additionQuery);
        quotationTableView.setItems(quotationDetails);
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
