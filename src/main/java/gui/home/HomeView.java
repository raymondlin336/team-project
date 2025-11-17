package gui.home;

import gui.new_task.NewTaskController;
import main.Habit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Collections;
import java.util.List;

import gui.home.HomeViewComponents.RoundedPanel;
import gui.home.HomeViewComponents.RoundedBorder;
import gui.home.HomeViewComponents.PillButton;
import gui.home.HomeViewComponents.CircleButton;

/// Skibidi

public class HomeView {

    private HomeViewModel homeViewModel;

    private gui.Home.HomeViewController HomeViewController;

    private JFrame mainFrame;

    // Header – frequency selector
    private JToggleButton dailyTab;
    private JToggleButton weeklyTab;
    private JToggleButton monthlyTab;

    // Container where task rows are rendered
    private JPanel tasksContainer;
    // Scroll pane that wraps the tasksContainer
    private JScrollPane tasksScrollPane;

    // "Add task" button at the bottom of the card (exposed for controller)
    private JButton addTaskButton;

    public HomeView(HomeViewModel homeViewModel, gui.Home.HomeViewController controller) {
        // Assign viewmodel
        this.homeViewModel = homeViewModel;
        this.HomeViewController = controller;
        this.mainFrame = new JFrame("Habits");
        createUIComponents(900, 600);
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    // Show the window.
    public void show() {
        mainFrame.setVisible(true);
    }

    private void createUIComponents(int width,int height) {
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(width, height);
        mainFrame.setLocationRelativeTo(null);

        // Background
        JPanel background = new JPanel(new GridBagLayout());
        background.setBorder(new EmptyBorder(30, 30, 30, 30));
        mainFrame.setContentPane(background);

        // Rounded JPanel containing all the elements in HomeView
        JPanel card = new RoundedPanel(24);
        card.setBackground(new Color(0xEDEDED));
        card.setLayout(new BorderLayout(20, 20));
        card.setBorder(new EmptyBorder(12, 16, 12, 16));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        background.add(card, gbc);

        // Header: frequency tabs on the left, small chips on the right ──
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        header.add(createFrequencySelector(), BorderLayout.WEST);
        header.add(createHeaderChips(), BorderLayout.EAST);

        card.add(header, BorderLayout.NORTH);

        // Tasks area
        tasksContainer = new JPanel();
        tasksContainer.setOpaque(false);
        tasksContainer.setLayout(new BoxLayout(tasksContainer, BoxLayout.Y_AXIS));

        // Wrap the tasks container in a scroll pane
        tasksScrollPane = new JScrollPane(tasksContainer);
        tasksScrollPane.setBorder(null); // no border so it blends with the card
        tasksScrollPane.setOpaque(false);
        tasksScrollPane.getViewport().setOpaque(false);

        // Only vertical scroll, no horizontal
        tasksScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        tasksScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        card.add(tasksScrollPane, BorderLayout.CENTER);

        // "Add task" pill button at the bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        bottomPanel.setOpaque(false);

        addTaskButton = new PillButton("Add task");
        addTaskButton.setPreferredSize(new Dimension(420, 36));
        addTaskButton.setFocusPainted(false);

        bottomPanel.add(addTaskButton);
        card.add(bottomPanel, BorderLayout.SOUTH);

        // Default selection & initial render
        dailyTab.setSelected(true);
        updateSegmentLook();
        showTasks(homeViewModel != null ? homeViewModel.dailyHabits : null);
    }

    /**
     * Creates the "Daily  Weekly  Monthly" segmented control.
     */
    private JPanel createFrequencySelector() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panel.setOpaque(false);

        ButtonGroup group = new ButtonGroup();

        dailyTab = createSegmentButton("Daily");
        weeklyTab = createSegmentButton("Weekly");
        monthlyTab = createSegmentButton("Monthly");

        group.add(dailyTab);
        group.add(weeklyTab);
        group.add(monthlyTab);

        panel.add(dailyTab);
        panel.add(weeklyTab);
        panel.add(monthlyTab);

        // Wire up actions to re-render the task list
        dailyTab.addActionListener(e -> {
            updateSegmentLook();
            showTasks(homeViewModel != null ? homeViewModel.dailyHabits : null);
        });

        weeklyTab.addActionListener(e -> {
            updateSegmentLook();
            showTasks(homeViewModel != null ? homeViewModel.weeklyHabits : null);
        });

        monthlyTab.addActionListener(e -> {
            updateSegmentLook();
            showTasks(homeViewModel != null ? homeViewModel.monthlyHabits : null);
        });

        return panel;
    }

    private JPanel createHeaderChips() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panel.setOpaque(false);

        JButton statChip = createChipButton("Stat");
        JButton setChip = createChipButton("Set");

        panel.add(statChip);
        panel.add(setChip);

