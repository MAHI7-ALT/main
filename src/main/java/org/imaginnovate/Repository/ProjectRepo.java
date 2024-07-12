package org.imaginnovate.Repository;

import java.util.List;

import org.imaginnovate.Dto.ProjectDto;
import org.imaginnovate.Entity.Division;
import org.imaginnovate.Entity.Project;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProjectRepo implements PanacheRepositoryBase<Project, Integer> {

    public List<ProjectDto> findAllProjects() {
        return getEntityManager()
                .createQuery("SELECT NEW org.imaginnovate.Dto.ProjectDto(p.id, p.name, p.description, p.divisionId.id, "
                        +
                        "p.createdBy.id, p.createdOn, p.modifiedBy.id, p.modifiedOn, p.deletedBy.id, p.deletedOn) " +
                        "FROM Project p", ProjectDto.class)
                .getResultList();
    }

    public List<Project> findByDivisionId(Division divisions) {
        String jpql = "SELECT p FROM Project p WHERE p.divisionId = :division";
        return getEntityManager().createQuery(jpql, Project.class).setParameter("division", divisions).getResultList();
    }

    public Project findById(Project projectId) {
        return getEntityManager().find(Project.class, projectId);
    }

}