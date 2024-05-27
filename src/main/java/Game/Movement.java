package Game;

import Pieces.Piece;

public class Movement {
    int prevCol;
    int prevRow;
    int nextCol;
    int nextRow;

    Piece piece;
    public Piece Captured;

    public Movement(ChessBoard Board, int nextCol, int nextRow, Piece piece) {
        this.prevCol = piece.col;
        this.prevRow = piece.row;
        this.nextCol = nextCol;
        this.nextRow = nextRow;

        this.piece = piece;
        this.Captured = Board.getPiece(nextCol, nextRow);   // possibile pezzo presente nella posizione di arrivo
    }
}
