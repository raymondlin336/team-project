package gui.home;

import entity.Freq;
import gui.ScreenManager;
import gui.task.TaskController;
import entity.Habit;

import javax.script.ScriptEngine;

public class HomeViewController extends TaskController {
    private ScreenManager screenManager;
    public HomeViewController(Boolean log_messages) {
        super(log_messages);
    }
    public void markTaskDone(Boolean task_done, Habit habit) {
        /// Mark task done for the day/week/month
        if (habit.get_next().freq == Freq.Daily) {
            /// Mark
        } else if (habit.get_next().freq == Freq.Weekly) {
            /// Mark done
        } else if (habit.get_next().freq == Freq.Monthly) {
            /// Mark done
        }

    }

    public void markTaskNotDone(Boolean task_done, Habit habit) {
        /// Mark task not done for the day/week/month
        if (habit.get_next().freq == Freq.Daily) {
            /// Mark
        } else if (habit.get_next().freq == Freq.Weekly) {
            /// Mark done
        } else if (habit.get_next().freq == Freq.Monthly) {
            /// Mark done
        }

    }


    public void addScreenManager(ScreenManager screenManager){
        this.screenManager = screenManager;
    }

    public void showEditTaskWindow(Habit habit){
        screenManager.showEditTaskView(habit.id); // Needs to be implemented somewhere!
    }

    public void showStatisticsWindow(){
        screenManager.showStatisticsView(); // Needs to be implemented somewhere!
    }

    public void showAddTaskWindow(){
        screenManager.showAddTaskView();
    }
}
