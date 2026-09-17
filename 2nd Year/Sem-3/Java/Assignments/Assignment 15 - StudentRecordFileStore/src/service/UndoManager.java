package service;

import model.Action;
import java.util.Stack;

/**
 * UndoManager.java
 * Maintains an undo history stack for student mutations using java.util.Stack.
 * Demonstrates the Stack (LIFO - Last In First Out) data structure in DSA.
 * 
 * Part of "Student Record File Store - OOP + DSA Edition"
 * Author: Sasanka Sekhar Kundu (Roll: 150096725118)
 */
public class UndoManager {

    // Stack stores previous actions for undo
    private final Stack<Action> undoStack;

    public UndoManager() {
        this.undoStack = new Stack<>();
    }

    /**
     * Pushes a completed action onto the top of the stack.
     */
    public void pushAction(Action action) {
        if (action != null) {
            undoStack.push(action);
        }
    }

    /**
     * Checks if any action is available to undo.
     */
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    /**
     * Pops and returns the most recent action from the stack.
     */
    public Action popAction() {
        if (canUndo()) {
            return undoStack.pop();
        }
        return null;
    }

    /**
     * Inspects the most recent action without removing it.
     */
    public Action peekAction() {
        if (canUndo()) {
            return undoStack.peek();
        }
        return null;
    }

    public int getUndoCount() {
        return undoStack.size();
    }

    public void clear() {
        undoStack.clear();
    }
}
