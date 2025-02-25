//    package org.setup.Listify.Assembler;
//
//    import org.setup.Listify.Controller.GitHubOAuthController;
//    import org.setup.Listify.Model.Users;
//    import org.springframework.hateoas.Link;
//    import org.springframework.hateoas.server.RepresentationModelAssembler;
//    import org.springframework.hateoas.EntityModel;
//    import org.springframework.stereotype.Component;
//
//    import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
//
//    @Component
//    public class UserAssembler implements RepresentationModelAssembler<Users, EntityModel<Users>> {
//
//        @Override
//        public EntityModel<Users> toModel(Users user) {
//
//            Link selfLink = linkTo(GitHubOAuthController.class)
//                    .slash("login")
//                    .withSelfRel();
//            return EntityModel.of(user, selfLink);
//        }
//    }