package org.setup.Listify.assembler;

import org.setup.Listify.model.Sections;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SectionModelAssembler {
    public Sections toModel(Sections section) {
        return section;
    }


    public List<Sections> toModels(List<Sections> sections) {
        return sections.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}


