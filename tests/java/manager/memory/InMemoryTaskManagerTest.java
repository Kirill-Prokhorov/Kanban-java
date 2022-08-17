package manager.memory;

import manager.TaskManagerTests;
import org.junit.jupiter.api.BeforeEach;
import service.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTests<InMemoryTaskManager> {

    @BeforeEach
    void setUp() {

        taskManager = new InMemoryTaskManager();
        taskManagerSetUp();

    }
}