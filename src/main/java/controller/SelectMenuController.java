package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.DBConnecter;
import model.PageManager;

public class SelectMenuController {

    @FXML
    protected JFXButton createPRButton;
    @FXML
    protected JFXButton createQuotationButton;
    @FXML
    protected JFXButton createPOButton;
    @FXML
    protected JFXButton viewAllPRsButton;
    @FXML
    protected JFXButton viewAllQuotationsButton;
    @FXML
    protected JFXButton viewAllPOsButton;
    @FXML
    protected JFXButton viewProductHistory;
    @FXML
    protected JFXButton backButton;
    @FXML
    protected Label timeLabel;

    private DBConnecter database = DBConnecter.getInstance();

    @FXML
    protected void initialize() {
        PageManager.setClockInView(timeLabel);
    }

    @FXML
    protected void handleCreatePRButton(ActionEvent e) {
        PageManager.swapPage(e, "CreatePRView.fxml");
    }

    @FXML
    protected void handleCreateQuotationButton(ActionEvent e) {
        PageManager.swapPage(e, "CreateQuotationView.fxml");
    }

    @FXML
    protected void handleCreatePOButton(ActionEvent e) {
        PageManager.swapPage(e, "CreatePOView.fxml");
    }

    @FXML
    protected void handleViewAllPRsButton(ActionEvent e) {
        PRListController controller = new PRListController(this);
        PageManager.swapPage(e, "PRListView.fxml", controller);
    }

    @FXML
    protected void handleViewAllQuotationsButton(ActionEvent e) {
        QuotationListController controller = new QuotationListController(this);
        PageManager.swapPage(e, "QuotationListView.fxml", controller);
    }

    @FXML
    protected void handleViewAllPOsButton(ActionEvent e) {
        PageManager.swapPage(e, "POListView.fxml");
    }

    @FXML
    protected void handleViewProductHistoryButton(ActionEvent e) {
        ProductListController controller = new ProductListController(this);
        PageManager.swapPage(e, "ProductListView.fxml", controller);
    }

    @FXML
    protected void handleBackButton(ActionEvent e) {
        PageManager.swapPage(e, "LogInView.fxml");
    }
}
