import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import java.net.MalformedURLException;

import java.io.File;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws MalformedURLException {

        File mediaFile = new File("C:\\Users\\ronio\\IdeaProjects\\myApp\\src\\main\\java\\_uploads_612f677dbe8614.26483524_fixed.mp4_preview.mp4");
        System.out.println("start: " + mediaFile);
        Media media = new Media(mediaFile.toURI().toURL().toString());
        System.out.println("media: " + media);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        System.out.println("mediaPlayer: " + mediaPlayer);
        MediaView mediaView = new MediaView(mediaPlayer);
        System.out.println("mediaView: " + mediaView);

        Scene scene = new Scene(new Pane(mediaView), 1024, 800);
        primaryStage.setScene(scene);
        primaryStage.show();

        mediaPlayer.play();
    }
}
