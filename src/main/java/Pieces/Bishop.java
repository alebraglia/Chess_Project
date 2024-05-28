package Pieces;

import Game.ChessBoard;
import Game.Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Bishop extends Piece {

    public Bishop(ChessBoard board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.isWhite = isWhite;
        this.xPos = col * board.tileDimension;
        this.yPos = row * board.tileDimension;

        this.type = "Bishop";
        try {
            if (isWhite) {
                this.image = ImageIO.read(Main.class.getResourceAsStream("/white_bishop.png")).getScaledInstance(board.tileDimension,board.tileDimension, BufferedImage.SCALE_SMOOTH);
            }
            else this.image = ImageIO.read(Main.class.getResourceAsStream("/black_bishop.png")).getScaledInstance(board.tileDimension,board.tileDimension, BufferedImage.SCALE_SMOOTH);
        }
        catch (IOException e){
            System.out.println("Errore caricamento immagini\n");
            System.exit(-1);
        }
    }

    @Override
    public boolean isValidMove(int col, int row) {
        return Math.abs(this.col - col) == Math.abs(this.row - row);
    }

    //verifico ogni direzione in cerca di un pezzo che ostacola il movimento
    @Override
    public boolean moveIsBlocked(int col, int row) {
        //in alto a sinistra
        if (this.col > col && this.row > row) {
            for (int i = 1; i < Math.abs(this.col - col); i++) {    // cerco usando il numero di colonne rimanenti a sinistra di quella attuale
                if (board.getPiece(this.col - i, this.row - i) != null) {
                    return true;
                }
            }
        }
        //in alto a destra
        if (this.col < col && this.row > row) {
            for (int i = 1; i < Math.abs(this.col - col); i++) {    // cerco usando il numero di colonne rimanenti a destra di quella attuale
                if (board.getPiece(this.col + i, this.row - i) != null) {
                    return true;
                }
            }
        }
        //in basso a sinistra
        if (this.col > col && this.row < row) {
            for (int i = 1; i < Math.abs(this.row - row); i++) {    // cerco usando il numero di righe rimanenti in basso rispetto a quella attuale
                if (board.getPiece(this.col - i, this.row + i) != null) {
                    return true;
                }
            }
        }
        //in basso a destra
        if (this.col < col && this.row < row) {
            for (int i = 1; i < Math.abs(this.row - row); i++) {    // cerco usando il numero di righe rimanenti in basso rispetto a quella attuale
                if (board.getPiece(this.col + i, this.row + i) != null) {
                    return true;
                }
            }
        }
    return false;
    }
}
