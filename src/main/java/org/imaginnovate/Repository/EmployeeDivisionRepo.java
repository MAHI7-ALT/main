package org.imaginnovate.Repository;

import java.util.List;
import java.util.Optional;

import org.imaginnovate.Dto.EmployeeDivisionDto;
import org.imaginnovate.Entity.Division;
import org.imaginnovate.Entity.Employee;
import org.imaginnovate.Entity.EmployeeDivision;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;

@ApplicationScoped
public class EmployeeDivisionRepo implements PanacheRepositoryBase<EmployeeDivision, Integer> {

    public List<EmployeeDivisionDto> findAllEmployeeDivisions() {
        return getEntityManager().createQuery(
                "SELECT NEW org.imaginnovate.Dto.EmployeeDivisionDto(" +
                        "ed.id, ed.employeeId.id, ed.divisionId.id, ed.primaryDivision, ed.canApproveTimesheets, " +
                        "ed.createdBy.id, ed.createdOn, ed.modifiedBy.id, ed.modifiedOn, ed.deletedBy.id, ed.deletedOn) "
                        +
                        "FROM EmployeeDivision ed " +
                        "ORDER BY ed.employeeId.id ASC, ed.divisionId.id ASC",
                EmployeeDivisionDto.class)
                .getResultList();
    }

    public EmployeeDivision findByEmployeeDivisionId(Integer id) {
        return getEntityManager().find(EmployeeDivision.class, id);
    }

    public EmployeeDivision findById(int id) {
        return getEntityManager().find(EmployeeDivision.class, id);
    }

    public void persist(EmployeeDivision employeeDivision) {
        getEntityManager().persist(employeeDivision);
    }

    public Optional<EmployeeDivision> findByEmployeeIdAndCanApproveTimesheets(Employee employee,
            boolean canApproveTimesheets) {
        return getEntityManager().createQuery(
                "SELECT ed FROM EmployeeDivision ed WHERE ed.employeeId = :employee AND ed.canApproveTimesheets = :canApproveTimesheets",
                EmployeeDivision.class)
                .setParameter("employee", employee)
                .setParameter("canApproveTimesheets", canApproveTimesheets)
                .getResultStream().findFirst();
    }

    public boolean canApproveSomeDivisions(Employee employee, Division division) {
        return getEntityManager().createQuery(
                "SELECT ed FROM EmployeeDivision ed WHERE ed.employeeId = :employee AND ed.divisionId = :division AND ed.canApproveTimesheets = true",
                EmployeeDivision.class)
                .setParameter("employee", employee)
                .setParameter("division", division)
                .getResultList().size() > 0;
    }
    

    public EmployeeDivision findByEmployeeAndDivision(Integer employeeId, Integer divisionId) {
        try {
            return getEntityManager().createQuery(
                    "SELECT ed FROM EmployeeDivision ed WHERE ed.employeeId.id = :employeeId AND ed.divisionId.id = :divisionId",
                    EmployeeDivision.class)
                    .setParameter("employeeId", employeeId)
                    .setParameter("divisionId", divisionId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public boolean existsByDivisionIdAndEmployeeId(Integer divisionId, Integer employeeId) {
        return getEntityManager().createQuery(
                "SELECT ed FROM EmployeeDivision ed WHERE ed.divisionId.id = :divisionId AND ed.employeeId.id = :employeeId",
                EmployeeDivision.class)
                .setParameter("divisionId", divisionId)
                .setParameter("employeeId", employeeId)
                .getResultList().size() > 0;
    }

    public List<EmployeeDivision> findByEmployeeId(Integer id) {
        return getEntityManager().createQuery(
                "SELECT ed FROM EmployeeDivision ed WHERE ed.employeeId.id = :id",
                EmployeeDivision.class)
                .setParameter("id", id)
                .getResultList();
    }

    public boolean canApproveInDivision(Employee employee) {
        return getEntityManager().createQuery("SELECT ed FROM EmployeeDivision ed " +
                "WHERE ed.employeeId.id = :employeeId AND ed.canApproveTimesheets = true", EmployeeDivision.class)
                .setParameter("employeeId", employee.id)
                .getResultList().size() > 0;
    }


}