import model.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.NextId;
import static model.Status.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TasksTests {

    private static Task task;

    @BeforeAll
    static void beforeAll() {

        NextId nextId = new NextId();
        task = new Task(nextId.getId(),"Test addNewTask", "Test addNewTask description", NEW);

    }


    @Test
    void setDescriptionTest() {

        task.setDescription("test description");
        assertEquals("test description", task.getDescription(), "Описания не совпадают");
    }

    @Test
    void setNameTest() {

        task.setName("TestSetName");
        assertEquals("TestSetName", task.getName(), "Имена не совпадают");
    }
}
