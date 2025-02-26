package org.setup.Listify.Assembler;

import org.setup.Listify.Controller.SectionsController;
import org.setup.Listify.Model.Sections;
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
                linkTo(methodOn(SectionsController.class).getSectionsById(section.getSectionID())).withSelfRel(),
                linkTo(methodOn(SectionsController.class).getAllSections()).withRel("sections"));
    }

    @Override
    public CollectionModel<EntityModel<Sections>> toCollectionModel(Iterable<? extends Sections> sections) {
        List<EntityModel<Sections>> sectionsModel = StreamSupport.stream(sections.spliterator(), false)
                .map(this::toModel)
                .toList();
        return CollectionModel.of(sectionsModel,
                linkTo(methodOn(SectionsController.class).getAllSections()).withSelfRel());
    }
}
