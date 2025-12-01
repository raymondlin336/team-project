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
import java.util.*;
import java.util.HashMap;
import java.util.Map;


import gui.home.HomeViewComponents.RoundedPanel;
import gui.home.HomeViewComponents.RoundedBorder;
import gui.home.HomeViewComponents.PillButton;
import gui.home.HomeViewComponents.CircleButton;

public class HomeView implements PropertyChangeListener {

    private HomeViewModel homeViewModel;
    private HomePresenter homePresenter;
    private HomeViewController homeViewController;

    private FrequencyTab currentTab = FrequencyTab.DAILY;



    // Map each habit id -> a fixed color from the palette
    private final Map<Integer, Color> habitColorMap = new HashMap<>();
    private int nextRowColorIndex = 0;


    // ---------- COLOR PALETTE (DARK + VIVID) ----------
    private static final Color COLOR_BG = new Color(0x0F172A);               // dark navy background
    private static final Color COLOR_CARD = new Color(0x111827);             // card background
    private static final Color COLOR_TEXT_PRIMARY = new Color(0xF9FAFB);     // near white
    private static final Color COLOR_TEXT_SECONDARY = new Color(0x9CA3AF);   // muted gray

    private static final Color COLOR_TAB_ACTIVE = new Color(0x6366F1);       // indigo
    private static final Color COLOR_TAB_INACTIVE = new Color(0x1F2937);     // dark gray
    private static final Color COLOR_TAB_INACTIVE_TEXT = new Color(0xE5E7EB);

    // vivid row colors (looped)
    private static final Color[] ROW_COLORS = new Color[]{
            new Color(0x4F46E5), // indigo
            new Color(0xD1952E), // amber
            new Color(0x006644), // emerald
            new Color(0x3B82F6), // blue
            new Color(0xEC4899)  // pink
    };

    private static final Color COLOR_STAT_BG = new Color(0x16A34A);          // green chip
    private static final Color COLOR_STAT_TEXT = new Color(0xECFDF5);

    private static final Color COLOR_ADD_TASK = new Color(0xF97316);         // bright orange
    private static final Color COLOR_ADD_TASK_TEXT = Color.WHITE;

    private static final Color COLOR_CHECKED = new Color(0x22C55E);          // green for checked circle
    private static final Color COLOR_CHECKED_PRESSED = new Color(0x16A34A);

    // --------------------------------------------------

    public enum FrequencyTab {
        DAILY, WEEKLY, MONTHLY
    }

    private JFrame mainFrame;
    private JPanel mainPanel;

    private JToggleButton dailyTab;
    private JToggleButton weeklyTab;
    private JToggleButton monthlyTab;

    private JPanel tasksContainer;
    private JScrollPane tasksScrollPane;

    private JLabel dateDisplayLabel;
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refreshAll();
        System.out.println("Property change fired.");
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

