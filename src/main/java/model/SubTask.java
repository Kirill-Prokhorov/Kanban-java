package model;

import java.time.LocalDateTime;


public class SubTask extends Task {

    private int parentId;

    public SubTask(int id, String name, String description, Status status, int parentId) {

        super(id, name, description, status);
        this.parentId = parentId;

    }

    public SubTask(int id, String name, String description, Status status, int parentId,
                   long duration, LocalDateTime startTime) {

        super(id, name, description, status, duration, startTime);
        this.parentId = parentId;

    }

    @Override
    public String toString() {
        return "SubTask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", id=" + super.getId() +
                ", myParentId=" + parentId +
                '}';
    }

    public int getParentId() {

        return parentId;
    }

    public void setParentId(int parentId) {

        this.parentId = parentId;

    }

}
