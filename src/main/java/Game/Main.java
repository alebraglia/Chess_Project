import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chess Game");

        // Crea una nuova istanza della scacchiera
        ChessBoard chessBoard = new ChessBoard();

        // Crea un layout BorderPane
        BorderPane borderPane = new BorderPane();

        // Aggiungi la scacchiera al centro del BorderPane
        borderPane.setCenter(chessBoard);

        // Crea una scena con il BorderPane
        Scene scene = new Scene(borderPane, 600, 600);

        // Imposta la scena sul palco
        primaryStage.setScene(scene);

        // Mostra il palco
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}