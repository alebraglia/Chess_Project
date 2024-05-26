import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;

public class ChessBoard extends GridPane {

    public ChessBoard() {
        // Board dimensions
        int tileDimension = 75;
        int cols = 8;
        int rows = 8;

        // Creation of the board squares
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Creation of the square
                Rectangle square = new Rectangle(tileDimension, tileDimension);

                // Set background for each square
                if ((row + col) % 2 == 0) {
                    square.setFill(Color.IVORY);
                } else {
                    square.setFill(Color.LIGHTBLUE);
                }

                // Add the square to the board
                this.add(square, col, row);
            }
        }
    }
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

    }
}
