package org.imaginnovate.Repository;

import java.util.List;

import org.imaginnovate.Dto.EmployeeProjectDto;
import org.imaginnovate.Entity.Employee;
import org.imaginnovate.Entity.EmployeeProject;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class EmployeeProjectRepo implements PanacheRepositoryBase<EmployeeProject, Integer> {

    @Transactional
    public List<EmployeeProjectDto> findAllEmployeeProjects() {
        return getEntityManager().createQuery("SELECT NEW org.imaginnovate.Dto.EmployeeProjectDto(" +
                "ep.id, " +
                "ep.employeeId.id, " +
                "ep.projectId.id, " +
                "ep.canApproveTimesheets, " +
                "ep.createdBy.id, " +
                "ep.createdOn, " +
                "ep.modifiedBy.id, " +
                "ep.modifiedOn, " +
                "ep.deletedBy.id, " +
                "ep.deletedOn) " +
                "FROM EmployeeProject ep ", EmployeeProjectDto.class).getResultList();
    }

    public EmployeeProject findById(int id) {
        return id == 0 ? null : getEntityManager().find(EmployeeProject.class, id);
    }

    public void delete(EmployeeProject employeeProjects) {
        getEntityManager().remove(employeeProjects);
    }

    public void persist(EmployeeProject employeeProjects) {
        getEntityManager().persist(employeeProjects);
    }

    @Transactional
    public List<EmployeeProject> findByEmployeeAndProject(Integer employeeId, Integer projectId) {
        return getEntityManager().createQuery(
                "SELECT ep FROM EmployeeProject ep " +
                        "WHERE ep.employeeId.id = :employeeId AND ep.projectId.id = :projectId",
                EmployeeProject.class)
                .setParameter("employeeId", employeeId)
                .setParameter("projectId", projectId)
                .getResultList();
    }

    public boolean canApproveInProject(Employee employee) {
        return getEntityManager().createQuery("SELECT ep FROM EmployeeProject ep " +
                "WHERE ep.employeeId.id = :employeeId AND ep.canApproveTimesheets = true", EmployeeProject.class)
                .setParameter("employeeId", employee.id)
                .getResultList().size() > 0;
    }

    public boolean canApproveSomeProjects(Integer employeeId, Integer projectId) {
        return getEntityManager().createQuery(
                "SELECT ed FROM EmployeeProject ed WHERE ed.employeeId.id = :employeeId AND ed.projectId.id = :projectId AND ed.canApproveTimesheets = true",
                EmployeeProject.class)
                .setParameter("employeeId", employeeId)
                .setParameter("projectId", projectId)
                .getResultList().size() > 0;
    }
}
