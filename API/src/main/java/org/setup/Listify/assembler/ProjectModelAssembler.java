package org.setup.Listify.assembler;

import org.setup.Listify.model.Projects;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectModelAssembler {
    public Projects toModel(Projects project) {

        return project;
    }

    public List<Projects> toModels(List<Projects> projects) {
        return projects.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}
