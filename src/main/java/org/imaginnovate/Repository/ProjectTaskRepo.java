package org.imaginnovate.Repository;

import java.util.List;

import org.imaginnovate.Dto.ProjectTaskDto;
import org.imaginnovate.Entity.ProjectTask;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@ApplicationScoped
public class ProjectTaskRepo implements PanacheRepositoryBase<ProjectTask, Integer> {

    public List<ProjectTaskDto> findAllProjectTasks() {
        return getEntityManager().createQuery(
                "SELECT NEW org.imaginnovate.Dto.ProjectTaskDto(pt.id, pt.projectId.id, pt.taskId.id, pt.createdBy.id, pt.createdOn, pt.modifiedBy.id, pt.modifiedOn, pt.deletedBy.id, pt.deletedOn) FROM ProjectTask pt LEFT JOIN pt.projectId",
                ProjectTaskDto.class).getResultList();

    }

    public ProjectTask findById(Integer id) {
        return id == null ? null : getEntityManager().find(ProjectTask.class, id);
    }

    public ProjectTask findByProjectAndTask(Integer projectId, Integer taskId) {
        String jpql = "SELECT pt FROM ProjectTask pt WHERE pt.projectId.id = :projectId AND pt.taskId.id = :taskId";
        TypedQuery<ProjectTask> query = getEntityManager().createQuery(jpql, ProjectTask.class);
        query.setParameter("projectId", projectId);
        query.setParameter("taskId", taskId);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
