package org.setup.listify.assembler;

import org.setup.listify.controller.SectionsController;
import org.setup.listify.model.Sections;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SectionsModelAssembler implements RepresentationModelAssembler<Sections, EntityModel<Sections>> {
    @Override
    public EntityModel<Sections> toModel(Sections section) {
        return EntityModel.of(section,
                linkTo(methodOn(SectionsController.class).getTaskBySectionId(section.getSectionID())).withRel("tasksInSection"));
    }

    @Override
    public CollectionModel<EntityModel<Sections>> toCollectionModel(Iterable<? extends Sections> sections) {
        List<EntityModel<Sections>> sectionsModel = StreamSupport.stream(sections.spliterator(), false)
                .map(this::toModel)
                .toList();
        return CollectionModel.of(sectionsModel,
                linkTo(methodOn(SectionsController.class)).withSelfRel());
    }
}
