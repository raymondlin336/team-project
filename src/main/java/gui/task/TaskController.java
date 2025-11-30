package gui.task;

import gui.ScreenManager;

import javax.swing.*;

public class TaskController {
    protected Boolean log_messages;
    protected ScreenManager screenManager;
    public TaskController(Boolean log_messages) {
        this.log_messages = log_messages;
    }
    protected void print_log_message(String action){
        if (log_messages) {
            System.out.println("Controller called. Action: " + action);
        }
    }
    public void cancel(){
        print_log_message("cancelling, closing window.");
    }
    public void addScreenManager(ScreenManager screenManager){
        this.screenManager = screenManager;
    }
    public void refreshEditView(JPanel jpanel){
        screenManager.refreshEditView(jpanel);
    }
    public void showHomeWindow(){
        screenManager.showHomeView();
    }
}
