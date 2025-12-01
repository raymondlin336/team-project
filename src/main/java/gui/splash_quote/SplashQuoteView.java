package gui.splash_quote;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SplashQuoteView extends JFrame {

    private final SplashQuoteViewModel viewModel;
    private final SplashQuoteController controller;

    private final JLabel quoteLabel;
    private final JLabel authorLabel;
    private final JProgressBar loadingIndicator;
    private final JButton nextButton;

    private final Timer autoTimer;

    public SplashQuoteView(SplashQuoteViewModel viewModel,
                           SplashQuoteController controller) {
        this.viewModel = viewModel;
        this.controller = controller;

        // --- Frame setup ---
        setTitle("Daily Motivation");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(640, 420);
        setLocationRelativeTo(null);

        // === DARK THEME COLOURS ===
        Color bgBlack        = new Color(0x05, 0x05, 0x08); // almost black
        Color cardDark       = new Color(0x16, 0x16, 0x1D); // dark grey card
        Color quoteGold      = new Color(0xF6, 0xC4, 0x53); // warm gold
        Color authorGrey     = new Color(0xD0, 0xD4, 0xDC); // light grey
        Color footerGrey     = new Color(0x9C, 0xA3, 0xAF);
        Color buttonGold     = quoteGold;
        Color buttonGoldDark = new Color(0xD6, 0xA2, 0x3F);

        // Background panel (dark)
        JPanel background = new JPanel(new BorderLayout());
        background.setBackground(bgBlack);
        background.setBorder(new EmptyBorder(30, 40, 30, 40));
        setContentPane(background);

        // Centered card panel (dark grey)
        JPanel card = new JPanel();
        card.setBackground(cardDark);
        card.setBorder(new EmptyBorder(30, 40, 30, 40));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        // Quote label – golden serif font
        quoteLabel = new JLabel("", SwingConstants.CENTER);
        quoteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        quoteLabel.setFont(new Font("Georgia", Font.BOLD, 24));
        quoteLabel.setForeground(quoteGold);

        // Author label – light grey
        authorLabel = new JLabel("", SwingConstants.CENTER);
        authorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        authorLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        authorLabel.setForeground(authorGrey);
        authorLabel.setBorder(new EmptyBorder(15, 0, 20, 0));

        // Loading indicator – subtle gold bar
        loadingIndicator = new JProgressBar();
        loadingIndicator.setIndeterminate(true);
        loadingIndicator.setBorderPainted(false);
        loadingIndicator.setPreferredSize(new Dimension(80, 8));
        loadingIndicator.setMaximumSize(new Dimension(80, 8));
        loadingIndicator.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadingIndicator.setForeground(quoteGold);
        loadingIndicator.setBackground(cardDark);

// Next button – gold pill with blue outline & white text
        nextButton = new JButton("Next");
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextButton.setPreferredSize(new Dimension(120, 36));
        nextButton.setBackground(buttonGold);
        nextButton.setForeground(Color.WHITE); // white text so it pops
        nextButton.setFocusPainted(false);
        nextButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

// Blue rounded outline around the button
        nextButton.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0x4C6FFF), 2, true), // blue outline
                        BorderFactory.createEmptyBorder(6, 22, 6, 22)                  // inner padding
                )
        );


        // Simple rollover effect
        nextButton.getModel().addChangeListener(e -> {
            ButtonModel m = nextButton.getModel();
            if (m.isPressed()) {
                nextButton.setBackground(buttonGoldDark);
            } else if (m.isRollover()) {
                nextButton.setBackground(buttonGold);
            } else {
                nextButton.setBackground(buttonGold);
            }
        });

        // Build card layout
        card.add(Box.createVerticalGlue());
        card.add(quoteLabel);
        card.add(Box.createVerticalStrut(20));
        card.add(authorLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(loadingIndicator);
        card.add(Box.createVerticalStrut(25));
        card.add(nextButton);
        card.add(Box.createVerticalGlue());

        background.add(card, BorderLayout.CENTER);

        // Footer: "Powered by ZenQuotes"
        JLabel powered = new JLabel("Powered by ZenQuotes", SwingConstants.CENTER);
        powered.setBorder(new EmptyBorder(10, 0, 0, 0));
        powered.setForeground(footerGrey);
        background.add(powered, BorderLayout.SOUTH);

        // Button click -> fetch new quote
        nextButton.addActionListener(e -> fetchNewQuote());

        // Auto-refresh every 20 seconds
        autoTimer = new Timer(20_000, e -> fetchNewQuote());
        autoTimer.setInitialDelay(0);
        autoTimer.start();

        // First quote right away
        fetchNewQuote();
    }

    private void fetchNewQuote() {
        loadingIndicator.setVisible(true);
        nextButton.setEnabled(false);

        // Run quote loading off the EDT
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                controller.loadQuote(); // calls use case, updates ViewModel
                return null;
            }

            @Override
            protected void done() {
                loadingIndicator.setVisible(false);
                nextButton.setEnabled(true);
                refreshFromViewModel();
            }
        }.execute();
    }

    private void refreshFromViewModel() {
        String error = viewModel.getErrorMessage();

        if (!error.isEmpty()) {
            quoteLabel.setText(wrapHtml(error));
            authorLabel.setText("");
        } else {
            String quoteText = viewModel.getQuoteText();
            String author = viewModel.getQuoteAuthor();
            quoteLabel.setText(wrapHtml("“" + quoteText + "”"));
            authorLabel.setText(author);
        }
    }

    // Make the JLabel wrap nicely and center-align
    private String wrapHtml(String text) {
        int width = 360; // content width in pixels inside the label
        return "<html><div style='text-align:center;width:" + width + "px;'>"
                + text + "</div></html>";
    }
}
