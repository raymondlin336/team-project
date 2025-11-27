package entity;

import entity.Date;
import entity.Freq;
import entity.Habit;
import entity.Task;
import gui.edit_task.EditTaskController;
import gui.edit_task.EditTaskView;
import gui.edit_task.EditTaskViewModel;
import gui.home.HomeView;
import gui.home.HomeViewController;
import gui.home.HomeViewModel;
import gui.new_task.NewTaskController;
import gui.new_task.NewTaskView;
import gui.statistics.StatisticsController;
import gui.statistics.StatisticsView;
import gui.statistics.StatisticsViewModel;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class EntityTests {

    @Test
    public void dateTest() {
        Date date = new Date(30, 11, 0);
        assertEquals(null, date.increase_date(Freq.Once));
        assertEquals(new Date(0, 0, 1), date.increase_date(Freq.Daily));
        assertEquals(new Date(6, 0, 1), date.increase_date(Freq.Weekly));
        assertEquals(new Date(30, 0, 1), date.increase_date(Freq.Monthly));
    }
}