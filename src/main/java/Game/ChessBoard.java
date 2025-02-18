package Game;

import Pieces.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ChessBoard extends JPanel {
    private static ChessBoard instance;

    // Metodo statico per ottenere l'istanza
    public static synchronized ChessBoard getInstance(JFrame mainFrame, Color whiteTileColor, Color blackTileColor) {
        if (instance == null) {
            instance = new ChessBoard(mainFrame, whiteTileColor, blackTileColor);
        }
        return instance;
    }

    //tile dimensions
    public int tileDimension = 75;
    // Board dimensions
    int cols = 8;
    int rows = 8;
    // pezzi della scacchiera
    public ArrayList<Piece> pieces = new ArrayList<>();
    public Piece selectedPiece;    //pezzo che si desidera muovere
    Controller controller = new Controller(this);   //controller
    public int enPassantTile = -1;         // salva la posizione per l en passant
    public Check checkScanner = new Check(this);    //oggetto che tiene traccia dei check
    private boolean isWhiteTurn = true;     //variabile usata per i turni
    boolean godMode;  // godMode
    final private JFrame mainFrame;   // collegamento al frame principale
    Color whiteTileColor;
    Color blackTileColor;

    //setter di godMode
    public void setGodMode(boolean godMode) {
        this.godMode = godMode;
    }

    private ChessBoard(JFrame mainFrame, Color whiteTileColor, Color blackTileColor) {
        this.setPreferredSize(new Dimension(cols * tileDimension, rows * tileDimension));
        this.godMode = false;
        //  inizializzo i mouseListener
        this.addMouseListener(controller);
        this.addMouseMotionListener(controller);
        this.mainFrame = mainFrame;
        this.blackTileColor = blackTileColor;
        this.whiteTileColor = whiteTileColor;

        addPieces();    // aggiunge i pezzi
    }


    //ritorna la posizione della casella attuale
    public int getTileNum(int col, int row) {
        return row * rows + col;
    }

    //ricerca del pezzo nella posizione Col = x e Row = y
    public Piece getPiece(int c, int r) {
        for (Piece piece : pieces) {
            if (piece.col == c && piece.row == r) {
                return piece;
            }
        }
        return null;
    }

    //verifica se una mossa è valida
    public boolean isValidMove(Movement move) {
        if (godMode) {
            return true;
        }
        if (move.piece.isWhite && !isWhiteTurn) {
            return false;
        }
        if (!move.piece.isWhite && isWhiteTurn) {
            return false;
        }
        if (sameTeam(move.piece, move.Captured)) {    //se si cerca di catturare (spostarsi) sul proprio pezzo la mossa non è valida
            return false;
        }
        if (!move.piece.isValidMove(move.nextCol, move.nextRow)) {   //il movimento deve rispettare il tipo del pezzo
            return false;
        }
        if (move.piece.moveIsBlocked(move.nextCol, move.nextRow)) {  // se il movimento è ostacolato da un pezzo
            return false;
        }
        return !checkScanner.kingIsChecked(move);
    }

    //effettua le movimentazioni
    public void makeMove(Movement move) {
        isWhiteTurn = !isWhiteTurn;
        if (move.piece.type.equals(PieceType.Pawn)) {
            movePawn(move);           // classe parallela che permette l' en passant
        } else if (move.piece.type.equals(PieceType.King)) {
            moveKing(move);
        }
        //aggiorno la posizione nella scacchiera
        move.piece.col = move.nextCol;
        move.piece.row = move.nextRow;
        //aggiorno la posizione dello schermo
        move.piece.xPos = move.nextCol * tileDimension;
        move.piece.yPos = move.nextRow * tileDimension;

        move.piece.isFirstMove = false;
        capture(move.Captured);

        VerifyMate();
    }

    // verifica un possibile scacco matto
    private void VerifyMate() {
        Piece king = this.findKing(isWhiteTurn);
        if (checkScanner.CheckMate(king)) {
            String winner = isWhiteTurn ? "Black player wins" : "White player wins";
            Dialog.endGameDialog(winner, this.mainFrame,this);
        }
    }


    //metodo per il castling
    //controllo canCastle in king.isValidMove
    private void moveKing(Movement move) {
        if (Math.abs(move.piece.col - move.nextCol) == 2) {
            Piece rook;
            // parte inferiore della scacchiera
            if (move.piece.col < move.nextCol) {
                rook = getPiece(7, move.piece.row);
                rook.col = 5;
            }
            // parte superiore della scacchiera
            else {
                rook = getPiece(0, move.piece.row);
                rook.col = 3;
            }
            rook.xPos = rook.col * tileDimension;   //sposto la torre
        }
    }

    //classe per la movimentazione del pawn che permette l'en passant e la promozione del pezzo
    private void movePawn(Movement move) {

        //en passant
        int team;           //cambio la variabile team a seconda della squadra del pezzo
        if (move.piece.isWhite) {
            team = 1;
        } else team = -1;

        //caso il move sia un en passant catturo il pezzo che subisce
        if (getTileNum(move.nextCol, move.nextRow) == enPassantTile) {
            move.Captured = getPiece(move.nextCol, move.nextRow + team);
        }
        // imposto la casella dell' en passant
        if (Math.abs(move.piece.row - move.nextRow) == 2) {
            enPassantTile = getTileNum(move.nextCol, move.nextRow + team);
        } else enPassantTile = -1;

        //promotion
        if (move.piece.isWhite) {
            team = 0;
        } else team = 7;

        if (move.nextRow == team) {
            Dialog.pawnPromotionDialog(move, this);
        }
    }

    // Funzione per applicare la promozione del pedone
    public void applyPromotion(Movement move, String piece) {

        switch (piece) {
            case "Queen":
                pieces.add(PieceFactory.createPiece(PieceType.Queen,this, move.nextCol, move.nextRow, move.piece.isWhite));
                break;
            case "Rook":
                pieces.add(PieceFactory.createPiece(PieceType.Rook,this, move.nextCol, move.nextRow, move.piece.isWhite));
                break;
            case "Bishop":
                pieces.add(PieceFactory.createPiece(PieceType.Bishop,this, move.nextCol, move.nextRow, move.piece.isWhite));
                break;
            case "Knight":
                pieces.add(PieceFactory.createPiece(PieceType.Knight,this, move.nextCol, move.nextRow, move.piece.isWhite));
                break;
        }

        // Cattura il pedone che si sta promuovendo
        capture(move.piece);
    }

    // cattura di un pezzo
    public void capture(Piece piece) {
        pieces.remove(piece);
    }

    //verifica se 2 pezzi appartengono alla stessa squadra
    public boolean sameTeam(Piece p1, Piece p2) {
        if (p1 == null || p2 == null) {
            return false;
        }
        if (p1.isWhite && p2.isWhite) {
            return true;
        }
        return !p1.isWhite && !p2.isWhite;
    }

    //funzione che mi ritorna il re
    Piece findKing(boolean isWhite) {
        for (Piece piece : pieces) {
            if (isWhite == piece.isWhite && piece.type.equals(PieceType.King)) {
                return piece;
            }
        }
        System.out.println("cant find King\n");
        return null;    //caso non si trovi il king (non dovrebbe succedere)
    }

    //add the pieces to the array
    public void addPieces() {
        // Pezzi neri
        pieces.add(PieceFactory.createPiece(PieceType.Rook, this, 0, 0, false));
        pieces.add(PieceFactory.createPiece(PieceType.Knight, this, 1, 0, false));
        pieces.add(PieceFactory.createPiece(PieceType.Bishop, this, 2, 0, false));
        pieces.add(PieceFactory.createPiece(PieceType.Queen, this, 3, 0, false));
        pieces.add(PieceFactory.createPiece(PieceType.King, this, 4, 0, false));
        pieces.add(PieceFactory.createPiece(PieceType.Bishop, this, 5, 0, false));
        pieces.add(PieceFactory.createPiece(PieceType.Knight, this, 6, 0, false));
        pieces.add(PieceFactory.createPiece(PieceType.Rook, this, 7, 0, false));

        for (int i = 0; i < 8; i++) {
            pieces.add(PieceFactory.createPiece(PieceType.Pawn, this, i, 1, false));
        }

        // Pezzi bianchi
        pieces.add(PieceFactory.createPiece(PieceType.Rook, this, 0, 7, true));
        pieces.add(PieceFactory.createPiece(PieceType.Knight, this, 1, 7, true));
        pieces.add(PieceFactory.createPiece(PieceType.Bishop, this, 2, 7, true));
        pieces.add(PieceFactory.createPiece(PieceType.Queen, this, 3, 7, true));
        pieces.add(PieceFactory.createPiece(PieceType.King, this, 4, 7, true));
        pieces.add(PieceFactory.createPiece(PieceType.Bishop, this, 5, 7, true));
        pieces.add(PieceFactory.createPiece(PieceType.Knight, this, 6, 7, true));
        pieces.add(PieceFactory.createPiece(PieceType.Rook, this, 7, 7, true));

        for (int i = 0; i < 8; i++) {
            pieces.add(PieceFactory.createPiece(PieceType.Pawn, this, i, 6, true));
        }
    }



    // ricarico tutte le pedine della scacchiera
    public void resetBoard() {
        pieces.clear();
        this.addPieces();
        repaint();
    }

    //disegna il gioco
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        //disegna il board
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if ((r + c) % 2 == 0) {
                    g2d.setColor(whiteTileColor);
                } else g2d.setColor(blackTileColor);

                g2d.fillRect(c * tileDimension, r * tileDimension, tileDimension, tileDimension);
            }
        }

        //se ho un pezzo è selezionato evidenzio le possibili mosse
        if (selectedPiece != null) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (isValidMove(new Movement(this, c, r, selectedPiece))) {
                        g2d.setColor(new Color(68, 180, 57, 190));
                        g2d.fillRect(c * tileDimension, r * tileDimension, tileDimension, tileDimension);
                    }

                }
            }
        }

        // draw the pieces in game-
        for (Piece piece : pieces) {
            piece.insert(g2d);
        }

    }
}

