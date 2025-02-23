package org.setup.Listify.exceptions;

public class AssignedTaskNotFoundException extends RuntimeException {

    public AssignedTaskNotFoundException(Long id) {
        super("Could not find assigned task with id: " + id);
    }
}
