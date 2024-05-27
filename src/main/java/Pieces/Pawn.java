package Pieces;

import Game.ChessBoard;
import Game.Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Pawn extends Piece {


    public Pawn(ChessBoard board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.isWhite = isWhite;
        this.xPos = col * board.tileDimension;
        this.yPos = row * board.tileDimension;

        this.type = "Pawn";
        try {
            if (isWhite) {
                this.image = ImageIO.read(Main.class.getResourceAsStream("/white_pawn.png")).getScaledInstance(board.tileDimension, board.tileDimension, BufferedImage.SCALE_SMOOTH);
            } else
                this.image = ImageIO.read(Main.class.getResourceAsStream("/black_pawn.png")).getScaledInstance(board.tileDimension, board.tileDimension, BufferedImage.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isValidMove(int col, int row) {

        int team;           //variabile che identifica a seconda della squadra del pawn la direzione in cui si muove
        if (this.isWhite) {
            team = 1;
        } else team = -1;

        //pawn si muove di 1
        if (col == this.col && row == this.row - team && board.getPiece(col, row) == null)
            return true;

        //pawn si muove di 2
        if (isFirstMove && col == this.col && row == this.row - team * 2 && board.getPiece(col, row) == null && board.getPiece(col, row + team) == null)
            return true;

        //cattura a sinistra
        if (col == this.col - 1 && row == this.row - team && board.getPiece(col, row) != null)
            return true;

        //cattura a destra
        if (col == this.col + 1 && row == this.row - team && board.getPiece(col, row) != null)
            return true;

        // en passant sinistra
        if (board.getTileNum(col, row) == board.enPassantTile && col == this.col - 1 && row == this.row - team && board.getPiece(col, row + team) != null)
            return true;

        // en passant destra
        if (board.getTileNum(col, row) == board.enPassantTile && col == this.col + 1 && row == this.row - team && board.getPiece(col, row + team) != null)
            return true;

            return false;
    }
}
