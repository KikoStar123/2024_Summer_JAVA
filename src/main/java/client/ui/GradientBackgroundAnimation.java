package client.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

//public class GradientBackgroundAnimation extends Application {
//    @Override
//    public void start(Stage primaryStage) {
//        WebView webView = new WebView();
//        //webView.setPrefSize(450, 300);// 设置 WebView 的大小
//        String htmlFilePath=getClass().getResource("/dynamic-button.html").toExternalForm();// HTML 文件路径
//        webView.getEngine().load(htmlFilePath);// 加载内容
//
//        Scene scene = new Scene(webView, 400, 400);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}

//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.layout.BorderPane;
//import javafx.stage.Stage;
//import javafx.scene.web.WebEngine;
//import javafx.scene.web.WebView;
//import netscape.javascript.JSObject;
//
//public class GradientBackgroundAnimation extends Application {
//
//    private JavaMethod javaMethod = new JavaMethod();
//
//    @Override
//    public void start(Stage primaryStage) {
//        WebView webView = new WebView();
//        WebEngine webEngine = webView.getEngine();
//
//        // Load your HTML file
//        String htmlFilePath = getClass().getResource("/dynamic-button.html").toExternalForm();
//        webEngine.load(htmlFilePath);
//
//        // Create the layout and scene
//        BorderPane root = new BorderPane();
//
//        Scene scene = new Scene(root, 600, 400);
//        primaryStage.setTitle("JavaFX Button Example");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//
//        // 监听网页加载状态
//        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
//            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
//                // Bind the Java object to JavaScript
//                JSObject window = (JSObject) webEngine.executeScript("window");
//                window.setMember("javaMethod", javaMethod);
//
//                // Execute JavaScript to manipulate the button
//
//                webEngine.executeScript(
//                        "var button = document.getElementById('dynamicButton');" +
//                                "if (button) {" +
//                                "   button.setAttribute('data-text', '添加文字');" + //添加文字
//                                "   var buttonText = button.getAttribute('data-text');" +
//                                "   button.innerHTML = '<span>' + buttonText + '</span>';" +
//                                "   button.addEventListener('click', function() {" +
//                                "       window.javaMethod.handleClick();" + //添加响应
//                                "   });" +
//                                "} else {" +
//                                "   console.error('ID 为 dynamicButton 的元素未找到');" +
//                                "}"
//                );
//            }
//        });
//
//        root.setCenter(webView);
//    }
//
//    //
//    public static class JavaMethod {
//        public void handleClick() {
//            System.out.println("Button clicked from JavaFX!");
//        }
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

