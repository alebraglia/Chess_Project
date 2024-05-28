package Game;

import Pieces.King;
import Pieces.Piece;

//classe che implementa tutte le funzioni relative al check

public class Check {
    ChessBoard board;

    public Check(ChessBoard board) {
        this.board = board;
    }

    public boolean kingIsChecked(Movement move) {

        Piece king = board.findKing(move.piece.isWhite);
        if (king == null) {
            System.out.println("ERROR 404: King not found\n");
            System.exit(1);
        }

        int kingCol = king.col;
        int kingRow = king.row;

        if (board.selectedPiece != null && board.selectedPiece.type.equals("King")) {
            kingCol = move.nextCol;
            kingRow = move.nextRow;
        }

        return false;
    }

    // verifica se una determinata posizione (data da col e row) si trova in una linea di
    // attacco diretta di una torre (Rook) o di una regina (Queen) nemica lungo la direzione di una delle linee
    // ortogonali rispetto alla posizione di un re (king), a partire dalla posizione del re (kingCol e kingRow).
    // La funzione viene chiamata con parametri aggiuntivi (colVal e rowVal) che determinano la direzione del
    // controllo (orizzontale, verticale o diagonale)
    private boolean inRookPath(int col, int row, Piece king, int kingCol, int kingRow, int colVal, int rowVal) {
        //Il ciclo itera per un massimo di 7 volte (poiché una torre o regina può attaccare a distanza massima di 7 caselle in una direzione su una scacchiera 8x8)
        for (int i = 1; i < 8; i++) {

            // Se la casella corrente (calcolata aggiungendo i * colVal a kingCol e i * rowVal a kingRow)
            // corrisponde alla posizione del re, il ciclo si interrompe perché non è necessario continuare a
            // controllare oltre il re.

            if (kingCol + (i * colVal) == col && kingRow + (i * rowVal) == row) {
                break;
            }
            //Recupero del pezzo nella posizione corrente
            Piece piece = board.getPiece(kingCol + (i * colVal), kingRow + (i * rowVal));

            //controllo del pezzo trovato
            if (piece != null && piece != board.selectedPiece) {
                //caso trovo una torre o una regina ritorno true
                if (!board.sameTeam(piece, king) && piece.type.equals("Rook") || piece.type.equals("Queen")) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    // verifica se una determinata posizione (data da col e row) si trova in una linea di
    // attacco diretta di una torre (Rook) o di una regina (Queen) nemica lungo la direzione di una delle linee
    // diagonali rispetto alla posizione di un re (king), a partire dalla posizione del re (kingCol e kingRow).
    // La funzione viene chiamata con parametri aggiuntivi (colVal e rowVal) che determinano la direzione del
    // controllo (orizzontale, verticale o diagonale)
    private boolean inBishopPath(int col, int row, Piece king, int kingCol, int kingRow, int colVal, int rowVal) {
        for (int i = 1; i < 8; i++) {
            if (kingCol - (i * colVal) == col && kingRow - (i * rowVal) == row) {   // non cerco oltre il re
                break;
            }
            Piece piece = board.getPiece(kingCol - (i * colVal), kingRow - (i * rowVal));
            if (piece != null && piece != board.selectedPiece) {
                //caso trovo una torre o una regina ritorno true
                if (!board.sameTeam(piece, king) && piece.type.equals("Bishop") || piece.type.equals("Queen")) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    //controlla se una casella specifica (definita da col e row) è minacciata da un cavallo (Knight) nemico che
    // potrebbe muoversi alla posizione del re (kingCol, kingRow) in uno dei suoi possibili movimenti a L
    private boolean inKinghtPath(int col, int row, Piece king, int kingCol, int kingRow) {
        return  chekKnight(board.getPiece(kingCol - 1, kingRow - 2), king, col, row) ||
                chekKnight(board.getPiece(kingCol + 1, kingRow - 2), king, col, row) ||
                chekKnight(board.getPiece(kingCol + 2, kingRow - 1), king, col, row) ||
                chekKnight(board.getPiece(kingCol + 2, kingRow + 1), king, col, row) ||
                chekKnight(board.getPiece(kingCol + 1, kingRow + 2), king, col, row) ||
                chekKnight(board.getPiece(kingCol - 1, kingRow + 2), king, col, row) ||
                chekKnight(board.getPiece(kingCol - 2, kingRow + 1), king, col, row) ||
                chekKnight(board.getPiece(kingCol - 2, kingRow - 1), king, col, row);
    }

    //controlla se c'è un cavollo nemico in posizione col, row
    private boolean chekKnight(Piece p, Piece king, int col, int row) {
        return p != null && board.sameTeam(p, king) && p.type.equals("Knight") && !(p.col == col && p.row == row);
        //!(p.col == col && p.row == row) garantisce che la funzione chekKnight consideri solo i pezzi cavallo che
        // minacciano la casella in esame, escludendo il caso in cui il cavallo stesso occupa la casella che
        // stiamo controllando
    }
}
