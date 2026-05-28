package co.edu.tdea.feedbacksystem.exception;

/**
 * Exception thrown when a business rule validation fails.
 * This is a runtime exception that indicates a violation of business logic constraints.
 *
 * Examples of business rule violations include:
 * - Sum of criterion weights exceeding 100%
 * - Attempting to modify an inactive rubric
 * - Invalid performance level score ranges
 *
 * @author Feedback System Team
 * @version 1.0
 * @since 2026-05-27
 */
public class BusinessValidationException extends RuntimeException {

    /**
     * Constructs a new business validation exception with the specified detail message.
     *
     * @param message the detail message explaining the validation failure
     */
    public BusinessValidationException(String message) {
        super(message);
    }

    /**
     * Constructs a new business validation exception with the specified detail message and cause.
     *
     * @param message the detail message explaining the validation failure
     * @param cause the cause of the validation failure
     */
    public BusinessValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
