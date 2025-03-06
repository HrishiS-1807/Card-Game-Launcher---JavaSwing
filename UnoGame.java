public class UnoGame extends JFrame {
    private List<String> playerHand, bot1Hand, bot2Hand, bot3Hand;
    private String topCard;
    private JLabel statusLabel, topCardLabel;
    private JPanel playerHandPanel;
    private JButton drawButton, passButton;
    private Random random = new Random();
    private GameLauncher mainMenu;
    private int currentTurn = 0; // 0 = Player, 1 = Bot1, 2 = Bot2, 3 = Bot3

    public UnoGame(GameLauncher mainMenu) {
        this.mainMenu = mainMenu;
        setTitle("UNO");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        playerHand = new ArrayList<>();
        bot1Hand = new ArrayList<>();
        bot2Hand = new ArrayList<>();
        bot3Hand = new ArrayList<>();

        dealCards(playerHand);
        dealCards(bot1Hand);
        dealCards(bot2Hand);
        dealCards(bot3Hand);

        topCard = drawRandomCard();

        statusLabel = new JLabel("Your Turn! Select a card to play or draw.", SwingConstants.CENTER);
        topCardLabel = new JLabel("Top Card: " + topCard, SwingConstants.CENTER);

        playerHandPanel = new JPanel();
        updatePlayerHandDisplay();

        JPanel buttonPanel = new JPanel();
        drawButton = new JButton("Draw Card");
        passButton = new JButton("Pass Turn");
        JButton backButton = new JButton("Back to Menu");

        drawButton.addActionListener(e -> drawCard());
        passButton.addActionListener(e -> passTurn());
        backButton.addActionListener(e -> {
            dispose();
            mainMenu.returnToMenu();
        });

        buttonPanel.add(drawButton);
        buttonPanel.add(passButton);
        buttonPanel.add(backButton);

        add(topCardLabel, BorderLayout.NORTH);
        add(playerHandPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void dealCards(List<String> hand) {
        for (int i = 0; i < 5; i++) {
            hand.add(drawRandomCard());
        }
    }

    private String drawRandomCard() {
        String[] colors = {"Red", "Blue", "Green", "Yellow"};
        String[] values = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "Skip", "Reverse", "Draw Two"};
        return colors[random.nextInt(colors.length)] + " " + values[random.nextInt(values.length)];
    }

    private void updatePlayerHandDisplay() {
        playerHandPanel.removeAll();
        for (String card : playerHand) {
            JButton cardButton = new JButton(card);
            cardButton.addActionListener(e -> playCard(card));
            playerHandPanel.add(cardButton);
        }
        playerHandPanel.revalidate();
        playerHandPanel.repaint();
    }

    private void drawCard() {
        String newCard = drawRandomCard();
        playerHand.add(newCard);
        statusLabel.setText("You drew: " + newCard + ". Select a card to play or pass.");
        updatePlayerHandDisplay();
    }

    private void playCard(String card) {
        if (canPlayCard(card)) {
            topCard = card;
            playerHand.remove(card);
            statusLabel.setText("You played: " + card + ". Bots' turn.");
            updatePlayerHandDisplay();
            checkWin();
            nextTurn();
        } else {
            statusLabel.setText("Invalid move! You must play a card that matches the color or number.");
        }
    }

    private boolean canPlayCard(String card) {
        String topColor = topCard.split(" ")[0];
        String topValue = topCard.split(" ")[1];
        String cardColor = card.split(" ")[0];
        String cardValue = card.split(" ")[1];

        return cardColor.equals(topColor) || cardValue.equals(topValue);
    }

    private void passTurn() {
        if (currentTurn == 0) {
            statusLabel.setText("You passed. Bots' turn.");
        }
        nextTurn();
    }

    private void botTurn() {
        List<String> botHand;
        String botName = "";

        if (currentTurn == 1) {
            botHand = bot1Hand;
            botName = "Bot 1";
        } else if (currentTurn == 2) {
            botHand = bot2Hand;
            botName = "Bot 2";
        } else {
            botHand = bot3Hand;
            botName = "Bot 3";
        }

        boolean cardPlayed = false;
        for (String card : botHand) {
            if (canPlayCard(card)) {
                topCard = card;
                botHand.remove(card);
                statusLabel.setText(botName + "'s Turn... put down " + topCard);
                cardPlayed = true;
                break;
            }
        }

        if (!cardPlayed) {
            String newCard = drawRandomCard();
            botHand.add(newCard);
            statusLabel.setText(botName + "'s Turn... no valid move, drawing a card.");
        }

        checkWin();
        nextTurn();
    }

    private void nextTurn() {
        currentTurn = (currentTurn + 1) % 4;

        if (currentTurn == 0) {
            statusLabel.setText("Your Turn! Select a card to play or draw.");
            updatePlayerHandDisplay();
        } else {
            botTurn();
        }

        topCardLabel.setText("Top Card: " + topCard);
    }

    private void checkWin() {
        if (playerHand.isEmpty()) {
            JOptionPane.showMessageDialog(this, "You win!");
            resetGame();
        } else if (bot1Hand.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bot 1 wins!");
            resetGame();
        } else if (bot2Hand.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bot 2 wins!");
            resetGame();
        } else if (bot3Hand.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bot 3 wins!");
            resetGame();
        }
    }

    private void resetGame() {
        playerHand.clear();
        bot1Hand.clear();
        bot2Hand.clear();
        bot3Hand.clear();

        dealCards(playerHand);
        dealCards(bot1Hand);
        dealCards(bot2Hand);
        dealCards(bot3Hand);

        topCard = drawRandomCard();
        statusLabel.setText("New Game! Your Turn.");
        topCardLabel.setText("Top Card: " + topCard);
        updatePlayerHandDisplay();
    }
}
