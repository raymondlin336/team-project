package gui.statistics;

import entity.Freq;
import entity.Habit;
import java.util.ArrayList;

public class StatisticsViewModel {
    private ArrayList<Habit> habits = new ArrayList<Habit>();
    public StatisticsViewModel(ArrayList<Habit> habits) {
        for (Habit habit : habits) {
            if (!(habit.get_next().freq == Freq.Once)) {
                this.habits.add(habit);
            }
        }
    }
    public int numOfTasks(){
        return this.habits.size();
    }
    public Habit getTask(int i){
        return this.habits.get(i);
    }
}
