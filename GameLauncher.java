import javax.swing.*;
import java.awt.*;

public class GameLauncher extends JFrame {
    public GameLauncher() {
        setTitle("Game Selector - Blackjack, UNO & Super Bowl Stats");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        JLabel titleLabel = new JLabel("Choose Your Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton blackjackButton = new JButton("Play Blackjack ");
        JButton unoButton = new JButton("Play UNO");
        

        blackjackButton.addActionListener(e -> {
            dispose();
            new BlackjackGUI(this).setVisible(true);
        });

        unoButton.addActionListener(e -> {
            dispose();
            new UnoGame(this).setVisible(true);
        });

        
        buttonPanel.add(blackjackButton);
        buttonPanel.add(unoButton);
 

        add(titleLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }

    public void returnToMenu() {
        SwingUtilities.invokeLater(() -> {
            new GameLauncher().setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GameLauncher().setVisible(true);
        });
    }
}
