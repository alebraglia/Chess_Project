package Game;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        // Definisco i colori per i temi
        Color[][] themes = {
                {Color.WHITE, Color.CYAN},     // Tema 1: (bianco, ciano)
                {Color.WHITE, Color.gray},    // Tema 2: (bianco, nero)
                {new Color(222, 184, 135), new Color(139, 69, 19)}  // Tema 3: (stile legno)
        };

        // Opzioni per il tema
        String[] options = {"Bianco e Ciano", "Bianco e grigio", "Legno"};

        // Mostra un popup per scegliere il tema
        int choice = JOptionPane.showOptionDialog(
                null,
                "Scegli il tema della scacchiera:",
                "Selezione Tema",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        // Controlla se l'utente ha chiuso il popup o premuto "Annulla"
        if (choice == -1) {
            System.exit(0);  // Chiude il programma
        }

        // Imposta i colori in base alla scelta
        Color whiteTileColor = themes[choice][0];
        Color blackTileColor = themes[choice][1];

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Chess");
        frame.setMinimumSize(new Dimension(600, 600));
        frame.setLocationRelativeTo(null);  // Posiziona la finestra al centro
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.DARK_GRAY);

        // Aggiungo la scacchiera al frame dopo aver impostato il layout
        ChessBoard board = new ChessBoard(frame, whiteTileColor, blackTileColor);
        frame.add(board, BorderLayout.CENTER);

        // Crea il pulsante per attivare/disattivare il God Mode
        JButton godModeButton = new JButton("God Mode");
        godModeButton.addActionListener(e -> {
            board.setGodMode(!board.godMode); // Inverti lo stato di God Mode
        });

        // Aggiunge il pulsante in alto a destra del frame
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(godModeButton);
        frame.add(buttonPanel, BorderLayout.NORTH);

        // Pack e visualizzo il frame
        frame.pack();   // Aggiusta la dimensione della finestra
        frame.setVisible(true);
    }
}