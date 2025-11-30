package gui.home;

import entity.Habit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;

import gui.home.HomeViewComponents.RoundedPanel;
import gui.home.HomeViewComponents.RoundedBorder;
import gui.home.HomeViewComponents.PillButton;
import gui.home.HomeViewComponents.CircleButton;

/// Skibidi

public class HomeView implements PropertyChangeListener {

    private HomeViewModel homeViewModel;
    private HomePresenter homePresenter;
    private HomeViewController homeViewController;

    private FrequencyTab currentTab = FrequencyTab.DAILY;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refreshAll();
        System.out.println("Property change fired.");
    }

    public enum FrequencyTab {
        DAILY, WEEKLY, MONTHLY
    }

    private JFrame mainFrame;

    // *** NEW: root panel that contains everything ***
    private JPanel mainPanel;

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

    public HomeView(HomeViewModel vm,
                    HomePresenter presenter,
                    HomeViewController controller) {
        this.homeViewModel = vm;
        this.homePresenter = presenter;
        this.homeViewController = controller;
        this.mainFrame = new JFrame("Habits");
        this.homeViewModel.addPropertyChangeListener(this);

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

        // Background/root panel – now stored in field mainPanel
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        mainFrame.setContentPane(mainPanel);

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
        mainPanel.add(card, gbc);

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
        addTaskButton.addActionListener(e -> {
            HomeViewController.showAddTaskWindow();
        });

        bottomPanel.add(addTaskButton);
        card.add(bottomPanel, BorderLayout.SOUTH);

        // Default selection & initial render
        dailyTab.setSelected(true);
        updateSegmentLook();
        showTasks(homeViewModel != null ? homeViewModel.dailyHabits : null);
        System.out.println(homeViewModel.dailyHabits);
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
            currentTab = FrequencyTab.DAILY;
            updateSegmentLook();
            showTasks(homeViewModel.dailyHabits);
        });

        weeklyTab.addActionListener(e -> {
            currentTab = FrequencyTab.WEEKLY;
            updateSegmentLook();
            showTasks(homeViewModel.weeklyHabits);
        });

        monthlyTab.addActionListener(e -> {
            currentTab = FrequencyTab.MONTHLY;
            updateSegmentLook();
            showTasks(homeViewModel.monthlyHabits);
        });

        return panel;
    }

    private JPanel createHeaderChips() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panel.setOpaque(false);

        JButton statChip = createChipButton("Stat");

        statChip.addActionListener(e -> {
            HomeViewController.showStatisticsWindow();
        });

        panel.add(statChip);

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

    ///  The two buttons
    private JButton createChipButton(String text) {
        JButton button = new JButton(text);
        button.setFont(button.getFont().deriveFont(Font.PLAIN, 12f));
        button.setBorder(new RoundedBorder(8));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setMargin(new Insets(4, 12, 4, 12));
        return button;
    }

    /**
     * Renders the tasks inside the central list according to the selected frequency.
     */
    public void showTasks(List<Habit> habits) {
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

    public void refreshAll() {
        if (currentTab == FrequencyTab.DAILY) {
            showTasks(homeViewModel.dailyHabits);
        } else if (currentTab == FrequencyTab.WEEKLY) {
            showTasks(homeViewModel.weeklyHabits);
        } else if (currentTab == FrequencyTab.MONTHLY) {
            showTasks(homeViewModel.monthlyHabits);
        }
    }

    private JPanel createTaskRow(Habit habit) {
        String habitLabel = habit.get_next().name;
        String desc = habit.get_next().desc;

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
        menuButton.addActionListener(e -> {
            HomeViewController.showEditTaskWindow(habit.id);
        });


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
        }
        else {
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
        boolean isCompleted = habit.get_next().completed;
        String initialText = isCompleted ? "[✓]" : "[  ]";

        CircleButton checkButton = new CircleButton(initialText);
        checkButton.setFocusPainted(false);

        checkButton.addActionListener(e -> {
            // 1. Optimistic UI update (makes UI feel fast)
            String currentText = checkButton.getText();
            if ("[  ]".equals(currentText)) {
                checkButton.setText("[✓]");
            } else {
                checkButton.setText("[  ]"); // Note: Unchecking logic not implemented in backend yet
            }

            // 2. Call the controller
            if (homeViewController != null) {
                homeViewController.onHabitCheckboxClicked(habit);
            }
        });


        return checkButton;
    }

    // *** Changed: now returns the root panel containing EVERYTHING ***
    public JPanel getPanel(){
        return mainPanel;
    }

}
