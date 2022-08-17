package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager{

    protected HashMap<Integer, Object> tasks;
    protected TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    public InMemoryTaskManager() {

        this.tasks = new HashMap<>();

    }

    /**
     * Передает id подзадачи в epic
     * @param epic
     * @param subTask
     */


    public void addSubTaskToEpic(Epic epic, SubTask subTask) {

        if(epic != null && subTask != null) {

            epic.setSubTasksId(subTask.getId());
            checkEpicStatus(epic);

        }


    }

    /**
     * Добавляет task в HashMap<>() tasks для хранения.
     * @param task
     */


    @Override
    public void newTask(Task task) {

        validation(task);
        this.tasks.put(task.getId(), task);
        this.prioritizedTasks.add(task);

    }


    /**
     * Добавляет epic в HashMap<>() tasks для хранения.
     * @param epic
     */

    @Override
    public void newEpic(Epic epic) {

        this.tasks.put(epic.getId(), epic);
        this.prioritizedTasks.add(epic);

    }

    /**
     * Добавляет subTask в HashMap<>() tasks для хранения.
     * @param subTask
     */

    @Override
    public void newSubTask(SubTask subTask) {

        validation(subTask);
        this.tasks.put(subTask.getId(), subTask);
        this.prioritizedTasks.add(subTask);

    }

    /**
     * Полностью очищает HashMap<>() tasks.
     */

    @Override
    public void deleteAllTasks() {

        tasks.clear();
        Managers.getDefaultHistory().clearHistory();

    }

    /**
     * Находит по @param id нужный объект в HashMap<> tasks и удаляет.
     * Если объект epic, то запрашивает все связаные подзадачи и удаляет. И подзадачи и epic.
     * @param id
     */

    @Override
    public void deleteById(int id) {

        if(tasks.containsKey(id)) {

            if (tasks.get(id).getClass() == Epic.class) {

                ArrayList<SubTask> subTasks;
                subTasks = getEpicsSubTasks(id);

                for (SubTask subTask : subTasks){

                    tasks.remove(subTask.getId());
                    prioritizedTasks.remove(subTask);

                    Managers.getDefaultHistory().remove(subTask.getId());
                }
                prioritizedTasks.remove((Epic)tasks.get(id));
                tasks.remove(id);
                Managers.getDefaultHistory().remove(id);
            }
            else if(tasks.get(id).getClass() == SubTask.class){

                int epicId = ((SubTask)tasks.get(id)).getParentId();
                deleteSubTaskIdFromEpic((Epic)(tasks.get(epicId)), id);
                prioritizedTasks.remove((SubTask)tasks.get(id));
                tasks.remove(id);
                Managers.getDefaultHistory().remove(id);
                checkEpicStatus((Epic) tasks.get(epicId));

            }
            else {

                prioritizedTasks.remove((Task)tasks.get(id));
                tasks.remove(id);
                Managers.getDefaultHistory().remove(id);

            }
        }

    }

    /**
     * Отвязывает подзадачу от epic по id подзадачи. Подзадаче присваивает нулевой родительский индекс
     * @param epic
     * @param id
     */

    @Override
    public void deleteSubTaskIdFromEpic(Epic epic, int id) {

        if(epic != null) {

            epic.deleteSubtasksId(id);
            checkEpicStatus(epic);

        }

        if(tasks.containsKey(id)) {
            assert epic != null;
            if(((SubTask) tasks.get(id)).getParentId() == epic.getId()){
                ((SubTask) tasks.get(id)).setParentId(0);
            }
        }

    }

    /**
     * Заменяет в HashMap<> tasks задачу на новую @param task с правильным статусом.
     * Если передается subTask то запрашивает проверку статуса связанного с subTask epic
     * @param task
     */

    @Override
    public void updateStatus(Object task) {

        if(task != null && tasks.containsKey(((Task)task).getId())){

            if(task.getClass() == Task.class) {

                validation((Task) task);
                tasks.put(((Task)task).getId(), task);
                prioritizedTasks.add((Task)task);
            }

            else if(task.getClass() == SubTask.class) {

                validation((SubTask) task);
                tasks.put(((SubTask)task).getId(), task);
                prioritizedTasks.add((SubTask) task);
                checkEpicStatus((Epic) tasks.get(((SubTask)task).getParentId()));
            }
        }

    }

    /**
     * Получает список всех связаных подзадач и проверяет их статусы
     * что бы рассчитать статус epic
     * @param epic
     */

    public void checkEpicStatus(Epic epic) {

        if(epic != null) {
            ArrayList<SubTask> subTasks = getEpicsSubTasks(epic.getId());
            ArrayList<Status> subTasksStatus = new ArrayList<>();

            for (SubTask subTask : subTasks){
                subTasksStatus.add(subTask.getStatus());
            }

            if(subTasks.size() == 0){

                epic.setStatus(Status.NEW);
            }
            else if(subTasksStatus.contains(Status.NEW) && !subTasksStatus.contains(Status.IN_PROGRESS)
                          && !subTasksStatus.contains(Status.DONE)) {
                epic.setStatus(Status.NEW);
            }
            else if(!subTasksStatus.contains(Status.NEW) && !subTasksStatus.contains(Status.IN_PROGRESS)
                    && subTasksStatus.contains(Status.DONE)) {
                epic.setStatus(Status.DONE);
            }
            else {
                epic.setStatus(Status.IN_PROGRESS);
            }
            //checkEpicsDurationAndStartAndEndTime(epic.getId());

        }
    }

    public void checkEpicsDurationAndStartAndEndTime(int epicId) {

        if(tasks.get(epicId) == null){
            return;
        }

        Epic epic = (Epic)tasks.get(epicId);
        ArrayList<Integer> epicsSubtaskId = epic.getSubTasksId();

        if(epicsSubtaskId.isEmpty()) {
            return;
        }

        LocalDateTime startEpic = LocalDateTime.MAX;
        LocalDateTime endEpic = LocalDateTime.MIN;
        long epicDuration = 0L;

        for(int subtaskId : epicsSubtaskId){
            if(tasks.containsKey(subtaskId)){

                SubTask subTask = (SubTask) tasks.get(subtaskId);
                LocalDateTime subtaskStart = subTask.getStartTime();
                LocalDateTime subtaskEnd = subTask.getEndTime();
                if(subtaskStart.isBefore(startEpic)){

                    startEpic = subtaskStart;
                }
                if(subtaskEnd.isAfter(endEpic)) {

                    endEpic = subtaskEnd;
                }
                epicDuration += subTask.getDuration();

            }

        }

        epic.setEndTime(endEpic);
        epic.setDuration(epicDuration);
        epic.setStartTime(startEpic);

    }

    public void validation(Task task) {

        if(tasks.isEmpty() && prioritizedTasks.isEmpty()) {
            return;
        }

        for(Task anyTask : prioritizedTasks){

            if(anyTask.getClass() == Epic.class){

                continue;
            }

            LocalDateTime innerTaskStart = anyTask.getStartTime();
            LocalDateTime innerTaskEnd = anyTask.getEndTime().plusMinutes(anyTask.getDuration());
            LocalDateTime taskStart = task.getStartTime();
            LocalDateTime taskEnd = task.getEndTime().plusMinutes(anyTask.getDuration());
            if(!((innerTaskStart.isBefore(taskStart.plusNanos(1)) &&
                    innerTaskEnd.isBefore(taskEnd.plusNanos(1))) ||
                    (innerTaskStart.isAfter(taskStart.minusNanos(1)) &&
                            innerTaskEnd.isAfter(taskEnd.minusNanos(1))))){

                throw new ManagerValidationException("Пересечение задач. Новая задача не создана");
            }

        }

    }

    @Override
    public ArrayList<Task> getPrioritizedTasks() {

        return new ArrayList<>(prioritizedTasks);

    }

    /**
     * Просто возвращает HashMap<Integer, Object> tasks содержащую все актуальные задачи
     * @return
     */

    @Override
    public HashMap<Integer, Object> getAllTasks() {

        return tasks;
    }

    /**
     * По переданому id ищет в HashMap<> tasks нужную задачу и возвращает.
     * @param id
     * @return
     */

    @Override
    public Object getTaskById(int id) {

        if(tasks.containsKey(id)) {
            Managers.getDefaultHistory().add((Task) tasks.get(id));
            return tasks.get(id);
        }
        return null;
    }

    /**
     * На входе получает id epic задачи. Перебирает все значения в HashMap<> tasks и если попадается subTask
     * сравнивает его родительский id с id epic. При совпадении добавляет subTask в ArrayList<> epicsSubTasks.
     * По завершении цикла возвращает ArrayList<> epicsSubTasks.
     * @param epicsId
     * @return
     */

    @Override
    public ArrayList<SubTask> getEpicsSubTasks(int epicsId) {

        ArrayList<SubTask> epicsSubTasks = new ArrayList<>();

        for (Object subTask : tasks.values()){

            if (subTask.getClass() == SubTask.class){

                if(((SubTask) subTask).getParentId() == epicsId) {

                    epicsSubTasks.add((SubTask) subTask);
                    Managers.getDefaultHistory().add((Task) subTask);
                }
            }
        }

        return epicsSubTasks;
    }

}


