package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {

    void newTask(Task task) ;

    void newEpic(Epic epic);

    void newSubTask(SubTask subTask);

    void deleteAllTasks();

    void deleteById(int id);

    void deleteSubTaskIdFromEpic(Epic epic, int id);

    void updateStatus(Object task);

    HashMap<Integer, Object> getAllTasks();

    Object getTaskById(int id);

    ArrayList<SubTask> getEpicsSubTasks(int epicsId);

    void addSubTaskToEpic(Epic epic, SubTask subTask);

    void checkEpicStatus(Epic epic);

    void checkEpicsDurationAndStartAndEndTime(int epicId);

    ArrayList<Task> getPrioritizedTasks();
}
