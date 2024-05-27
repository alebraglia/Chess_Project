package Pieces;

import Game.ChessBoard;

import java.awt.*;

public class Piece {

    public int col,row;         // coordinate nella scacchiera
    public int xPos, yPos;      // coordinate nello schermo
    public boolean isWhite;     // bool per distinghere le squadre
    public String type;         //tipo del pezzo
    public ChessBoard board;    //board di riferimento
    public boolean isFirstMove = true;        //serve per il primo movimento del pawn e per fare castelling

    Image image = null;    //carica, modifica e gestisce immagini in memoria.

    public Piece (ChessBoard board){
        this.board = board;
    }

    // funzione per inserire il pezzo
    public void insert (Graphics2D g2d){
        g2d.drawImage(image,xPos,yPos,null);
    }

    //funzioni default da essere sovrascritte per ogni pezzo
    public boolean isValidMove(int col,int row){
        return true;
    }
    //funzione per identificare caso ci siano pezzi nel traggito del selezionato (in modo da non saltare pezzi)
    public boolean moveIsBlocked(int col, int row){
        return false;
    }
}