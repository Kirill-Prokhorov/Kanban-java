package manager.file;

import manager.TaskManagerTests;
import model.Epic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FileBackedTasksManager;
import java.io.File;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class FileBackedTaskManagerTests extends TaskManagerTests<FileBackedTasksManager> {

    File file;

    @BeforeEach
    void setUp() {

        file = new File("src/resource/file.csv");
        taskManager = new FileBackedTasksManager(file.getPath());
        taskManagerSetUp();

    }

    @AfterEach
    void tearDown() {

        taskManager.deleteAllTasks();
    }

    @Test
    void loadFromFile() throws Exception {

        taskManager.newTask(task);
        taskManager.getTaskById(task.getId());
        FileBackedTasksManager tasksManager = FileBackedTasksManager.loadFromFile(file.getPath());
        HashMap<Integer, Object> tasks = tasksManager.getAllTasks();
        assertNotNull(tasks, "Список задач не возвращается");
        assertEquals(1, tasks.size(), "Не верное количество задач");
        assertEquals(task, tasks.get(task.getId()), "Задачи не совпадают");

    }

    @Test
    void loadFromFileWithEmptyList() throws Exception {

        FileBackedTasksManager tasksManager = FileBackedTasksManager.loadFromFile(file.getPath());
        HashMap<Integer, Object> tasks = tasksManager.getAllTasks();
        assertNotNull(tasks, "Список задач не возвращается");
        assertEquals(0, tasks.size(), "Не верное количество задач");
        assertTrue(tasks.isEmpty(), "Список не пустой");

    }

    @Test
    void loadFromFileWithEpicWithOutSubtasks() throws Exception {

        taskManager.newEpic(epic);
        taskManager.getTaskById(epic.getId());
        FileBackedTasksManager tasksManager = FileBackedTasksManager.loadFromFile(file.getPath());
        HashMap<Integer, Object> tasks = tasksManager.getAllTasks();
        assertNotNull(tasks, "Список задач не возвращается");
        assertEquals(1, tasks.size(), "Не верное количество задач");
        assertEquals(epic, tasks.get(epic.getId()), "Задачи не совпадают");
        List<Integer> epicsSubTasks = ((Epic)tasks.get(epic.getId())).getSubTasksId();
        assertNotNull(epicsSubTasks, "Список подзадач не возвращается");
        assertEquals(0, epicsSubTasks.size(), "Не верное количество задач");
        assertTrue(epicsSubTasks.isEmpty(), "Список не пустой");

    }
}
