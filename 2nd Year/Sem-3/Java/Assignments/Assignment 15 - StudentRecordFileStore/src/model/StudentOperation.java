package model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * StudentOperation.java
 * Encapsulates a unit of work queued in FIFO order for processing.
 * Demonstrates the Queue data structure in DSA.
 * 
 * Part of "Student Record File Store - OOP + DSA Edition"
 * Author: Sasanka Sekhar Kundu (Roll: 150096725118)
 */
public class StudentOperation {

    public enum OperationType {
        ADD,
        UPDATE,
        DELETE,
        RELOAD
    }

    private final OperationType type;
    private final Student student;
    private final long timestamp;
    private final String description;

    public StudentOperation(OperationType type, Student student, String description) {
        this.type = type;
        this.student = (student != null) ? new Student(student) : null;
        this.timestamp = System.currentTimeMillis();
        this.description = description;
    }

    public OperationType getType() {
        return type;
    }

    public Student getStudent() {
        return student;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

    public String getFormattedTime() {
        LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        return time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    @Override
    public String toString() {
        return "[" + getFormattedTime() + "] " + type + ": " + description;
    }
}
