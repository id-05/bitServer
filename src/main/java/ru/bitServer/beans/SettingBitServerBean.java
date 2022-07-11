package ru.bitServer.beans;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.primefaces.PrimeFaces;
import ru.bitServer.dao.*;
import ru.bitServer.dicom.OrthancStudy;
import ru.bitServer.util.LogTool;
import ru.bitServer.util.OrthancRestApi;
import ru.bitServer.util.SessionUtils;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static ru.bitServer.beans.AutoriseBean.showMessage;
import static ru.bitServer.beans.MainBean.info;
import static ru.bitServer.beans.MainBean.mainServer;

@ManagedBean(name = "settingBitServerBean")
@ViewScoped
public class SettingBitServerBean implements UserDao {

    final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMdd");
    final SimpleDateFormat FORMAT2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    int progress1;
    List<Users> usersList;
    List<Users> selectedUsers;
    Users selectedUser;
    List<Usergroup> usergroupList;
    List<String> usergroupListRuName;
    List<Usergroup> selectedUsergroups;
    Usergroup selectedUsergroup;
    String httpmode;
    String updateQueueAfterOpen;
    String showStat;
    String osimisAddress;
    String orthancAddress;
    String orthancWebPort;
    String orthancLogin;
    String orthancPassword;
    String orthancPathToJson;
    String orthancPathToResultFile;
    String luaScriptPath;
    List<BitServerResources> bitServerResourcesList = new ArrayList<>();
    String networksetpathfile;
    Date syncdate;
    Date startDate;
    Date stopDate;
    String colStatus;
    String colPreview;
    String colDateBirth;
    String colDate;
    String colDescription;
    String colModality;
    String colWhereSend;
    OrthancRestApi connection;
    List<BitServerStudy> studiesFromTableBitServer = new ArrayList<>();
    Users currentUser;
    private int number;

    public String getShowStat() {
        return showStat;
    }

    public void setShowStat(String showStat) {
        this.showStat = showStat;
    }

    public void increment() {
        number = progress1;
    }

    public String getNumber() {
        return number+"%";
    }

