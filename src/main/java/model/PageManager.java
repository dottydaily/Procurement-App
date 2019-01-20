package model;

import controller.LoginController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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

    public static void setClockInView(Label timeLabel) {
        timeLabel.setStyle(" -fx-background-color: WHITE; -fx-border-width: 1; -fx-border-color: GREY");
        timeLabel.setAlignment(Pos.CENTER);
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern( "HH:mm:ss" );
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern( "E dd MMMM YYYY" );
        timeLabel.setText(LocalDate.now().format(dateFormat) + " " + LocalTime.now().format(timeFormat));
        final Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis( 500 ),
                        event -> {
                            timeLabel.setText(LocalDate.now().format(dateFormat) + " " + LocalTime.now().format(timeFormat));
                        }
                )
        );
        timeline.setCycleCount( Animation.INDEFINITE );
        timeline.play();
    }
}
