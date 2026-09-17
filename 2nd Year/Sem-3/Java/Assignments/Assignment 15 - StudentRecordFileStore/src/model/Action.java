package model;

/**
 * Action.java
 * Encapsulates operation states pushed onto the Stack for the Undo mechanism.
 * Demonstrates the Stack data structure in DSA.
 * 
 * Part of "Student Record File Store - OOP + DSA Edition"
 * Author: Sasanka Sekhar Kundu (Roll: 150096725118)
 */
public class Action {

    public enum ActionType {
        ADD,
        UPDATE,
        DELETE
    }

    private final ActionType type;
    private final Student targetStudent;
    private final Student previousState; // Kept for reversing UPDATE actions
    private final long timestamp;

    public Action(ActionType type, Student targetStudent) {
        this(type, targetStudent, null);
    }

    public Action(ActionType type, Student targetStudent, Student previousState) {
        this.type = type;
        this.targetStudent = (targetStudent != null) ? new Student(targetStudent) : null;
        this.previousState = (previousState != null) ? new Student(previousState) : null;
        this.timestamp = System.currentTimeMillis();
    }

    public ActionType getType() {
        return type;
    }

    public Student getTargetStudent() {
        return targetStudent;
    }

    public Student getPreviousState() {
        return previousState;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        switch (type) {
            case ADD:
                return "Added student " + (targetStudent != null ? targetStudent.getId() : "");
            case UPDATE:
                return "Updated student " + (targetStudent != null ? targetStudent.getId() : "");
            case DELETE:
                return "Deleted student " + (targetStudent != null ? targetStudent.getId() : "");
            default:
                return "Action";
        }
    }
}
