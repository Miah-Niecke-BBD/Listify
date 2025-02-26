package org.setup.Listify.exception;

public class SectionNotFoundException extends RuntimeException {
    public SectionNotFoundException(Long id) {
        super("Section with id: "+id+" not found");
    }
}
