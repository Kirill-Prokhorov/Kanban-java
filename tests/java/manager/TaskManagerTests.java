package manager;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.InMemoryHistoryManager;
import service.NextId;
import service.TaskManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import static model.Status.*;
import static org.junit.jupiter.api.Assertions.*;


public abstract class TaskManagerTests<T extends TaskManager> {

    protected T taskManager;
    protected Task task;
    protected Epic epic;
    protected SubTask subTask;
    protected SubTask subTask1;

    protected void taskManagerSetUp() {

        NextId nextId = new NextId();
        epic = new Epic(nextId.getId(), "Epic1", "Any", NEW);
        subTask = new SubTask(nextId.getId(),"Test", "Test description", NEW, epic.getId(),
                15, LocalDateTime.now());
        subTask1 = new SubTask(nextId.getId(),"Test", "Test description", NEW, epic.getId(),
                15, LocalDateTime.now());
        task = new Task(nextId.getId(),"Test addNewTask", "Test addNewTask description", NEW,
                15, LocalDateTime.now());

    }

    @AfterEach
    void tearDown() {

        HistoryManager historyManager = new InMemoryHistoryManager();
        historyManager.clearHistory();

    }

    @Test
    void newTaskTest() {

        taskManager.newTask(task);
        final Task savedTask = (Task) taskManager.getTaskById(task.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final HashMap<Integer, Object> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(task.getId()), "Задачи не совпадают.");

    }

