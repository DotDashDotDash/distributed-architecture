package org.codwh.common.exception;

public class DataPersistentException extends Exception {

    private static final long serialVersionUID = 5635061303503185873L;

    private String message;

    public DataPersistentException() {
        super();
    }

    public DataPersistentException(String message) {
        super();
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
