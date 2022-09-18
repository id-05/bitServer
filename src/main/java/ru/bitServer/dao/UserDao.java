package ru.bitServer.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.bitServer.util.HibernateSessionFactoryUtil;
import ru.bitServer.util.LogTool;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Date;

import static javax.persistence.TemporalType.DATE;
import static ru.bitServer.beans.MainBean.*;

public interface UserDao {

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    default void deleteUser(Users user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(user);
        transaction.commit();
        session.close();
    }

    default void updateUser(Users user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(user);
        transaction.commit();
        session.close();
    }

    default void saveNewUsergroup(Usergroup usergroup) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(usergroup);
        transaction.commit();
        session.close();
    }

    default void deleteUsergroup(Usergroup usergroup) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(usergroup);
        transaction.commit();
        session.close();
    }

    default void updateUsergroup(Usergroup usergroup) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(usergroup);
        transaction.commit();
        session.close();
    }

    default List<Usergroup> getBitServerUsergroupList() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Usergroup> criteriaQuery = builder.createQuery(Usergroup.class);
        Root<Usergroup> root = criteriaQuery.from(Usergroup.class);
        criteriaQuery.select(root);
        Query<Usergroup> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    default List<Usergroup> getRealBitServerUsergroupList() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Query query;
        String hql= "from Usergroup  where forlocal=:forgroup and status=:pstatus";
        query = session.createQuery(hql);
        query.setParameter("pstatus", "active");
        query.setParameter("forgroup", false);
        List<Usergroup> results = query.list();
        session.close();
        return results;
    }

    default Users validateUserAndGetIfExist(String ulogin, String upassword) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = "FROM Users U WHERE U.uname = '" + ulogin + "' and U.password = '" + upassword + "'";
        Query query =  session.createQuery(hql);
        List<Users> results = query.list();

        if (results.size() > 0) {
            Iterator<Users> it = results.iterator();
            return it.next();
        }else{
            Users user = new Users();
            return user;
        }
    }

    default Users getUserById(String userid){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = "FROM Users U WHERE U.id = '" + userid + "'";
        Query query = session.createQuery(hql);
        List<Users> results = query.list();

        if (results.size() > 0) {
            Iterator<Users> it = results.iterator();
            return it.next();
        }else{
            Users user = new Users();
            return user;
        }
    }

    default Usergroup getUsergroupById(String groupid){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = "FROM Usergroup U WHERE U.id = '" + groupid + "'";
        Query query = session.createQuery(hql);
        List<Usergroup> results = query.list();

        if (results.size() > 0) {
            Iterator<Usergroup> it = results.iterator();
            return it.next();
        }else{
            Usergroup usergroup = new Usergroup();
            return usergroup;
        }
    }

    default List<Users> getBitServerUserList() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Users> criteriaQuery = builder.createQuery(Users.class);
        Root<Users> root = criteriaQuery.from(Users.class);
        criteriaQuery.select(root);
        Query<Users> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    default BitServerResources getBitServerResource(String uname) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = "FROM BitServerResources U WHERE U.rname = '" + uname + "'";
        Query query = session.createQuery(hql);
        List<BitServerResources> results = query.list();

        if (results.size() > 0) {
            Iterator<BitServerResources> it = results.iterator();
            return  it.next();
        }
        return null;
    }

    static BitServerResources getStaticBitServerResource(String uname) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = "FROM BitServerResources U WHERE U.rname = '" + uname + "'";
        Query query = session.createQuery(hql);
        List<BitServerResources> results = query.list();

        if (results.size() > 0) {
            Iterator<BitServerResources> it = results.iterator();
            return  it.next();
        }
        return null;
    }

    default List<BitServerResources> getAllBitServerResource() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<BitServerResources> criteriaQuery = builder.createQuery(BitServerResources.class);
        Root<BitServerResources> root = criteriaQuery.from(BitServerResources.class);
        criteriaQuery.select(root);
        Query<BitServerResources> BitServerDBresources = session.createQuery(criteriaQuery);
        return BitServerDBresources.list();
    }

    default void updateBitServiceResource(BitServerResources bitServerResources) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(bitServerResources);
        transaction.commit();
        session.close();
    }

    default void saveBitServiceResource(BitServerResources bitServerResources) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(bitServerResources);
        transaction.commit();
        session.close();
    }

    default void deleteBitServerResource(BitServerResources bitServerResources) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(bitServerResources);
        transaction.commit();
        session.close();
    }

    default List<BitServerModality> getAllBitServerModality() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<BitServerModality> criteriaQuery = builder.createQuery(BitServerModality.class);
        Root<BitServerModality> root = criteriaQuery.from(BitServerModality.class);
        criteriaQuery.select(root);
        Query<BitServerModality> BitServerDBresources = session.createQuery(criteriaQuery);
        return BitServerDBresources.list();
    }

    default StringBuilder getAllNameBitServerModality() {

        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<BitServerModality> criteriaQuery = builder.createQuery(BitServerModality.class);
        Root<BitServerModality> root = criteriaQuery.from(BitServerModality.class);
        criteriaQuery.select(root);
        Query<BitServerModality> BitServerDBresources = session.createQuery(criteriaQuery);
        StringBuilder bufStr = new StringBuilder();
        for(BitServerModality bufModaliti:BitServerDBresources.list()){
            if (BitServerDBresources.list().size() < 1) {
                bufStr = new StringBuilder("" + bufModaliti.getName() + "");
            } else{
                bufStr.append(",").append(bufModaliti.getName());
            }
        }

        return bufStr;
    }

    default void updateBitServerModality(BitServerModality bitServerModality) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(bitServerModality);
        transaction.commit();
        session.close();
    }

    default void saveBitServerModality(BitServerModality bitServerModality) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(bitServerModality);
        transaction.commit();
        session.close();
    }

    default void deleteBitServerModality(BitServerModality bitServerModality) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(bitServerModality);
        transaction.commit();
        session.close();
    }

    default List<BitServerStudy> getBitServerStudyModality(String value) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = "FROM BitServerStudy U WHERE U.modality = '" + value + "'";
        Query query = session.createQuery(hql);
        List<BitServerStudy> results = query.list();
        return results;
    }

    default List<BitServerStudy> getBitServerStudySource(String value) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = "FROM BitServerStudy U WHERE U.source = '" + value + "'";
        Query query = session.createQuery(hql);
        List<BitServerStudy> results = query.list();
        return results;
    }

    default List<BitServerStudy> getBitServerStudy(int state, String dateSeachType, Date firstdate, Date seconddate, String strModality) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        switch (dateSeachType){
            case "today":
                firstdate = new Date();
                seconddate = new Date();
//                Calendar cal2 = new GregorianCalendar();
//                cal2.set(2012,10,10,0,0);
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
        firstdate.setHours(00);
        firstdate.setMinutes(00);
        firstdate.setSeconds(00);
        seconddate.setHours(23);
        seconddate.setMinutes(59);
        seconddate.setSeconds(59);
        Query query;
        String hql;
        if(state==5){
            if(dateSeachType.equals("all")){
                System.out.println("start query ");
                hql= "from BitServerStudy";
                System.out.println("start query hql= "+hql);
                query = session.createQuery(hql);
            }else{
                //extract(DAY FROM CURRENT_TIMESTAMP())
                hql = "from BitServerStudy  where sdate BETWEEN :frmdate and  :todate";
                query = session.createQuery(hql);
                query.setParameter("frmdate", firstdate,DATE);
                query.setParameter("todate", seconddate,DATE);
            }
        }else{
            if(dateSeachType.equals("all")){
                hql= "from BitServerStudy  where status=:pstatus";
                query = session.createQuery(hql);
                query.setParameter("pstatus", state);
            }else{
                hql= "from BitServerStudy  where status=:pstatus and sdate BETWEEN :frmdate and :todate";
                query = session.createQuery(hql);
                query.setParameter("pstatus", state);
                query.setParameter("frmdate", firstdate,DATE);
                query.setParameter("todate", seconddate,DATE);
            }
        }
        System.out.println("qls = "+query.list().size());
        List<BitServerStudy> results = query.list();
        session.close();
//        List<BitServerStudy> results2 = new ArrayList<>();
//        for(BitServerStudy bufStudy:results){
//            if(strModality.contains(bufStudy.getModality())){
//                results2.add(bufStudy);
//            }
//        }
        return results;
    }

    default List<BitServerStudy> getMyStudy(Users currentUser) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Query query;
        String hql= "from BitServerStudy  where status=:pstatus and userwhodiagnost=:puser";
        query = session.createQuery(hql);
        query.setParameter("pstatus", 2);
        query.setParameter("puser", currentUser.getUid().toString());
        List<BitServerStudy> results = query.list();
        session.close();
        return results;
    }

    default List<BitServerStudy> getBitServerStudyOnAnalisis(String usergroup) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Query query;
        String hql= "from BitServerStudy  where usergroupwhosees=:pgroup and status=:pstatus";
        query = session.createQuery(hql);
        query.setParameter("pgroup", usergroup);
        query.setParameter("pstatus", 1);
        List<BitServerStudy> results = query.list();
        session.close();
        return results;
    }

    default BitServerStudy getStudyById(String studyid){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = "FROM BitServerStudy U WHERE U.id = '" + studyid + "'";
        Query query = session.createQuery(hql);
        List<BitServerStudy> results = query.list();

        if (results.size() > 0) {
            Iterator<BitServerStudy> it = results.iterator();
            return it.next();
        }else{
            BitServerStudy bitServerStudy = new BitServerStudy();
            return bitServerStudy;
        }
    }

    default List<BitServerStudy> getAllBitServerStudy() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<BitServerStudy> criteriaQuery = builder.createQuery(BitServerStudy.class);
        Root<BitServerStudy> root = criteriaQuery.from(BitServerStudy.class);
        criteriaQuery.select(root);
        System.out.println("START SQL");
        Query<BitServerStudy> query = session.createQuery(criteriaQuery);
        System.out.println("qls = "+query.list().size());
        return query.getResultList();
    }

    default List<BitServerStudy> getAllBitServerStudyJDBC() {
        List<BitServerStudy> resultList = new ArrayList<>();
        String reqizite = "sid, shortid, sdescription, sdate, modality, patientname, patientbirthdate, patientsex, status";
        String query = "select "+reqizite+" from BitServerStudy";
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                BitServerStudy buf = new BitServerStudy(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDate(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getDate(7),
                        rs.getString(8),
                        rs.getInt(9));
                resultList.add(buf);
            }
            con.close();
        } catch (Exception e) {
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
        }
        return resultList;
    }

    default void deleteAllStudyJDBC() throws SQLException {
        String query = "TRUNCATE BitServerStudy";
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
            stmt.executeQuery(query);
            con.close();
        } catch (Exception e) {
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
        }
    }

    default List<BitServerStudy> getAllBitServerStudyOnlyId() throws SQLException {
        String reqizite = "sid, sdate";
        String query = "select "+reqizite+" from BitServerStudy";
        List<BitServerStudy> resultList = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                BitServerStudy buf = new BitServerStudy(
                        rs.getString(1),
                        rs.getDate(2));
                resultList.add(buf);
            }
        }catch (Exception e){
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
        }
        return resultList;
    }

    default List<BitServerStudy> getAllBitServerStudy(Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<BitServerStudy> criteriaQuery = builder.createQuery(BitServerStudy.class);
        Root<BitServerStudy> root = criteriaQuery.from(BitServerStudy.class);
        criteriaQuery.select(root);
        System.out.println("START SQL");
        Query<BitServerStudy> query = session.createQuery(criteriaQuery);
        System.out.println("qls = "+query.list().size());
        return query.getResultList();
    }