    @Test
    void newEpicTest() {

        taskManager.newEpic(epic);
        final Epic savedTask = (Epic) taskManager.getTaskById(epic.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(epic, savedTask, "Задачи не совпадают.");

        final HashMap<Integer, Object> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(epic, tasks.get(epic.getId()), "Задачи не совпадают.");

    }

    @Test
    void newSubTaskTest() {

        taskManager.newSubTask(subTask);
        final SubTask savedTask = (SubTask) taskManager.getTaskById(subTask.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(subTask, savedTask, "Задачи не совпадают.");

        final HashMap<Integer, Object> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(subTask, tasks.get(subTask.getId()), "Задачи не совпадают.");
    }

    @Test
    void deleteAllTasksTest() {

        taskManager.newTask(task);
        taskManager.newEpic(epic);
        taskManager.newSubTask(subTask);
        final HashMap<Integer, Object> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(3, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(task.getId()), "Задачи не совпадают.");
        assertEquals(epic, tasks.get(epic.getId()), "Задачи не совпадают.");
        assertEquals(subTask, tasks.get(subTask.getId()), "Задачи не совпадают.");

        taskManager.deleteAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertTrue(tasks.isEmpty(), "Список задач не пустой");

    }

    @Test
    void deleteByIdTest() {

        taskManager.newTask(task);
        final Task savedTask = (Task) taskManager.getTaskById(task.getId());
        final HashMap<Integer, Object> tasks = taskManager.getAllTasks();
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");

        taskManager.deleteById(task.getId());

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertTrue(tasks.isEmpty(), "Список задач не пустой");

    }

    @Test
    void deleteByWrongIdTest() {

        taskManager.newTask(task);
        final Task savedTask = (Task) taskManager.getTaskById(task.getId());
        final HashMap<Integer, Object> tasks = taskManager.getAllTasks();
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");

        taskManager.deleteById(0);

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");

    }

    @Test
    void deleteSubTaskIdFromEpicTest() {

        taskManager.newSubTask(subTask);
        int subTaskId = subTask.getId();
        taskManager.newEpic(epic);
        taskManager.addSubTaskToEpic(epic, subTask);
        ArrayList<Integer> subTaskFromEpicId = epic.getSubTasksId();
        ArrayList<SubTask> epicsSubTasks = taskManager.getEpicsSubTasks(epic.getId());
        assertEquals(subTaskId, subTaskFromEpicId.get(0), "ID подзадач не совпадают");
        assertNotNull(epicsSubTasks, "Задачи не возвращаются");
        assertEquals(1,epicsSubTasks.size(), "Не верное количество подзадач");
        assertEquals(subTask, epicsSubTasks.get(0), "Задачи не совпадают");
        taskManager.deleteSubTaskIdFromEpic(epic, subTaskId);
        epicsSubTasks = taskManager.getEpicsSubTasks(epic.getId());
        assertNotNull(epicsSubTasks, "Задачи не возвращаются");
        assertTrue(epicsSubTasks.isEmpty(), "Не верное количество подзадач");

    }

    @Test
    void deleteSubTaskWrongIdFromEpicTest() {

        taskManager.newSubTask(subTask);
        int subTaskId = subTask.getId();
        taskManager.newEpic(epic);
        taskManager.addSubTaskToEpic(epic, subTask);
        ArrayList<Integer> subTaskFromEpicId = epic.getSubTasksId();
        ArrayList<SubTask> epicsSubTasks = taskManager.getEpicsSubTasks(epic.getId());
        assertEquals(subTaskId, subTaskFromEpicId.get(0), "ID подзадач не совпадают");
        assertNotNull(epicsSubTasks, "Задачи не возвращаются");
        assertEquals(1,epicsSubTasks.size(), "Не верное количество подзадач");
        assertEquals(subTask, epicsSubTasks.get(0), "Задачи не совпадают");
        taskManager.deleteSubTaskIdFromEpic(epic, 0);
        epicsSubTasks = taskManager.getEpicsSubTasks(epic.getId());
        assertEquals(subTaskId, subTaskFromEpicId.get(0), "ID подзадач не совпадают");
        assertNotNull(epicsSubTasks, "Задачи не возвращаются");
        assertEquals(1,epicsSubTasks.size(), "Не верное количество подзадач");
        assertEquals(subTask, epicsSubTasks.get(0), "Задачи не совпадают");

    }

    @Test
    void updateStatusTest() {

        taskManager.newEpic(epic);
        taskManager.newSubTask(subTask);
        taskManager.addSubTaskToEpic(epic, subTask);
        taskManager.checkEpicStatus((Epic) taskManager.getTaskById(epic.getId()));
        assertEquals(NEW, epic.getStatus(), "Статусы не совпадают");
        subTask.setStatus(DONE);
        taskManager.checkEpicStatus((Epic) taskManager.getTaskById(epic.getId()));
        assertEquals(DONE, epic.getStatus(), "Статусы не совпадают");

    }

    @Test
    void getAllTasksTest() {

        taskManager.newTask(task);
        taskManager.newEpic(epic);
        taskManager.newSubTask(subTask);
        HashMap<Integer,Object> tasks = taskManager.getAllTasks();
        assertNotNull(tasks, "Нет списка");
        assertEquals(3, tasks.size(), "Не верное количество задач");
        assertEquals(epic, tasks.get(1), "Не верная задача");
        assertEquals(subTask, tasks.get(2), "Не верная задача");
        assertEquals(task, tasks.get(4), "Не верная задача");
    }

    @Test
    void getAllTasksFromEmptyListTest() {

        HashMap<Integer,Object> tasks = taskManager.getAllTasks();
        assertNotNull(tasks, "Нет списка");
        assertTrue(tasks.isEmpty(), "Список не пустой");

    }

    @Test
    void getTaskByIdTest() {

        taskManager.newTask(task);
        HashMap<Integer,Object> tasks = taskManager.getAllTasks();
        Task tasksById = (Task)taskManager.getTaskById(task.getId());
        assertEquals(1, tasks.size(), "Не верное количество задач");
        assertNotNull(tasksById, "Задача не найдена");
        assertEquals(task, tasksById, "Задачи не совпадают");
    }

    @Test
    void getTaskByWrongIdTest() {

        taskManager.newTask(task);
        HashMap<Integer,Object> tasks = taskManager.getAllTasks();
        Task tasksById = (Task)taskManager.getTaskById(0);
        assertEquals(1, tasks.size(), "Не верное количество задач");
        assertNull(tasksById, "Задача найдена");

    }

    @Test
    void getTaskByIdFromEmptyListTest() {

        HashMap<Integer,Object> tasks = taskManager.getAllTasks();
        Task tasksById = (Task)taskManager.getTaskById(0);
        assertEquals(0, tasks.size(), "Не верное количество задач");
        assertNull(tasksById, "Задача найдена");

    }

    @Test
    void getEpicsSubTasksTest() {

        taskManager.newSubTask(subTask);
        int subTaskId = subTask.getId();
        taskManager.newEpic(epic);
        taskManager.addSubTaskToEpic(epic, subTask);
        ArrayList<Integer> subTaskFromEpicId = epic.getSubTasksId();
        ArrayList<SubTask> epicsSubTasks = taskManager.getEpicsSubTasks(epic.getId());
        assertNotNull(epicsSubTasks, "Задачи не возвращаются");
        assertEquals(subTaskId, subTaskFromEpicId.get(0), "ID подзадач не совпадают");
        assertEquals(1,epicsSubTasks.size(), "Не верное количество подзадач");
        assertEquals(subTask, epicsSubTasks.get(0), "Задачи не совпадают");

    }

    @Test
    void getEpicsSubTasksEmptyListTest(){

        ArrayList<SubTask> epicsSubTasks = taskManager.getEpicsSubTasks(epic.getId());
        assertNotNull(epicsSubTasks, "Задачи не возвращаются");
        assertTrue(epicsSubTasks.isEmpty(), "Не верное количество подзадач");

    }

    @Test
    void getEpicsSubTasksWithWrongEpicsIdTest(){

        taskManager.newSubTask(subTask);
        taskManager.newEpic(epic);
        taskManager.addSubTaskToEpic(epic, subTask);
        ArrayList<SubTask> epicsSubTasks = taskManager.getEpicsSubTasks(0);
        assertNotNull(epicsSubTasks, "Задачи возвращаются");
        assertEquals(0, epicsSubTasks.size());
        assertEquals(2, taskManager.getAllTasks().size(), "Не верное количество задач");

    }

    @Test
    void addSubTaskToEpicTest() {

        taskManager.newSubTask(subTask);
        int subTaskId = subTask.getId();
        taskManager.newEpic(epic);
        taskManager.addSubTaskToEpic(epic, subTask);
        ArrayList<Integer> subTaskFromEpicId = epic.getSubTasksId();
        ArrayList<SubTask> epicsSubTasks = taskManager.getEpicsSubTasks(epic.getId());
        assertNotNull(epicsSubTasks, "Задачи не возвращаются");
        assertEquals(epicsSubTasks.get(0).getParentId(), epic.getId(), "ID эпика и родительский ID подзадачи" +
                "не совпадают");
        assertEquals(subTaskId, subTaskFromEpicId.get(0), "ID подзадач не совпадают");
        assertEquals(1,epicsSubTasks.size(), "Не верное количество подзадач");
        assertEquals(subTask, epicsSubTasks.get(0), "Задачи не совпадают");

    }

    @Test
    void checkEpicStatusTest() {

        taskManager.newEpic(epic);
        taskManager.newSubTask(subTask1);
        taskManager.newSubTask(subTask);
        taskManager.addSubTaskToEpic(epic, subTask);
        taskManager.addSubTaskToEpic(epic, subTask1);
        taskManager.checkEpicStatus((Epic) taskManager.getTaskById(epic.getId()));
        assertEquals(NEW, epic.getStatus(), "Статусы не совпадают");
        subTask.setStatus(DONE);
        taskManager.checkEpicStatus((Epic) taskManager.getTaskById(epic.getId()));
        assertEquals(IN_PROGRESS, epic.getStatus(), "Статусы не совпадают");

    }

    @Test
    void allSubtaskHaveNewStatusTest() {

        taskManager.newEpic(epic);
        taskManager.newSubTask(subTask1);
        taskManager.newSubTask(subTask);
        taskManager.addSubTaskToEpic(epic, subTask);
        taskManager.addSubTaskToEpic(epic, subTask1);

        taskManager.checkEpicStatus((Epic) taskManager.getTaskById(epic.getId()));
        assertEquals(NEW, epic.getStatus(), "Статусы не совпадают");
    }

    @Test
    void allSubtaskHaveDoneStatusTest() {

        taskManager.newEpic(epic);
        taskManager.newSubTask(subTask1);
        taskManager.newSubTask(subTask);
        taskManager.addSubTaskToEpic(epic, subTask);
        taskManager.addSubTaskToEpic(epic, subTask1);
        subTask.setStatus(DONE);
        subTask1.setStatus(DONE);
        taskManager.checkEpicStatus((Epic) taskManager.getTaskById(epic.getId()));
        assertEquals(DONE, epic.getStatus(), "Статусы не совпадают");
    }

    @Test
    void allSubtaskHaveNewAndDoneStatusTest() {

        taskManager.newEpic(epic);
        taskManager.newSubTask(subTask1);
        taskManager.newSubTask(subTask);
        taskManager.addSubTaskToEpic(epic, subTask);
        taskManager.addSubTaskToEpic(epic, subTask1);
        subTask1.setStatus(NEW);
        subTask.setStatus(DONE);
        taskManager.checkEpicStatus((Epic) taskManager.getTaskById(epic.getId()));
        assertEquals(IN_PROGRESS, epic.getStatus(), "Статусы не совпадают");
    }

    @Test
    void allSubtaskHaveIn_progressStatusTest() {

        taskManager.newEpic(epic);
        taskManager.newSubTask(subTask1);
        taskManager.newSubTask(subTask);
        taskManager.addSubTaskToEpic(epic, subTask);
        taskManager.addSubTaskToEpic(epic, subTask1);
        subTask.setStatus(IN_PROGRESS);
        subTask1.setStatus(IN_PROGRESS);
        taskManager.checkEpicStatus((Epic) taskManager.getTaskById(epic.getId()));
        assertEquals(IN_PROGRESS, epic.getStatus(), "Статусы не совпадают");
    }

    @Test
    void getAndSetDuration() {

        long saveDuration = task.getDuration();
        task.setDuration(10L);
        assertNotEquals(task.getDuration(), saveDuration, "Продолжительность не различается");
        assertEquals(10L, task.getDuration(), "Продолжительность не совпадает с указанной");
    }

    @Test
    void getAndSetStartTime() {

        LocalDateTime saveStartTime = task.getStartTime();
        task.setStartTime(saveStartTime.plusMinutes(10));
        assertNotEquals(task.getStartTime(), saveStartTime, "Время не различается");
        assertEquals(task.getStartTime(), saveStartTime.plusMinutes(10), "Время не совпадает");

    }

    @Test
    void getEndTime() {

        LocalDateTime savedStart = task.getStartTime();
        assertEquals(savedStart.plusMinutes(15), task.getEndTime(),"Время окончания не верное");

    }

    @Test
    void epicsDuration() {

        taskManager.newEpic(epic);
        taskManager.newSubTask(subTask);
        taskManager.newSubTask(subTask1);
        taskManager.addSubTaskToEpic(epic, subTask);
        taskManager.addSubTaskToEpic(epic, subTask1);
        taskManager.checkEpicsDurationAndStartAndEndTime(epic.getId());
        assertEquals(epic.getStartTime(), subTask.getStartTime(), "Время старта не верное");
        assertEquals(epic.getDuration(), 30, "Продолжительность не верная");
        assertEquals(epic.getEndTime(), subTask1.getEndTime(), "Время завершения не верное");

    }

}