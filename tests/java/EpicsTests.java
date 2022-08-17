
import model.Epic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import service.Managers;
import service.NextId;
import service.TaskManager;

import java.util.ArrayList;

import static model.Status.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicsTests {

    private static final NextId nextId = new NextId();
    private static final TaskManager taskManager = Managers.getDefault();





    @AfterEach
    void afterEach() {

        taskManager.deleteAllTasks();
    }


    @Test
    void emptyListSubtaskTest(){

        ArrayList<Integer> subTasksId = new ArrayList<>();

        Epic epic = new Epic(nextId.getId(), "Epic1", "Any", NEW);
        taskManager.checkEpicStatus(epic);
        assertEquals(NEW, epic.getStatus(), "Статусы не совпадают");
        assertEquals(subTasksId, epic.getSubTasksId(), "Список подзадач в эпике не пустой");
    }

}
