package model;

import service.Managers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<Integer> subTasksId;
    private LocalDateTime endTime;

    public Epic(int id, String name, String description, Status status) {

        super(id, name, description, status);
        subTasksId = new ArrayList<>();
        endTime = startTime.plusMinutes(duration);

    }
    public Epic(int id, String name, String description, Status status,
                long duration, LocalDateTime startTime) {

        super(id, name, description, status,duration,startTime);
        subTasksId = new ArrayList<>();
        endTime = startTime.plusMinutes(duration);

    }

    public ArrayList<Integer> getSubTasksId() {

        return subTasksId;
    }

    public void setSubTasksId(int idSubtask) {

        this.subTasksId.add(idSubtask);
    }

    public LocalDateTime getEndTime() {

        return endTime;

    }

    public void setEndTime(LocalDateTime endTime){

        this.endTime = endTime;

    }

    /**
     * Получает на входе id дочерней подзадачи и удаляет ее из списка ArrayList<> subTasksId
     * @param subTaskId
     */

    public void deleteSubtasksId(int subTaskId) {

        this.subTasksId.removeIf(t -> t.equals(subTaskId));
        Managers.getDefaultHistory().remove(subTaskId);
    }

    public Status getStatus() {

        return status;
    }

    @Override
    public String toString() {

        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", id=" + super.getId() +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTasksId, epic.subTasksId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), subTasksId);
    }
}
