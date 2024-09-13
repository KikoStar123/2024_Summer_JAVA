package client.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * GradientBackgroundAnimation 类用于创建一个 JavaFX 应用程序，
 * 该应用程序展示了一个包含动态背景颜色的侧边栏导航栏。
 */
public class GradientBackgroundAnimation extends Application {

    /**
     * 应用程序的入口点，设置并显示主舞台。
     *
     * @param primaryStage 主舞台
     */
    @Override
    public void start(Stage primaryStage) {
        VBox navbar = new VBox();
        navbar.setStyle("-fx-background-color: #fff; " +
                "-fx-padding: 16px; " +
                "-fx-spacing: 8px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.03), 40, 0, 0, 0);" +
                "-fx-border-radius: 10px; " +
                "-fx-background-radius: 10px;");

        String[] items = {"Home", "Messages", "Customers", "Projects", "Resources", "Help", "Settings"};

        for (String item : items) {
            VBox itemBox = new VBox();
            itemBox.setStyle("-fx-alignment: center; " +
                    "-fx-background-color: #eaeef6; " +
                    "-fx-padding: 16px; " +
                    "-fx-border-radius: 10px; " +
                    "-fx-background-radius: 10px;");

            Circle icon = new Circle(10, Color.web("#406ff3"));
            Label label = new Label(item);
            label.setFont(Font.font("Open Sans", FontWeight.BOLD, 14));
            label.setTextFill(Color.web("#6a778e"));
            Tooltip tooltip = new Tooltip(item);
            Tooltip.install(label, tooltip);

            itemBox.getChildren().addAll(icon, label);
            navbar.getChildren().add(itemBox);

            itemBox.setOnMouseEntered(e -> {
                itemBox.setStyle("-fx-background-color: #406ff3; " +
                        "-fx-alignment: center; " +
                        "-fx-padding: 16px; " +
                        "-fx-border-radius: 10px; " +
                        "-fx-background-radius: 10px;");
                label.setTextFill(Color.WHITE);
            });

            itemBox.setOnMouseExited(e -> {
                itemBox.setStyle("-fx-background-color: #eaeef6; " +
                        "-fx-alignment: center; " +
                        "-fx-padding: 16px; " +
                        "-fx-border-radius: 10px; " +
                        "-fx-background-radius: 10px;");
                label.setTextFill(Color.web("#6a778e"));
            });
        }

        Scene scene = new Scene(navbar, 300, 600);
        primaryStage.setTitle("JavaFX Navbar");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * 程序的主入口点，用于启动 JavaFX 应用程序。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        launch(args);
    }
}