    public Users getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Users currentUser) {
        this.currentUser = currentUser;
    }

    public String getColStatus() {
        return colStatus;
    }

    public String getColPreview() {
        return colPreview;
    }

    public void setColPreview(String colPreview) {
        this.colPreview = colPreview;
    }

    public void setColStatus(String colStatus) {
        this.colStatus = colStatus;
    }

    public String getColDateBirth() {
        return colDateBirth;
    }

    public void setColDateBirth(String colDateBirth) {
        this.colDateBirth = colDateBirth;
    }

    public String getColDate() {
        return colDate;
    }

    public void setColDate(String colDate) {
        this.colDate = colDate;
    }

    public String getColDescription() {
        return colDescription;
    }

    public void setColDescription(String colDescription) {
        this.colDescription = colDescription;
    }

    public String getColModality() {
        return colModality;
    }

    public void setColModality(String colModality) {
        this.colModality = colModality;
    }

    public String getColWhereSend() {
        return colWhereSend;
    }

    public void setColWhereSend(String colWhereSend) {
        this.colWhereSend = colWhereSend;
    }

    public String getUpdateQueueAfterOpen() {
        return updateQueueAfterOpen;
    }

    public void setUpdateQueueAfterOpen(String updateQueueAfterOpen) {
        this.updateQueueAfterOpen = updateQueueAfterOpen;
    }

    public String getLuaScriptPath() {
        return luaScriptPath;
    }

    public void setLuaScriptPath(String luaScriptPath) {
        this.luaScriptPath = luaScriptPath;
    }

    public String getHttpmode() {
        return httpmode;
    }

    public void setHttpmode(String httpmode) {
        this.httpmode = httpmode;
    }

    public String getNetworksetpathfile() {
        return networksetpathfile;
    }

    public void setNetworksetpathfile(String networksetpathfile) {
        this.networksetpathfile = networksetpathfile;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStopDate() {
        return stopDate;
    }

    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
    }

    public String getOrthancWebPort() {
        return orthancWebPort;
    }

    public void setOrthancWebPort(String orthancWebPort) {
        this.orthancWebPort = orthancWebPort;
    }

    public String getOrthancLogin() {
        return orthancLogin;
    }

    public void setOrthancLogin(String orthancLogin) {
        this.orthancLogin = orthancLogin;
    }

    public String getOrthancPassword() {
        return orthancPassword;
    }

    public void setOrthancPassword(String orthancPassword) {
        this.orthancPassword = orthancPassword;
    }

    public String getOrthancPathToJson() {
        return orthancPathToJson;
    }

    public void setOrthancPathToJson(String orthancPathToJson) {
        this.orthancPathToJson = orthancPathToJson;
    }

    public String getOrthancPathToResultFile() {
        return orthancPathToResultFile;
    }

    public void setOrthancPathToResultFile(String orthancPathToResultFile) {
        this.orthancPathToResultFile = orthancPathToResultFile;
    }

    public String getOrthancAddress() {
        return orthancAddress;
    }

    public void setOrthancAddress(String orthancAddress) {
        this.orthancAddress = orthancAddress;
    }

    public List<String> getUsergroupListRuName() {
        usergroupListRuName = new ArrayList<>();
        for(Usergroup bufgroup:usergroupList){
            usergroupListRuName.add(bufgroup.getRuName());
        }
        return usergroupListRuName;
    }

    public void setUsergroupListRuName(List<String> usergroupListRuName) {
        this.usergroupListRuName = usergroupListRuName;
    }

    public List<Usergroup> getUsergroupList() {
        return usergroupList;
    }

    public void setUsergroupList(List<Usergroup> usergroupList) {
        this.usergroupList = usergroupList;
    }

    public List<Usergroup> getSelectedUsergroups() {
        return selectedUsergroups;
    }

    public void setSelectedUsergroups(List<Usergroup> selectedUsergroups) {
        this.selectedUsergroups = selectedUsergroups;
    }

    public Usergroup getSelectedUsergroup() {
        return selectedUsergroup;
    }

    public void setSelectedUsergroup(Usergroup selectedUsergroup) {
        this.selectedUsergroup = selectedUsergroup;
    }

    public List<Users> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<Users> usersList) {
        this.usersList = usersList;
    }

    public Users getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(Users selectedUser) {
        this.selectedUser = selectedUser;
    }

    public List<Users> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<Users> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public void syncAll() {

        JsonParser parserJson = new JsonParser();
        JsonArray studies = (JsonArray) parserJson.parse(connection.makeGetConnectionAndStringBuilder("/studies/").toString());
        Iterator<JsonElement> studiesIterator = studies.iterator();

        ArrayList<String> idOrthancStudy = new ArrayList<>();
        while (studiesIterator.hasNext()) {
            String studyData =  studiesIterator.next().getAsString();
            idOrthancStudy.add(studyData);
        }

        studiesFromTableBitServer = getAllBitServerStudy();
        ArrayList<String> idBitServerStudy = new ArrayList<>();
        for(BitServerStudy bufStudy:getAllBitServerStudy()){
            idBitServerStudy.add(bufStudy.getSid());
        }
        idOrthancStudy.removeAll(idBitServerStudy);
        double dProgress = (double) 100 / idOrthancStudy.size();
        progress1 = 0;
        int i = 0;
        for(String bufId:idOrthancStudy){
            StringBuilder sb = connection.makeGetConnectionAndStringBuilder("/studies/"+bufId);
            parserJson = new JsonParser();
            JsonObject jsonObject = (JsonObject) parserJson.parse(sb.toString());
            OrthancStudy bufStudy = connection.parseStudy(jsonObject);
            BitServerStudy buf = new BitServerStudy(bufStudy.getOrthancId(), bufStudy.getShortId(), bufStudy.getStudyDescription(),
                    bufStudy.getInstitutionName(), bufStudy.getDate(),
                    bufStudy.getModality(), new Date(), bufStudy.getPatientName(), bufStudy.getPatientBirthDate(), bufStudy.getPatientSex(), "", "", 0);
            addStudy(buf);
            i++;
            progress1 = (int) (dProgress * i);
        }
        PrimeFaces.current().executeScript("PF('statusDialog').hide()");
        showMessage("Сообщение", "Синхронизация завершена! Всего добавлено: " + i, info);
    }

    public void syncPeriod(){
        int sum = 0;
        Instant startInstant = Instant.parse(FORMAT2.format(startDate)+".00Z");
        Instant stopInstant = Instant.parse(FORMAT2.format(stopDate)+".00Z");
        if(!startInstant.equals(stopInstant)) {
            Calendar cal1 = new GregorianCalendar();
            Calendar cal2 = new GregorianCalendar();
            cal1.setTime(startDate);
            cal2.setTime(stopDate);
            int days = (int) ((cal2.getTime().getTime() - cal1.getTime().getTime()) / (1000 * 60 * 60 * 24));
            Instant bufInstant = startInstant.plus(1, ChronoUnit.DAYS);
            double dProgress;
            if (days != 0) {
                dProgress = (double) 100 / days;
            } else {
                dProgress = 0;
            }
            int i = 1;
            progress1 = 0;
            if (!FORMAT.format(Date.from(bufInstant)).equals(FORMAT.format(Date.from(stopInstant)))) {
                while (!FORMAT.format(Date.from(bufInstant)).equals(FORMAT.format(Date.from(stopInstant)))) {
                    sum = sum + readStudyFromDB(startInstant, bufInstant);
                    startInstant = startInstant.plus(1, ChronoUnit.DAYS);
                    bufInstant = startInstant.plus(1, ChronoUnit.DAYS);
                    i++;
                    progress1 = (int) (dProgress * i);
                }
            }
            PrimeFaces.current().executeScript("PF('statusDialog').hide()");
            showMessage("Сообщение", "Синхронизация завершена! Всего добавлено: " + sum, info);
        }else{
            PrimeFaces.current().executeScript("PF('statusDialog').hide()");
            showMessage("Сообщение","Выбраны недопустимые даты!", info);
        }
    }

    public void dateBaseFactoryReset() throws IOException {
        JsonParser parserJson = new JsonParser();
        JsonArray studies = (JsonArray) parserJson.parse(connection.makeGetConnectionAndStringBuilder("/studies/").toString());
        Iterator<JsonElement> studiesIterator = studies.iterator();
        double dProgress = (double) 100 / studies.size();
        progress1 = 0;
        int i = 0;
        while (studiesIterator.hasNext()) {
            String studyData =  studiesIterator.next().getAsString();
            connection.deleteStudyFromOrthanc(studyData);
            i++;
            progress1 = (int) (dProgress * i);
        }
        List<BitServerStudy> listStudys = getAllBitServerStudy();
        for(BitServerStudy bufStudy:listStudys){
            deleteStudy(bufStudy);
        }
        PrimeFaces.current().executeScript("PF('statusDialog').hide()");
        showMessage("Сообщение", "Удаление завершено! Всего удалено: " + i, info);
    }

    public Integer readStudyFromDB(Instant startDate, Instant stopDate) {
        int sum = 0;
        ArrayList<OrthancStudy> studiesFromRestApi;
        JsonObject query = new JsonObject();
        query.addProperty("Level", "Studies");
        query.addProperty("CaseSensitive", false);
        query.addProperty("Expand", true);
        query.addProperty("Limit", 0);
        JsonObject queryDetails = new JsonObject();
        String dateStr = FORMAT.format(Date.from(startDate)) + "-" + FORMAT.format(Date.from(stopDate));
        queryDetails.addProperty("StudyDate", dateStr);
        queryDetails.addProperty("PatientID", "*");
        queryDetails.addProperty("Modality", "");
        query.add("Query", queryDetails);
        StringBuilder sb = connection.makePostConnectionAndStringBuilder("/tools/find", query.toString());
        assert sb != null;
        boolean existInTable;
        studiesFromRestApi = connection.getStudiesFromJson(sb.toString());
        studiesFromTableBitServer = getAllBitServerStudy();
        for(OrthancStudy bufStudy:studiesFromRestApi){
            existInTable = false;
            if(studiesFromTableBitServer.size()>0) {
                for (BitServerStudy bBSS : studiesFromTableBitServer) {
                    if (bufStudy.getOrthancId().equals(bBSS.getSid())) {
                        existInTable = true;
                        break;
                    }
                }
            }
            if(!existInTable) {
                BitServerStudy buf = new BitServerStudy(bufStudy.getOrthancId(), bufStudy.getShortId(), bufStudy.getStudyDescription(),
                        bufStudy.getInstitutionName(), bufStudy.getDate(),
                        bufStudy.getModality(), new Date(), bufStudy.getPatientName(), bufStudy.getPatientBirthDate(), bufStudy.getPatientSex(), "","",0);
                addStudy(buf);
                sum++;
            }
        }
        return sum;
    }

    @PostConstruct
    public void init() {
        connection = new OrthancRestApi(mainServer.getIpaddress(),mainServer.getPort(),mainServer.getLogin(),mainServer.getPassword());
        startDate = new Date();
        stopDate = new Date();
        usergroupList = getBitServerUsergroupList();
        usersList = prepareUserList();
        initNewUser();
        initNewUsergroup();
        bitServerResourcesList = getAllBitServerResource();
        HttpSession session = SessionUtils.getSession();
        currentUser = getUserById(session.getAttribute("userid").toString());

        boolean updateRes = false;
        boolean haspreview = false;

        for(BitServerResources buf: bitServerResourcesList){
            if (buf.getRname().equals("colpreview")) {
                haspreview = true;
                break;
            }
        }

        for(BitServerResources buf: bitServerResourcesList){
            if (buf.getRname().equals("colstatus")) {
                updateRes = true;
                break;
            }
        }

        if(!haspreview){
            bitServerResourcesList.add(new BitServerResources("colpreview","true"));
        }

        if(!updateRes){
            bitServerResourcesList.add(new BitServerResources("colstatus","false"));
            bitServerResourcesList.add(new BitServerResources("colpreview","false"));
            bitServerResourcesList.add(new BitServerResources("colDateBirth","false"));
            bitServerResourcesList.add(new BitServerResources("colDate","false"));
            bitServerResourcesList.add(new BitServerResources("colDescription","false"));
            bitServerResourcesList.add(new BitServerResources("colModality","false"));
            bitServerResourcesList.add(new BitServerResources("colWhereSend","false"));
        }

        for(BitServerResources buf: bitServerResourcesList){
            switch (buf.getRname()){

                case "orthancaddress": orthancAddress = buf.getRvalue();
                    break;
                case "updateafteropen": updateQueueAfterOpen = buf.getRvalue();
                    break;
                case "httpmode": httpmode = buf.getRvalue();
                    break;
                case "showStat": showStat = buf.getRvalue();

                    break;
                case "luascriptpathfile": luaScriptPath = buf.getRvalue();
                    break;
                case "port": orthancWebPort = buf.getRvalue();
                    break;
                case "login": orthancLogin = buf.getRvalue();
                    break;
                case "password": orthancPassword = buf.getRvalue();
                    break;
                case "pathtojson": orthancPathToJson = buf.getRvalue();
                    break;
                case "pathtoresultfile": orthancPathToResultFile = buf.getRvalue();
                    break;
                case "addressforosimis": osimisAddress = buf.getRvalue();
                    break;
                case "networksetpathfile": networksetpathfile= buf.getRvalue();
                    break;
                case "syncdate": {
                    try {
                        syncdate = format.parse(buf.getRvalue());
                    }catch (Exception e){
                        LogTool.getLogger().warn("Error date transfer settingBitServerBean: "+e.getMessage());
                    }
                }
                    break;
                case "colstatus": colStatus = buf.getRvalue();
                    break;
                case "colpreview": colPreview = buf.getRvalue();
                    break;
                case "colDateBirth": colDateBirth = buf.getRvalue();
                    break;
                case "colDate": colDate = buf.getRvalue();
                    break;
                case "colDescription": colDescription = buf.getRvalue();
                    break;
                case "colModality": colModality = buf.getRvalue();
                    break;
                case "colWhereSend": colWhereSend = buf.getRvalue();
                    break;
            }
        }
    }

    public List<Users> prepareUserList(){
        usersList = getBitServerUserList();
        for (Users users : usersList) {
            int buf = Integer.parseInt(users.getUgroup());
            users.setUgroup(getVisibleNameOfGroup(buf));
            switch (users.getRole()){
                case "admin": {
                    users.setRole("Администратор");
                    break;
                }

                case "localuser": {
                    users.setRole("Локальный пользователь");
                    break;
                }

                case "remoteuser": {
                    users.setRole("Удаленный пользователь");
                    break;
                }

                case "client": {
                    users.setRole("Клиент");
                    break;
                }

                case "onlyview": {
                    users.setRole("Локальный только просмотр");
                    break;
                }

                case "demo": {
                    users.setRole("demo");
                    break;
                }

            }
        }
        return usersList;
    }

    public String getVisibleNameOfGroup(int i){
        String buf = null;
        for(Usergroup bufgroup:usergroupList){
            if(bufgroup.getId()==i){
                buf = bufgroup.getRuName();
                break;
            }
        }
        return buf;
    }

    public void saveParam(){
        for(BitServerResources buf: bitServerResourcesList){
            switch (buf.getRname()){
                case "orthancaddress": buf.setRvalue(orthancAddress);
                    break;
                case "updateafteropen": buf.setRvalue(updateQueueAfterOpen);
                    break;
                case "httpmode": buf.setRvalue(httpmode);
                    break;
                case "luascriptpathfile": buf.setRvalue(luaScriptPath);
                    break;
                case "port": buf.setRvalue(orthancWebPort);
                    break;
                case "login": buf.setRvalue(orthancLogin);
                    break;
                case "password": buf.setRvalue(orthancPassword);
                    break;
                case "pathtojson": buf.setRvalue(orthancPathToJson);
                    break;
                case "pathtoresultfile": buf.setRvalue(orthancPathToResultFile);
                    break;
                case "addressforosimis": buf.setRvalue(osimisAddress);
                    break;
                case "syncdate": buf.setRvalue(format.format(syncdate));
                    break;
                case "colstatus": buf.setRvalue(colStatus);
                    break;
                case "colpreview": buf.setRvalue(colPreview);
                    break;
                case "colDateBirth": buf.setRvalue(colDateBirth);
                    break;
                case "colDate": buf.setRvalue(colDate);
                    break;
                case "colDescription": buf.setRvalue(colDescription);
                    break;
                case "colModality": buf.setRvalue(colModality);
                    break;
                case "colWhereSend": buf.setRvalue(colWhereSend);
                    break;
                case "showStat": buf.setRvalue(showStat);
                    break;
            }
            updateBitServiceResource(buf);
        }
        PrimeFaces.current().ajax().update(":form:accord:dt-studys");
    }

    public void initNewUser() {
        selectedUser = new Users();
    }

    public void initNewUsergroup() {
        selectedUsergroup = new Usergroup();
    }

    public void addNewUser(){
        if((selectedUser.getUname()!=null)&(!selectedUser.getPassword().equals(""))&(!selectedUser.getUgroup().equals(""))
                &(!selectedUser.getRole().equals(""))&(!selectedUser.getRuFamily().equals(""))&(!selectedUser.getRuMiddleName().equals(""))
                &(!selectedUser.getRuName().equals("")))
        {
            Users bufUser = getRealUserForBase(selectedUser);
            updateUser(bufUser);
            usersList = prepareUserList();
            PrimeFaces.current().executeScript("PF('manageUserDialog').hide()");
            PrimeFaces.current().ajax().update(":form:accord:dt-users");
            LogTool.getLogger().debug("Admin: "+currentUser.getSignature()+" add new user "+bufUser.getUname());
        }else{
            showMessage("Внимание!","Все поля должны быть заполнены!",FacesMessage.SEVERITY_ERROR);
        }
    }

    public Users getRealUserForBase(Users sourseUser){
        for(Usergroup bufgroup:usergroupList){
            if(bufgroup.getRuName().equals(sourseUser.getUgroup())){
                sourseUser.setUgroup(String.valueOf(bufgroup.getId()));
            }

            String buf = null;
            switch (sourseUser.getRole()){
                case "Администратор": {
                    buf = "admin";
                    break;
                }

                case "Локальный пользователь": {
                    buf = "localuser";
                    break;
                }

                case "Удаленный пользователь": {
                    buf = "remoteuser";
                    break;
                }

                case "Клиент": {
                    buf = "client";
                    break;
                }

                case "Локальный только просмотр": {
                    buf = "onlyview";
                    break;
                }

                case "demo": {
                    buf = "demo";
                    break;
                }
            }
            if(buf!=null) {
                sourseUser.setRole(buf);
            }
        }
        return sourseUser;
    }

    public void deleteUserSetting() {
        deleteUser(selectedUser);
        usersList.remove(selectedUser);
        selectedUser = new Users();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Пользователь удален!"));
        PrimeFaces.current().ajax().update(":form:accord:dt-users");
    }

    public void addNewUsergroup(){
        if((selectedUsergroup.getRuContragent()!=null)&(!selectedUsergroup.getRuName().equals(""))&(!selectedUsergroup.getStatus().equals("")))
        {
            boolean verifiUnical = true;
            for(Usergroup bufUsergroup:usergroupList){
                if (bufUsergroup.getRuName().equals(selectedUsergroup.getRuName())) {
                    verifiUnical = false;
                    break;
                }
            }
            if(verifiUnical) {
                usergroupList.add(new Usergroup(selectedUsergroup.getRuContragent(), selectedUsergroup.getRuName(), selectedUsergroup.getStatus(),selectedUsergroup.isDownloadTrue(),selectedUsergroup.isForlocal()));
                saveNewUsergroup(selectedUsergroup);
                PrimeFaces.current().executeScript("PF('manageUsergroupDialog').hide()");
                PrimeFaces.current().ajax().update(":form:accord:dt-usergroup");
                usergroupList = getBitServerUsergroupList();
            }else{
                updateUsergroup(selectedUsergroup);
                usergroupList = getBitServerUsergroupList();
                PrimeFaces.current().executeScript("PF('manageUsergroupDialog').hide()");
                PrimeFaces.current().ajax().update(":form:accord:dt-usergroup");
            }
        }else{
            showMessage("Внимание!","Все поля должны быть заполнены!",FacesMessage.SEVERITY_ERROR);
        }
    }

    public void deleteUsergroupSetting() {
        try {
            deleteUsergroup(selectedUsergroup);
            usergroupList.remove(selectedUsergroup);
            selectedUsergroup = new Usergroup();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Группа удалена!"));
            PrimeFaces.current().ajax().update(":form:accord:dt-usergroup");
        }catch (Exception e){
            PrimeFaces.current().executeScript("PF('errorDeleteUsergroupDialog').show()");
            LogTool.getLogger().debug("Error deleteUserGroupSettings SettingsBitServerBean: "+e.getMessage());
        }
    }

}