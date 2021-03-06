package org.example.dao;

import org.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.*;


@Repository
public class UserDaoImpl implements UserDao {

    public UserDaoImpl() {
    }

    @PersistenceContext
    private EntityManager entityManager;
    private RoleDao roleDao;

    @Autowired
    public UserDaoImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public User justSaveUser(User user) {
        entityManager.persist(user);
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        entityManager.remove(findUserById(id));
        entityManager.flush();
        entityManager.clear();

    }

    @Override
    public User updateUser(User updatedUser) {

        entityManager.merge(updatedUser);

        return updatedUser;
    }

    @Override
    public List<User> usersList() {
        return entityManager.createQuery("FROM User", User.class).getResultList();
    }

    @Override
    public User findUserById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User getUserByName(String username) {

        try {
            return entityManager.
                    createQuery("select u from User u where u.username = :username", User.class).
                    setParameter("username", username). getSingleResult();
        } catch (NoResultException e) {
            System.out.println("no result");
            e.printStackTrace();
            return null;
        }
    }
}
