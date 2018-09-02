package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class PageSwapper {
    public static void swapPage(ActionEvent e, String fxmlName) {
        Button button = (Button) e.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        double width = button.getScene().getWidth();
        double height = button.getScene().getHeight();

        FXMLLoader loader = new FXMLLoader(LoginUiController.class.getResource("/fxml/" + fxmlName));

        try {
            stage.setScene(new Scene(loader.load(), width, height));
        } catch (IOException pageE) {
            pageE.printStackTrace();
        }
    }
}
