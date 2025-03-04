package org.setup.listify.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProjectRepositoryTest {

    @Autowired
    private ProjectsRepository projectRepository;

    @Test
    public void testGetProjectDetails() {
        Long userID = 70L;  // Use a valid userID
        Long projectID = 81L;  // Use a valid projectID

        List<Object[]> result = projectRepository.getProjectDetails(userID, projectID);

        assertNotNull(result);  // Assert that the result is not null
        assertFalse(result.isEmpty(), "Result should not be empty");

        // Print out results for debugging
        for (Object[] row : result) {
            System.out.println("Row data: ");
            for (Object col : row) {
                System.out.print(col + " ");
            }
            System.out.println();  // Move to the next row
        }

        // Add more specific assertions based on expected values
        assertEquals(81L, result.get(0)[1]);
    }
}
