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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Date;

import static javax.persistence.TemporalType.DATE;
import static ru.bitServer.beans.MainBean.*;

public interface UserDao {

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMdd");

    default Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        Properties props = new Properties();
        props.setProperty("user", "orthanc");
        props.setProperty("password", "orthanc");
        java.sql.Connection connection = DriverManager.getConnection(url2, props);
        return connection;
    }

    default void LegacydeleteUser(Users user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(user);
        transaction.commit();
        session.close();
    }

    default void LegacyupdateUser(Users user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(user);
        transaction.commit();
        session.close();
    }

    default void updateUser(Users user) {
        try {
            Connection conn = getConnection();
            Statement statement = conn.createStatement();
            String strSql = "UPDATE bitserver SET rvalue ='"+user.getUname()+"' WHERE bitserver.internalid = '" + user.getUid() + "'";
            statement.executeUpdate(strSql);
            strSql = "UPDATE bitserver SET rvalue ='"+user.getPassword()+"' WHERE bitserver.parentid = '" + user.getUid() + "' AND bitserver.rtype='4'";
            statement.executeUpdate(strSql);
            strSql = "UPDATE bitserver SET rvalue ='"+user.getRuName()+"' WHERE bitserver.parentid = '" + user.getUid() + "' AND bitserver.rtype='5'";
            statement.executeUpdate(strSql);
            strSql = "UPDATE bitserver SET rvalue ='"+user.getRuMiddleName()+"' WHERE bitserver.parentid = '" + user.getUid() + "' AND bitserver.rtype='6'";
            statement.executeUpdate(strSql);
            strSql = "UPDATE bitserver SET rvalue ='"+user.getRuFamily()+"' WHERE bitserver.parentid = '" + user.getUid() + "' AND bitserver.rtype='7'";
            statement.executeUpdate(strSql);
            strSql = "UPDATE bitserver SET rvalue ='"+user.getRole()+"' WHERE bitserver.parentid = '" + user.getUid() + "' AND bitserver.rtype='8'";
            statement.executeUpdate(strSql);
            conn.close();
        }catch (Exception e){
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    default void addUser(Users user) {
        try {
            Connection conn = getConnection();
            Statement statement = conn.createStatement();
            String strSql = "INSERT INTO bitserver (rtype,rvalue) VALUES ( '3','" + user.getUname() + "')";
            statement.executeUpdate(strSql);

            strSql = "SELECT internalId FROM bitserver WHERE bitserver.rvalue = '"+ user.getUname() +"' and bitserver.rtype = '3'";
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(strSql);
            rs.next();
            String bufStudy = rs.getString(1);

            strSql = "INSERT INTO bitserver (rtype,rvalue,parentId) VALUES ( '4','"+ user.getPassword() + "','"+bufStudy+"')";
            statement.executeUpdate(strSql);

            strSql = "INSERT INTO bitserver (rtype,rvalue,parentId) VALUES ( '5','"+ user.getRuName() + "','"+bufStudy+"')";
            statement.executeUpdate(strSql);

            strSql = "INSERT INTO bitserver (rtype,rvalue,parentId) VALUES ( '6','"+ user.getRuMiddleName() + "','"+bufStudy+"')";
            statement.executeUpdate(strSql);

            strSql = "INSERT INTO bitserver (rtype,rvalue,parentId) VALUES ( '7','"+ user.getRuFamily() + "','"+bufStudy+"')";
            statement.executeUpdate(strSql);

            strSql = "INSERT INTO bitserver (rtype,rvalue,parentId) VALUES ( '8','"+ user.getRole() + "','"+bufStudy+"')";
            statement.executeUpdate(strSql);

            conn.close();
        }catch (Exception e){
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
        }
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

    default List<Users> getLegacyBitServerUserList() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Users> criteriaQuery = builder.createQuery(Users.class);
        Root<Users> root = criteriaQuery.from(Users.class);
        criteriaQuery.select(root);
        Query<Users> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    default List<Users> getBitServerUserList() {
        List<Users> resultList = new ArrayList<>();
        try {
            Connection conn = getConnection();
            String resultSQL = "SELECT tag.rvalue, tag1.rvalue, tag2.rvalue, tag3.rvalue, tag4.rvalue, tag5.rvalue, tag.internalid FROM bitserver AS tag" +
                    " INNER JOIN bitserver AS tag1 ON tag.internalid = tag1.parentid AND tag1.rtype = '4'"+//login
                    " INNER JOIN bitserver AS tag2 ON tag.internalid = tag2.parentid AND tag2.rtype = '5'"+//password
                    " INNER JOIN bitserver AS tag3 ON tag.internalid = tag3.parentid AND tag3.rtype = '6'"+//name
                    " INNER JOIN bitserver AS tag4 ON tag.internalid = tag4.parentid AND tag4.rtype = '7'"+//midlename
                    " INNER JOIN bitserver AS tag5 ON tag.internalid = tag5.parentid AND tag5.rtype = '8'";//family
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(resultSQL);
            while (rs.next()) {
                Users bufUser = new Users(rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getString(4),rs.getString(5),rs.getString(6),Long.parseLong(rs.getString(7)));
                resultList.add(bufUser);
            }
            conn.close();
        } catch (Exception  e) {
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
        }
        return resultList;
    }

    default BitServerResources getBitServerResource(String uname) {
        BitServerResources resultResources = new BitServerResources();
        try {
            Connection conn = getConnection();
            String resultSQL = "SELECT tag.rvalue, tag1.rvalue, tag.internalid FROM bitserver AS tag" +
                    " INNER JOIN bitserver AS tag1 ON tag.internalid = tag1.parentid AND tag1.rtype = '2' AND tag.rvalue = '" + uname +"'" ;
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(resultSQL);
            while (rs.next()) {
                resultResources = new BitServerResources(rs.getString(1), rs.getString(2),Long.parseLong(rs.getString(3)));
            }
            conn.close();
        } catch (Exception  e) {
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
        }

        return resultResources;
    }

    default BitServerResources getLegacyBitServerResource(String uname) {
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

    default List<BitServerResources> getLegacyAllBitServerResource() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<BitServerResources> criteriaQuery = builder.createQuery(BitServerResources.class);
        Root<BitServerResources> root = criteriaQuery.from(BitServerResources.class);
        criteriaQuery.select(root);
        Query<BitServerResources> BitServerDBresources = session.createQuery(criteriaQuery);
        return BitServerDBresources.list();
    }

    default List<BitServerResources> getAllBitServerResource() {
        List<BitServerResources> resultList = new ArrayList<>();
        try {
            Connection conn = getConnection();
            String resultSQL = "SELECT tag.rvalue, tag1.rvalue, tag.internalid FROM bitserver AS tag" +
                    " INNER JOIN bitserver AS tag1 ON tag.internalid = tag1.parentid AND tag1.rtype = '2'";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(resultSQL);
            while (rs.next()) {
                BitServerResources bufResource = new BitServerResources(rs.getString(1), rs.getString(2), Long.parseLong(rs.getString(3)));
                resultList.add(bufResource);
            }
            conn.close();
        } catch (Exception  e) {
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
        }
        return resultList;
    }

    default void updateBitServiceResource(BitServerResources bitServerResources) {
        try {
            Connection conn = getConnection();
            Statement statement = conn.createStatement();
            String strSql = "UPDATE bitserver SET rvalue ='"+bitServerResources.getRname()+"' WHERE bitserver.internalid = '" + bitServerResources.getId() + "'";
            statement.executeUpdate(strSql);
            strSql = "UPDATE bitserver SET rvalue ='"+bitServerResources.getRvalue()+"' WHERE bitserver.parentid = '" + bitServerResources.getId() + "'";
            statement.executeUpdate(strSql);
            conn.close();
        }catch (Exception e){
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    default void saveBitServiceResource(BitServerResources bitServerResources) {
        try {
            Connection conn = getConnection();
            Statement statement = conn.createStatement();
            String strSql = "INSERT INTO bitserver (rtype,rvalue) VALUES ( '1','"+ bitServerResources.getRname()+"')";
            statement.executeUpdate(strSql);
            strSql = "SELECT internalId FROM bitserver WHERE bitserver.rvalue = '"+ bitServerResources.getRname()+"' and bitserver.rtype = '1'";
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(strSql);
            rs.next();
            String bufStudy = rs.getString(1);
            strSql = "INSERT INTO bitserver (rtype,rvalue,parentId) VALUES ( '2','"+ bitServerResources.getRvalue()+"','"+bufStudy+"')";
            statement.executeUpdate(strSql);
            conn.close();
        }catch (Exception e){
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
        }
    }

    default void deleteFromBitServerTable(Long id) {
        try {
            Connection conn = getConnection();
            Statement statement = conn.createStatement();
            String strSql = "DELETE FROM bitserver WHERE bitserver.internalId = '"+id.toString()+"' ";
            statement.executeUpdate(strSql);
            conn.close();
        }catch (Exception e){
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
        }
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

    default void createBitServerDateTable(){
        try {
            java.sql.Connection conn = getConnection();
            String resultSQL;
            Statement statement = conn.createStatement();

            resultSQL = "CREATE TABLE bitserver( internalId SERIAL PRIMARY KEY, " +
                    "rtype INTEGER null , " +
                    "rvalue TEXT, " +
                    "parentId INTEGER REFERENCES bitserver(internalId) ON DELETE CASCADE)";
            statement.executeUpdate(resultSQL);

            for(BitServerResources bufResourses:getAllBitServerResource()){
                String sqlInsert = "INSERT INTO bitserver (rtype,rvalue) VALUES ( '1','"+ bufResourses.getRname()+"')";
                statement.executeUpdate(sqlInsert);

                resultSQL = "SELECT internalId FROM bitserver WHERE bitserver.rvalue = '"+ bufResourses.getRname()+"'";
                statement = conn.createStatement();
                ResultSet rs = statement.executeQuery(resultSQL);
                rs.next();
                String bufStudy = rs.getString(1);
                sqlInsert = "INSERT INTO bitserver (rtype,rvalue,parentId) VALUES ( '2','"+ bufResourses.getRvalue()+"','"+bufStudy+"')";
                statement.executeUpdate(sqlInsert);
            }

            for(Users bufUser:getLegacyBitServerUserList()){
                String sqlInsert = "INSERT INTO bitserver (rtype,rvalue) VALUES ( '3','"+ bufUser.getUname()+"')";
                statement.executeUpdate(sqlInsert);

                resultSQL = "SELECT internalId FROM bitserver WHERE bitserver.rvalue = '"+ bufUser.getUname()+"'";
                statement = conn.createStatement();
                ResultSet rs = statement.executeQuery(resultSQL);
                rs.next();
                String buf = rs.getString(1);

                sqlInsert = "INSERT INTO bitserver (rtype,rvalue,parentId) VALUES ( '4','"+ bufUser.getPassword()+"','"+buf+"')";
                statement.executeUpdate(sqlInsert);

                sqlInsert = "INSERT INTO bitserver (rtype,rvalue,parentId) VALUES ( '5','"+ bufUser.getRuName()+"','"+buf+"')";
                statement.executeUpdate(sqlInsert);

                sqlInsert = "INSERT INTO bitserver (rtype,rvalue,parentId) VALUES ( '6','"+ bufUser.getRuMiddleName()+"','"+buf+"')";
                statement.executeUpdate(sqlInsert);

                sqlInsert = "INSERT INTO bitserver (rtype,rvalue,parentId) VALUES ( '7','"+ bufUser.getRuFamily()+"','"+buf+"')";
                statement.executeUpdate(sqlInsert);

                sqlInsert = "INSERT INTO bitserver (rtype,rvalue,parentId) VALUES ( '8','"+ bufUser.getRole()+"','"+buf+"')";
                statement.executeUpdate(sqlInsert);
            }
            resultSQL = "CREATE OR REPLACE FUNCTION public.bitserverdeletedfunc() " +
                    "RETURNS trigger LANGUAGE plpgsql AS $function$ " +
                    "BEGIN " +
                    "IF EXISTS (SELECT 1 FROM bitserver WHERE parentId = old.parentId) THEN " +
                    "SELECT rtype, rvalue FROM bitserver WHERE internalId = old.parentId; " +
                    "ELSE DELETE FROM bitserver WHERE internalId = old.parentId; " +
                    "END IF; " +
                    "RETURN NULL; " +
                    "END; $function$";
            statement.executeUpdate(resultSQL);

            resultSQL = "CREATE TRIGGER bitserverdeleted AFTER DELETE ON public.bitserver " +
                    "FOR EACH ROW " +
                    "EXECUTE PROCEDURE bitserverdeletedfunc()";
            statement.executeUpdate(resultSQL);

            conn.close();
        }catch (Exception e){
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    default List<BitServerStudy> getStudyFromOrthanc(int state, String dateSeachType, Date firstdate, Date seconddate, String strModality) {
        List<BitServerStudy> resultList = new ArrayList<>();
        switch (dateSeachType){
            case "today":
                firstdate = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(firstdate);
                c.add(Calendar.DATE, 1);
                seconddate = c.getTime();
                break;
            case "week":
                seconddate = new Date();
                c = Calendar.getInstance();
                c.setTime(seconddate);
                c.add(Calendar.DATE, -7);
                firstdate = c.getTime();
                break;
            case "mounth":
                seconddate = new Date();
                c = Calendar.getInstance();
                c.setTime(seconddate);
                c.add(Calendar.MONTH, -1);
                firstdate = c.getTime();
                break;
            case "year":
                seconddate = new Date();
                c = Calendar.getInstance();
                c.setTime(seconddate);
                c.add(Calendar.YEAR, -1);
                firstdate = c.getTime();
                break;
            case "yesterday":
                seconddate = new Date();
                c = Calendar.getInstance();
                c.setTime(seconddate);
                c.add(Calendar.DATE, -1);
                firstdate = c.getTime();
                break;
            case "targetdate":
                c = Calendar.getInstance();
                c.setTime(firstdate);
                c.add(Calendar.DATE, 1);
                seconddate = c.getTime();
                break;
            case "range":
                c = Calendar.getInstance();
                c.setTime(seconddate);
                c.add(Calendar.DATE, 1);
                seconddate = c.getTime();
                break;
        }
        try {
            Connection conn = getConnection();
            String resultSQL ="";
            String staticSQL = "SELECT DISTINCT patientid, part1.publicid, tag1.value, tag2.value," +
                    "tag3.value, tag4.value, tag5.value, tag6.value, tag7.value, tag8.value FROM patientrecyclingorder" +
                    " INNER JOIN resources AS part1 ON part1.parentid = patientrecyclingorder.patientid" +
                    " INNER JOIN resources AS part2 ON part2.parentid = part1.internalid" +
                    " INNER JOIN maindicomtags AS tag1 ON tag1.id = part1.internalid AND tag1.taggroup = '16' AND tag1.tagelement = '16'" +  //ФИО
                    " INNER JOIN maindicomtags AS tag2 ON tag2.id = part1.internalid AND tag2.taggroup = '16' AND tag2.tagelement = '32'" +  //STUDY ID ИЗ АППАРАТА
                    " INNER JOIN maindicomtags AS tag3 ON tag3.id = part1.internalid AND tag3.taggroup = '16' AND tag3.tagelement = '48'" +  //BIRTH DAY
                    " INNER JOIN maindicomtags AS tag4 ON tag4.id = part1.internalid AND tag4.taggroup = '16' AND tag4.tagelement = '64'" +  //SEX
                    " INNER JOIN maindicomtags AS tag5 ON tag5.id = part1.internalid AND tag5.taggroup = '8' AND tag5.tagelement = '32'" +  //STUDY DATE
                    " INNER JOIN maindicomtags AS tag6 ON tag6.id = part1.internalid AND tag6.taggroup = '8' AND tag6.tagelement = '128'" +  //SOURCE
                    " INNER JOIN maindicomtags AS tag7 ON tag7.id = part2.internalid AND tag7.taggroup = '24' AND tag7.tagelement = '21'" + //DESCRIPTION
                    " INNER JOIN maindicomtags AS tag8 ON tag8.id = part2.internalid AND tag8.taggroup = '8' AND tag8.tagelement = '96'";    //MODALITY
            Statement statement = conn.createStatement();

            if(dateSeachType.equals("all")){
                resultSQL = staticSQL;
            }else{
                resultSQL = staticSQL + " WHERE tag5.value BETWEEN  '"+FORMAT.format(firstdate)+"' AND '"+FORMAT.format(seconddate)+"'";
            }
            ResultSet rs = statement.executeQuery(resultSQL);

            while (rs.next()) {
                    BitServerStudy bufStudy = new BitServerStudy(rs.getString(2), rs.getString(4), rs.getString(9), getDateFromText(rs.getString(7)),
                            rs.getString(10),rs.getString(3),getDateFromText(rs.getString(5)),rs.getString(6),0);
                    resultList.add(bufStudy);
            }
            conn.close();

        } catch (Exception  e) {
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
        }
        return resultList;
    }

    default Date getDateFromText(String strDate) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date returnDate = new Date();
        if(!strDate.equals("")){
            returnDate = formatter.parse(strDate);
        }
        return returnDate;
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
        try{
            Connection con = DriverManager.getConnection(url, user, password);
            query = "INSERT INTO BitServerStudy (sid, shortid, sdescription, source, sdate, modality, dateaddinbase, patientname, " +
                    "patientbirthdate, patientsex, status, typeresult, userwhoblock) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, clearStr(study.getSid()));
            pstmt.setString(2, clearStr(study.getShortid()));
            pstmt.setString(3, clearStr(study.getSdescription()));
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
        }catch (SQLException e){
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
        if(buf.length()>59){
            buf = buf.substring(0,59);
        }
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
