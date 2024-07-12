package org.imaginnovate.Repository;

import java.util.List;

import org.imaginnovate.Dto.TaskDto;
import org.imaginnovate.Entity.Task;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TaskRepo implements PanacheRepositoryBase<Task, Integer> {

    public List<TaskDto> findAllTasks() {
        return getEntityManager().createQuery("SELECT NEW org.imaginnovate.Dto.TaskDto(t.id,t.name,t.description" +
                ",t.createdBy.id,t.createdOn,t.modifiedBy.id,t.modifiedOn,t.deletedBy.id,t.deletedOn) FROM Task t",
                TaskDto.class).getResultList();
    }

    public Task findById(Integer id) {
        return getEntityManager().find(Task.class, id);
    }

}