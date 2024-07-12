package org.imaginnovate.Repository;

import java.util.List;

import org.imaginnovate.Dto.UserDto;
import org.imaginnovate.Entity.User;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepo implements PanacheRepositoryBase<User, Integer> {

    public List<UserDto> findAllUsers() {
        return getEntityManager().createQuery(
                "SELECT NEW org.imaginnovate.Dto.UserDto(u.id, u.userName, u.employeeId.id, u.password, u.resetToken, "
                        +
                        "u.resetTokenExpiresAt, u.createdBy.id, u.createdOn, u.modifiedBy.id, u.modifiedOn, u.deletedBy.id, u.deletedOn) "
                        +
                        "FROM User u ",
                UserDto.class).getResultList();
    }

    public User findById(Integer id) {
        return getEntityManager().find(User.class, id);
    }

    public void deleteById(int id) {
        getEntityManager().remove(getEntityManager().find(User.class, id));
    }
}