package org.imaginnovate.Repository;

import java.util.List;

import org.imaginnovate.Dto.TimesheetDto;
import org.imaginnovate.Entity.Timesheet;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TimesheetRepo implements PanacheRepositoryBase<Timesheet, Integer> {

    public List<TimesheetDto> findAllTimesheets() {
        return getEntityManager().createQuery("SELECT new org.imaginnovate.Dto.TimesheetDto("
                + "t.id, "
                + "epd.divisionId.id, "
                + "ep.employeeId.id, "
                + "pt.taskId.id, "
                + "t.description, "
                + "t.hoursWorked, "
                + "t.submittedBy.id, "
                + "t.submittedOn, "
                + "t.status.id, "
                + "t.approvedBy.id, "
                + "t.createdBy.id, "
                + "t.createdOn, "
                + "t.modifiedBy.id, "
                + "t.modifiedOn, "
                + "t.deletedBy.id, "
                + "t.deletedOn) "
                + "FROM Timesheet t "
                + "JOIN t.employeeDivisionId epd "
                + "JOIN t.employeeProjectId ep "
                + "JOIN t.projectTaskId pt ", TimesheetDto.class).getResultList();
    }

    public boolean deleteTimesheet(int id) {
        return false;

    }

    public Timesheet findById(Integer id) {
        return id == null ? null : getEntityManager().find(Timesheet.class, id);
    }

    public void save(Timesheet timesheet) {
        getEntityManager().persist(timesheet);
    }

}