import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Set;

public class Main extends Application {
    private String barcode =  "";
    private Stage primaryStage;
    public static void main(String[] args) {
        launch(args);
    }

    public void switchToImageScene(String barcode_file_path) {
        /*System.out.println("I changed");
        var p = Paths.get(barcode_file_path);
        System.out.println("barcode file path: " + p);
        String s = null;
        try {
            s = Files.readAllLines(p).get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("barcode file info: " + s);*/
        Image image = null;
        try {
            image = new Image(new FileInputStream("C:\\Users\\ronio\\Desktop\\projects\\barcodeFx\\src\\resources\\barcodes\\676525004375.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("before imageStream: " + image);
        StackPane root2 = new StackPane();
        System.out.println("after imageStream: " + image);
        ImageView imageView = new ImageView(image);
        System.out.println("after imageview");
        imageView.setX(400);
        imageView.setY(400);
        imageView.setFitWidth(500);
        imageView.setFitHeight(500);
        System.out.println("before imageview added");
        System.out.println("imageview added");
        root2.getChildren().add(imageView);
        System.out.println("done add");

        Scene scene2 = new Scene(root2, 300, 275, Color.BLACK);
        System.out.println("done scene2");
        this.primaryStage.setScene(scene2);
        System.out.println("set scene2");
        //primaryStage.show();
        //System.out.println("show scene2");
    }
    @Override
    public void start(Stage primaryStage) throws MalformedURLException {
        this.primaryStage = primaryStage;
        var BASE_DIR  = System.getProperty("user.dir");
        var vid_path = BASE_DIR + "\\src\\file_example_MP4_1280_10MG.mp4";
        System.out.println("vid_path:\t" + vid_path);
        File mediaFile = new File(vid_path);
        final String barcode_file_path =BASE_DIR + "\\src\\main\\file.txt";
        File barcode_file = new File(barcode_file_path);
        final Media m = new Media(mediaFile.toURI().toString());
        final MediaPlayer mp = new MediaPlayer(m);
        mp.setCycleCount(MediaPlayer.INDEFINITE);
        final MediaView mv = new MediaView(mp);

        final DoubleProperty width = mv.fitWidthProperty();
        final DoubleProperty height = mv.fitHeightProperty();

        width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));

        //mv.setPreserveRatio(true);
        StackPane root = new StackPane();





        root.getChildren().add(mv);


        final Scene scene = new Scene(root, 960, 540);
        scene.setFill(Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Full Screen Video Player");
        //primaryStage.setFullScreen(true);
        primaryStage.show();

        mp.play();
        FileWatcher fw = new FileWatcher(barcode_file) {
            @Override
            public void doOnChange() {
                Platform.runLater(()-> {
                    switchToImageScene("C:\\Users\\ronio\\Desktop\\projects\\barcodeFx\\src\\resources\\barcodes\\676525004375.png");
                });
            }
        };
        fw.start();



        /*
        System.out.println("start:\t\t" + mediaFile);
        Media media = new Media(mediaFile.toURI().toURL().toString());
        System.out.println("media: " + media);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        System.out.println("mediaPlayer: " + mediaPlayer);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.
        System.out.println("mediaView: " + mediaView);

        Scene scene = new Scene(new Pane(mediaView), 1024, 800);
        primaryStage.setScene(scene);
        primaryStage.show();

        mediaPlayer.play();

         */
    }
}
