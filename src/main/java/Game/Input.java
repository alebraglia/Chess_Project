package Game;

import Pieces.Piece;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Input implements MouseListener, MouseMotionListener {

    ChessBoard board;
    public Input(ChessBoard board) {
        this.board = board;
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int col = e.getX() / board.tileDimension;     // coordinata x / dimensione dei blocchi produce la coordinata desiderata
        int row = e.getY() / board.tileDimension;     //...

        Piece selectedPiece = board.getPiece(col,row);  //cerco il pezzo selezionato
        if (selectedPiece != null){
            board.selectedPiece = selectedPiece;        // inizializzo selected piece
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        int col = e.getX() / board.tileDimension;     // coordinata x / dimensione dei blocchi produce la coordinata della destinazione
        int row = e.getY() / board.tileDimension;

        //caso abbiamo un pezzo selezionato, qundo rilasciamo tale si cerca di effetuare il movimento
        if (board.selectedPiece != null){
            Movement move = new Movement(board,col,row,board.selectedPiece);    // definisco la possibile mossa a nextCol e nextRow

            // caso la mossa sia valida la si effetua
            if (board.isValidMove(move)){
                board.makeMove(move);
            }
            else {      //caso la mossa non sia valida la riporto alla posizione originale
                board.selectedPiece.xPos = board.selectedPiece.col * board.tileDimension;
                board.selectedPiece.yPos = board.selectedPiece.row * board.tileDimension;
            }
        }

        board.selectedPiece = null; //resetto il pezzo selezionato
        board.repaint();

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (board.selectedPiece != null){
            board.selectedPiece.xPos = e.getX() - (board.tileDimension / 2);    //- (board.tileDimension / 2) serve per centrare il pezzo con il mouse
            board.selectedPiece.yPos = e.getY() - (board.tileDimension / 2);

            board.repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
