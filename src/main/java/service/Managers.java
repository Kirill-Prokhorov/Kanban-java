package service;

public class Managers  {

    public static TaskManager getDefault(){

        //return new InMemoryTaskManager();
        return new FileBackedTasksManager("src/resource/file.csv");
    }

    public static HistoryManager getDefaultHistory() {

        return new InMemoryHistoryManager();

    }

    public static IdManager getDefaultId() {

        return new NextId();
    }
}
