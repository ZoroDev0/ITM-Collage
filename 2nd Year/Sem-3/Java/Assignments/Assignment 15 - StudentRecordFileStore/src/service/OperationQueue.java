package service;

import model.StudentOperation;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * OperationQueue.java
 * Manages pending operations in FIFO order using java.util.Queue.
 * Demonstrates the Queue (FIFO - First In First Out) data structure in DSA.
 * 
 * Part of "Student Record File Store - OOP + DSA Edition"
 * Author: Sasanka Sekhar Kundu (Roll: 150096725118)
 */
public class OperationQueue {

    // Queue processes student operations in FIFO order
    private final Queue<StudentOperation> pendingQueue;
    private final List<StudentOperation> processedHistory;
    private static final int MAX_HISTORY = 20;

    public OperationQueue() {
        this.pendingQueue = new LinkedList<>();
        this.processedHistory = new ArrayList<>();
    }

    /**
     * Enqueues an operation at the tail of the queue.
     */
    public synchronized void enqueue(StudentOperation operation) {
        if (operation != null) {
            pendingQueue.offer(operation);
        }
    }

    /**
     * Dequeues and processes the head of the queue in FIFO order.
     */
    public synchronized StudentOperation dequeue() {
        StudentOperation op = pendingQueue.poll();
        if (op != null) {
            processedHistory.add(0, op);
            if (processedHistory.size() > MAX_HISTORY) {
                processedHistory.remove(processedHistory.size() - 1);
            }
        }
        return op;
    }

    public synchronized int getPendingCount() {
        return pendingQueue.size();
    }

    public synchronized boolean hasPending() {
        return !pendingQueue.isEmpty();
    }

    public synchronized List<StudentOperation> getProcessedHistory() {
        return new ArrayList<>(processedHistory);
    }
}