//public class GradientBackgroundAnimation extends Application {
//
//    @Override
//    public void start(Stage primaryStage) {
//        // Create a WebView
//        WebView webView = new WebView();
//        WebEngine webEngine = webView.getEngine();
//
////        // Load your HTML file
////        String htmlFilePath = getClass().getResource("/jelly-catalog.html").toExternalForm();
////        webEngine.load(htmlFilePath);
//
//        // Define HTML and CSS content
//        String htmlContent = """
//        <!DOCTYPE html>
//        <html lang="en">
//        <head>
//            <meta charset="UTF-8">
//            <meta name="viewport" content="width=device-width, initial-scale=1.0">
//            <title>Navbar</title>
//            <style>
//                $borderRadius: 10px;
//                $spacer: 1rem;
//                $primary: #406ff3;
//                $text: #6a778e;
//                $linkHeight: $spacer * 3.5;
//                $timing: 250ms;
//                $transition: $timing ease all;
//
//                @mixin gooeyEffect($i) {
//                  @keyframes gooeyEffect-#{$i} {
//                    0% {
//                      transform: scale(1, 1);
//                    }
//                    50% {
//                      transform: scale(0.5, 1.5);
//                    }
//                    100% {
//                      transform: scale(1, 1);
//                    }
//                  }
//                }
//                body {
//                  background: #eaeef6;
//                  font-family: 'Open Sans', sans-serif;
//                }
//                .navbar {
//                  position: fixed;
//                  top: $spacer;
//                  left: $spacer;
//                  background: #fff;
//                  border-radius: $borderRadius;
//                  padding: $spacer 0;
//                  box-shadow: 0 0 40px rgba(0,0,0,0.03);
//                  height: calc(100vh - #{$spacer*4});
//                }
//                .navbar__link {
//                  position:relative;
//                  display: flex;
//                  align-items: center;
//                  justify-content: center;
//                  height: $linkHeight;
//                  width: $spacer * 5.5;
//                  color: $text;
//                  transition: $transition;
//                }
//                .navbar__link span {
//                  position: absolute;
//                  left: 100%;
//                  transform: translate(-($spacer*3));
//                  margin-left: 1rem;
//                  opacity: 0;
//                  pointer-events: none;
//                  color: $primary;
//                  background: #fff;
//                  padding: $spacer *0.75;
//                  transition: $transition;
//                  border-radius: $borderRadius * 1.75;
//                }
//                .navbar__link:hover span,
//                .navbar__link:focus span {
//                  opacity: 1;
//                  transform: translate(0);
//                }
//                .navbar__item:last-child:before {
//                  content: '';
//                  position: absolute;
//                  opacity: 0;
//                  z-index: -1;
//                  top: 0;
//                  left: $spacer;
//                  width: $linkHeight;
//                  height: $linkHeight;
//                  background: $primary;
//                  border-radius: $borderRadius * 1.75;
//                  transition: $timing cubic-bezier(1, 0.2, 0.1, 1.2) all;
//                }
//                .navbar__item:hover:before {
//                  opacity: 1;
//                }
//                .navbar__item:hover:before {
//                  @include gooeyEffect(12);
//                  top: (100% / 12) * (12 - 1);
//                  animation: gooeyEffect-12 $timing 1;
//                }
//            </style>
//        </head>
//        <body>
//            <nav class="navbar">
//                <ul class="navbar__menu">
//                    <li class="navbar__item">
//                        <a href="#" class="navbar__link"><i data-feather="home"></i><span>Home</span></a>
//                    </li>
//                    <li class="navbar__item">
//                        <a href="#" class="navbar__link"><i data-feather="message-square"></i><span>Messages</span></a>
//                    </li>
//                    <li class="navbar__item">
//                        <a href="#" class="navbar__link"><i data-feather="users"></i><span>Customers</span></a>
//                    </li>
//                    <li class="navbar__item">
//                        <a href="#" class="navbar__link"><i data-feather="folder"></i><span>Projects</span></a>
//                    </li>
//                    <li class="navbar__item">
//                        <a href="#" class="navbar__link"><i data-feather="archive"></i><span>Resources</span></a>
//                    </li>
//                    <li class="navbar__item">
//                        <a href="#" class="navbar__link"><i data-feather="help-circle"></i><span>Help</span></a>
//                    </li>
//                    <li class="navbar__item">
//                        <a href="#" class="navbar__link"><i data-feather="settings"></i><span>Settings</span></a>
//                    </li>
//                </ul>
//            </nav>
//            <script src="https://unpkg.com/feather-icons"></script>
//            <script>
//                feather.replace()
//            </script>
//        </body>
//        </html>
//        """;
//
//        // Load the HTML content into the WebView
//        webEngine.loadContent(htmlContent);
//
//        // Create the main layout and add the WebView
//        BorderPane borderPane = new BorderPane();
//        borderPane.setCenter(webView);
//
//        // Set up the scene and stage
//        Scene scene = new Scene(borderPane, 800, 600);
//        primaryStage.setTitle("WebView Example");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}

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

public class GradientBackgroundAnimation extends Application {

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

    public static void main(String[] args) {
        launch(args);
    }
}

//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.web.WebView;
//import javafx.stage.Stage;
//
//public class GradientBackgroundAnimation extends Application {
//
//    @Override
//    public void start(Stage primaryStage) {
//        WebView webView = new WebView();
//        webView.getEngine().load(getClass().getResource("/jelly-catalog.html").toExternalForm());
//
//        Scene scene = new Scene(webView, 300, 600);
//        primaryStage.setTitle("JavaFX WebView Navbar");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}


