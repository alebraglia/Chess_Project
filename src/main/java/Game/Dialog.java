package Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Dialog {

    static public void startGameDialog(){
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
        try{
            Image icon = ImageIO.read(Main.class.getResourceAsStream("/black_knight.png"));
            frame.setIconImage(icon);
        }
        catch (IOException e) {
            System.out.println("Errore caricamento immagini\n");
            System.exit(-1);
        }

        // Aggiungo la scacchiera al frame dopo aver impostato il layout
        ChessBoard board = ChessBoard.getInstance(frame, whiteTileColor, blackTileColor);
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

    static public void pawnPromotionDialog(Movement move, ChessBoard board){

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
                board.applyPromotion(move, "Queen");
                dialog.dispose();
            });

            rookButton.addActionListener(e -> {
                board.applyPromotion(move, "Rook");
                dialog.dispose();
            });

            bishopButton.addActionListener(e -> {
                board.applyPromotion(move, "Bishop");
                dialog.dispose();
            });

            knightButton.addActionListener(e -> {
                board.applyPromotion(move, "Knight");
                dialog.dispose();
            });

            // Mostra il dialogo
            dialog.setVisible(true);

    }

    static public void endGameDialog(String winner, JFrame jFrame, ChessBoard chessBoard){
// creo il jframe
        JFrame frame = new JFrame("Game Over");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);

        // jlabel che mostrerà il vincitore
        JLabel label = new JLabel(winner, SwingConstants.CENTER);

        // bottone New game
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> {
            chessBoard.resetBoard();
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
        frame.setLocationRelativeTo(jFrame);
        // mostra il frame
        frame.setVisible(true);
        }
}
