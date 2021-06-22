/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kelaskoding.repo;

import com.kelaskoding.entity.User;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import static javax.transaction.Transactional.TxType.REQUIRED;

/**
 *
 * @author jarvis
 */
@ApplicationScoped
public class UserRepo {

    @PersistenceContext(unitName = "dbitems_pu")
    private EntityManager em;

    @Transactional(REQUIRED)
    public void create(User user) {
        em.persist(user);
    }
    
    public List<User> findAll(){
        return em.createQuery("SELECT u FROM User u").getResultList();
    }
    
    public User findOne(Long id){
        return em.find(User.class, id);
    }
    
    public User findByEmailAndPassword(String email, String password){
        return  (User) em.createQuery("SELECT u FROM User u WHERE u.email = :email AND u.password = :password", User.class)
                .setParameter("email", email)
                .setParameter("password", password)
                .getSingleResult();
    }
}
