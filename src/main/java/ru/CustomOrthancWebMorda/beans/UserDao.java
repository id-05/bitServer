package ru.CustomOrthancWebMorda.beans;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.CustomOrthancWebMorda.beans.dao.Usergroup;
import ru.CustomOrthancWebMorda.beans.dao.Users;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Iterator;
import java.util.List;

interface UserDao {

    public default void initialHibernate(){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
    }

    public default Users findById(long id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Users.class, id);
    }

    public default void saveNewUser(Users user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(user);
        transaction.commit();
        session.close();
    }

    public default void deleteUser(Users user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(user);
        transaction.commit();
        session.close();
    }

    public default void updateUser(Users user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(user);
        transaction.commit();
        session.close();
    }

    public default void saveNewUsergroup(Usergroup usergroup) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(usergroup);
        transaction.commit();
        session.close();
    }

    public default void deleteUsergroup(Usergroup usergroup) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(usergroup);
        transaction.commit();
        session.close();
    }

    public default void updateUsergroup(Usergroup usergroup) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(usergroup);
        transaction.commit();
        session.close();
    }

    public default List<Usergroup> getBitServerUsergroupList() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Usergroup> criteriaQuery = builder.createQuery(Usergroup.class);
        Root<Usergroup> root = criteriaQuery.from(Usergroup.class);
        criteriaQuery.select(root);
        Query<Usergroup> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public default Users validateUserAndGetIfExist(String ulogin, String upassword) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = "FROM Users U WHERE U.uname = '" + ulogin + "' and U.password = '" + upassword + "'";
        Query query = session.createQuery(hql);
        List<Users> results = query.list();

        if (results.size() > 0) {
            Iterator<Users> it = results.iterator();
            return (Users) it.next();
        }else{
            Users user = new Users();
            return user;
        }
    }

    public default List<Users> getBitServerUserList() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Users> criteriaQuery = builder.createQuery(Users.class);
        Root<Users> root = criteriaQuery.from(Users.class);
        criteriaQuery.select(root);
        Query<Users> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public default BitServiceDBresources getBitServerResource(String uname) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = "FROM BitServiceDBresources U WHERE U.rname = '" + uname + "'";
        System.out.println("hql = " + hql);
        Query query = session.createQuery(hql);
        List<BitServiceDBresources> results = query.list();

        if (results.size() > 0) {
            Iterator<BitServiceDBresources> it = results.iterator();
            return  (BitServiceDBresources) it.next();
        }
        return null;
    }

    public default void updateBitServiceDBresource(BitServiceDBresources bitServiceDBresources) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(bitServiceDBresources);
        transaction.commit();
        session.close();
    }

}
