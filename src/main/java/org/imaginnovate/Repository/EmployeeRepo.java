package org.imaginnovate.Repository;

import java.util.List;
import java.util.Optional;

import org.imaginnovate.Dto.EmployeeDto;
import org.imaginnovate.Entity.Employee;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EmployeeRepo implements PanacheRepositoryBase<Employee, Integer> {

    public List<EmployeeDto> findAllEmployees() {
        return getEntityManager().createQuery("SELECT NEW org.imaginnovate.Dto.EmployeeDto(" +
                "e.id, e.firstName, e.lastName, e.gender, e.email, e.designation, e.startDate, e.endDate, " +
                "e.reportsToId.id, e.createdBy.id, e.createdOn, e.modifiedBy.id, e.modifiedOn, " +
                "e.deletedBy.id, e.deletedOn) " +
                "FROM Employee e LEFT JOIN e.reportsToId", EmployeeDto.class).getResultList();
    }

    public void deleteById(Employee employee) {
        try {
            String jpql = "DELETE FROM Employee e WHERE e.id = :id";
            getEntityManager().createQuery(jpql).setParameter("id", employee.id).executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error deleting employee by ID", e);
        }
    }

    public Employee findById(Integer id) {
        try {
            return find("id", id).firstResult();
        } catch (Exception e) {
            throw new RuntimeException("Error finding employee by ID", e);
        }
    }

    public Optional<Employee> findByIdOptional(Integer id) {
        try {
            return Optional.ofNullable(findById(id));
        } catch (Exception e) {
            throw new RuntimeException("Error finding employee by ID optionally", e);
        }
    }

    public EmployeeDto findEmployeeDtoById(Employee submittedByEmployee) {
        return findEmployeeDtoById(submittedByEmployee.id);
    }

    public EmployeeDto findEmployeeDtoById(Integer submittedBy) {
        return getEntityManager().createQuery("SELECT NEW org.imaginnovate.Dto.EmployeeDto(" +
                "e.id, e.firstName, e.lastName, e.gender, e.email, e.designation, e.startDate, e.endDate, " +
                "e.reportsToId.id, e.createdBy.id, e.createdOn, e.modifiedBy.id, e.modifiedOn, " +
                "e.deletedBy.id, e.deletedOn) " +
                "FROM Employee e LEFT JOIN e.reportsToId WHERE e.id = :id", EmployeeDto.class)
                .setParameter("id", submittedBy)
                .getSingleResult();
    }

    public Employee findById(Employee reportsToId) {
        return findById(reportsToId.id);
    }

    public List<Employee> findEmployeesWithApprovalRights() {
        return getEntityManager().createQuery(
                "SELECT e FROM EmployeeDivision ed JOIN ed.employeeId e WHERE ed.canApproveTimesheets = true",
                Employee.class)
                .getResultList();
    }

    public boolean canApproveTimesheets(Integer employeeId) {
        String jpql = "SELECT COUNT(ed) > 0 FROM EmployeeDivision ed " +
                "WHERE ed.employeeId.id = :employeeId AND ed.canApproveTimesheets = true";
        return getEntityManager().createQuery(jpql, Boolean.class)
                .setParameter("employeeId", employeeId)
                .getSingleResult();
    }

}
