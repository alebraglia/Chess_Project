package Pieces;


import Game.ChessBoard;

public class PieceFactory {
        public static Piece createPiece(PieceType type, ChessBoard board, int x, int y, boolean isWhite) {
            return switch (type) {
                case Rook -> new Rook(board, x, y, isWhite);
                case Knight -> new Knight(board, x, y, isWhite);
                case Bishop -> new Bishop(board, x, y, isWhite);
                case Queen -> new Queen(board, x, y, isWhite);
                case King -> new King(board, x, y, isWhite);
                case Pawn -> new Pawn(board, x, y, isWhite);
            };
        }
    }


