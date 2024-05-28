package Game;
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

        return          //controllo rook e queen
                inRookPath(move.nextCol, move.nextRow, king, kingCol, kingRow, 0, 1) ||     //alto
                        inRookPath(move.nextCol, move.nextRow, king, kingCol, kingRow, 1, 0) ||     //destra
                        inRookPath(move.nextCol, move.nextRow, king, kingCol, kingRow, 0, -1) ||    //basso
                        inRookPath(move.nextCol, move.nextRow, king, kingCol, kingRow, -1, 0) ||    //sinistra
                        //controllo Bishop e queen
                        inBishopPath(move.nextCol, move.nextRow, king, kingCol, kingRow, -1, -1) || //alto-sinistra
                        inBishopPath(move.nextCol, move.nextRow, king, kingCol, kingRow, 1, -1) ||  //alto-destra
                        inBishopPath(move.nextCol, move.nextRow, king, kingCol, kingRow, 1, 1) ||   //basso-destra
                        inBishopPath(move.nextCol, move.nextRow, king, kingCol, kingRow, -1, 1) ||   //basso-sinistra
                        //controllo knight
                        inKnightPath(move.nextCol, move.nextRow, king, kingCol, kingRow) ||
                        //controllo Pawn
                        inPawnPath(move.nextCol, move.nextRow, king, kingCol, kingRow) ||
                        //controllo re
                        inKingPath(king, kingCol, kingRow);
    }

    // Verifica se una determinata posizione (data da col e row) si trova in una linea di
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
            //Recupero del pezzo nella prossima posizione
            Piece piece = board.getPiece(kingCol + (i * colVal), kingRow + (i * rowVal));

            //controllo del pezzo trovato
            if (piece != null && piece != board.selectedPiece) {
                //caso trovo una torre o una regina ritorno true
                if (!board.sameTeam(piece, king) && (piece.type.equals("Rook") || piece.type.equals("Queen"))) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    // Verifica se una determinata posizione (data da col e row) si trova in una linea di
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
                if (!board.sameTeam(piece, king) && (piece.type.equals("Bishop") || piece.type.equals("Queen"))) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    //controlla se una casella specifica (definita da col e row) è minacciata da un cavallo (Knight) nemico che
    // potrebbe muoversi alla posizione del re (kingCol, kingRow) in uno dei suoi possibili movimenti a L
    private boolean inKnightPath(int col, int row, Piece king, int kingCol, int kingRow) {
        return checkKnight(board.getPiece(kingCol - 1, kingRow - 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 1, kingRow - 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 2, kingRow - 1), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 2, kingRow + 1), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 1, kingRow + 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol - 1, kingRow + 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol - 2, kingRow + 1), king, col, row) ||
                checkKnight(board.getPiece(kingCol - 2, kingRow - 1), king, col, row);
    }

    //controlla se c'è un cavallo nemico in posizione col, row
    private boolean checkKnight(Piece p, Piece king, int col, int row) {
        return p != null && !board.sameTeam(p, king) && p.type.equals("Knight") && !(p.col == col && p.row == row);
        //!(p.col == col && p.row == row) garantisce che la funzione checkKnight consideri solo i pezzi cavallo che
        // minacciano la casella in esame, escludendo il caso in cui il cavallo stesso occupa la casella che
        // stiamo controllando
    }


    //controllo tutta la zona intorno al pezzo in cerca di un re
    private boolean inKingPath(Piece king, int kingCol, int kingRow) {
        return checkKing(board.getPiece(kingCol - 1, kingRow - 1), king) ||
                checkKing(board.getPiece(kingCol + 1, kingRow - 1), king) ||
                checkKing(board.getPiece(kingCol, kingRow - 1), king) ||
                checkKing(board.getPiece(kingCol - 1, kingRow), king) ||
                checkKing(board.getPiece(kingCol + 1, kingRow), king) ||
                checkKing(board.getPiece(kingCol - 1, kingRow + 1), king) ||
                checkKing(board.getPiece(kingCol + 1, kingRow + 1), king) ||
                checkKing(board.getPiece(kingCol, kingRow + 1), king);
    }

    //verifica la presenza del re
    private boolean checkKing(Piece p, Piece k) {
        return p != null && !board.sameTeam(p, k) && p.type.equals("King");
    }

    private boolean inPawnPath(int col, int row, Piece king, int kingCol, int kingRow) {
        int team;
        if (king.isWhite) {
            team = -1;
        } else team = 1;

        return checkPawn(board.getPiece(kingCol + 1, kingRow + team), king, col, row) ||
                checkPawn(board.getPiece(kingCol - 1, kingRow + team), king, col, row);

    }

    private boolean checkPawn(Piece p, Piece k, int col, int row) {
        return p != null && !board.sameTeam(p, k) && p.type.equals("Pawn") && !(p.col == col && p.row == row);
    }

    //funzione che verifica uno scacco matto cercando se esiste ancora alcuna mossa valida
    public boolean CheckMate(Piece king) {
        for (Piece piece : board.pieces) {
            if (board.sameTeam(piece, king)) {
                if (piece == king) {
                    board.selectedPiece = king;
                } else board.selectedPiece = null;

                for (int r = 0; r < board.rows; r++) {
                    for (int c = 0; c < board.cols; c++) {
                        Movement move = new Movement(board, c, r, piece);
                        if (board.isValidMove(move)) {
                            return false;          //ho trovato una mossa valida
                        }

                    }
                }
            }
        }
        return true;    // il gioco è finito
    }
}
