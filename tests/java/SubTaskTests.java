import model.SubTask;
import org.junit.jupiter.api.Test;
import service.NextId;

import static model.Status.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubTaskTests {

    @Test
    void setParentIdTest() {

        NextId nextId = new NextId();
        SubTask subTask = new SubTask(nextId.getId(),"Test", "Test description", NEW, 1);
        subTask.setParentId(2);
        assertEquals(2, subTask.getParentId());

    }

}
