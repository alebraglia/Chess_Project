package Game;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Chess");
        frame.setMinimumSize(new Dimension(800, 800));
        frame.setLocationRelativeTo(null);  //posiziono la finestra nel centro
        frame.setLayout(new GridBagLayout());
        frame.getContentPane().setBackground(Color.DARK_GRAY);

        // Aggiungo la scacchiera al frame dopo aver impostato il layout
        ChessBoard board = new ChessBoard();
        frame.add(board);

        // Pack e visualizzo il frame
        frame.pack();   //aggiusto il size della finestra
        frame.setVisible(true);
    }
}
