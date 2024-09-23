package org.bazarPet.exception;

public class DonorNotFoundException extends Exception {
    public DonorNotFoundException(String message) {
        super(message);
    }

    public DonorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
