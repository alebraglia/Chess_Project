package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Chess");
        frame.setMinimumSize(new Dimension(600, 600));
        frame.setLocationRelativeTo(null);  //posiziono la finestra nel centro
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.DARK_GRAY);

        // Aggiungo la scacchiera al frame dopo aver impostato il layout
        ChessBoard board = new ChessBoard(frame);
        frame.add(board, BorderLayout.CENTER);

        // Crea il pulsante per attivare/disattivare il God Mode
        JButton godModeButton = new JButton("God Mode");
        godModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.setGodMode(!board.godMode); // Inverti lo stato di God Mode
            }
        });

        // Aggiunge il pulsante in alto a destra del frame
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(godModeButton);
        frame.add(buttonPanel, BorderLayout.NORTH);

        // Pack e visualizzo il frame
        frame.pack();   //aggiusto il size della finestra
        frame.setVisible(true);
    }
}
