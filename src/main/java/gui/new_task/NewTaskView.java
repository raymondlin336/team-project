package gui.new_task;

import gui.home.HomeViewComponents;
import gui.task.TaskView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewTaskView extends TaskView {
    private NewTaskController newTaskController;
    private JButton add;

    public NewTaskView(NewTaskController newTaskController) {
        super("New Task", newTaskController);
        this.newTaskController = newTaskController;
        createUI(800, 600, view_name);
        addAddButton();
        addCancelButton();
    }

    private void addAddButton() {
        add = new HomeViewComponents.PillButton("Add");
        add.setBorderPainted(false);
        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String habit_name = habitNameTF.getText();
                String habit_desc = habitDescTF.getText();
                String habit_repeat = habitRepeatCB.getSelectedItem().toString();
                newTaskController.add_habit(habit_name, habit_desc, habit_repeat);
                mainframe.dispose();
            }
        });
        buttonsRow.add(add);
    }

}
