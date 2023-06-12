package aoc.simulandoprocessador;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainApp extends Application {

    public Stage stage = new Stage();

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader janela = new FXMLLoader(MainApp.class.getResource("scene-aoc.fxml"));
        Scene scene = new Scene(janela.load(), 600, 400);
        this.stage.setTitle("Processador Intel Pentium 4");
        this.stage.setScene(scene);
        this.stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}