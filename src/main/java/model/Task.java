package model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    protected String name;
    protected String description;
    protected Status status;
    protected long duration;
    protected LocalDateTime startTime;
    private final int id;



    public Task(int id, String name, String description, Status status) {

        this.name = name;
        this.description = description;
        this.id = id;
        startTime = LocalDateTime.now();
        setStatus(status);

    }
    public Task(int id, String name, String description, Status status, long duration, LocalDateTime startTime) {

        this.name = name;
        this.description = description;
        this.id = id;
        this.duration = duration;
        this.startTime = startTime;
        setStatus(status);

    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public Status getStatus() {

        return status;
    }

    public int getId() {

        return id;
    }

    public void setStatus(Status status) {

        if (status != null) {
            switch (status) {
                case IN_PROGRESS:
                    this.status = Status.IN_PROGRESS;
                    break;
                case DONE:
                    this.status = Status.DONE;
                    break;
                default:
                    this.status = Status.NEW;
                    break;
            }
        }

    }

    public LocalDateTime getEndTime() {

        return startTime.plusMinutes(duration);

    }

    public LocalDateTime getStartTime() {

        return startTime;

    }

    public void setStartTime(LocalDateTime startTime) {

        this.startTime = startTime;

    }

    public long getDuration() {

        return duration;

    }

    public void setDuration(long duration) {

        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return duration == task.duration && id == task.id && Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) && status == task.status &&
                Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, duration, startTime, id);
    }

}
