package gui.home;

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


public class HomeView {

    private HomeViewModel homeViewModel;

    private JFrame mainFrame;

    // Header – frequency selector
    private JToggleButton dailyTab;
    private JToggleButton weeklyTab;
    private JToggleButton monthlyTab;

    // Container where task rows are rendered
    private JPanel tasksContainer;

    // "Add task" button at the bottom of the card (exposed for controller)
    private JButton addTaskButton;

    public HomeView(HomeViewModel homeViewModel) {
        // Assign viewmodel
        this.homeViewModel = homeViewModel;
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
        background.setBackground(new Color(0xF6F6F6));
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
        card.add(tasksContainer, BorderLayout.CENTER);

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
        JButton accChip = createChipButton("Acc");

        panel.add(statChip);
        panel.add(setChip);
        panel.add(accChip);

        return panel;
    }

    private JToggleButton createSegmentButton(String text) {
        JToggleButton button = new JToggleButton(text);
        button.setFont(button.getFont().deriveFont(Font.PLAIN, 13f));
        button.setBorder(new RoundedBorder(18));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setMargin(new Insets(6, 14, 6, 14));
        return button;
    }

    private JButton createChipButton(String text) {
        JButton button = new JButton(text);
        button.setFont(button.getFont().deriveFont(Font.PLAIN, 12f));
        button.setBorder(new RoundedBorder(18));
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
        int minRows = 1;
        int currentRows = safeList.size();
        for (int i = currentRows; i < minRows; i++) {
            tasksContainer.add(createEmptyTaskRow());
        }

        tasksContainer.revalidate();
        tasksContainer.repaint();
    }

    private JPanel createTaskRow(Habit habit) {
        String label = habit != null ? habit.toString() : "";

        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(6, 0, 6, 0));

        JTextField textField = new JTextField(label);
        textField.setEditable(false);
        textField.setBorder(new RoundedBorder(18));
        textField.setBackground(Color.WHITE);
        textField.setOpaque(true);
        textField.setMargin(new Insets(4, 12, 4, 12));
        textField.setPreferredSize(new Dimension(420, 32));

        row.add(textField, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);

        JButton checklistButton = new CircleButton("[ ]");
        JButton menuButton = new CircleButton("...");

        actions.add(checklistButton);
        actions.add(menuButton);

        row.add(actions, BorderLayout.EAST);

        return row;
    }

    private JPanel createEmptyTaskRow() {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(6, 0, 6, 0));

        JTextField textField = new JTextField();
        textField.setEditable(false);
        textField.setBorder(new RoundedBorder(18));
        textField.setBackground(Color.WHITE);
        textField.setOpaque(true);
        textField.setMargin(new Insets(4, 12, 4, 12));
        textField.setPreferredSize(new Dimension(420, 32));

        row.add(textField, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);

        JButton checklistButton = new CircleButton("[ ]");
        JButton menuButton = new CircleButton("...");

        actions.add(checklistButton);
        actions.add(menuButton);

        row.add(actions, BorderLayout.EAST);

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
}
