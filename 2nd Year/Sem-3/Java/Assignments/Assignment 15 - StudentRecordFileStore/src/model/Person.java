package model;

/**
 * Person.java
 * Abstract base class representing a generic Person entity.
 * Demonstrates Abstraction and Encapsulation in OOP.
 * 
 * Part of "Student Record File Store - OOP + DSA Edition"
 * Author: Sasanka Sekhar Kundu (Roll: 150096725118)
 */
public abstract class Person {

    // Encapsulated fields
    protected String id;
    protected String name;

    public Person() {
    }

    public Person(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Abstract method enforcing polymorphic display behavior in subclasses.
     * Demonstrates Abstraction.
     */
    public abstract String displayInfo();
}
