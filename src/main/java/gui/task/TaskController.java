package gui.task;

public class TaskController {
    protected Boolean log_messages;
    public TaskController(Boolean log_messages) {
        this.log_messages = log_messages;
    }
    protected void print_log_message(String action){
        if (log_messages) {
            System.out.println("Controlled called. Action: " + action);
        }
    }
    public void cancel(){
        print_log_message("cancelling, closing window.");
    }
}
