package gui.statistics;

import placeholders.PlaceholderTask;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class StatisticsViewModel {
    private ArrayList<PlaceholderTask> tasks = new ArrayList<PlaceholderTask>();
    public StatisticsViewModel(ArrayList<PlaceholderTask> tasks) {
        for (PlaceholderTask task : tasks) {
            if (!task.repeat.equals("once")) {
                this.tasks.add(task);
            }
        }
    }
    public int numOfTasks(){
        return this.tasks.size();
    }
    public PlaceholderTask getTask(int i){
        return this.tasks.get(i);
    }
}
