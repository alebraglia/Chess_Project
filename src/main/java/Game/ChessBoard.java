package Game;

import Pieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ChessBoard extends JPanel {
    //tile dimensions
    public int tileDimension = 75;
    // Board dimensions
    int cols = 8;
    int rows = 8;
    ArrayList<Piece> pieces = new ArrayList<>();
    public Piece selectedPiece;    //pezzo che si desidera muovere
    Input input = new Input(this);

    public ChessBoard() {
        this.setPreferredSize(new Dimension(cols * tileDimension, rows * tileDimension));
        //  inizializzo i mouseListener
        this.addMouseListener(input);
        this.addMouseMotionListener(input);

        addPieces();
    }

    //ricerca del pezzo nella posizione Col = x e Row = y
    public Piece getPiece(int x, int y) {
        for (Piece piece : pieces) {
            if (piece.col == x && piece.row == y) {
                return piece;
            }
        }
        return null;
    }

    //verifica se una mossa è valida
    public boolean isValidMove(Movement move){
        if (sameTeam(move.piece,move.Captured)){    //se si cerca di catturare il proprio pezzo la mossa non è valida
            return false;
        }
        return true;
    }

    //effetua le movimentazioni
    public void makeMove(Movement move){
        //aggiorno la posizione nella scacchiera
        move.piece.col = move.nextCol;
        move.piece.row = move.nextRow;
        //aggiorno la posizione dello schermo
        move.piece.xPos = move.nextCol * tileDimension;
        move.piece.yPos = move.nextRow * tileDimension;

        capture(move);
    }

    // cattura di un pezzo
    public void capture(Movement move){
        pieces.remove(move.Captured);
    }

    //verifica se 2 pezzi appartengono alla stessa squadra
    public boolean sameTeam(Piece p1, Piece p2){
        if (p1 == null || p2 == null){
            return false;
        }
        if (p1.isWhite && p2.isWhite){
            return true;
        }
        if (!p1.isWhite && !p2.isWhite){
            return true;
        }
        return false;
    }
    //add the pieces to the array
    public void addPieces() {
        pieces.add(new Rook(this,0,0,false));
        pieces.add(new Knight(this, 1, 0, false));
        pieces.add(new Bishop(this, 2, 0, false));
        pieces.add(new Queen(this, 3, 0, false));
        pieces.add(new King(this, 4, 0, false));
        pieces.add(new Bishop(this, 5, 0, false));
        pieces.add(new Knight(this, 6, 0, false));
        pieces.add(new Rook(this, 7, 0, false));

        pieces.add(new Pawn(this,0,1,false));
        pieces.add(new Pawn(this,1,1,false));
        pieces.add(new Pawn(this,2,1,false));
        pieces.add(new Pawn(this,3,1,false));
        pieces.add(new Pawn(this,4,1,false));
        pieces.add(new Pawn(this,5,1,false));
        pieces.add(new Pawn(this,6,1,false));
        pieces.add(new Pawn(this,7,1,false));

        pieces.add(new Rook(this,0,7,true));
        pieces.add(new Knight(this,1,7,true));
        pieces.add(new Bishop(this,2,7,true));
        pieces.add(new Queen(this,3,7,true));
        pieces.add(new King(this,4,7,true));
        pieces.add(new Bishop(this,5,7,true));
        pieces.add(new Knight(this,6,7,true));
        pieces.add(new Rook(this,7,7,true));

        pieces.add(new Pawn(this,0,6,true));
        pieces.add(new Pawn(this,1,6,true));
        pieces.add(new Pawn(this,2,6,true));
        pieces.add(new Pawn(this,3,6,true));
        pieces.add(new Pawn(this,4,6,true));
        pieces.add(new Pawn(this,5,6,true));
        pieces.add(new Pawn(this,6,6,true));
        pieces.add(new Pawn(this,7,6,true));

    }

    //draw the game
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        //drawn the board
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if ((r + c) % 2 == 0) {
                    g2d.setColor(new Color(0x34E7C3));
                }
                else g2d.setColor(new Color(0xFFFFFF));

                g2d.fillRect(c*tileDimension,r*tileDimension,tileDimension,tileDimension);
            }
        }
        // draw the pieces in game
        for (Piece piece : pieces) {
            piece.insert(g2d);
        }

    }
}
