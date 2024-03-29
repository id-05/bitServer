package ru.bitServer.dao;

import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import ru.bitServer.dicom.OrthancSerie;
import ru.bitServer.service.TimetableTask;
import ru.bitServer.util.LogTool;
import ru.bitServer.util.OrthancRestApi;
import ru.bitServer.util.OrthancSettingSnapshot;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import static ru.bitServer.beans.MainBean.*;

public interface UserDao {

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMdd");

    default Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        Properties props = new Properties();
        props.setProperty("user", "orthanc");
        props.setProperty("password", "orthanc");
        return DriverManager.getConnection(url, props);
    }

    default void updateUser(BitServerUser user) {
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
        }
    }

    default void addUser(BitServerUser user) {
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

    default BitServerUser validateUserAndGetIfExist(String ulogin, String upassword) {
        BitServerUser user = null;
        try {
            Connection conn = getConnection();
            String resultSQL = "SELECT tag.rvalue, tag1.rvalue, tag2.rvalue, tag3.rvalue, tag4.rvalue, tag5.rvalue, tag.internalid FROM bitserver AS tag" +
                    " INNER JOIN bitserver AS tag1 ON tag.internalid = tag1.parentid AND tag1.rtype = '4'"+
                    " INNER JOIN bitserver AS tag2 ON tag.internalid = tag2.parentid AND tag2.rtype = '5'"+
                    " INNER JOIN bitserver AS tag3 ON tag.internalid = tag3.parentid AND tag3.rtype = '6'"+
                    " INNER JOIN bitserver AS tag4 ON tag.internalid = tag4.parentid AND tag4.rtype = '7'"+
                    " INNER JOIN bitserver AS tag5 ON tag.internalid = tag5.parentid AND tag5.rtype = '8' WHERE tag.rvalue = '"+ulogin+"' AND tag1.rvalue = '"+upassword+"'";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(resultSQL);

            while (rs.next()) {
                user = new BitServerUser(rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getString(4),rs.getString(5),rs.getString(6),Long.parseLong(rs.getString(7)));
            }
            conn.close();
        } catch (Exception  e) {
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
            return user;
        }
        return user;
    }

    default BitServerUser getUserById(String uid){
        BitServerUser user = new BitServerUser();
        try {
            Connection conn = getConnection();
            String resultSQL = "SELECT tag.rvalue, tag1.rvalue, tag2.rvalue, tag3.rvalue, tag4.rvalue, tag5.rvalue, tag.internalid FROM bitserver AS tag" +
                    " INNER JOIN bitserver AS tag1 ON tag.internalid = tag1.parentid AND tag1.rtype = '4'"+
                    " INNER JOIN bitserver AS tag2 ON tag.internalid = tag2.parentid AND tag2.rtype = '5'"+
                    " INNER JOIN bitserver AS tag3 ON tag.internalid = tag3.parentid AND tag3.rtype = '6'"+
                    " INNER JOIN bitserver AS tag4 ON tag.internalid = tag4.parentid AND tag4.rtype = '7'"+
                    " INNER JOIN bitserver AS tag5 ON tag.internalid = tag5.parentid AND tag5.rtype = '8' WHERE tag.internalid = '"+uid+"'";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(resultSQL);
            while (rs.next()) {
                user = new BitServerUser(rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getString(4),rs.getString(5),rs.getString(6),Long.parseLong(rs.getString(7)));

            }
            conn.close();
        } catch (Exception  e) {
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
        }
        return user;
    }

    default ArrayList<OrthancSerie> getSeriesFromStudy(String uid){
        ArrayList<OrthancSerie> seriesList = new ArrayList<>();
        OrthancRestApi connection = new OrthancRestApi(mainServer.getIpaddress(),mainServer.getPort(),mainServer.getLogin(),mainServer.getPassword());
        try {
            Connection conn = getConnection();
            String resultSQL = "SELECT tag1.resourcetype, tag1.publicid, tag1.internalid, tag2.value FROM resources AS tag" +
                    " INNER JOIN resources AS tag1 ON tag.internalid = tag1.parentid AND tag1.resourcetype = '2'" +
                    " INNER JOIN maindicomtags AS tag2 ON tag1.internalid = tag2.id AND tag2.tagelement = '4158' WHERE tag.publicid = '"+uid+"'";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(resultSQL);
            while (rs.next()) {
                String resultSubSQL = "SELECT publicid FROM resources  WHERE parentid = '"+rs.getString(3)+"'";
                Statement subStatement = conn.createStatement();
                ResultSet subRs = subStatement.executeQuery(resultSubSQL);
                ArrayList<byte[]> bufInstances = new ArrayList<>();
                while(subRs.next()){
                    bufInstances.add(getDicomAsByte(connection,subRs.getString(1)));
                }
                seriesList.add(new OrthancSerie(rs.getString(2), rs.getString(4),bufInstances));
            }
            conn.close();
        } catch (Exception  e) {
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
        }
        return seriesList;
    }

    default byte[] getDicomAsByte(OrthancRestApi connection, String fileName) throws Exception {
        String url="/instances/"+fileName+"/file";
        HttpURLConnection conn = connection.makeGetConnection (url);
        InputStream inputStream = conn.getInputStream();
        return IOUtils.toByteArray(inputStream);
    }

    default BitServerUser getUserByLogin(String login){
        BitServerUser user = new BitServerUser();
        try {
            Connection conn = getConnection();
            String resultSQL = "SELECT tag.rvalue, tag1.rvalue, tag2.rvalue, tag3.rvalue, tag4.rvalue, tag5.rvalue, tag.internalid FROM bitserver AS tag" +
                    " INNER JOIN bitserver AS tag1 ON tag.internalid = tag1.parentid AND tag1.rtype = '4'"+
                    " INNER JOIN bitserver AS tag2 ON tag.internalid = tag2.parentid AND tag2.rtype = '5'"+
                    " INNER JOIN bitserver AS tag3 ON tag.internalid = tag3.parentid AND tag3.rtype = '6'"+
                    " INNER JOIN bitserver AS tag4 ON tag.internalid = tag4.parentid AND tag4.rtype = '7'"+
                    " INNER JOIN bitserver AS tag5 ON tag.internalid = tag5.parentid AND tag5.rtype = '8' WHERE tag1.rvalue = '"+login+"'";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(resultSQL);
            while (rs.next()) {
                user = new BitServerUser(rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getString(4),rs.getString(5),rs.getString(6),Long.parseLong(rs.getString(7)));

            }
            conn.close();
        } catch (Exception  e) {
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
        }
        return user;
    }

    default ArrayList<BitServerUser> getAllBitServerUserList() {
        ArrayList<BitServerUser> resultList = new ArrayList<>();
        try {
            Connection conn = getConnection();
            String resultSQL = "SELECT tag.rvalue, tag1.rvalue, tag2.rvalue, tag3.rvalue, tag4.rvalue, tag5.rvalue, tag.internalid FROM bitserver AS tag" +
                    " INNER JOIN bitserver AS tag1 ON tag.internalid = tag1.parentid AND tag1.rtype = '4'"+
                    " INNER JOIN bitserver AS tag2 ON tag.internalid = tag2.parentid AND tag2.rtype = '5'"+
                    " INNER JOIN bitserver AS tag3 ON tag.internalid = tag3.parentid AND tag3.rtype = '6'"+
                    " INNER JOIN bitserver AS tag4 ON tag.internalid = tag4.parentid AND tag4.rtype = '7'"+
                    " INNER JOIN bitserver AS tag5 ON tag.internalid = tag5.parentid AND tag5.rtype = '8'";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(resultSQL);
            while (rs.next()) {
                BitServerUser bufUser = new BitServerUser(rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getString(4),rs.getString(5),rs.getString(6),Long.parseLong(rs.getString(7)));
                resultList.add(bufUser);
            }
            conn.close();
        } catch (Exception  e) {
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
        }
        return resultList;
    }

    default List<BitServerGroup> getAllBitServerGroupList(){
        List<BitServerGroup> resultList = new ArrayList<>();
        try {
            Connection conn = getConnection();
            String resultSQL = "SELECT DISTINCT tag.internalid, tag.rvalue  FROM bitserver AS tag" +
                    " INNER JOIN bitserver AS tag1 ON tag.internalid = tag1.parentid AND tag1.rtype = '10'";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(resultSQL);
            while (rs.next()) {
                ArrayList<BitServerUser> userList = new ArrayList<>();

                {
                    try{
                        Connection connSub = getConnection();
                        String resultSQLSub = "SELECT tag.internalid, tag.rvalue, tag.parentid  FROM bitserver AS tag" +
                                " WHERE tag.parentid = '"+rs.getLong(1)+"' AND tag.rtype = '10'";
                        Statement statementSub = connSub.createStatement();
                        ResultSet rsSub = statementSub.executeQuery(resultSQLSub);
                        while (rsSub.next()) {
                            userList.add(getUserById(rsSub.getString(2)));
                            connSub.close();
                        }
                    }catch (Exception e){
                        LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
                    }
                }

                BitServerGroup bufGroup = new BitServerGroup(rs.getLong(1), rs.getString(2), userList);
                resultList.add(bufGroup);
            }
            conn.close();
        } catch (Exception  e) {
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
        }
        return resultList;
    }

    default void saveNewBitServiceGroup(BitServerGroup bitServerGroup) {
        try {
            Connection conn = getConnection();
            Statement statement = conn.createStatement();
            String strSql = "INSERT INTO bitserver (rtype,rvalue) VALUES ( '9','" + bitServerGroup.getRuName() + "')";
            statement.executeUpdate(strSql);
            strSql = "SELECT internalId FROM bitserver WHERE bitserver.rvalue = '" + bitServerGroup.getRuName() + "' and bitserver.rtype = '9'";
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(strSql);
            rs.next();
            String buf = rs.getString(1);
            for(BitServerUser bufUser:bitServerGroup.getUserList()){
                strSql = "INSERT INTO bitserver (rtype,rvalue,parentId) VALUES ( '10','" + bufUser.getUid() + "','"+buf+"')";
                statement.executeUpdate(strSql);
            }
            conn.close();
        }catch (Exception e){
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
        }
    }

    default BitServerResources getBitServerResource(String uname) {
        BitServerResources resultResources = new BitServerResources("empty","empty");
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

    static BitServerResources getStaticBitServerResource(String uname) {
        BitServerResources resultResources = new BitServerResources();
        try {
            Class.forName("org.postgresql.Driver");
            Properties props = new Properties();
            props.setProperty("user", "orthanc");
            props.setProperty("password", "orthanc");
            Connection conn = DriverManager.getConnection(url, props);
            String resultSQL = "SELECT tag.rvalue, tag1.rvalue, tag.internalid FROM bitserver AS tag" +
                    " INNER JOIN bitserver AS tag1 ON tag.internalid = tag1.parentid AND tag1.rtype = '2' AND tag.rvalue = '" + uname +"'" ;
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(resultSQL);
            while (rs.next()) {
                resultResources = new BitServerResources(rs.getString(1), rs.getString(2),Long.parseLong(rs.getString(3)));
            }
            conn.close();
        } catch (Exception  e) {
            LogTool.getLogger().error("getStaticBitServerResource from UserDao: "+ e.getMessage());
        }
        return resultResources;
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

    default void saveTask(TimetableTask newTask) {
        JsonObject jsonOb = new JsonObject();
        jsonOb.addProperty("time", newTask.getStrTime());
        jsonOb.addProperty("action", newTask.getAction());
        jsonOb.addProperty("source", newTask.getAltSource());
        jsonOb.addProperty("destination", newTask.getDestination());
        if(newTask.getId()==0){
            try {
                Connection conn = getConnection();
                Statement statement = conn.createStatement();
                String strSql = "INSERT INTO bitserver (rtype,rvalue) VALUES ( '11','"+ jsonOb.toString()+"')";
                statement.executeUpdate(strSql);
                conn.close();
            }catch (Exception e){
                LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
            }
        }else{
            try {
                Connection conn = getConnection();
                Statement statement = conn.createStatement();
                String strSql = "UPDATE bitserver SET rvalue ='" + jsonOb.toString() + "' WHERE bitserver.internalid = '" + newTask.getId() + "'";
                statement.executeUpdate(strSql);
            }catch (Exception e){
                LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
            }
        }
    }

    default void saveSnapshot(OrthancSettingSnapshot snapshot) {
        ArrayList<OrthancSettingSnapshot> snapshots = getAllOrthancSnapshots();
        if(snapshots.size()>9){
            deleteFromBitServerTable(Long.parseLong(snapshots.get(1).getId()));
        }

        JsonObject jsonOb = new JsonObject();
        jsonOb.addProperty("date", snapshot.getDate());
        jsonOb.addProperty("description", snapshot.getDescription());
        jsonOb.add("settings", snapshot.getSettingJson());
        try {
            Connection conn = getConnection();
            Statement statement = conn.createStatement();
            String strSql = "INSERT INTO bitserver (rtype,rvalue) VALUES ( '12','"+ jsonOb.toString()+"')";
            statement.executeUpdate(strSql);
            conn.close();
        }catch (Exception e){
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
        }
    }

    default ArrayList<TimetableTask> getAllTasks() {
        ArrayList<TimetableTask> resultList = new ArrayList<>();
        try {
            Connection conn = getConnection();
            String resultSQL = "SELECT internalid, rvalue FROM bitserver WHERE rtype = '11'";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(resultSQL);
            while (rs.next()) {
                TimetableTask bufTask = new TimetableTask(rs.getString(1),rs.getString(2));
                resultList.add(bufTask);
            }
            LogTool.getLogger().info(this.getClass().getSimpleName()+":getAllTasks resultList.size = "+resultList.size());
            conn.close();
        } catch (Exception  e) {
            LogTool.getLogger().error(this.getClass().getSimpleName()+": (procedure getAllTasks) "+ e.getMessage());
        }
        return resultList;
    }

    default ArrayList<OrthancSettingSnapshot> getAllOrthancSnapshots() {
        ArrayList<OrthancSettingSnapshot> resultList = new ArrayList<>();
        try {
            Connection conn = getConnection();
            String resultSQL = "SELECT internalid, rvalue FROM bitserver WHERE rtype = '12'";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(resultSQL);
            while (rs.next()) {
                OrthancSettingSnapshot bufTask = new OrthancSettingSnapshot(rs.getString(1),rs.getString(2));
                resultList.add(bufTask);
            }
            conn.close();
        } catch (Exception  e) {
            LogTool.getLogger().error(this.getClass().getSimpleName()+": (procedure getAllSnapshot) "+ e.getMessage());
        }
        return resultList;
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

    default List<BitServerStudy> getStudyFromOrthanc(int state, String dateSeachType, Date firstdate, Date seconddate, String source) {
        List<BitServerStudy> resultList = new ArrayList<>();
        Calendar c;
        switch (dateSeachType){
            case "today":
                firstdate = new Date();
                seconddate = firstdate;
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
                seconddate = firstdate;
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
            String resultSQL;
            String staticSQL = "SELECT DISTINCT patientid, part1.publicid, tag1.value, tag2.value," +
                    "tag3.value, tag4.value, tag5.value, tag6.value, tag7.value, tag8.value, tag9.value, tag10.value, tag11.value, tag12.value FROM patientrecyclingorder" +
                    " INNER JOIN resources AS part1 ON part1.parentid = patientrecyclingorder.patientid" +
                    " INNER JOIN resources AS part2 ON part2.parentid = part1.internalid" +
                    " INNER JOIN resources AS part3 ON part3.parentid = part2.internalid" +
                    " LEFT JOIN maindicomtags AS tag1 ON tag1.id = part1.internalid AND tag1.taggroup = '16' AND tag1.tagelement = '16'" +  //FIO
                    " LEFT JOIN maindicomtags AS tag2 ON tag2.id = part1.internalid AND tag2.taggroup = '16' AND tag2.tagelement = '32'" +  //STUDY ID ИЗ АППАРАТА
                    " LEFT JOIN maindicomtags AS tag3 ON tag3.id = part1.internalid AND tag3.taggroup = '16' AND tag3.tagelement = '48'" +  //BIRTH DAY
                    " LEFT JOIN maindicomtags AS tag4 ON tag4.id = part1.internalid AND tag4.taggroup = '16' AND tag4.tagelement = '64'" +  //SEX
                    " LEFT JOIN maindicomtags AS tag5 ON tag5.id = part1.internalid AND tag5.taggroup = '8' AND tag5.tagelement = '32'" +   //STUDY DATE
                    " LEFT JOIN maindicomtags AS tag6 ON tag6.id = part1.internalid AND tag6.taggroup = '8' AND tag6.tagelement = '128'" +  //!!error delete this
                    " LEFT JOIN maindicomtags AS tag7 ON tag7.id = part2.internalid AND tag7.taggroup = '24' AND tag7.tagelement = '21'" +  //DESCRIPTION
                    " LEFT JOIN maindicomtags AS tag8 ON tag8.id = part2.internalid AND tag8.taggroup = '8' AND tag8.tagelement = '96'" +   //MODALITY
                    " LEFT JOIN maindicomtags AS tag9 ON tag9.id = part2.internalid AND tag9.taggroup = '8' AND tag9.tagelement = '112'" +  //Manufacturer
                    " LEFT JOIN maindicomtags AS tag10 ON tag10.id = part1.internalid AND tag10.taggroup = '8' AND tag10.tagelement = '128'" +  //InstitutionName
                    " LEFT JOIN maindicomtags AS tag11 ON tag11.id = part2.internalid AND tag11.taggroup = '8' AND tag11.tagelement = '4112'" +   //StationName
                    " LEFT JOIN metadata AS tag12 ON tag12.id = part3.internalid AND tag12.type = '3'";   //source
            Statement statement = conn.createStatement();

            if(dateSeachType.equals("all")){
                resultSQL = staticSQL;
            }else{
                resultSQL = staticSQL + " WHERE tag5.value BETWEEN  '"+FORMAT.format(firstdate)+"' AND '"+FORMAT.format(seconddate)+"'";
            }
            if(!source.equals("all")){
                resultSQL = resultSQL + " AND tag12.value = '"+source+"'";
            }
            ResultSet rs = statement.executeQuery(resultSQL);


            while (rs.next()) {

 //               if(getBitServerResource("debug").getRvalue().equals("true")){
//                    LogTool.getLogger().debug(this.getClass().getSimpleName()+":"+rs.getString(2)+"/"+
//                            rs.getString(4)+"/"+ rs.getString(9)+"/"+ getDateFromText(rs.getString(7))+"/"+
//                            rs.getString(10)+"/"+rs.getString(3)+"/"+getDateFromText(rs.getString(5))+"/"+
//                            rs.getString(6)+"/"+0+"/"+ rs.getString(11)+"/"+ rs.getString(12)+"/"+
//                            rs.getString(13)+"/"+ rs.getString(14));
//                }

                BitServerStudy bufStudy = new BitServerStudy(rs.getString(2),
                        rs.getString(4), rs.getString(9), getDateFromText(rs.getString(7)),
                        rs.getString(10),rs.getString(3),getDateFromText(rs.getString(5)),
                        rs.getString(6),0, rs.getString(11), rs.getString(12),
                        rs.getString(13), rs.getString(14));
                resultList.add(bufStudy);
            }

            conn.close();

        } catch (Exception  e) {
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
        }
        if((getBitServerResource("debug").getRvalue().equals("true"))) {
            LogTool.getLogger().info(this.getClass().getSimpleName() + ": " + "Количеcтво найденных записей при поиске: " + resultList.size());
        }
        return resultList;
    }

    default ArrayList<String> getDateFromMaindicomTags(String distinctOrNo,int tagElement){
        ArrayList<String> resultList = new ArrayList<>();
        try {
            Connection conn = getConnection();
            String staticSQL = "SELECT "+distinctOrNo+" value FROM maindicomtags " +
                    " WHERE taggroup = '8' AND tagelement = '"+tagElement+"'" ;
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(staticSQL);
            while (rs.next()) {
                resultList.add(rs.getString(1));
            }
            conn.close();
        }catch (Exception e){
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
        }
        return resultList;
    }

    default ArrayList<String> getSourceDicom(){
        ArrayList<String> resultList = new ArrayList<>();
        try {
            Connection conn = getConnection();
            String staticSQL = "SELECT DISTINCT patientid, tag12.value FROM patientrecyclingorder" +
                    " INNER JOIN resources AS part1 ON part1.parentid = patientrecyclingorder.patientid" +
                    " INNER JOIN resources AS part2 ON part2.parentid = part1.internalid" +
                    " INNER JOIN resources AS part3 ON part3.parentid = part2.internalid" +
                    " LEFT JOIN metadata AS tag12 ON tag12.id = part3.internalid AND tag12.type = '3'";   //source
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(staticSQL);
            while (rs.next()) {
                if(rs.getString(2).equals("")){
                    resultList.add("Manual upload");
                }else{
                    resultList.add(rs.getString(2));
                }
            }
            conn.close();
        }catch (Exception e){
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
        }
        return resultList;
    }

    default ArrayList<BitServerStudy> getDateFromMaindicomTagsLong(){
        ArrayList<BitServerStudy> resultList = new ArrayList<>();
        try {
            Connection conn = getConnection();
            String staticSQL = "SELECT DISTINCT part1.publicid, tag1.value FROM patientrecyclingorder" +
                    " INNER JOIN resources AS part1 ON part1.parentid = patientrecyclingorder.patientid" +
                    " LEFT JOIN maindicomtags AS tag1 ON tag1.id = part1.internalid AND tag1.taggroup = '8' AND tag1.tagelement = '32'";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(staticSQL);
            while (rs.next()) {
                resultList.add(new BitServerStudy(rs.getString(1),getDateFromText(rs.getString(2))));
            }
            conn.close();
        }catch (Exception e){
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
        }
        return resultList;
    }

    default ArrayList<String> getModalitiOfStudies(){
        ArrayList<String> resultList = new ArrayList<>();
        try {
            Connection conn = getConnection();
            String staticSQL = "SELECT DISTINCT patientid, part1.publicid, tag1.value FROM patientrecyclingorder" +
                    " INNER JOIN resources AS part1 ON part1.parentid = patientrecyclingorder.patientid" +
                    " INNER JOIN resources AS part2 ON part2.parentid = part1.internalid" +
                    " INNER JOIN resources AS part3 ON part3.parentid = part2.internalid" +
                    " LEFT JOIN maindicomtags AS tag1 ON tag1.id = part2.internalid AND tag1.taggroup = '8' AND tag1.tagelement = '96'";   //MODALITY
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(staticSQL);
            while (rs.next()) {
                resultList.add(rs.getString(3));
            }
            conn.close();
        }catch (Exception e){
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
        }
        return resultList;
    }

    default Date getDateFromText(String strDate) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date returnDate = new Date();
        try {
            if (!strDate.equals("")) {
                returnDate = formatter.parse(strDate);
            }
        }catch (Exception e){
            if((getBitServerResource("debug").getRvalue().equals("true"))) {
                LogTool.getLogger().info(this.getClass().getSimpleName() + ": " + "Ошибка парсинга даты: " + strDate);
            }
        }
        return returnDate;
    }

    default void saveJsonSettingtToFile(JsonObject newJson) {
        boolean luaRead = true;
        try {
            luaRead = Boolean.parseBoolean(getBitServerResource("luaRead").getRvalue());
        } catch (Exception e) {
            LogTool.getLogger().error("Error during saveFile() SettingOrthancBean");
        }
        if (luaRead) {
            String modifyStr = ModifyStr(newJson.toString());
            String urlParameters = "f = io.open(\"" + ModifyStr(mainServer.getPathToJson()) + "orthanc.json\",\"w+\");" +
                    "f:write(\"" + modifyStr + "\"); " +
                    "f:close()";
            OrthancRestApi connection = new OrthancRestApi(mainServer.getIpaddress(),mainServer.getPort(),mainServer.getLogin(),mainServer.getPassword());
            connection.makePostConnectionAndStringBuilder("/tools/execute-script", urlParameters);
        } else {
            try (FileOutputStream fileOutputStream = new FileOutputStream(mainServer.getPathToJson() + "orthanc.json")) {
                byte[] buffer = newJson.toString().getBytes();
                fileOutputStream.write(buffer, 0, buffer.length);
            } catch (IOException e) {
                LogTool.getLogger().error("Error saveSettings() NetworkSettingsBean: " + e.getMessage());
            }
        }
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

                String sqlInsert = "INSERT INTO bitserver (rtype,rvalue) VALUES ( '3','admin')";
                statement.executeUpdate(sqlInsert);

                resultSQL = "SELECT internalId FROM bitserver WHERE bitserver.rvalue =\'admin\'";
                statement = conn.createStatement();
                ResultSet rs = statement.executeQuery(resultSQL);
                rs.next();
                String buf = rs.getString(1);

                sqlInsert = "INSERT INTO bitserver (rtype,rvalue,parentId) VALUES ( '4','admin','"+buf+"')";
                statement.executeUpdate(sqlInsert);

                sqlInsert = "INSERT INTO bitserver (rtype,rvalue,parentId) VALUES ( '5','admin','"+buf+"')";
                statement.executeUpdate(sqlInsert);

                sqlInsert = "INSERT INTO bitserver (rtype,rvalue,parentId) VALUES ( '6','admin','"+buf+"')";
                statement.executeUpdate(sqlInsert);

                sqlInsert = "INSERT INTO bitserver (rtype,rvalue,parentId) VALUES ( '7','admin','"+buf+"')";
                statement.executeUpdate(sqlInsert);

                sqlInsert = "INSERT INTO bitserver (rtype,rvalue,parentId) VALUES ( '8','admin','"+buf+"')";
                statement.executeUpdate(sqlInsert);

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

    default String ModifyStr(String str){
        String result;
        String buf0;
        String buf;
        String buf2;
        buf0 = str.replace("\\","\\\\");
        buf = buf0.replace("/","\\/");
        buf2 = buf.replace("\"","\\\"");
        result = buf2.replace(",",",\\n");
        return result;
    }

    default StringBuilder getFileFromResName(String resName){
        StringBuilder result = new StringBuilder();
        try(FileReader reader = new FileReader(getBitServerResource(resName).getRvalue())) {
            int c;
            while ((c = reader.read()) != -1) {
                result.append((char) c);
            }
        } catch (Exception e) {
            LogTool.getLogger().warn("Error read luascript file: "+e.getMessage());
        }
        return result;
    }

}
