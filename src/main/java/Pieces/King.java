package Pieces;

import Game.ChessBoard;
import Game.Main;
import Game.Movement;

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
                this.image = ImageIO.read(Main.class.getResourceAsStream("/white_king.png")).getScaledInstance(board.tileDimension, board.tileDimension, BufferedImage.SCALE_SMOOTH);
            } else
                this.image = ImageIO.read(Main.class.getResourceAsStream("/black_king.png")).getScaledInstance(board.tileDimension, board.tileDimension, BufferedImage.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isValidMove(int col, int row) {
        // il movimento del re consiste in un quadrato intorno a lui, quindi ritorno
        // true se il movimento si trova tra, rispettivamente, gli angoli e i lati del re
        return Math.abs((col - this.col) * (row - this.row)) == 1 || Math.abs(col - this.col) + Math.abs(row - this.row) == 1 || canCastle(col, row);
    }

    private boolean canCastle(int col, int row) {

        if (this.row == row) {
            if (col == 6) {      //cioè la posizione iniziale del re
                Piece rook = board.getPiece(7, row);
                if (rook != null && rook.isFirstMove && this.isFirstMove) {
                    return board.getPiece(5, row) == null &&
                            board.getPiece(5, row) == null &&
                            !board.checkScanner.kingIsChecked(new Movement(board, 5, row, this));

                }
            }
            if (col == 2) {      //cioè la posizione iniziale del re
                Piece rook = board.getPiece(0, row);
                if (rook != null && rook.isFirstMove && this.isFirstMove) {
                    return board.getPiece(3, row) == null &&
                            board.getPiece(2, row) == null &&
                            board.getPiece(1, row) == null &&
                            !board.checkScanner.kingIsChecked(new Movement(board, 3, row, this));

                }
            }
        }
        return false;
    }
}
