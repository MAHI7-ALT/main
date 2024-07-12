package org.imaginnovate.Repository;

import java.util.List;
import java.util.Optional;

import org.imaginnovate.Dto.DivisionDto;
import org.imaginnovate.Entity.Division;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DivisionRepo implements PanacheRepositoryBase<Division, Integer> {

    public List<DivisionDto> findAllDivisions() {
        return getEntityManager().createQuery(
                "SELECT new org.imaginnovate.Dto.DivisionDto(d.id, d.name, p.id, d.createdBy.id, d.createdOn, " +
                        "d.modifiedBy.id, d.modifiedOn, d.deletedBy.id, d.deletedOn) FROM Division d " +
                        "LEFT JOIN d.parentId p",
                DivisionDto.class)
                .getResultList();
    }
    
    public List<Division> findByDivisionId(int divisionId) {
        return find("id", divisionId).list();
    }    

    public Optional<DivisionDto> findDivisionById(int id) {
        return getEntityManager().createQuery(
                "SELECT new org.imaginnovate.Dto.DivisionDto(d.id, d.name, p.id, d.createdBy.id, d.createdOn, " +
                        "d.modifiedBy.id, d.modifiedOn, d.deletedBy.id, d.deletedOn) FROM Division d " +
                        "LEFT JOIN d.parentId p " +
                        "WHERE d.id = :id",
                DivisionDto.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst();
    }
    
    

    public Division findByName(String name) {
        return find("name", name).firstResult();
    }

    public Division findById(Division divisionId) {
        return find("id", divisionId.id).firstResult();
    }
}
