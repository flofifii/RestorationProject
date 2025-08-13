package model;

import java.time.LocalTime;

public class Shift {
    //private int personID;
    private Worker worker;      // Clé étrangère vers Worker
    private LocalTime startTime;
    private LocalTime endTime;

    // Constructeur
    public Shift(Worker worker, LocalTime startTime, LocalTime endTime) {
        this.worker = worker;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters
    public Worker getworker() {
        return worker;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    // Setters
    public void setworker(Worker worker) {
        this.worker = worker;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
