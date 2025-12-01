package gui.home;

import entity.Date;
import entity.Habit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import gui.home.HomeViewComponents.RoundedPanel;
import gui.home.HomeViewComponents.RoundedBorder;
import gui.home.HomeViewComponents.PillButton;
import gui.home.HomeViewComponents.CircleButton;

public class HomeView implements PropertyChangeListener {

    // ********* PALETTE (roughly matching your screenshot) *********
    private static final Color APP_BACKGROUND   = new Color(0xE6E9FF); // soft periwinkle
    private static final Color CARD_BACKGROUND  = Color.WHITE;

    private static final Color TAB_SELECTED     = new Color(0x7C82FF); // purple
    private static final Color TAB_UNSELECTED   = new Color(0xF3F3F7);

    private static final Color STAT_GREEN       = new Color(0x7ED7A6); // Stat button
    private static final Color STAT_GREEN_DARK  = new Color(0x20553B);

    private static final Color CHECK_GREEN      = new Color(0x7ED7A6); // checkbox filled
    private static final Color CHECK_GREEN_BORDER = new Color(0x4E9A6B);

    // Card-style row colours (cycle through)
    private static final Color[] ROW_COLORS = new Color[]{
            new Color(0xFFF5C7), // pastel yellow
            new Color(0xE7F2FF), // pastel blue
            new Color(0xF2E9FF), // pastel lavender
            new Color(0xFFE6F0), // pastel pink
            new Color(0xE4F7E5)  // pastel green
    };

    private HomeViewModel homeViewModel;
    private HomePresenter homePresenter;
    private HomeViewController homeViewController;

