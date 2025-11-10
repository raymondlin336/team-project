package gui;

public class IndividualTaskViewModel {
    private PlaceholderTask task;
    public IndividualTaskViewModel(PlaceholderTask task) {
        this.task = task;
    }
    public void updateTaskName(String name){
        task.updateName(name);
    }
    public String getTaskName(){
        return task.name;
    }
    public Boolean getRepeat(){
        return task.repeat;
    }
}
