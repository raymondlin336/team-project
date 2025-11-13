package gui.statistics;

import placeholders.PlaceholderTask;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StatisticsView {
    private String view_name;
    private StatisticsViewModel statisticsViewModel;
    private JFrame mainframe;
    private JPanel mainpanel;

    public StatisticsView(String view_name, StatisticsViewModel statisticsViewModel) {
        this.view_name = view_name;
        this.statisticsViewModel = statisticsViewModel;
        createUI(800, 600);
    }

    private void createUI(int wd_width,int wd_height) {
        mainframe = new JFrame(view_name);
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.setSize(wd_width, wd_height);

        mainpanel = new JPanel();
        mainpanel.setLayout(new BoxLayout(mainpanel, BoxLayout.Y_AXIS));
        mainpanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (int i = 0; i < this.statisticsViewModel.numOfTasks(); i++) {
            PlaceholderTask task = this.statisticsViewModel.getTask(i);
            addRow(task.name, task.completion, task.repeat, task.colour);
        }

        mainframe.add(mainpanel);
        mainframe.setLocationRelativeTo(null);
    }

    private void addRow(String labelText, ArrayList<Integer> completion, String repeat, int colour) {
        JPanel rowPanel = addHabitRow(labelText, completion, repeat, 700, 70, 40, 40, 5, colour);

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));
        wrapper.add(Box.createHorizontalGlue());
        wrapper.add(rowPanel);
        wrapper.add(Box.createHorizontalGlue());

        mainpanel.add(wrapper);
        mainpanel.add(Box.createVerticalStrut(10));
    }

    private JPanel addHabitRow(String labelText, ArrayList<Integer> completed_array, String repeat, int row_width, int row_height, int block_width, int block_height, int gap, int colour) {
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        rowPanel.setPreferredSize(new Dimension(row_width, row_height));
        rowPanel.setMaximumSize(new Dimension(row_width, row_height));
        // Left side label
        JLabel label = new JLabel(labelText);
        label.setFont(label.getFont().deriveFont(Font.PLAIN, 16f));
        label.setPreferredSize(new Dimension(120, 40));
        rowPanel.add(label, BorderLayout.WEST);
        // Scrollable right panel
        JPanel blocksPanel = new JPanel();
        blocksPanel.setLayout(new BoxLayout(blocksPanel, BoxLayout.X_AXIS));
        // Create the scrollable squares
        int base = 1;
        if (repeat.equals("every day")) {
            base = 7;
        }
        if (repeat.equals("every month")) {
            block_width = Math.round((float)((block_width + gap) * 4.35));
        }
        ArrayList<Double> completed_perc = new ArrayList<>();
        for (int completed: completed_array) {
            completed_perc.add((double)completed/base);
        }
        System.out.println(completed_perc);
        SquareRowPanel block = new SquareRowPanel(completed_perc, block_width, block_height, gap, colour);
        blocksPanel.add(block);
        blocksPanel.add(Box.createRigidArea(new Dimension(4, 0)));

        JScrollPane scrollPane = new JScrollPane(blocksPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(550, 50));
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
                // outline
//                g.setColor(Color.BLACK);
//                g.drawRect(x, 0, square_width, square_height);

                x += square_width + gap;
            }
        }
    }
    public void setVisible(){
        mainframe.setVisible(true);
    }
}
