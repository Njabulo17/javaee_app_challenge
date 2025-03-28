package com.example.db.dao;


import com.example.db.entity.UserEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import javassist.NotFoundException;

import java.util.List;

@Stateless
@Transactional
public class UserDao {

        @PersistenceContext(unitName = "pu")
        private EntityManager em;


    public UserEntity createUser(UserEntity userEntity) {
        em.persist(userEntity);
        return userEntity;
    }

    public UserEntity updateUser(UserEntity updatedEntity) throws NotFoundException {
        if (updatedEntity.getId() == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        UserEntity existingUser = em.find(UserEntity.class, updatedEntity.getId());

        if (existingUser == null) {
            throw new NotFoundException("user doesn't exist");
        }
        if (updatedEntity.getName() != null) {
            existingUser.setName(updatedEntity.getName());
        }
        if (updatedEntity.getEmailAddress() != null) {
            existingUser.setEmailAddress(updatedEntity.getEmailAddress());
        }
        em.merge(existingUser);
        return existingUser;
    }

    public UserEntity getUserById(Long id) {
        return em.find(UserEntity.class, id);
    }

    public List<UserEntity> getUsers() {
        return em.createQuery("SELECT u FROM UserEntity u", UserEntity.class).getResultList();
    }

    public UserEntity deleteUserById(Long id) {
        UserEntity user = em.find(UserEntity.class, id);
        if (user != null) {
            em.remove(user);
        }
        return user;
    }

}