        // root panel
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(24, 24, 24, 24));
        mainPanel.setBackground(COLOR_BG);
        mainFrame.setContentPane(mainPanel);

        // card
        JPanel card = new RoundedPanel(24);
        card.setBackground(COLOR_CARD);
        card.setLayout(new BorderLayout(20, 20));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(card, gbc);

        // header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(createFrequencySelector(), BorderLayout.WEST);
        header.add(createHeaderChips(), BorderLayout.EAST);
        card.add(header, BorderLayout.NORTH);

        // tasks area
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

        // bottom "Add task"
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        bottomPanel.setOpaque(false);

        addTaskButton = new PillButton("Add task");
        addTaskButton.setPreferredSize(new Dimension(420, 40));
        addTaskButton.setFocusPainted(false);
        addTaskButton.setBackground(COLOR_ADD_TASK);
        addTaskButton.setForeground(COLOR_ADD_TASK_TEXT);
        addTaskButton.addActionListener(e -> HomeViewController.showAddTaskWindow());

        bottomPanel.add(addTaskButton);
        card.add(bottomPanel, BorderLayout.SOUTH);

        // default tab & date
        dailyTab.setSelected(true);
        updateSegmentLook();
        updateDateLabel(FrequencyTab.DAILY);
        showTasks(homeViewModel != null ? homeViewModel.dailyHabits : null);
    }

    // ------------------- HEADER -------------------

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

        dateDisplayLabel = new JLabel();
        dateDisplayLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        dateDisplayLabel.setForeground(COLOR_TEXT_PRIMARY);
        panel.add(Box.createHorizontalStrut(16));
        panel.add(dateDisplayLabel);

        dailyTab.addActionListener(e -> {
            currentTab = FrequencyTab.DAILY;
            updateSegmentLook();
            updateDateLabel(FrequencyTab.DAILY);
            showTasks(homeViewModel.dailyHabits);
        });

        weeklyTab.addActionListener(e -> {
            currentTab = FrequencyTab.WEEKLY;
            updateSegmentLook();
            updateDateLabel(FrequencyTab.WEEKLY);
            showTasks(homeViewModel.weeklyHabits);
        });

        monthlyTab.addActionListener(e -> {
            currentTab = FrequencyTab.MONTHLY;
            updateSegmentLook();
            updateDateLabel(FrequencyTab.MONTHLY);
            showTasks(homeViewModel.monthlyHabits);
        });

        return panel;
    }

    private JPanel createHeaderChips() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panel.setOpaque(false);

        JButton statChip = createChipButton("Stat");
        statChip.addActionListener(e -> HomeViewController.showStatisticsWindow());
        panel.add(statChip);

        return panel;
    }

    private JToggleButton createSegmentButton(String text) {
        JToggleButton button = new JToggleButton(text);
        button.setFont(button.getFont().deriveFont(Font.PLAIN, 13f));
        button.setBorder(new RoundedBorder(10));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(true); // so background colour is visible
        button.setMargin(new Insets(6, 14, 6, 14));
        button.setBackground(COLOR_TAB_INACTIVE);
        button.setForeground(COLOR_TAB_INACTIVE_TEXT);
        return button;
    }

    private JButton createChipButton(String text) {
        JButton button = new JButton(text);
        button.setFont(button.getFont().deriveFont(Font.PLAIN, 12f));
        button.setBorder(new RoundedBorder(14));
        button.setFocusPainted(false);
        button.setMargin(new Insets(6, 16, 6, 16));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBackground(COLOR_STAT_BG);
        button.setForeground(COLOR_STAT_TEXT);
        return button;
    }

    private void updateSegmentLook() {
        updateSegmentLook(dailyTab);
        updateSegmentLook(weeklyTab);
        updateSegmentLook(monthlyTab);
    }

    private void updateSegmentLook(AbstractButton button) {
        if (button == null) return;

        if (button.isSelected()) {
            button.setBackground(COLOR_TAB_ACTIVE);
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(COLOR_TAB_INACTIVE);
            button.setForeground(COLOR_TAB_INACTIVE_TEXT);
        }
    }

    // ------------------- TASK LIST -------------------

    public void showTasks(List<Habit> habits) {
        tasksContainer.removeAll();

        List<Habit> safeList = habits != null ? habits : Collections.emptyList();

        int index = 0;
        for (Habit habit : safeList) {
            tasksContainer.add(createTaskRow(habit));
            index++;
        }

        int minRows = 10;
        for (int i = safeList.size(); i < minRows; i++) {
            tasksContainer.add(createEmptyTaskRow());
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
    /** Returns a stable color for this habit, so it keeps its color even if rows re-order. */
    private Color getColorForHabit(Habit habit) {
        if (habit == null) {
            // fallback neutral
            return new Color(0xF9FAFB);
        }

        Integer id = habit.id; // assuming habit.id is the same field you've been using

        // Defensive: if id somehow is 0 or not set, still make a stable key
        if (id == null) {
            id = System.identityHashCode(habit);
        }

        Color c = habitColorMap.get(id);
        if (c == null) {
            c = ROW_COLORS[nextRowColorIndex % ROW_COLORS.length];
            habitColorMap.put(id, c);
            nextRowColorIndex++;
        }
        return c;
    }


    private JPanel createTaskRow(Habit habit) {
        String habitLabel = habit.get_next().name;
        String desc = habit.get_next().desc;

        // Get a stable color for this specific habit
        Color rowColor = getColorForHabit(habit);

        // Container for entire row
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(6, 0, 6, 0));
        row.setMaximumSize(new Dimension(520, 72)); // fixed height, flexible width
        row.setPreferredSize(new Dimension(520, 72));

        // Text: Habit Name
        JLabel label = new JLabel(habitLabel);
        label.setBorder(new RoundedBorder(8));
        label.setBackground(rowColor); // <-- use habit color
        label.setOpaque(true);
        label.setPreferredSize(new Dimension(160, 42));
        label.setMaximumSize(label.getPreferredSize());
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(new Color(0x111827)); // dark text so it pops

        // Text: Description
        JLabel description = new JLabel(desc);
        description.setBorder(new RoundedBorder(8));
        description.setBackground(rowColor); // <-- same habit color
        description.setOpaque(true);
        description.setPreferredSize(new Dimension(300, 42));
        description.setMaximumSize(description.getPreferredSize());
        description.setHorizontalAlignment(SwingConstants.CENTER);
        description.setForeground(new Color(0x111827));


        JButton checklistButton = createChecklistButton(habit);
        CircleButton menuButton = new CircleButton("...");
        menuButton.setIdleColor(new Color(0x4B5563));       // dark gray circle
        menuButton.setPressedColor(new Color(0x374151));
        menuButton.setForeground(Color.WHITE);
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

    private JPanel createEmptyTaskRow() {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(6, 0, 6, 0));
        row.setMaximumSize(new Dimension(520, 72));
        row.setPreferredSize(new Dimension(520, 72));

        JLabel label = new JLabel("", SwingConstants.CENTER);
        label.setBorder(new RoundedBorder(10));
        label.setOpaque(true);
        label.setBackground(new Color(0x1F2937)); // subtle dark card
        label.setForeground(COLOR_TEXT_SECONDARY);
        label.setPreferredSize(new Dimension(160, 42));
        label.setMaximumSize(label.getPreferredSize());

        JLabel description = new JLabel("", SwingConstants.CENTER);
        description.setBorder(new RoundedBorder(10));
        description.setOpaque(true);
        description.setBackground(new Color(0x111827));
        description.setForeground(COLOR_TEXT_SECONDARY);
        description.setPreferredSize(new Dimension(300, 42));
        description.setMaximumSize(description.getPreferredSize());

        CircleButton checklistButton = new CircleButton("[ ]");
        checklistButton.setIdleColor(new Color(0x111827));
        checklistButton.setPressedColor(new Color(0x1F2937));
        checklistButton.setForeground(COLOR_TEXT_SECONDARY);

        CircleButton menuButton = new CircleButton("...");
        menuButton.setIdleColor(new Color(0x111827));
        menuButton.setPressedColor(new Color(0x1F2937));
        menuButton.setForeground(COLOR_TEXT_SECONDARY);

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

    // ------------------- CHECK CIRCLE -------------------

    private CircleButton createChecklistButton(Habit habit) {
        LocalDate now = LocalDate.now();
        Date today = new Date(now.getDayOfMonth(), now.getMonthValue() - 1, now.getYear());

        boolean isCompleted = habit.get_task_by_date(today).completed;
        String initialText = isCompleted ? "[✓]" : "[ ]";

        CircleButton checkButton = new CircleButton(initialText);
        checkButton.setFocusPainted(false);

        if (isCompleted) {
            // Green circle + white text
            checkButton.setIdleColor(COLOR_CHECKED);
            checkButton.setPressedColor(COLOR_CHECKED_PRESSED);
            checkButton.setForeground(Color.WHITE);
        } else {
            // White circle + dark text (so it's visible)
            checkButton.setIdleColor(Color.WHITE);
            checkButton.setPressedColor(new Color(0xE5E7EB));
            checkButton.setForeground(new Color(0x111827)); // dark navy/black-ish
        }

        checkButton.addActionListener(e -> {
            String currentText = checkButton.getText();
            if ("[ ]".equals(currentText)) {
                // switching to checked
                checkButton.setText("[✓]");
                checkButton.setIdleColor(COLOR_CHECKED);
                checkButton.setPressedColor(COLOR_CHECKED_PRESSED);
                checkButton.setForeground(Color.WHITE);
            } else {
                // switching back to unchecked
                checkButton.setText("[ ]");
                checkButton.setIdleColor(Color.WHITE);
                checkButton.setPressedColor(new Color(0xE5E7EB));
                checkButton.setForeground(new Color(0x111827)); // dark text on white
            }

            if (homeViewController != null) {
                homeViewController.onHabitCheckboxClicked(habit);
            }
        });

        return checkButton;
    }


    // ------------------- DATE LABEL -------------------

    private void updateDateLabel(FrequencyTab tab) {
        LocalDate today = LocalDate.now();
        String text = "";

        switch (tab) {
            case DAILY:
                String dayName = today.format(DateTimeFormatter.ofPattern("EEEE"));
                String month = today.format(DateTimeFormatter.ofPattern("MMMM"));
                int day = today.getDayOfMonth();
                int year = today.getYear();
                text = String.format("%s, %s %d%s %d", dayName, month, day, getDayNumberSuffix(day), year);
                break;

            case WEEKLY:
                LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                LocalDate endOfWeek = startOfWeek.plusDays(6);
                DateTimeFormatter shortFmt = DateTimeFormatter.ofPattern("MMM d");
                text = "Week of " + startOfWeek.format(shortFmt) + " - " + endOfWeek.format(shortFmt);
                break;

            case MONTHLY:
                text = today.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
                break;
        }

        if (dateDisplayLabel != null) {
            dateDisplayLabel.setText(text);
        }
    }

    private String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
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
