package client.ui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.context.annotation.DeferredImportSelector;
import javafx.animation.*;
import javafx.animation.Timeline;

public class GradientBackgroundAnimation extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 创建一个Group作为场景的根节点
        Group root = new Group();
        // 创建一个场景，并将Group作为其根节点
        Scene scene = new Scene(root, 600, 400);

        // 创建渐变背景
        // 创建一个矩形，用于作为渐变背景
        Rectangle rectangle = new Rectangle(600, 400);
        // 创建一个线性渐变，定义了渐变的开始和结束颜色
        Timeline timeline = new Timeline();
        timeline.setCycleMethod(Timeline.CycleMethod.NO_CYCLE);
        LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, Timeline.CycleMethod.NO_CYCLE, new Stop[]{
                new Stop(0, Color.BLUE),    // 渐变开始的颜色为蓝色
                new Stop(0.5, Color.CYAN),   // 渐变中间的颜色为青色
                new Stop(1, Color.BLUE)     // 渐变结束的颜色为蓝色
        });
        // 将渐变应用到矩形的填充
        rectangle.setFill(gradient);

        // 动画渐变的偏移量
        TranslateTransition transition = new TranslateTransition(Duration.seconds(5), rectangle);
        transition.setByX(1);
        transition.setCycleCount(Animation.INDEFINITE);
        transition.setAutoReverse(true);
        transition.play();

        root.getChildren().add(rectangle);
        primaryStage.setTitle("Gradient Background Animation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

//public class BackgroundAnimation {
//    private Rectangle rectangle;
//    private Scene scene;
//
//    public BackgroundAnimation(Scene scene) {
//        this.scene = scene;
//        createAnimation();
//    }
//
//    private void createAnimation() {
//        rectangle = new Rectangle(600, 400);
//        LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, new Stop[]{
//                new Stop(0, Color.BLUE),
//                new Stop(0.5, Color.CYAN),
//                new Stop(1, Color.BLUE)
//        });
//        rectangle.setFill(gradient);
//
//        TranslateTransition transition = new TranslateTransition(Duration.seconds(5), rectangle);
//        transition.setByX(1);
//        transition.setCycleCount(Animation.INDEFINITE);
//        transition.setAutoReverse(true);
//        transition.play();
//
//        scene.getRoot().getChildren().add(rectangle);
//    }
//
//    public Rectangle getRectangle() {
//        return rectangle;
//    }
//}