    private FrequencyTab currentTab = FrequencyTab.DAILY;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refreshAll();
    }

    public enum FrequencyTab {
        DAILY, WEEKLY, MONTHLY
    }

    private JFrame mainFrame;
    private JPanel mainPanel;

    // Header – frequency selector
    private JToggleButton dailyTab;
    private JToggleButton weeklyTab;
    private JToggleButton monthlyTab;

    // Container where task rows are rendered
    private JPanel tasksContainer;
    private JScrollPane tasksScrollPane;

    private JLabel dateDisplayLabel;

    // "Add task" pill
    private PillButton addTaskButton;

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

    public void show() {
        mainFrame.setVisible(true);
    }

    private void createUIComponents(int width, int height) {
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(width, height);
        mainFrame.setLocationRelativeTo(null);

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(APP_BACKGROUND);
        mainFrame.setContentPane(mainPanel);

        JPanel card = new RoundedPanel(24);
        card.setBackground(CARD_BACKGROUND);
        card.setLayout(new BorderLayout(20, 20));
        card.setBorder(new EmptyBorder(18, 20, 18, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(card, gbc);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        header.add(createFrequencySelector(), BorderLayout.WEST);
        header.add(createHeaderChips(), BorderLayout.EAST);

        card.add(header, BorderLayout.NORTH);

        // Tasks area
        tasksContainer = new JPanel();
        tasksContainer.setOpaque(false);
        tasksContainer.setLayout(new BoxLayout(tasksContainer, BoxLayout.Y_AXIS));

        tasksScrollPane = new JScrollPane(tasksContainer);
        tasksScrollPane.setBorder(null);
        tasksScrollPane.setOpaque(false);
        tasksScrollPane.getViewport().setOpaque(false);
        tasksScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        tasksScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        card.add(tasksScrollPane, BorderLayout.CENTER);

        // "Add task" pill
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        bottomPanel.setOpaque(false);

        addTaskButton = new PillButton("Add task");
        addTaskButton.setPreferredSize(new Dimension(420, 36));
        addTaskButton.setBaseColor(new Color(0xFFB574));       // orange
        addTaskButton.setBorderColor(new Color(0xE39A54));
        addTaskButton.setFocusPainted(false);
        addTaskButton.addActionListener(e -> HomeViewController.showAddTaskWindow());

        bottomPanel.add(addTaskButton);
        card.add(bottomPanel, BorderLayout.SOUTH);

        // Default selection and first render
        dailyTab.setSelected(true);
        updateSegmentLook();
        updateDateLabel(FrequencyTab.DAILY);
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

        // date label
        dateDisplayLabel = new JLabel();
        dateDisplayLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        dateDisplayLabel.setForeground(new Color(0x4F4F60));
        panel.add(dateDisplayLabel);

        dailyTab.addActionListener(e -> {
            currentTab = FrequencyTab.DAILY;
            updateSegmentLook();
            showTasks(homeViewModel.dailyHabits);
            updateDateLabel(FrequencyTab.DAILY);
        });

        weeklyTab.addActionListener(e -> {
            currentTab = FrequencyTab.WEEKLY;
            updateSegmentLook();
            showTasks(homeViewModel.weeklyHabits);
            updateDateLabel(FrequencyTab.WEEKLY);
        });

        monthlyTab.addActionListener(e -> {
            currentTab = FrequencyTab.MONTHLY;
            updateSegmentLook();
            showTasks(homeViewModel.monthlyHabits);
            updateDateLabel(FrequencyTab.MONTHLY);
        });

        return panel;
    }

    private JPanel createHeaderChips() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panel.setOpaque(false);

        JButton statChip = createChipButton("Stat");
        statChip.setBackground(STAT_GREEN);
        statChip.setForeground(STAT_GREEN_DARK);

        statChip.addActionListener(e -> HomeViewController.showStatisticsWindow());

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
        button.setOpaque(true);
        button.setMargin(new Insets(6, 16, 6, 16));
        return button;
    }

    private JButton createChipButton(String text) {
        JButton button = new JButton(text);
        button.setFont(button.getFont().deriveFont(Font.PLAIN, 12f));
        button.setBorder(new RoundedBorder(8));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setMargin(new Insets(4, 14, 4, 14));
        return button;
    }

    /** Renders tasks according to the current frequency tab. */
    public void showTasks(List<Habit> habits) {
        tasksContainer.removeAll();

        List<Habit> safeList = habits != null ? habits : Collections.emptyList();

        int rowIndex = 0;
        for (Habit habit : safeList) {
            tasksContainer.add(createTaskRow(habit, rowIndex));
            rowIndex++;
        }

        int minRows = 10;
        int currentRows = safeList.size();
        for (int i = currentRows; i < minRows; i++) {
            tasksContainer.add(createEmptyTaskRow(rowIndex));
            rowIndex++;
        }

        tasksContainer.revalidate();
        tasksContainer.repaint();

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
        } else {
            showTasks(homeViewModel.monthlyHabits);
        }
    }

    private Color getRowBackground(int rowIndex) {
        return ROW_COLORS[rowIndex % ROW_COLORS.length];
    }

    private JPanel createTaskRow(Habit habit, int rowIndex) {
        String habitLabel = habit.get_next().name;
        String desc = habit.get_next().desc;

        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setOpaque(true);
        row.setBackground(getRowBackground(rowIndex));
        row.setBorder(new EmptyBorder(8, 16, 8, 16));
        row.setMaximumSize(new Dimension(520, 72));
        row.setPreferredSize(new Dimension(520, 72));

        JLabel label = new JLabel(habitLabel);
        label.setBorder(new RoundedBorder(8));
        label.setBackground(Color.WHITE);
        label.setOpaque(true);
        label.setPreferredSize(new Dimension(160, 42));
        label.setMaximumSize(label.getPreferredSize());
        label.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel description = new JLabel(desc);
        description.setBorder(new RoundedBorder(8));
        description.setBackground(Color.WHITE);
        description.setOpaque(true);
        description.setPreferredSize(new Dimension(300, 42));
        description.setMaximumSize(description.getPreferredSize());
        description.setHorizontalAlignment(SwingConstants.CENTER);

        JButton checklistButton = createChecklistButton(habit);

        CircleButton menuButton = new CircleButton("...");
        menuButton.setFillColor(Color.WHITE);
        menuButton.setBorderColor(new Color(0xD4C7FF)); // soft lilac
        menuButton.addActionListener(e -> HomeViewController.showEditTaskWindow(habit.id));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        actions.add(checklistButton);
        actions.add(menuButton);
        actions.setAlignmentX(Component.CENTER_ALIGNMENT);

        row.add(label);
        row.add(Box.createHorizontalStrut(12));
        row.add(description);
        row.add(Box.createHorizontalStrut(12));
        row.add(actions);

        return row;
    }

    private JPanel createEmptyTaskRow(int rowIndex) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setOpaque(true);
        row.setBackground(getRowBackground(rowIndex));
        row.setBorder(new EmptyBorder(8, 16, 8, 16));
        row.setMaximumSize(new Dimension(520, 72));
        row.setPreferredSize(new Dimension(520, 72));

        JLabel label = new JLabel();
        label.setBorder(new RoundedBorder(8));
        label.setBackground(Color.WHITE);
        label.setOpaque(true);
        label.setPreferredSize(new Dimension(160, 42));
        label.setMaximumSize(label.getPreferredSize());
        label.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel description = new JLabel();
        description.setBorder(new RoundedBorder(8));
        description.setBackground(Color.WHITE);
        description.setOpaque(true);
        description.setPreferredSize(new Dimension(300, 42));
        description.setMaximumSize(description.getPreferredSize());
        description.setHorizontalAlignment(SwingConstants.CENTER);

        CircleButton checklistButton = new CircleButton("[ ]");
        checklistButton.setFillColor(Color.WHITE);
        checklistButton.setBorderColor(new Color(0xD4D4E0));

        CircleButton menuButton = new CircleButton("...");
        menuButton.setFillColor(Color.WHITE);
        menuButton.setBorderColor(new Color(0xD4D4E0));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        actions.add(checklistButton);
        actions.add(menuButton);
        actions.setAlignmentX(Component.CENTER_ALIGNMENT);

        row.add(label);
        row.add(Box.createHorizontalStrut(12));
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
            button.setBackground(TAB_SELECTED);
        } else {
            button.setForeground(new Color(0x505065));
            button.setBackground(TAB_UNSELECTED);
        }
    }

    /**
     * Creates the circular checklist button for a habit.
     * Empty = white, Checked = pastel green.
     */
    private CircleButton createChecklistButton(Habit habit) {
        LocalDate now = LocalDate.now();
        Date today = new Date(now.getDayOfMonth(), now.getMonthValue() - 1, now.getYear());

        boolean isCompleted = habit.get_task_by_date(today).completed;
        String initialText = isCompleted ? "[✓]" : "[ ]";

        CircleButton checkButton = new CircleButton(initialText);
        checkButton.setBorderColor(CHECK_GREEN_BORDER);
        if (isCompleted) {
            checkButton.setFillColor(CHECK_GREEN);
            checkButton.setForeground(new Color(0x1E3B28));
        } else {
            checkButton.setFillColor(Color.WHITE);
            checkButton.setForeground(new Color(0x444444));
        }

        checkButton.setFocusPainted(false);

        checkButton.addActionListener(e -> {
            String currentText = checkButton.getText();
            boolean nowCompleted;
            if ("[ ]".equals(currentText)) {
                checkButton.setText("[✓]");
                checkButton.setFillColor(CHECK_GREEN);
                checkButton.setForeground(new Color(0x1E3B28));
                nowCompleted = true;
            } else {
                checkButton.setText("[ ]");
                checkButton.setFillColor(Color.WHITE);
                checkButton.setForeground(new Color(0x444444));
                nowCompleted = false;
            }

            if (homeViewController != null) {
                homeViewController.onHabitCheckboxClicked(habit);
            }
        });

        return checkButton;
    }

    private void updateDateLabel(FrequencyTab tab) {
        LocalDate today = LocalDate.now();
        String text;

        switch (tab) {
            case DAILY:
                String dayName = today.format(DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH));
                String month = today.format(DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH));
                int day = today.getDayOfMonth();
                int year = today.getYear();
                text = String.format("%s, %s %d%s %d",
                        dayName, month, day, getDayNumberSuffix(day), year);
                break;

            case WEEKLY:
                LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                LocalDate endOfWeek = startOfWeek.plusDays(6);
                DateTimeFormatter shortFmt = DateTimeFormatter.ofPattern("MMM d", Locale.ENGLISH);
                text = "Week of " + startOfWeek.format(shortFmt) + " - " + endOfWeek.format(shortFmt);
                break;

            case MONTHLY:
                text = today.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH));
                break;

            default:
                text = "";
        }

        if (dateDisplayLabel != null) {
            dateDisplayLabel.setText(text);
        }
    }

    private String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) return "th";
        switch (day % 10) {
            case 1:  return "st";
            case 2:  return "nd";
            case 3:  return "rd";
            default: return "th";
        }
    }

    public JPanel getPanel() {
        return mainPanel;
    }
}
