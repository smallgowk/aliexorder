package com.phanduy.aliexorder;

import com.phanduy.aliexorder.utils.AliexScraper;
import com.phanduy.aliexorder.utils.ThreadManager;
import com.phanduy.aliexorder.utils.VersionUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class AliexOrderClientApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/phanduy/order/HomePanel.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Aliexpress Orders - version " + VersionUtils.getAppVersionFromResource());
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/image/aliexscrap.png")));
        primaryStage.setScene(new Scene(root, 500, 400));
        primaryStage.setResizable(false);

        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            ThreadManager.getInstance().shutdown();
            AliexScraper.getInstance().quit();
            Platform.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch(AliexOrderClientApp.class, args);
    }

    @Override
    public void stop() throws Exception {
        ThreadManager.getInstance().shutdown();
        AliexScraper.getInstance().quit();
        Platform.exit();
        System.exit(0);
    }
}
