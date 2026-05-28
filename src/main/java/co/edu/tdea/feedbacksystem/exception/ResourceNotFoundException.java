package co.edu.tdea.feedbacksystem.exception;

import java.util.UUID;

/**
 * Exception thrown when a requested resource cannot be found in the database.
 * This is a runtime exception that indicates an attempt to access a non-existent entity.
 *
 * Examples of resource not found scenarios include:
 * - Rubric not found by ID
 * - Criterion not found by ID
 * - Performance level not found by ID
 *
 * @author Feedback System Team
 * @version 1.0
 * @since 2026-05-27
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new resource not found exception with the specified detail message.
     *
     * @param message the detail message explaining what resource was not found
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new resource not found exception for a specific entity type and ID.
     *
     * @param entityName the name of the entity type that was not found
     * @param id the UUID of the entity that was not found
     */
    public ResourceNotFoundException(String entityName, UUID id) {
        super(entityName + " con id '" + id + "' no fue encontrado");
    }

    public ResourceNotFoundException(String entityName, Object id) {
        super(entityName + " con referencia '" + id + "' no fue encontrado");
    }

    /**
     * Constructs a new resource not found exception with the specified detail message and cause.
     *
     * @param message the detail message explaining what resource was not found
     * @param cause the cause of the exception
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
