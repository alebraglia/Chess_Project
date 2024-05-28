package Pieces;

import Game.ChessBoard;
import Game.Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Queen extends Piece {

    public Queen(ChessBoard board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.isWhite = isWhite;
        this.xPos = col * board.tileDimension;
        this.yPos = row * board.tileDimension;

        this.type = "Queen";
        try {
            if (isWhite) {
                this.image = ImageIO.read(Main.class.getResourceAsStream("/white_queen.png")).getScaledInstance(board.tileDimension, board.tileDimension, BufferedImage.SCALE_SMOOTH);
            } else
                this.image = ImageIO.read(Main.class.getResourceAsStream("/black_queen.png")).getScaledInstance(board.tileDimension, board.tileDimension, BufferedImage.SCALE_SMOOTH);
        } catch (IOException e) {
            System.out.println("Errore caricamento immagini\n");
            System.exit(-1);
        }
    }

    // isValidMovement e moveIsBlocked sono l'insieme di rook e bishop
    @Override
    public boolean isValidMove(int col, int row) {
        return ((Math.abs(this.col - col) == Math.abs(this.row - row)) || (this.col == col || this.row == row));
    }

    @Override
    public boolean moveIsBlocked(int col, int row) {
        if (this.col == col || this.row == row) {   //controllo come il rook
            //sinistra
            if (this.col > col) {
                for (int c = this.col - 1; c > col; c--) {  //controllo che tra la posizione finale e quella di partenza non ci sia un blocco
                    if (board.getPiece(c, this.row) != null) {
                        return true;
                    }
                }
            }
            //destra
            if (this.col < col) {
                for (int c = this.col + 1; c < col; c++) {  //controllo che tra la posizione finale e quella di partenza non ci sia un blocco
                    if (board.getPiece(c, this.row) != null) {
                        return true;
                    }
                }
            }
            //alto
            if (this.row > row) {
                for (int r = this.row - 1; r > row; r--) {  //controllo che tra la posizione finale e quella di partenza non ci sia un blocco
                    if (board.getPiece(this.col, r) != null) {
                        return true;
                    }
                }
            }
            //basso
            if (this.row < row) {
                for (int r = this.row + 1; r < row; r++) {  //controllo che tra la posizione finale e quella di partenza non ci sia un blocco
                    if (board.getPiece(this.col, r) != null) {
                        return true;
                    }
                }
            }
        } else {      //controllo come il bishop
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
        }
        return false;
    }
}
