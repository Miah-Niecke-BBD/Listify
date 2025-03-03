package org.setup.listify.exception;

public class ListNotFoundException extends RuntimeException {

    public ListNotFoundException(String list) {
        super("A list of " + list + " was not found" );
    }
}