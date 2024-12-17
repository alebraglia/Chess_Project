package Pieces;

import Game.ChessBoard;
import Game.Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Knight extends Piece {

    public Knight(ChessBoard board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.isWhite = isWhite;
        this.xPos = col * board.tileDimension;
        this.yPos = row * board.tileDimension;

        this.type = PieceType.Knight;
        try {
            if (isWhite) {
                this.image = ImageIO.read(Main.class.getResourceAsStream("/white_knight.png")).getScaledInstance(board.tileDimension, board.tileDimension, BufferedImage.SCALE_SMOOTH);
            } else
                this.image = ImageIO.read(Main.class.getResourceAsStream("/black_knight.png")).getScaledInstance(board.tileDimension, board.tileDimension, BufferedImage.SCALE_SMOOTH);
        } catch (IOException e) {
            System.out.println("Errore caricamento immagini\n");
            System.exit(-1);
        }
    }

    //movimentazione del cavallo
    @Override
    public boolean isValidMove(int col, int row) {
        return Math.abs(col - this.col) * Math.abs(row - this.row) == 2;
    }
}
