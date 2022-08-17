package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager{

    private final String filePath;

    public FileBackedTasksManager(String filePath){

        super();
        this.filePath = filePath;
    }

    @Override
    public void newTask(Task task)  {

        super.newTask(task);
        save();
    }

    @Override
    public void addSubTaskToEpic(Epic epic, SubTask subTask) {

        super.addSubTaskToEpic(epic, subTask);
        save();
    }

    @Override
    public void newEpic(Epic epic) {

        super.newEpic(epic);
        save();
    }

    @Override
    public void newSubTask(SubTask subTask) {

        super.newSubTask(subTask);
        save();
    }

    @Override
    public Object getTaskById(int id) {

        Object task = super.getTaskById(id);
        save();
        return task;

    }

    @Override
    public void deleteById(int id) {

        super.deleteById(id);
        save();
    }

    @Override
    public void deleteSubTaskIdFromEpic(Epic epic, int id) {

        super.deleteSubTaskIdFromEpic(epic, id);
        save();
    }

    @Override
    public void updateStatus(Object task) {

        super.updateStatus(task);
        save();
    }

    @Override
    public void checkEpicStatus(Epic epic) {

        super.checkEpicStatus(epic);
        save();
    }

    @Override
    public void checkEpicsDurationAndStartAndEndTime(int epicId) {

        super.checkEpicsDurationAndStartAndEndTime(epicId);
        save();

    }

    @Override
    public ArrayList<SubTask> getEpicsSubTasks(int epicsId) {

        ArrayList<SubTask> epicsSubTask = super.getEpicsSubTasks(epicsId);
        save();
        return epicsSubTask;
    }

    @Override
    public ArrayList<Task> getPrioritizedTasks() {

        ArrayList<Task> prTasks = super.getPrioritizedTasks();
        save();
        return prTasks;
    }


        @Override
    public void deleteAllTasks() {

        super.deleteAllTasks();
        save();
    }

    private void save()  {

        Path path = Paths.get(filePath);
        if(Files.notExists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, StandardCharsets.UTF_8))){

            bw.write("id,type,name,status,description,duration,startTime,epic \n");

            for(Object task : tasks.values()){

                bw.write(CSVTaskSerializator.taskToString((Task)task));
            }
            bw.write("\n");
            bw.write(CSVTaskSerializator.historyToString(Managers.getDefaultHistory()));
        } catch (IOException e){

            throw new ManagerSaveException(e.getMessage());
        }

    }

    public static FileBackedTasksManager loadFromFile(String file) {

        int maxId = 0;
        FileBackedTasksManager manager = new FileBackedTasksManager(file);

        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            br.readLine();

            while (br.ready()){
                String str = br.readLine();
                if(!str.isBlank()) {
                    Task task = CSVTaskSerializator.taskFromString(str);
                    if (task instanceof Epic) {
                       // manager.tasks.put(task.getId(), task);
                        manager.newEpic((Epic) task);
                    } else if (task instanceof SubTask) {
                        manager.newSubTask((SubTask) task);
                    } else {
                        manager.newTask(task);
                    }
                }
                else{
                    if(br.ready()) {
                        str = br.readLine();
                        List<Integer> history = CSVTaskSerializator.historyFromString(str);
                        for (Integer taskId : history) {
                            Managers.getDefaultHistory().add((Task) manager.tasks.get(taskId));
                        }
                    }
                }
            }

        } catch (IOException e) {

            throw new ManagerSaveException(e.getMessage());
        }
        for (Map.Entry<Integer, Object> id : manager.tasks.entrySet()){
            if(id.getKey() > maxId){
                maxId = id.getKey();
            }
        }
        Managers.getDefaultId().setId(maxId+1);
        return manager;
    }

    public static void main(String[] args)  {

        TaskManager taskManager = Managers.getDefault();
        NextId nextId = new NextId();

        Task task = new Task(nextId.getId(), "Таск1", "Писать код", Status.NEW);
        Epic epic = new Epic(nextId.getId(), "Epic1", "Any", Status.NEW);
        SubTask subTask = new SubTask(nextId.getId(), "1", "testDescription", Status.NEW, epic.getId());
        taskManager.newTask(task);
        taskManager.newSubTask(subTask);
        taskManager.addSubTaskToEpic(epic, subTask);
        taskManager.newEpic(epic);
        taskManager.getTaskById(1);
        taskManager.getTaskById(3);
        taskManager.getTaskById(2);
        System.out.println(epic.getSubTasksId());
        System.out.println(taskManager.getAllTasks());

        TaskManager managers1 = loadFromFile("src/resource/file.csv");
        Task task2 = new Task(nextId.getId(), "Таск20", " код", Status.NEW);
        managers1.newTask(task2);
        managers1.getTaskById(4);
        System.out.println(managers1.getAllTasks());
    }
}


