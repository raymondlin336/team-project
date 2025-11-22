package gui.statistics;

import entity.Freq;
import gui.home.HomeViewComponents;
import entity.Habit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StatisticsView {
    private String view_name;
    private StatisticsController statisticsController;
    private StatisticsViewModel statisticsViewModel;
    private JFrame mainframe;
    private JPanel mainpanel;

    public StatisticsView(String view_name, StatisticsViewModel statisticsViewModel, StatisticsController statisticsController) {
        this.view_name = view_name;
        this.statisticsViewModel = statisticsViewModel;
        this.statisticsController = statisticsController;
        createUI(800, 600);
    }

    private void createUI(int wd_width,int wd_height) {
        mainframe = new JFrame(view_name);
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.setSize(wd_width, wd_height);

        mainpanel = new JPanel();
        mainpanel.setLayout(new BoxLayout(mainpanel, BoxLayout.Y_AXIS));
        mainpanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        addButtonsRow();
        for (int i = 0; i < this.statisticsViewModel.numOfTasks(); i++) {
            Habit habit = this.statisticsViewModel.getTask(i);
            addStatsRow(habit.get_next().name, habit.get_completion_data(), habit.get_next().freq, habit.colour);
        }

        mainframe.add(mainpanel);
        mainframe.setLocationRelativeTo(null);
    }
    private void addButtonsRow(){
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rowPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        rowPanel.setPreferredSize(new Dimension(700, 70));
        rowPanel.setMaximumSize(new Dimension(700, 70));

        JButton homeButton = configureMenuButton("Home", 65, 40);
        JButton settingsButton = configureMenuButton("Settings", 80, 40);

        rowPanel.add(homeButton);
        rowPanel.add(settingsButton);

        mainpanel.add(rowPanel);
    }

    private JButton configureMenuButton(String text, int width, int height) {
        JButton button = new HomeViewComponents.PillButton(text);
        button.setPreferredSize(new Dimension(width, height));
        button.setBorderPainted(false);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (text.equals("Home")) {
                    statisticsController.goHome();
                }
                else {
                    statisticsController.goSettings();
                }
                mainframe.dispose();
            }
        });
        return button;
    }

    private void addStatsRow(String labelText, Boolean[] completion, Freq repeat, int colour) {
        JPanel rowPanel = configureStatsRow(labelText, completion, repeat, 700, 70, 40, 40, 5, colour);

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));
        wrapper.add(Box.createHorizontalGlue());
        wrapper.add(rowPanel);
        wrapper.add(Box.createHorizontalGlue());

        mainpanel.add(wrapper);
        mainpanel.add(Box.createVerticalStrut(10));
    }

    private JPanel configureStatsRow(String labelText, Boolean[] completed_array, Freq repeat, int row_width, int row_height, int block_width, int block_height, int gap, int colour) {
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        rowPanel.setPreferredSize(new Dimension(row_width, row_height));
        rowPanel.setMaximumSize(new Dimension(row_width, row_height));

        // Calculate completions
        int base = 1;
        if (repeat == Freq.Daily) {
            base = 7;
        }
        if (repeat == Freq.Monthly) {
            block_width = Math.round((float)((block_width + gap) * 4.35));
        }
        ArrayList<Double> completed_perc = new ArrayList<>();
        for (int i = 0; i < completed_array.length; i += base) {
            int completed = 0;
            for  (int j = i; j < Math.min(j + base, completed_array.length); j += 1) {
                completed += completed_array[j] ? 1 : 0;
            }
            System.out.println(completed);
            completed_perc.add((double)completed/base);
        }

        // Panel and texts to the left
        int num_completed = 0;
        for (Boolean c: completed_array) {
            if (c == true) {
                num_completed += 1;
            }
        }
        String per_text = num_completed + " " + repeat;
        if (num_completed > 1){
            per_text += "s";
        }
        JPanel leftLabelPanel = new JPanel();
        leftLabelPanel.setLayout(new BoxLayout(leftLabelPanel, BoxLayout.Y_AXIS));
        leftLabelPanel.setOpaque(false);
        JLabel mainLabel = new JLabel(labelText);
        mainLabel.setFont(mainLabel.getFont().deriveFont(Font.BOLD, 15f));
        JLabel subLabel = new JLabel(per_text);
        subLabel.setFont(subLabel.getFont().deriveFont(Font.PLAIN, 12f));
        subLabel.setForeground(new Color(90, 90, 90)); // soft grey
        mainLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftLabelPanel.add(mainLabel);
        leftLabelPanel.add(subLabel);
        leftLabelPanel.setPreferredSize(new Dimension(150, 50));
        leftLabelPanel.setMaximumSize(new Dimension(150, 60));
        rowPanel.add(leftLabelPanel, BorderLayout.WEST);

        // Scrollable right panel
        JPanel blocksPanel = new JPanel();
        blocksPanel.setLayout(new BoxLayout(blocksPanel, BoxLayout.X_AXIS));

        // Create graphics blocks
        SquareRowPanel block = new SquareRowPanel(completed_perc, block_width, block_height, gap, colour);
        blocksPanel.add(block);
        blocksPanel.add(Box.createRigidArea(new Dimension(4, 0)));

        // Create graphics scrolling
        JScrollPane scrollPane = new JScrollPane(blocksPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(550, 50));
        scrollPane.getHorizontalScrollBar().setUI(new ColoredScrollBarUI(Color.getHSBColor((float)colour/360, (float)80/100, (float)60/100), Color.getHSBColor((float)colour/360, (float)10/100, (float)95/100)));
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(550, 12));
        rowPanel.add(scrollPane, BorderLayout.CENTER);
        return rowPanel;
    }

    static class SquareRowPanel extends JPanel {
        private int square_width;
        private int square_height;
        private int gap;
        private int colour;
        private ArrayList<Double> fillPercentages;
        public SquareRowPanel(ArrayList<Double> fillPercentages,
                              int square_width,
                              int square_height,
                              int gap,
                              int colour) {
            this.fillPercentages = fillPercentages;
            this.square_width = square_width;
            this.square_height = square_height;
            this.gap = gap;
            this.colour = colour;
            setPreferredSize(new Dimension((square_width + gap) * fillPercentages.size(), square_height));
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int x = 0;
            for (double pct : fillPercentages) {
                // filled green
                g.setColor(Color.getHSBColor((float)colour/360, (float)80/100, (float)85/100));
                g.fillRect(x, 0, square_width, square_height);
                // unfilled red
                g.setColor(Color.getHSBColor((float)colour/360, (float)10/100, (float)95/100));
                int filled_height = (int)((square_height) * (1 - pct));
                g.fillRect(x, 0, square_width, filled_height);
                x += square_width + gap;
            }
        }
    }
    static class ColoredScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        private final Color thumbColor;
        private final Color trackColor;

        public ColoredScrollBarUI(Color thumbColor, Color trackColor) {
            this.thumbColor = thumbColor;
            this.trackColor = trackColor;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(thumbColor);
            g2.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);
            g2.dispose();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(trackColor);
            g2.fillRect(r.x, r.y, r.width, r.height);
            g2.dispose();
        }
    }
    public void setVisible(){
        mainframe.setVisible(true);
    }
    public JPanel getPanel(){
        return mainpanel;
    }
}