        return panel;
    }

    private JToggleButton createSegmentButton(String text) {
        JToggleButton button = new JToggleButton(text);
        button.setFont(button.getFont().deriveFont(Font.PLAIN, 13f));
        button.setBorder(new RoundedBorder(8));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setMargin(new Insets(6, 14, 6, 14));
        return button;
    }

    ///  The three
    private JButton createChipButton(String text) {
        JButton button = new JButton(text);
        button.setFont(button.getFont().deriveFont(Font.PLAIN, 12f));
        button.setBorder(new RoundedBorder(8));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setMargin(new Insets(4, 12, 4, 12));
        return button;
    }

    /**
     * Renders the tasks inside the central list according to the selected frequency.
     */
    private void showTasks(List<Habit> habits) {
        tasksContainer.removeAll();

        List<Habit> safeList = habits != null ? habits : Collections.emptyList();

        for (Habit habit : safeList) {
            tasksContainer.add(createTaskRow(habit));
        }

        // Always show at least one row, padding with empties
        int minRows = 10;
        int currentRows = safeList.size();
        for (int i = currentRows; i < minRows; i++) {
            tasksContainer.add(createEmptyTaskRow());
        }

        tasksContainer.revalidate();
        tasksContainer.repaint();

        // Reset scroll to top whenever tasks are refreshed
        if (tasksScrollPane != null) {
            SwingUtilities.invokeLater(() ->
                    tasksScrollPane.getVerticalScrollBar().setValue(0)
            );
        }
    }

    private JPanel createTaskRow(Habit habit) {
        String habitLabel = habit.name;
        String desc = habit.desc;

        ///  Container for entire row
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(6, 0, 6, 0));
        row.setMaximumSize(new Dimension(520, 72)); // fixed height, flexible width
        row.setPreferredSize(new Dimension(520, 72));

        ///  Text: Habit Name
        JLabel label = new JLabel(habitLabel);
        label.setBorder(new RoundedBorder(8));
        label.setBackground(Color.WHITE);
        label.setOpaque(true);
        label.setPreferredSize(new Dimension(160, 42));
        label.setMaximumSize(label.getPreferredSize()); // stops vertical stretching
        label.setHorizontalAlignment(SwingConstants.CENTER);

        ///  Text: Description
        JLabel description = new JLabel(desc);
        description.setBorder(new RoundedBorder(8));
        description.setBackground(Color.WHITE);
        description.setOpaque(true);
        description.setPreferredSize(new Dimension(300, 42));
        description.setMaximumSize(label.getPreferredSize()); // stops vertical stretching
        description.setHorizontalAlignment(SwingConstants.CENTER);

        ///  The Buttons
        JButton checklistButton = createChecklistButton(habit);
        JButton menuButton = new CircleButton("...");

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        actions.add(checklistButton);
        actions.add(menuButton);
        actions.setAlignmentX(Component.CENTER_ALIGNMENT);

        row.add(label);
        row.add(Box.createHorizontalStrut(12));  // spacing
        row.add(description);
        row.add(Box.createHorizontalStrut(12));
        row.add(actions);

        return row;
    }



    private JPanel createEmptyTaskRow() {
        ///  Container for entire row
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(6, 0, 6, 0));
        row.setMaximumSize(new Dimension(520, 72)); // fixed height, flexible width
        row.setPreferredSize(new Dimension(520, 72));

        ///  Text: Habit Name
        JLabel label = new JLabel();
        label.setBorder(new RoundedBorder(8));
        label.setBackground(Color.WHITE);
        label.setOpaque(true);
        label.setPreferredSize(new Dimension(160, 42));
        label.setMaximumSize(label.getPreferredSize()); // stops vertical stretching
        label.setHorizontalAlignment(SwingConstants.CENTER);

        ///  Text: Description
        JLabel description = new JLabel();
        description.setBorder(new RoundedBorder(8));
        description.setBackground(Color.WHITE);
        description.setOpaque(true);
        description.setPreferredSize(new Dimension(300, 42));
        description.setMaximumSize(label.getPreferredSize()); // stops vertical stretching
        description.setHorizontalAlignment(SwingConstants.CENTER);

        ///  The Buttons
        JButton checklistButton = new CircleButton("[  ]");
        JButton menuButton = new CircleButton("...");

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        actions.add(checklistButton);
        actions.add(menuButton);
        actions.setAlignmentX(Component.CENTER_ALIGNMENT);

        row.add(label);
        row.add(Box.createHorizontalStrut(12));  // spacing
        row.add(description);
        row.add(Box.createHorizontalStrut(12));
        row.add(actions);

        return row;
    }

    private void updateSegmentLook() {
        updateSegmentLook(dailyTab);
        updateSegmentLook(weeklyTab);
        updateSegmentLook(monthlyTab);
    }

    private void updateSegmentLook(AbstractButton button) {
        if (button == null) return;

        if (button.isSelected()) {
            button.setForeground(Color.WHITE);
            button.setBackground(Color.BLACK);
            button.setOpaque(true);
        } else {
            button.setForeground(Color.DARK_GRAY);
            button.setBackground(Color.WHITE);
            button.setOpaque(true);
        }
    }

    /**
     * Creates the circular checklist button for a habit.
     * The button has two states: [ ] (not done) and [✓] (done).
     */
    private CircleButton createChecklistButton(Habit habit) {
        // Initial state: assume NOT done; change if you have a real field
        boolean done = false;  // e.g. habit.isDoneToday() if you have that

        CircleButton checkButton = new CircleButton(done ? "[✓]" : "[  ]");
        checkButton.setFocusPainted(false);

        // Store the current done state on the button itself
        checkButton.putClientProperty("done", done);

        checkButton.addActionListener(e -> {
            // Read current state
            boolean currentlyDone = (Boolean) checkButton.getClientProperty("done");
            boolean nowDone = !currentlyDone;

            // Update stored state
            checkButton.putClientProperty("done", nowDone);

            // Update the text shown on the button
            checkButton.setText(nowDone ? "[✓]" : "[  ]");

            // Tell the controller so it can update the Habit model
            if (nowDone) {
                // task just became DONE
                if (this.HomeViewController != null) {
                    this.HomeViewController.markTaskDone(true, habit);
                }
            } else {
                // task just became NOT DONE
                if (this.HomeViewController != null) {
                    this.HomeViewController.markTaskNotDone(false, habit);
                }
            }

            // Make sure UI refreshes
            checkButton.revalidate();
            checkButton.repaint();
        });

        return checkButton;
    }

}

