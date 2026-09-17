package auth;

import exception.StudentException;

/**
 * AuthenticationManager.java
 * Manages user authentication and session credentials.
 * 
 * Default Credentials:
 * - Username: admin
 * - Password: admin123
 * 
 * Part of "Student Record File Store - OOP + DSA Edition"
 * Author: Sasanka Sekhar Kundu (Roll: 150096725118)
 */
public class AuthenticationManager {

    // Configurable default credentials
    public static final String DEFAULT_USERNAME = "admin";
    public static final String DEFAULT_PASSWORD = "admin123";

    private String currentUsername = null;
    private boolean authenticated = false;

    public AuthenticationManager() {
    }

    /**
     * Validates credentials and authenticates session.
     * Throws StudentException on validation or matching failure.
     */
    public boolean authenticate(String username, String password) throws StudentException {
        if (username == null || username.trim().isEmpty()) {
            throw new StudentException("Username cannot be empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new StudentException("Password cannot be empty.");
        }

        if (DEFAULT_USERNAME.equals(username.trim()) && DEFAULT_PASSWORD.equals(password.trim())) {
            this.currentUsername = username.trim();
            this.authenticated = true;
            return true;
        } else {
            throw new StudentException("Invalid username or password. Please try again.");
        }
    }

    public void logout() {
        this.currentUsername = null;
        this.authenticated = false;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }
}