//    default void addStudy(BitServerStudy study) {
//        Date date = study.getSdate();
//        date.setHours(23);
//        study.setSdate(date);
//        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
//        Transaction transaction = session.beginTransaction();
//        session.save(study);
//        transaction.commit();
//        session.close();
//    }

    default void addStudyJDBC(BitServerStudy study) {
        String query = "";
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            query = "INSERT INTO BitServerStudy (sid, shortid, sdescription, source, sdate, modality, dateaddinbase, patientname, " +
                    "patientbirthdate, patientsex, status, typeresult, userwhoblock) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, study.getSid());
            pstmt.setString(2, clearStr(study.getShortid()));
            pstmt.setString(3, study.getSdescription());
            pstmt.setString(4, clearStr(study.getSource()));
            pstmt.setDate(5, checkDate(study.getSdate()));
            pstmt.setString(6, study.getModality());
            pstmt.setDate(7, new java.sql.Date(new Date().getTime()));
            pstmt.setString(8, clearStr(study.getPatientname()));
            pstmt.setDate(9, new java.sql.Date(study.getPatientbirthdate().getTime()));
            pstmt.setString(10, clearStr(study.getPatientsex()));
            pstmt.setString(11, "0");
            pstmt.setString(12, "0");
            pstmt.setString(13, "0");
            pstmt.execute();
            con.close();
        }catch (Exception e){
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ "Error sql add study: "+e.getMessage()+" sql = "+query);
        }
    }

    default java.sql.Date checkDate(Date bufDate){
        java.sql.Date returnDate = null;
        if(bufDate.equals(null)){
            returnDate = new java.sql.Date(new Date().getTime());
        }else{
            returnDate = new java.sql.Date(bufDate.getTime());
        }
        return returnDate;
    }

    default String clearStr(String buf){
        String result = "";
        String taboo = "^'*/\\\"~";
        if(!buf.equals("")) {
            for (char c : taboo.toCharArray()) {
                result = buf.replace(c, ' ');
            }
        }else{
            result=" ";
        }
        return result;
    }

    default void updateStudy(BitServerStudy study) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(study);
        transaction.commit();
        session.close();
    }

    default void deleteStudy(BitServerStudy study) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(study);
        transaction.commit();
        session.close();

    }

    default void saveNewRule(BitServerScheduler rule) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(rule);
        transaction.commit();
        session.close();
    }

    default void deleteRule(BitServerScheduler rule) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(rule);
        transaction.commit();
        session.close();
    }

    default void updateRule(BitServerScheduler rule) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(rule);
        transaction.commit();
        session.close();
    }

    default List<BitServerScheduler> getAllBitServerSheduler() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<BitServerScheduler> criteriaQuery = builder.createQuery(BitServerScheduler.class);
        Root<BitServerScheduler> root = criteriaQuery.from(BitServerScheduler.class);
        criteriaQuery.select(root);
        Query<BitServerScheduler> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

}
