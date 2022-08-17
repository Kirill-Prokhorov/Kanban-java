package manager;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.InMemoryHistoryManager;
import service.NextId;

import java.util.List;

import static model.Status.*;
import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTests {


    HistoryManager historyManager;
    private Task task;
    private Epic epic;
    private SubTask subTask;

    @BeforeEach
    void setUp() {

        historyManager = new InMemoryHistoryManager();
        NextId nextId = new NextId();
        task = new Task(nextId.getId(),"Task", "History Test", NEW);
        epic = new Epic(nextId.getId(), "Epic", "History Tes", NEW);
        subTask = new SubTask(nextId.getId(),"SubTask", "History Tes", NEW, epic.getId());

    }

    @AfterEach
    void tierDown() {

        historyManager.clearHistory();

    }

    @Test
    void addTest() {

        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История пустая.");
        assertEquals(1, history.size(), "Не верное количество элементов в истории");

    }

    @Test
    void removeFirstTest() {

        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Нет списка");
        assertEquals(3, history.size(), "Не верное количество задач");
        historyManager.remove(task.getId());
        history = historyManager.getHistory();
        assertNotNull(history, "Нет списка");
        assertEquals(2, history.size(), "Не верное количество задач после удаления одной");
        assertEquals(epic, history.get(0), "Не верный порядок после удаления");
        assertEquals(subTask, history.get(1), "Не верный порядок после удаления");

    }

    @Test
    void removeMiddleTest() {

        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Нет списка");
        assertEquals(3, history.size(), "Не верное количество задач");
        historyManager.remove(epic.getId());
        history = historyManager.getHistory();
        assertNotNull(history, "Нет списка");
        assertEquals(2, history.size(), "Не верное количество задач после удаления одной");
        assertEquals(task, history.get(0), "Не верный порядок после удаления");
        assertEquals(subTask, history.get(1), "Не верный порядок после удаления");

    }

    @Test
    void removeLastTest() {

        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Нет списка");
        assertEquals(3, history.size(), "Не верное количество задач");
        historyManager.remove(subTask.getId());
        history = historyManager.getHistory();
        assertNotNull(history, "Нет списка");
        assertEquals(2, history.size(), "Не верное количество задач после удаления одной");
        assertEquals(task, history.get(0), "Не верный порядок после удаления");
        assertEquals(epic, history.get(1), "Не верный порядок после удаления");

    }


    @Test
    void clearHistoryTest() {

        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Нет списка");
        assertEquals(1, history.size(), "История пустая.");
        historyManager.clearHistory();
        history = historyManager.getHistory();
        assertNotNull(history, "Нет списка после очистки истории");
        assertTrue(history.isEmpty(), "Нет списка истории");

    }

    @Test
    void getHistoryTest() {

        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Нет списка истории");
        assertTrue(history.isEmpty(), "Нет списка истории");
    }

    @Test
    void addTwiceTest() {

        historyManager.add(task);
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Нет списка истории");
        assertEquals(1, history.size(), "Количество элемнтов не верное");
    }
}