package Game;

import Pieces.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ChessBoard extends JPanel {
    //tile dimensions
    public int tileDimension = 75;
    // Board dimensions
    int cols = 8;
    int rows = 8;
    public ArrayList<Piece> pieces = new ArrayList<>();
    public Piece selectedPiece;    //pezzo che si desidera muovere
    Input input = new Input(this);
    public int enPassantTile = -1;         // salva la posizione per l en passant
    public Check checkScanner = new Check(this);
    private boolean isWhiteTurn = true;     //variabile usata per i turni
    boolean godMode;  // godMode
    final private JFrame mainFrame;   // collegamento al frame principale

    //setter di godMode
    public void setGodMode(boolean godMode) {
        this.godMode = godMode;
    }

    public ChessBoard(JFrame mainFrame) {
        this.setPreferredSize(new Dimension(cols * tileDimension, rows * tileDimension));
        this.godMode = false;
        //  inizializzo i mouseListener
        this.addMouseListener(input);
        this.addMouseMotionListener(input);
        this.mainFrame = mainFrame;

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
        if (move.piece.moveIsBlocked(move.nextCol, move.nextRow)) {
            return false;
        }
        if (checkScanner.kingIsChecked(move)) {
            return false;
        }
        return true;
    }

    //effettua le movimentazioni
    public void makeMove(Movement move) {    // classe parallela che permette l' en passant
        if (isWhiteTurn) {
            isWhiteTurn = false;
        } else isWhiteTurn = true;
        if (move.piece.type.equals("Pawn")) {
            movePawn(move);
        } else if (move.piece.type.equals("King")) {
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

            // creo il jframe
            JFrame frame = new JFrame("Game Over");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 150);

            // jlabel che mostrerà il vincitore
            JLabel label = new JLabel(winner, SwingConstants.CENTER);

            // bottone New game
            JButton newGameButton = new JButton("New Game");
            newGameButton.addActionListener(e -> {
                resetBoard();
                frame.dispose(); // Close the current window
            });

            // bottone close
            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(e -> {
                System.exit(0); // Close the application
            });

            // crea il jpanel con i bottoni
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(newGameButton);
            buttonPanel.add(closeButton);

            // aggiunge le componenti al frame
            frame.setLayout(new BorderLayout());
            frame.add(label, BorderLayout.CENTER);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            // Center the popup relative to the main frame
            frame.setLocationRelativeTo(mainFrame);
            // mostra il frame
            frame.setVisible(true);
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
            promotePawn(move);
        }
    }

    // Metodo per promuovere il pawn
    public void promotePawn(Movement move) {
        // Creazione del pannello personalizzato con i bottoni
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));

        JButton queenButton = new JButton("Queen");
        JButton rookButton = new JButton("Rook");
        JButton bishopButton = new JButton("Bishop");
        JButton knightButton = new JButton("Knight");

        panel.add(queenButton);
        panel.add(rookButton);
        panel.add(bishopButton);
        panel.add(knightButton);

        // Creazione del dialogo
        JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
        JDialog dialog = optionPane.createDialog("Promote Pawn");

        // Aggiunta degli action listener ai bottoni
        queenButton.addActionListener(e -> {
            applyPromotion(move, "Queen");
            dialog.dispose();
        });

        rookButton.addActionListener(e -> {
            applyPromotion(move, "Rook");
            dialog.dispose();
        });

        bishopButton.addActionListener(e -> {
            applyPromotion(move, "Bishop");
            dialog.dispose();
        });

        knightButton.addActionListener(e -> {
            applyPromotion(move, "Knight");
            dialog.dispose();
        });

        // Mostra il dialogo
        dialog.setVisible(true);
    }

    // Funzione per applicare la promozione del pedone
    private void applyPromotion(Movement move, String piece) {

        switch (piece) {
            case "Queen":
                pieces.add(new Queen(this, move.nextCol, move.nextRow, move.piece.isWhite));
                break;
            case "Rook":
                pieces.add(new Rook(this, move.nextCol, move.nextRow, move.piece.isWhite));
                break;
            case "Bishop":
                pieces.add(new Bishop(this, move.nextCol, move.nextRow, move.piece.isWhite));
                break;
            case "Knight":
                pieces.add(new Knight(this, move.nextCol, move.nextRow, move.piece.isWhite));
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
        if (!p1.isWhite && !p2.isWhite) {
            return true;
        }
        return false;
    }

    //funzione che mi ritorna il re
    Piece findKing(boolean isWhite) {
        for (Piece piece : pieces) {
            if (isWhite == piece.isWhite && piece.type.equals("King")) {
                return piece;
            }
        }
        System.out.println("cant find King\n");
        return null;    //caso non si trovi il king (non dovrebbe succedere)
    }

    //add the pieces to the array
    public void addPieces() {
        pieces.add(new Rook(this, 0, 0, false));
        pieces.add(new Knight(this, 1, 0, false));
        pieces.add(new Bishop(this, 2, 0, false));
        pieces.add(new Queen(this, 3, 0, false));
        pieces.add(new King(this, 4, 0, false));
        pieces.add(new Bishop(this, 5, 0, false));
        pieces.add(new Knight(this, 6, 0, false));
        pieces.add(new Rook(this, 7, 0, false));

        pieces.add(new Pawn(this, 0, 1, false));
        pieces.add(new Pawn(this, 1, 1, false));
        pieces.add(new Pawn(this, 2, 1, false));
        pieces.add(new Pawn(this, 3, 1, false));
        pieces.add(new Pawn(this, 4, 1, false));
        pieces.add(new Pawn(this, 5, 1, false));
        pieces.add(new Pawn(this, 6, 1, false));
        pieces.add(new Pawn(this, 7, 1, false));

        pieces.add(new Rook(this, 0, 7, true));
        pieces.add(new Knight(this, 1, 7, true));
        pieces.add(new Bishop(this, 2, 7, true));
        pieces.add(new Queen(this, 3, 7, true));
        pieces.add(new King(this, 4, 7, true));
        pieces.add(new Bishop(this, 5, 7, true));
        pieces.add(new Knight(this, 6, 7, true));
        pieces.add(new Rook(this, 7, 7, true));

        pieces.add(new Pawn(this, 0, 6, true));
        pieces.add(new Pawn(this, 1, 6, true));
        pieces.add(new Pawn(this, 2, 6, true));
        pieces.add(new Pawn(this, 3, 6, true));
        pieces.add(new Pawn(this, 4, 6, true));
        pieces.add(new Pawn(this, 5, 6, true));
        pieces.add(new Pawn(this, 6, 6, true));
        pieces.add(new Pawn(this, 7, 6, true));

    }

    // ricarico tutte le pedine della scacchiera
    public void resetBoard() {
        pieces.clear();
        this.addPieces();
        repaint();
    }

    //draw the game
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        //disegna il board
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if ((r + c) % 2 == 0) {
                    g2d.setColor(new Color(0x34E7C3));
                } else g2d.setColor(new Color(0xFFFFFF));

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

        // draw the pieces in game
        for (Piece piece : pieces) {
            piece.insert(g2d);
        }

    }
}

