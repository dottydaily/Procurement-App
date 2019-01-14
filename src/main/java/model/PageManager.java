package model;

import controller.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class PageManager {
    public static void swapPage(ActionEvent e, String fxmlName) {
        swapPage(e, fxmlName, null);
    }

    public static void swapPage(ActionEvent e, String fxmlName, Object controller) {
        Button button = (Button) e.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        double width = button.getScene().getWidth();
        double height = button.getScene().getHeight();

        FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("/fxml/" + fxmlName));

        if (controller != null) {
            loader.setController(controller);
        }

        try {
            stage.setScene(new Scene(loader.load(), width, height));
        } catch (IOException pageE) {
            System.out.println("Exception by swaping to page: " + fxmlName);
            pageE.printStackTrace();
        }
    }

    public static Stage newWindow(String fxmlName, String title, Boolean isWait) {
        FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("/fxml/"+ fxmlName));

        Stage stage = createNewWindowStage(title, loader, fxmlName);

        stage.setX(100);
        stage.setY(100);

        if (isWait) {
            stage.showAndWait();
        }
        else {
            stage.show();
        }

        return stage;
    }

    public static Stage newWindow(String fxmlName, String title, Boolean isWait, Object controller) {
        FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("/fxml/"+ fxmlName));
        loader.setController(controller);

        Stage stage = createNewWindowStage(title, loader, fxmlName);

        stage.setX(100);
        stage.setY(100);

        if (isWait) {
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
        else {
            stage.show();
        }

        return stage;
    }

    private static Stage createNewWindowStage(String title, FXMLLoader loader, String fxmlName) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        try {
            stage.setScene(new Scene(loader.load(), 800, 600));
        } catch (IOException pageE) {
            System.out.println("Exception by create new window with page: " + fxmlName);
            pageE.printStackTrace();
        }

        return stage;
    }

    public static void newAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
