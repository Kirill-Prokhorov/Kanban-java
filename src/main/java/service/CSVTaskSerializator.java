package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class CSVTaskSerializator {

    public static String taskToString(Task task) {

        String stringTask;

        if(task instanceof SubTask) {

            stringTask = String.format("%s,%s,%s,%s,%s,%s,%s,%s\n", task.getId(), TypeTask.SUBTASK,task.getName(),
                    task.getStatus(), task.getDescription(),
                    task.getDuration(), task.getStartTime(), ((SubTask) task).getParentId());
        }
        else if(task instanceof Epic){

            stringTask = String.format("%s,%s,%s,%s,%s,%s,%s\n", task.getId(), TypeTask.EPIC,task.getName(),
                    task.getStatus(), task.getDescription(), task.getDuration(), task.getStartTime());
        }
        else {

            stringTask = String.format("%s,%s,%s,%s,%s,%s,%s\n", task.getId(), TypeTask.TASK,task.getName(),
                    task.getStatus(), task.getDescription(), task.getDuration(), task.getStartTime());
        }

        return stringTask;
    }

    public static String historyToString(HistoryManager manager){

        StringBuilder history = new StringBuilder();

        if(!InMemoryHistoryManager.nodes.isEmpty()) {
            for (int i = 0; i < manager.getHistory().size(); i++) {

                if (i == manager.getHistory().size() - 1) {

                    history.append(manager.getHistory().get(i).getId());
                } else {

                    history.append(manager.getHistory().get(i).getId()).append(",");
                }

            }
        }

        return history.toString();
    }

    public static Task taskFromString(String value) {

        String[] dataFromTask = value.split(",");
        int id = Integer.parseInt(dataFromTask[0]);
        TypeTask type = TypeTask.valueOf(dataFromTask[1]);
        String name = dataFromTask[2];
        Status status = Status.valueOf(dataFromTask[3]);
        String description = dataFromTask[4];
        long duration = Long.parseLong(dataFromTask[5]);
        LocalDateTime startTime = LocalDateTime.parse(dataFromTask[6]);


        if(type.equals(TypeTask.SUBTASK)){
            int epic = Integer.parseInt(dataFromTask[7]);
            return new SubTask(id,name,description,status,epic,duration,startTime);
        }
        else if (type.equals(TypeTask.TASK)){
            return new Task(id,name,description,status,duration,startTime);
        }
        else {
            return new Epic(id,name,description,status,duration,startTime);
        }
    }

    public static List<Integer> historyFromString(String value) {

        List<Integer> id = new LinkedList<>();
        String [] numbers = value.split(",");
        for (String number : numbers){
            id.add(Integer.parseInt(number));
        }
        return id;
    }
}
