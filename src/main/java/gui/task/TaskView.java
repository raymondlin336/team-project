package gui.task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TaskView {
    protected String view_name;
    protected TaskController taskController;
    protected JFrame mainframe;
    protected JPanel mainpanel;
    protected JPanel habitNameRow;
    protected JTextField habitNameTF;
    protected JPanel habitDescRow;
    protected JTextField habitDescTF;
    protected JPanel habitRepeatRow;
    protected JComboBox<String> habitRepeatCB;
    protected JPanel habitDueRow;
    protected JLabel habitDueLB;
    protected JPanel buttonsRow;
    protected JButton cancel;

    protected TaskView(String view_name, TaskController taskController) {
        this.view_name = view_name;
        this.taskController = taskController;
    }

    protected void addCancelButton(){
        cancel = getSizedButton(100, 36, "Cancel", new Color(255, 255, 255));
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                taskController.cancel();
                mainframe.dispose();
            }
        });
        buttonsRow.add(cancel);
    }

    protected void createUI(int wd_width,int wd_height, String window_name) {
        mainframe = new JFrame(window_name);
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.setSize(wd_width, wd_height);

        // Outer wrapper centers everything
        JPanel wrapper = new JPanel(new GridBagLayout());
        mainframe.setContentPane(wrapper);

        // Fixed-size mainpanel panel
        mainpanel = new JPanel(new GridBagLayout());
        Dimension mainSize = new Dimension(500, 300);
        mainpanel.setPreferredSize(mainSize);
        mainpanel.setMinimumSize(mainSize);
        mainpanel.setMaximumSize(mainSize);
        mainpanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(8, 0, 8, 0);

        // Row 1: habit name
        habitNameTF = getSizedTextField(320, 36, new Color(255, 255, 255));
        habitNameRow = labeledRowJPanel("Habit", habitNameTF);
        gbc.gridy = 0;
        mainpanel.add(habitNameRow, gbc);

        // Row 2: habit name
        habitDescTF = getSizedTextField(320, 36, new Color(255, 255, 255));
        habitDescRow = labeledRowJPanel("Description", habitDescTF);
        gbc.gridy = 1;
        mainpanel.add(habitDescRow, gbc);

        // Row 3: frequency
        habitRepeatCB = new JComboBox<>(new String[]{"Once","Every day", "Every week", "Every month"});
        setSize(habitRepeatCB, 320, 36);
        habitRepeatCB.setBackground(new Color(255, 255, 255));
        habitRepeatRow = labeledRowJPanel("Frequency", habitRepeatCB);
        gbc.gridy = 2;
        mainpanel.add(habitRepeatRow, gbc);

        // Row 4: add description of due date
        habitDueLB = new JLabel();
        habitDueLB.setVerticalAlignment(SwingConstants.CENTER);
        habitDueLB.setPreferredSize(new Dimension(320, 36));
        habitDueRow = labeledRowJPanel("Due", habitDueLB);
        gbc.gridy = 3;
        mainpanel.add(habitDueRow, gbc);

        // Row 5: save, cancel and delete buttons
        buttonsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        gbc.gridy = 4;
        mainpanel.add(buttonsRow, gbc);

        wrapper.add(mainpanel, new GridBagConstraints());
        mainframe.setLocationRelativeTo(null);
    }

    protected static JTextField getSizedTextField(int w, int h, Color colour) {
        JTextField tf = new JTextField();
        tf.setBackground(colour);
        setSize(tf, w, h);
        return tf;
    }

    protected static JButton getSizedButton(int w, int h, String text, Color colour) {
        JButton b = new JButton(text);
        setSize(b, w, h);
        b.setBackground(colour);
        return b;
    }

    protected static void setSize(JComponent c, int w, int h) {
        Dimension d = new Dimension(w, h);
        c.setPreferredSize(d);
        c.setMinimumSize(d);
        c.setMaximumSize(d);
    }

    protected static JPanel labeledRowJPanel(String label, JComponent field) {
        JPanel row = new JPanel(new GridBagLayout());
        setSize(row, 460, 48);

        JLabel l = new JLabel(label);
        setSize(l, 80, 24);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(0, 0, 0, 12);
        g.anchor = GridBagConstraints.WEST;

        // label
        g.gridx = 0;
        row.add(l, g);

        // field aligned
        g.gridx = 1;
        row.add(field, g);

        return row;
    }
    public void setVisible(){
        mainframe.setVisible(true);
    }
}
