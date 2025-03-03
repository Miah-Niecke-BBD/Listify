package org.setup.listify.exception;

public class SectionNotFoundException extends RuntimeException {
    public SectionNotFoundException(Long id) {
        super("Section with id: "+id+" not found");
    }
}
