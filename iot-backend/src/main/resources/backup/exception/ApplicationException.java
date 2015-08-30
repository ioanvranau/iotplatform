package backup.exception;


public class ApplicationException extends Exception {

    public static final int INVALID_MESSAGE_CODE = 0;
    public static final int TECHNICAL_PROBLEM_CODE = 1;

    private int code;

    private ApplicationException(int code, String message) {
        super(message);

        this.code = code;
    }

    private ApplicationException(int code, String message, Throwable e) {
        super(message, e);

        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String toStringForLogger() {
        return String.format("application exception code %d, message '%s'",
                getCode(), getMessage());
    }

    public static ApplicationException newInvalidMessage(Throwable e) {
        return new ApplicationException(INVALID_MESSAGE_CODE, "Invalid message", e);
    }

    public static ApplicationException newTechnicalProblem(Throwable e) {
        return new ApplicationException(TECHNICAL_PROBLEM_CODE, "Technical problem message", e);
    }

}
