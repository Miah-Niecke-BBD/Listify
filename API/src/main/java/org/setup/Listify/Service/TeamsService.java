package org.setup.Listify.Service;

import org.setup.Listify.Model.Teams;
import org.setup.Listify.Repo.TeamsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {
    @Autowired
    private TeamsRepository teamRepository;

    public void getAllTeams() {
        List<Teams> teams = teamRepository.findAll();
        teams.forEach(team -> System.out.println(team));
    }
}
