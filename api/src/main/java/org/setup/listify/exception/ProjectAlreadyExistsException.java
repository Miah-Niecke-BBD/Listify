package org.setup.listify.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProjectAlreadyExistsException {

    private static final Logger logger = LoggerFactory.getLogger(ProjectAlreadyExistsException.class);

    @GetMapping("/process")
    public ResponseEntity<String> process(@RequestParam(required = false) String input) {
        try {
            if (input == null || input.isEmpty()) {
                throw new IllegalArgumentException("Input cannot be null or empty");
            }
            // Simulate processing logic
            String result = "Processed: " + input;
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid input: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error: ", e);
            throw new RuntimeException("Internal server error");
        }
    }
}

