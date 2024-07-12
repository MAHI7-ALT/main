package org.imaginnovate.Repository;

import org.imaginnovate.Entity.TimesheetStatus;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TimesheetStatusRepo implements PanacheRepositoryBase<TimesheetStatus, Integer> {

    public TimesheetStatus findById(Byte id) {
        return id == null ? null : getEntityManager().find(TimesheetStatus.class, id);
    }

    public Object findByName(String name) {
        return name;
    }

}