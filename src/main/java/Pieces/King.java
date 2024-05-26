package Pieces;

import Game.ChessBoard;
import Game.Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class King extends Piece {

    public King(ChessBoard board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.isWhite = isWhite;
        this.xPos = col * board.tileDimension;
        this.yPos = row * board.tileDimension;

        this.type = "King";
        try {
            if (isWhite) {
                this.image = ImageIO.read(Main.class.getResourceAsStream("/white_king.png")).getScaledInstance(board.tileDimension,board.tileDimension, BufferedImage.SCALE_SMOOTH);
            }
            else this.image = ImageIO.read(Main.class.getResourceAsStream("/black_king.png")).getScaledInstance(board.tileDimension,board.tileDimension, BufferedImage.SCALE_SMOOTH);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
