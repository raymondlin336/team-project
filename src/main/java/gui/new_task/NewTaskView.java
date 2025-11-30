package gui.new_task;

import gui.home.HomeViewComponents;
import gui.task.TaskView;
import use_case.habit.create.CreateHabitOutputBoundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class NewTaskView extends TaskView implements PropertyChangeListener {
    private NewTaskController newTaskController;
    private NewTaskViewModel newTaskViewModel;
    private JButton add;

    public NewTaskView(NewTaskController newTaskController, NewTaskViewModel newTaskViewModel) {
        super("New Task", newTaskController);
        this.newTaskViewModel = newTaskViewModel;
        this.newTaskViewModel.addPropertyChangeListener(this);
        this.newTaskController = newTaskController;
        createUI(800, 600, view_name, false,"");
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("AddSuccess")){
            newTaskController.showHomeWindow();
        }
        if (evt.getPropertyName().equals("AddFailed")){
            createUI(800, 600, view_name, false, evt.getNewValue().toString());
            addAddButton();
            addCancelButton();
            newTaskController.refreshEditView(getPanel());
        }
    }
}
