package Pieces;

import Game.ChessBoard;
import Game.Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Rook extends Piece {

    public Rook(ChessBoard board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.isWhite = isWhite;
        this.xPos = col * board.tileDimension;
        this.yPos = row * board.tileDimension;

        this.type = "Rook";
        try {
            if (isWhite) {
                this.image = ImageIO.read(Main.class.getResourceAsStream("/white_rook.png")).getScaledInstance(board.tileDimension,board.tileDimension, BufferedImage.SCALE_SMOOTH);
            }
            else this.image = ImageIO.read(Main.class.getResourceAsStream("/black_rook.png")).getScaledInstance(board.tileDimension,board.tileDimension, BufferedImage.SCALE_SMOOTH);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    //movimento del rook
    @Override
    public boolean isValidMove(int col, int row) {
        return this.col == col || this.row == row;
    }

    //verifico ogni direzione in cerca di un pezzo che ostacola il movimento
    @Override
    public boolean moveIsBlocked(int col, int row) {

        //sinistra
        if (this.col > col){
            for (int c = this.col - 1; c > col ; c--){  //controllo che tra la posizione finale e quella di partenza non ci sia un blocco
                if (board.getPiece(c,this.row) != null){
                    return true;
                }
            }
        }
        //destra
        if (this.col < col){
            for (int c = this.col + 1; c < col ; c++){  //controllo che tra la posizione finale e quella di partenza non ci sia un blocco
                if (board.getPiece(c,this.row) != null){
                    return true;
                }
            }
        }
        //alto
        if (this.row > row){
            for (int r = this.row - 1; r > row ; r--){  //controllo che tra la posizione finale e quella di partenza non ci sia un blocco
                if (board.getPiece(this.col,r) != null){
                    return true;
                }
            }
        }
        //basso
        if (this.row < row){
            for (int r = this.row + 1; r < row ; r++){  //controllo che tra la posizione finale e quella di partenza non ci sia un blocco
                if (board.getPiece(this.col,r) != null){
                    return true;
                }
            }
        }
        return false;
    }
}
