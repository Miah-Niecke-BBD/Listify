package org.setup.Listify.Exception;

public class ListNotFoundException extends RuntimeException {

    public ListNotFoundException(String list) {
        super("A list of " + list + " was not found" );
    }
}