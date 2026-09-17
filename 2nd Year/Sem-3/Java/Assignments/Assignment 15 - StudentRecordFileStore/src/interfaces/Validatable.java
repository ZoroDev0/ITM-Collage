package interfaces;

import exception.StudentException;

/**
 * Validatable.java
 * Interface defining the validation contract for entities before persistence or operations.
 * Demonstrates the Interface concept in OOP.
 * 
 * Part of "Student Record File Store - OOP + DSA Edition"
 * Author: Sasanka Sekhar Kundu (Roll: 150096725118)
 */
public interface Validatable {
    /**
     * Validates entity properties according to domain rules.
     * Throws StudentException if validation fails.
     */
    void validate() throws StudentException;
}
