package ru.bitServer.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.bitServer.util.HibernateSessionFactoryUtil;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static javax.persistence.TemporalType.DATE;

public interface UserDao {

    final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public default void initialHibernate(){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
    }

    public default Users findById(long id) {
        return (Users) HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Users.class, id);
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
        session.saveOrUpdate(usergroup);
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

    public default List<Usergroup> getRealBitServerUsergroupList() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Query query = null;
        String hql= "from Usergroup  where forlocal=:forgroup and status=:pstatus";
        query = session.createQuery(hql);
        query.setParameter("pstatus", "active");
        query.setParameter("forgroup", false);
        List<Usergroup> results = query.list();
        session.close();
        return results;
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

    public default Users getUserById(String userid){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = "FROM Users U WHERE U.id = '" + userid + "'";
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

    public default Usergroup getUsergroupById(String groupid){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = "FROM Usergroup U WHERE U.id = '" + groupid + "'";
        Query query = session.createQuery(hql);
        List<Usergroup> results = query.list();

        if (results.size() > 0) {
            Iterator<Usergroup> it = results.iterator();
            return (Usergroup) it.next();
        }else{
            Usergroup usergroup = new Usergroup();
            return usergroup;
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

    public default BitServerResources getBitServerResource(String uname) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = "FROM BitServerResources U WHERE U.rname = '" + uname + "'";
        Query query = session.createQuery(hql);
        List<BitServerResources> results = query.list();

        if (results.size() > 0) {
            Iterator<BitServerResources> it = results.iterator();
            return  (BitServerResources) it.next();
        }
        return null;
    }

    public default List<BitServerResources> getAllBitServerResource() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<BitServerResources> criteriaQuery = builder.createQuery(BitServerResources.class);
        Root<BitServerResources> root = criteriaQuery.from(BitServerResources.class);
        criteriaQuery.select(root);
        Query<BitServerResources> BitServerDBresources = session.createQuery(criteriaQuery);
        return BitServerDBresources.list();
    }

    public default void updateBitServiceResource(BitServerResources bitServerResources) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(bitServerResources);
        transaction.commit();
        session.close();
    }

    public default List<BitServerStudy> getBitServerStudy(int state, String dateSeachType, Date firstdate, Date seconddate) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        switch (dateSeachType){
            case "today":
                firstdate = new Date();
                seconddate = new Date();
                break;
            case "yesterday":
                Instant now = Instant.now();
                Instant yesterday = now.minus(1, ChronoUnit.DAYS);
                firstdate = Date.from(yesterday);
                seconddate = Date.from(yesterday);
                break;
            case "targetdate":
                Calendar cal = Calendar.getInstance();
                cal.set(Integer.parseInt(new SimpleDateFormat("yyyy").format(firstdate)), Integer.parseInt(new SimpleDateFormat("MM").format(firstdate))-1
                        ,Integer.parseInt(new SimpleDateFormat("dd").format(firstdate)),23,59);
                seconddate = cal.getTime();
                break;
        }
        Query query = null;

        if(state==5){
            if(dateSeachType.equals("all")){
                String hql= "from BitServerStudy";
                query = session.createQuery(hql);
            }else{
                String hql= "from BitServerStudy  where sdate BETWEEN :frmdate and :todate";
                query = session.createQuery(hql);
                query.setParameter("frmdate", firstdate,DATE);
                query.setParameter("todate", seconddate,DATE);
            }
        }else{
            if(dateSeachType.equals("all")){
                String hql= "from BitServerStudy  where status=:pstatus";
                query = session.createQuery(hql);
                query.setParameter("pstatus", state);
            }else{
                String hql= "from BitServerStudy  where status=:pstatus and sdate BETWEEN :frmdate and :todate";
                query = session.createQuery(hql);
                System.out.println("hql "+hql);
                System.out.println("date1 "+firstdate.toString());
                System.out.println("date2 "+seconddate.toString());
                query.setParameter("pstatus", state);
                query.setParameter("frmdate", firstdate,DATE);
                query.setParameter("todate", seconddate,DATE);
            }
        }

        List<BitServerStudy> results = query.list();
        session.close();
        return results;
    }

    public default List<BitServerStudy> getMyStudy(Users currentUser) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Query query = null;
        String hql= "from BitServerStudy  where status=:pstatus and userwhodiagnost=:puser";
        query = session.createQuery(hql);
        query.setParameter("pstatus", 2);
        query.setParameter("puser", currentUser.getUid().toString());
        List<BitServerStudy> results = query.list();
        session.close();
        return results;
    }

    public default List<BitServerStudy> getAllHasResultStudies() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Query query = null;
        String hql= "from BitServerStudy  where status=:pstatus";
        query = session.createQuery(hql);
        query.setParameter("pstatus", 2);
        List<BitServerStudy> results = query.list();
        session.close();
        return results;
    }

    public default List<BitServerStudy> getBitServerStudyOnAnalisis(String usergroup) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Query query = null;
        String hql= "from BitServerStudy  where usergroupwhosees=:pgroup and status=:pstatus";
        query = session.createQuery(hql);
        query.setParameter("pgroup", usergroup);
        query.setParameter("pstatus", 1);
        List<BitServerStudy> results = query.list();
        session.close();
        return results;
    }

    public default List<BitServerStudy> getMyBitServerStudy(Users requestUser) {
        System.out.println("requestuser "+requestUser.getUid());
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Query query = null;
        String hql= "from BitServerStudy  where userwhodiagnost=:userid";
        System.out.println("hql = "+hql);
        query = session.createQuery(hql);
        query.setParameter("userid", requestUser.getUid());
        List<BitServerStudy> results = query.list();
        session.close();
        return results;
    }

    public default BitServerStudy getStudyById(String studyid){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = "FROM BitServerStudy U WHERE U.id = '" + studyid + "'";
        Query query = session.createQuery(hql);
        List<BitServerStudy> results = query.list();

        if (results.size() > 0) {
            Iterator<BitServerStudy> it = results.iterator();
            return (BitServerStudy) it.next();
        }else{
            BitServerStudy bitServerStudy = new BitServerStudy();
            return bitServerStudy;
        }
    }

    public default List<BitServerStudy> getAllBitServerStudy() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<BitServerStudy> criteriaQuery = builder.createQuery(BitServerStudy.class);
        Root<BitServerStudy> root = criteriaQuery.from(BitServerStudy.class);
        criteriaQuery.select(root);
        Query<BitServerStudy> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public default void addStudyInBitServerStudyTable(BitServerStudy study) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(study);
        transaction.commit();
        session.close();
    }

    public default void updateStudyInBitServerStudyTable(BitServerStudy study) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(study);
        transaction.commit();
        session.close();
    }

    public default void saveNewRule(BitServerScheduler rule) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(rule);
        transaction.commit();
        session.close();
    }

    public default void deleteRule(BitServerScheduler rule) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(rule);
        transaction.commit();
        session.close();
    }

    public default void updateRule(BitServerScheduler rule) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(rule);
        transaction.commit();
        session.close();
    }

    public default List<BitServerScheduler> getAllBitServerSheduler() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<BitServerScheduler> criteriaQuery = builder.createQuery(BitServerScheduler.class);
        Root<BitServerScheduler> root = criteriaQuery.from(BitServerScheduler.class);
        criteriaQuery.select(root);
        Query<BitServerScheduler> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

}
