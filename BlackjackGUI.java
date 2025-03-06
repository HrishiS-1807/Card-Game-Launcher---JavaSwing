import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class BlackjackGUI extends JFrame {
    private static final int MIN_BET = 10;
    private int playerPeanuts = 100;
    private int currentBet = 0;

    private List<Integer> playerHand, dealerHand;
    private JLabel peanutLabel, playerHandLabel, dealerHandLabel, statusLabel;
    private JButton hitButton, standButton, betButton;
    private Random random = new Random();
    private GameLauncher mainMenu;

    public BlackjackGUI(GameLauncher mainMenu) {
        this.mainMenu = mainMenu;
        setTitle("Blackjack with Peanuts");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();

        // Main panel for game elements
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(5, 1));

        peanutLabel = new JLabel("Peanuts: " + playerPeanuts, SwingConstants.CENTER);
        playerHandLabel = new JLabel("Your Hand: ", SwingConstants.CENTER);
        dealerHandLabel = new JLabel("Dealer's Hand: ", SwingConstants.CENTER);
        statusLabel = new JLabel("Place your bet!", SwingConstants.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        hitButton = new JButton("Hit");
        standButton = new JButton("Stand");
        betButton = new JButton("Bet 10 Peanuts");
        JButton backButton = new JButton("Back to Menu");

        hitButton.setEnabled(false);
        standButton.setEnabled(false);

        betButton.addActionListener(e -> placeBet());
        hitButton.addActionListener(e -> hit());
        standButton.addActionListener(e -> stand());
        backButton.addActionListener(e -> {
            dispose();
            mainMenu.returnToMenu();
        });

        buttonPanel.add(betButton);
        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);
        buttonPanel.add(backButton);

        mainPanel.add(peanutLabel);
        mainPanel.add(playerHandLabel);
        mainPanel.add(dealerHandLabel);
        mainPanel.add(statusLabel);
        mainPanel.add(buttonPanel);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void placeBet() {
        if (playerPeanuts < MIN_BET) {
            statusLabel.setText("You're out of peanuts! Game Over.");
            return;
        }

        currentBet = MIN_BET;
        playerPeanuts -= currentBet;
        peanutLabel.setText("Peanuts: " + playerPeanuts);

        playerHand.clear();
        dealerHand.clear();

        playerHand.add(drawCard());
        playerHand.add(drawCard());
        dealerHand.add(drawCard());
        dealerHand.add(drawCard());

        updateLabels(true); // Hide dealer's second card at the start

        statusLabel.setText("Your turn! Choose Hit or Stand.");
        hitButton.setEnabled(true);
        standButton.setEnabled(true);
        betButton.setEnabled(false);
    }

    private void hit() {
        playerHand.add(drawCard());
        updateLabels(true);

        if (handValue(playerHand) > 21) {
            statusLabel.setText("You busted! Dealer wins.");
            endRound();
        }
    }

    private void stand() {
        statusLabel.setText("Dealer's turn...");
        
        while (handValue(dealerHand) < 17) {
            dealerHand.add(drawCard());
        }

        updateLabels(false); // Show full dealer hand
        determineWinner();
    }

    private void determineWinner() {
        int playerTotal = handValue(playerHand);
        int dealerTotal = handValue(dealerHand);

        if (dealerTotal > 21 || playerTotal > dealerTotal) {
            statusLabel.setText("You win! +3 Peanuts.");
            playerPeanuts += 3;
        } else if (playerTotal == dealerTotal) {
            statusLabel.setText("It's a tie! Bet returned.");
            playerPeanuts += currentBet;
        } else {
            statusLabel.setText("Dealer wins! You lost your bet.");
        }

        endRound();
    }

    private void endRound() {
        peanutLabel.setText("Peanuts: " + playerPeanuts);
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        betButton.setEnabled(playerPeanuts >= MIN_BET);
    }

    private int drawCard() {
        return Math.min(random.nextInt(13) + 1, 10); // Face cards = 10
    }

    private int handValue(List<Integer> hand) {
        int sum = 0, aces = 0;
        for (int card : hand) {
            sum += card;
            if (card == 1) aces++;
        }
        while (aces > 0 && sum + 10 <= 21) {
            sum += 10;
            aces--;
        }
        return sum;
    }

    private void updateLabels(boolean hideDealerSecondCard) {
        playerHandLabel.setText("Your Hand: " + playerHand + " (Total: " + handValue(playerHand) + ")");

        if (hideDealerSecondCard) {
            dealerHandLabel.setText("Dealer's Hand: [" + dealerHand.get(0) + ", ?]");
        } else {
            dealerHandLabel.setText("Dealer's Hand: " + dealerHand + " (Total: " + handValue(dealerHand) + ")");
        }
    }
}
