package service;

import exception.StudentException;
import model.Action;
import model.Action.ActionType;
import model.Student;
import model.StudentOperation;
import model.StudentOperation.OperationType;

import java.util.ArrayList;

/**
 * StudentManager.java
 * Manages in-memory student records using ArrayList<Student>.
 * Integrates UndoManager (Stack) and OperationQueue (Queue) to support undo
 * and transaction processing.
 * 
 * Demonstrates:
 * - ArrayList (primary collection)
 * - Stack (UndoManager)
 * - Queue (OperationQueue)
 * - Interface validation (Validatable)
 * 
 * Part of "Student Record File Store - OOP + DSA Edition"
 * Author: Sasanka Sekhar Kundu (Roll: 150096725118)
 */
public class StudentManager {

    // Primary in-memory collection: ArrayList<Student>
    private ArrayList<Student> studentList;

    // DSA Component Integrations
    private final UndoManager undoManager;
    private final OperationQueue operationQueue;

    public StudentManager() {
        this.studentList = new ArrayList<>();
        this.undoManager = new UndoManager();
        this.operationQueue = new OperationQueue();
    }

    public UndoManager getUndoManager() {
        return undoManager;
    }

    public OperationQueue getOperationQueue() {
        return operationQueue;
    }

    /**
     * Checks whether a student with the given ID already exists in the ArrayList.
     */
    public boolean exists(String studentId) {
        if (studentId == null) return false;
        for (Student s : studentList) {
            if (s.getId().equalsIgnoreCase(studentId.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a new student to the ArrayList.
     * Demonstrates:
     * - Validation via Validatable interface
     * - Queue transaction tracking
     * - Stack undo tracking
     */
    public void addStudent(Student student) throws StudentException {
        if (student == null) {
            throw new StudentException("Student object cannot be null.");
        }

        // Interface validation
        student.validate();

        if (exists(student.getId())) {
            throw new StudentException("Student with ID '" + student.getId().trim() + "' already exists. Duplicate IDs are not allowed.");
        }

        // 1. Enqueue operation (Queue)
        StudentOperation op = new StudentOperation(OperationType.ADD, student, "Added student ID " + student.getId());
        operationQueue.enqueue(op);

        // 2. Add to primary ArrayList
        studentList.add(student);

        // 3. Process from Queue
        operationQueue.dequeue();

        // 4. Push to Undo Stack (Stack)
        undoManager.pushAction(new Action(ActionType.ADD, student));
    }

    /**
     * Returns all students stored in the ArrayList.
     */
    public ArrayList<Student> getAllStudents() {
        return new ArrayList<>(studentList);
    }

    /**
     * Finds a student by their unique ID.
     */
    public Student findStudentById(String id) throws StudentException {
        if (id == null || id.trim().isEmpty()) {
            throw new StudentException("Please provide a valid Student ID to search.");
        }

        for (Student s : studentList) {
            if (s.getId().equalsIgnoreCase(id.trim())) {
                return s;
            }
        }

        throw new StudentException("Student with ID '" + id.trim() + "' was not found.");
    }

    /**
     * Searches students by ID, Name, or Course (case-insensitive).
     */
    public ArrayList<Student> searchStudents(String query) {
        return SearchService.searchLinear(studentList, query);
    }

    /**
     * Updates an existing student record in the ArrayList.
     */
    public void updateStudent(Student updatedStudent) throws StudentException {
        if (updatedStudent == null) {
            throw new StudentException("Student object cannot be null.");
        }

        updatedStudent.validate();

        int targetIndex = -1;
        Student previousState = null;

        for (int i = 0; i < studentList.size(); i++) {
            if (studentList.get(i).getId().equalsIgnoreCase(updatedStudent.getId().trim())) {
                targetIndex = i;
                previousState = new Student(studentList.get(i)); // snapshot
                break;
            }
        }

        if (targetIndex == -1) {
            throw new StudentException("Cannot update: Student with ID '" + updatedStudent.getId().trim() + "' does not exist.");
        }

        // 1. Enqueue operation
        operationQueue.enqueue(new StudentOperation(OperationType.UPDATE, updatedStudent, "Updated student ID " + updatedStudent.getId()));

        // 2. Update ArrayList
        studentList.set(targetIndex, updatedStudent);

        // 3. Process operation
        operationQueue.dequeue();

        // 4. Push to Undo Stack
        undoManager.pushAction(new Action(ActionType.UPDATE, updatedStudent, previousState));
    }

    /**
     * Deletes a student by their unique ID from the ArrayList.
     */
    public void deleteStudent(String id) throws StudentException {
        if (id == null || id.trim().isEmpty()) {
            throw new StudentException("Please specify a valid Student ID to delete.");
        }

        int targetIndex = -1;
        Student removedStudent = null;

        for (int i = 0; i < studentList.size(); i++) {
            if (studentList.get(i).getId().equalsIgnoreCase(id.trim())) {
                targetIndex = i;
                removedStudent = studentList.get(i);
                break;
            }
        }

        if (targetIndex == -1) {
            throw new StudentException("Cannot delete: Student with ID '" + id.trim() + "' does not exist.");
        }

        // 1. Enqueue operation
        operationQueue.enqueue(new StudentOperation(OperationType.DELETE, removedStudent, "Deleted student ID " + id));

        // 2. Remove from ArrayList
        studentList.remove(targetIndex);

        // 3. Process operation
        operationQueue.dequeue();

        // 4. Push to Undo Stack
        undoManager.pushAction(new Action(ActionType.DELETE, removedStudent));
    }

    /**
     * Reverses the last mutation operation using the Undo Stack.
     * Returns an informative message of what was undone.
     */
    public String undoLastAction() throws StudentException {
        if (!undoManager.canUndo()) {
            throw new StudentException("No actions available to undo.");
        }

        Action action = undoManager.popAction();
        if (action == null) {
            throw new StudentException("No valid action found to undo.");
        }

        switch (action.getType()) {
            case ADD:
                // Undo ADD: remove the added student from ArrayList
                Student added = action.getTargetStudent();
                studentList.removeIf(s -> s.getId().equalsIgnoreCase(added.getId()));
                operationQueue.enqueue(new StudentOperation(OperationType.DELETE, added, "Undo ADD: Removed student " + added.getId()));
                operationQueue.dequeue();
                return "Undone: Removed added student '" + added.getName() + "' (ID: " + added.getId() + ")";

            case UPDATE:
                // Undo UPDATE: restore the previous state
                Student prev = action.getPreviousState();
                if (prev != null) {
                    for (int i = 0; i < studentList.size(); i++) {
                        if (studentList.get(i).getId().equalsIgnoreCase(prev.getId())) {
                            studentList.set(i, new Student(prev));
                            break;
                        }
                    }
                    operationQueue.enqueue(new StudentOperation(OperationType.UPDATE, prev, "Undo UPDATE: Restored previous state for ID " + prev.getId()));
                    operationQueue.dequeue();
                    return "Undone: Restored previous details for student ID " + prev.getId();
                }
                return "Undone: Update reversed";

            case DELETE:
                // Undo DELETE: restore the deleted student back to ArrayList
                Student deleted = action.getTargetStudent();
                if (deleted != null) {
                    studentList.add(new Student(deleted));
                    operationQueue.enqueue(new StudentOperation(OperationType.ADD, deleted, "Undo DELETE: Restored student " + deleted.getId()));
                    operationQueue.dequeue();
                    return "Undone: Restored deleted student '" + deleted.getName() + "' (ID: " + deleted.getId() + ")";
                }
                return "Undone: Delete reversed";

            default:
                return "Undone last action.";
        }
    }

    public int getStudentCount() {
        return studentList.size();
    }

    public void setStudents(ArrayList<Student> newStudents) {
        this.studentList = (newStudents != null) ? new ArrayList<>(newStudents) : new ArrayList<>();
    }

    public void clearAll() {
        this.studentList.clear();
        this.undoManager.clear();
    }
}
