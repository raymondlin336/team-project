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

        // Background panel (blue)
        JPanel background = new JPanel(new BorderLayout());
        background.setBackground(new Color(0x6FAAD8)); // bluish
        background.setBorder(new EmptyBorder(30, 40, 30, 40));
        setContentPane(background);

        // Centered card panel (white)
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(30, 40, 30, 40));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        // Quote label (wrapped text)
        quoteLabel = new JLabel("", SwingConstants.CENTER);
        quoteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        quoteLabel.setFont(new Font("SansSerif", Font.BOLD, 22));

        // Author label
        authorLabel = new JLabel("", SwingConstants.CENTER);
        authorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        authorLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        authorLabel.setBorder(new EmptyBorder(15, 0, 20, 0));

        // Loading "circle" (indeterminate progress bar)
        loadingIndicator = new JProgressBar();
        loadingIndicator.setIndeterminate(true);
        loadingIndicator.setBorderPainted(false);
        loadingIndicator.setPreferredSize(new Dimension(60, 12));
        loadingIndicator.setMaximumSize(new Dimension(60, 12));
        loadingIndicator.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Next button
        nextButton = new JButton("Next");
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextButton.setPreferredSize(new Dimension(120, 36));

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
        powered.setForeground(new Color(0x1F3C5B));
        background.add(powered, BorderLayout.SOUTH);

        // Button click -> fetch new quote
        nextButton.addActionListener(e -> fetchNewQuote());

        // Auto-refresh every 20 seconds (tweak as you like)
        autoTimer = new Timer(20_000, e -> fetchNewQuote());
        autoTimer.setInitialDelay(0); // also triggers once when started
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

    // Make the JLabel wrap nicely and center-align, like your mockup
    private String wrapHtml(String text) {
        int width = 360; // content width in pixels inside the label
        return "<html><div style='text-align:center;width:" + width + "px;'>"
                + text + "</div></html>";
    }
}

