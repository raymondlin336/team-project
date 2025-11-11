package gui.edit_task;

import gui.task.TaskView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditTaskView extends TaskView {
    private EditTaskViewModel editTaskViewModel;
    private EditTaskController editTaskController;
    private JFrame mainframe;
    private JPanel mainpanel;
    private JPanel habitNameRow;
    private JTextField habitNameTF;
    private JPanel habitDescRow;
    private JTextField habitDescTF;
    private JPanel habitRepeatRow;
    private JComboBox<String> habitRepeatCB;
    private JPanel habitDueRow;
    private JLabel habitDueLB;
    private JPanel buttonsRow;
    private JButton save;
    private JButton cancel;
    private JButton delete;

    public EditTaskView(EditTaskViewModel editTaskViewModel, EditTaskController editTaskController) {
        this.editTaskViewModel = editTaskViewModel;
        this.editTaskController = editTaskController;
        createUI(800, 600, "New Habit");
    }
    private void createUI(int wd_width,int wd_height, String window_name) {
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
        habitRepeatCB = new JComboBox<>(new String[]{"once","every day", "every week", "every month"});
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
        save = getSizedButton(100, 36, "Save", new Color(67, 182, 240));
        cancel = getSizedButton(100, 36, "Cancel", new Color(255, 255, 255));
        delete = getSizedButton(100, 36, "Delete", new Color(240, 102, 67));
        buttonsRow.add(save);
        buttonsRow.add(cancel);
        buttonsRow.add(delete);
        gbc.gridy = 4;
        mainpanel.add(buttonsRow, gbc);

        wrapper.add(mainpanel, new GridBagConstraints());
        mainframe.setLocationRelativeTo(null);
        addActionListeners();
        showHabitInfo();
    }

    private static JTextField getSizedTextField(int w, int h, Color colour) {
        JTextField tf = new JTextField();
        tf.setBackground(colour);
        setSize(tf, w, h);
        return tf;
    }

    private static JButton getSizedButton(int w, int h, String text, Color colour) {
        JButton b = new JButton(text);
        setSize(b, w, h);
        b.setBackground(colour);
        return b;
    }

    private static void setSize(JComponent c, int w, int h) {
        Dimension d = new Dimension(w, h);
        c.setPreferredSize(d);
        c.setMinimumSize(d);
        c.setMaximumSize(d);
    }

    private static JPanel labeledRowJPanel(String label, JComponent field) {
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

    private void addActionListeners(){
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int habit_id = 1;
                String habit_name = habitNameTF.getText();
                String habit_desc = habitDescTF.getText();
                String habit_repeat = habitRepeatCB.getSelectedItem().toString();
                editTaskController.save_habit_info(habit_id, habit_name, habit_desc, habit_repeat);
            }
        });
        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int habit_id = 1;
                editTaskController.delete_habit(habit_id);
                mainframe.dispose();
            }
        });
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editTaskController.cancel();
                mainframe.dispose();
            }
        });
    }

    private void showHabitInfo(){
        habitNameTF.setText(editTaskViewModel.getName());
        habitDescTF.setText(editTaskViewModel.getDesc());
        habitRepeatCB.setSelectedItem(editTaskViewModel.getRepeat());
        habitDueLB.setText(editTaskViewModel.getDueDate());
    }

    public void setVisible(){
        mainframe.setVisible(true);
    }
}