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
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.WindowEvent;

import java.io.*;
import java.net.MalformedURLException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main extends Application {
    //private String barcode =  "";
    private Stage primaryStage;
    private Scene mainScene;
    private Set<String> barcodeSet = new HashSet<String>();
    private int primaryWidth = 960; //-1;
    private int primaryHeight = 540;// -1;
    private setTimeoutWithStop lastExitTask = null;

    public static void main(String[] args) {
        launch(args);
    }

    private void switchScene(Scene scene) {
        this.primaryStage.setScene(scene);
        if (this.primaryStage.isFullScreen() == false){
            primaryStage.setFullScreenExitHint("");
            // TODO: uncomment this line to fullscreen:
            // this.primaryStage.setFullScreen(true);
        }
    }
    private int getPrimaryWidth() {
        if(primaryWidth == -1) {
            primaryWidth = (int) Screen.getPrimary().getBounds().getWidth();
        }
        return primaryWidth;
    }
    private int getPrimaryHeight() {
        if(primaryHeight == -1) {
            primaryHeight = (int) Screen.getPrimary().getBounds().getHeight();
        }
        return primaryHeight;
    }
    private Scene getBasicScene(Pane root) {
        int width = this.getPrimaryWidth();
        int height =this.getPrimaryHeight();
        System.out.println("getBasic, got width x height: " + width + " X " +  height);
        Scene scene = new Scene(root,width,height);
        System.out.println("getBasic, done create new scene");
        //Scene scene = new Scene(root, 960, 540);
        scene.setFill(Color.BLACK);

        System.out.println("getBasic, set FullScreen");
        return scene;
    }


    /*public static void setTimeout(Runnable runnable, int delay){
        Thread t2 = new Thread() {
            private int t = 0;
            @Override
            void run
        }
        Thread t = new Thread(
                new Runnable() {

            public void mStop() {
                exit = true;
            }
            @Override
            public void run() {

                try {
                    Thread.sleep(delay);
                    if(exit == false) {
                        runnable.run();
                    }
                }
                catch (Exception e){
                    System.err.println(e);
                }
            }
        });
        mStop();
        t.start();
    }*/

    public void switchToImageScene(String barcode) {
        // TODO: load image based on barcode string

        Image image = null;
        try {
            image = new Image(new FileInputStream("C:\\Users\\ronio\\Desktop\\projects\\barcodeFx\\src\\resources\\barcodes\\" + barcode + ".png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("before imageStream: " + image);
        StackPane root2 = new StackPane();
        System.out.println("after imageStream: " + image);
        ImageView imageView = new ImageView(image);
        System.out.println("after imageview");
        imageView.setX(0);
        imageView.setY(0);
        imageView.setFitWidth(getPrimaryWidth());
        imageView.setFitHeight(getPrimaryHeight());
        System.out.println("before imageview added");
        System.out.println("imageview added");
        root2.getChildren().add(imageView);
        System.out.println("done add");

        //Scene scene2 = new Scene(root2, 300, 275, Color.BLACK);
        Scene scene2 = getBasicScene(root2);// new Scene(root2, 300, 275, Color.BLACK);
        System.out.println("done scene2");
        Platform.runLater(()-> {
                    switchScene(scene2);
                    System.out.println("set scene2");
        });
        setTimeoutWithStop t = new setTimeoutWithStop( 20000, () -> {
            this.returnToMainScene();
        });
        if(this.lastExitTask == null) {
            this.lastExitTask = t;
        }else{
            System.out.println("lastExitTask is: " + lastExitTask);
            this.lastExitTask.stop();
            this.lastExitTask = t;
            System.out.println("lastExitTask renew to: " + lastExitTask);
        }
        //t.stop();
        //primaryStage.show();
        //System.out.println("show scene2");
    }
    public void returnToMainScene() {
        Platform.runLater(()-> {
            switchScene(mainScene);
        });
        System.out.println("done returnToMainScene");
    }

    private void handleBarcodeSetThread() {
        while(true) {
            if(!barcodeSet.isEmpty()) {
                String barcode = barcodeSet.iterator().next();
                System.out.println("barcodeSet piked up new barcode: " + barcode);
                this.switchToImageScene(barcode);
                barcodeSet.remove(barcode);

            }
            try {
                Thread.sleep(750);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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


        mainScene = getBasicScene(root);
        primaryStage.setTitle("M.S. Global");

        primaryStage.setScene(mainScene);
        //
        primaryStage.show();

        mp.play();
        Thread barcodeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                handleBarcodeSetThread();
            }
        });
        barcodeThread.start();
        FileWatcher fw = new FileWatcher(barcode_file) {
            @Override
            public void doOnChange() {
                BufferedReader Buff = null;
                String text = "error";
                try {
                    Buff = new BufferedReader(new FileReader(barcode_file));
                    text = Buff.readLine();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("text " + text);
                if(text != null) {
                    barcodeSet.add(text);
                }

                //switchToImageScene("C:\\Users\\ronio\\Desktop\\projects\\barcodeFx\\src\\resources\\barcodes\\676525004375.png");

            }
        };
        fw.start();

        // Handle Exit of window
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });



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
