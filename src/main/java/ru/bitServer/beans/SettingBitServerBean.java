package ru.bitServer.beans;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DualListModel;
import ru.bitServer.dao.*;
import ru.bitServer.dicom.OrthancSettings;
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
import java.util.*;

import static ru.bitServer.beans.AutoriseBean.showMessage;
import static ru.bitServer.beans.MainBean.*;

@ManagedBean(name = "settingBitServerBean")
@ViewScoped
public class SettingBitServerBean implements UserDao {

//    final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMdd");
//    final SimpleDateFormat FORMAT2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    int progress1;
    int progress2;
    List<BitServerUser> bitServerUserList;
    List<BitServerUser> selectedUsers;
    BitServerUser selectedUser;
    List<BitServerGroup> bitServerGroupList;
    List<BitServerGroup> selectedBitServerGroups;
    BitServerGroup selectedBitServerGroup;
    String httpmode;
    String showStat;
    String osimisAddress;
    String orthancAddress;
    String orthancWebPort;
    String hl7port;
    String workListLifeTime;
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
    Date startDelDate;
    Date stopDelDate;
    String colStatus;
    String colPreview;
    String colDateBirth;
    String colDate;
    String colDescription;
    String colModality;
    String colWhereSend;
    String optSend;
    String optDownload;
    String showSeachTime;
    OrthancRestApi connection;
    BitServerUser currentUser;
    int number;
    String remoteaddr;
    String remoteport;
    String remotelogin;
    String remotepass;
    String remoteTransStatus;
    String vremoteTransStatus;
    List<String> modalityName = new ArrayList<>();
    List<String> selectedModalitiName = new ArrayList<>();
    String showHelp;
    DualListModel<BitServerUser> cities;

    public BitServerGroup getSelectedBitServerGroup() {
        return selectedBitServerGroup;
    }

    public void setSelectedBitServerGroup(BitServerGroup selectedBitServerGroup) {
        this.selectedBitServerGroup = selectedBitServerGroup;
    }

    public List<BitServerUser> getBitServerUserList() {
        return bitServerUserList;
    }

    public void setBitServerUserList(List<BitServerUser> bitServerUserList) {
        this.bitServerUserList = bitServerUserList;
    }

    public List<BitServerGroup> getBitServerGroupList() {
        return bitServerGroupList;
    }

    public void setBitServerGroupList(List<BitServerGroup> bitServerGroupList) {
        this.bitServerGroupList = bitServerGroupList;
    }

    public String getShowHelp() {
        return showHelp;
    }

    public void setShowHelp(String showHelp) {
        this.showHelp = showHelp;
    }

    public String getShowSeachTime() {
        return showSeachTime;
    }

    public void setShowSeachTime(String showSeachTime) {
        this.showSeachTime = showSeachTime;
    }

    public List<String> getModalityName() {
        return modalityName;
    }

    public void setModalityName(List<String> modalityName) {
        this.modalityName = modalityName;
    }

    public List<String> getSelectedModalitiName() {
        return selectedModalitiName;
    }

    public void setSelectedModalitiName(List<String> selectedModalitiName) {
        this.selectedModalitiName = selectedModalitiName;
    }

    public int getProgress2() {
        return progress2;
    }

    public void setProgress2(int progress2) {
        this.progress2 = progress2;
    }

    public int getProgress1() {
        return progress1;
    }

    public void setProgress1(int progress1) {
        this.progress1 = progress1;
    }

    public String getVremoteTransStatus() {
        return vremoteTransStatus;
    }

    public void setVremoteTransStatus(String vremoteTransStatus) {
        this.vremoteTransStatus = vremoteTransStatus;
    }

    public String getRemoteaddr() {
        return remoteaddr;
    }

    public void setRemoteaddr(String remoteaddr) {
        this.remoteaddr = remoteaddr;
    }

    public String getRemoteport() {
        return remoteport;
    }

    public void setRemoteport(String remoteport) {
        this.remoteport = remoteport;
    }

    public String getRemotelogin() {
        return remotelogin;
    }

    public void setRemotelogin(String remotelogin) {
        this.remotelogin = remotelogin;
    }

    public String getRemotepass() {
        return remotepass;
    }

    public void setRemotepass(String remotepass) {
        this.remotepass = remotepass;
    }

    public String getShowStat() {
        return showStat;
    }

    public void setShowStat(String showStat) {
        this.showStat = showStat;
    }

    public void increment() {
        number = progress1;
    }

    public void incrementRT() {
        vremoteTransStatus = remoteTransStatus;
    }

    public String getNumber() {
        return number+"%";
    }

    public BitServerUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(BitServerUser currentUser) {
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

    public Date getStartDelDate() {
        return startDelDate;
    }

    public void setStartDelDate(Date startDelDate) {
        this.startDelDate = startDelDate;
    }

    public Date getStopDelDate() {
        return stopDelDate;
    }

    public void setStopDelDate(Date stopDelDate) {
        this.stopDelDate = stopDelDate;
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

    public List<BitServerGroup> getSelectedUsergroups() {
        return selectedBitServerGroups;
    }

    public void setSelectedUsergroups(List<BitServerGroup> selectedBitServerGroups) {
        this.selectedBitServerGroups = selectedBitServerGroups;
    }

    public BitServerUser getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(BitServerUser selectedUser) {
        this.selectedUser = selectedUser;
    }

    public List<BitServerUser> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<BitServerUser> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public String getOptSend() {
        return optSend;
    }

    public void setOptSend(String optSend) {
        this.optSend = optSend;
    }

    public String getOptDownload() {
        return optDownload;
    }

    public void setOptDownload(String optDownload) {
        this.optDownload = optDownload;
    }

    public String getHl7port() {
        return hl7port;
    }

    public void setHl7port(String hl7port) {
        this.hl7port = hl7port;
    }

    public String getWorkListLifeTime() {
        return workListLifeTime;
    }

    public void setWorkListLifeTime(String workListLifeTime) {
        this.workListLifeTime = workListLifeTime;
    }

    public DualListModel<BitServerUser> getCities() {
        return cities;
    }

    public void setCities(DualListModel<BitServerUser> cities) {
        this.cities = cities;
    }

    @PostConstruct
    public void init() {
        System.out.println("settingBitServerBean");
        remoteTransStatus = "Статус: не запущено";
        connection = new OrthancRestApi(mainServer.getIpaddress(),mainServer.getPort(),mainServer.getLogin(),mainServer.getPassword());
        startDate = new Date();
        stopDate = new Date();
        startDelDate = new Date();
        stopDelDate = new Date();
        bitServerUserList = getAllBitServerUserList();
        bitServerGroupList = getAllBitServerGroupList();
        initNewUser();
        initNewUsergroup();
        bitServerResourcesList = getAllBitServerResource();
        HttpSession session = SessionUtils.getSession();
        currentUser = getUserById(session.getAttribute("userid").toString());
        remoteaddr = "127.0.0.1";
        remoteport = "8042";
        remotelogin = "doctor";
        remotepass = "doctor";


        List<BitServerUser> citiesSource = new ArrayList<>();
        List<BitServerUser> citiesTarget = new ArrayList<>();

        citiesSource = getAllBitServerUserList();

        cities = new DualListModel<BitServerUser>(citiesSource, citiesTarget);



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
            bitServerResourcesList.add(new BitServerResources("OptionSend","false"));
            bitServerResourcesList.add(new BitServerResources("OptionDownload","false"));
        }

        for(BitServerResources buf: bitServerResourcesList){
            switch (buf.getRname()){

                case "orthancaddress": orthancAddress = buf.getRvalue();
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
                case "networksetpathfile": networksetpathfile = buf.getRvalue();
                    break;
                case "hl7port": hl7port= buf.getRvalue();
                    break;
                case "workListLifeTime": workListLifeTime = buf.getRvalue();
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
                case "OptionSend": optSend = buf.getRvalue();
                    break;
                case "OptionDownload": optDownload = buf.getRvalue();
                    break;
                case "ShowSeachTime": showSeachTime = buf.getRvalue();
                    break;
                case "ShowHelp": showHelp = buf.getRvalue();
                    break;
            }
        }
        //selectedModalitiName = modalityName;
    }

    public void applicationReboot() {
        showMessage("Внимание","Приложение будет перезагружено!",FacesMessage.SEVERITY_INFO);
        LogTool.getLogger().info("Admin: "+currentUser.getUname()+" reboot server!");
        try {
            Process proc = Runtime.getRuntime().exec("sudo ./home/tomcat/scripts/tomcatreboot");
            proc.waitFor();
        }catch (Exception e){
            showMessage("Внимание",e.getMessage(),FacesMessage.SEVERITY_INFO);
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ "Error powerReboot() admin: "+e.getMessage());
        }
    }

    public void powerReboot() {
        showMessage("Внимание","Сервер будет перезагружен!",FacesMessage.SEVERITY_INFO);
        LogTool.getLogger().info("Admin: "+currentUser.getUname()+" reboot server!");
        try {
            Process proc = Runtime.getRuntime().exec("sudo ./home/tomcat/scripts/reboot");
            proc.waitFor();
        }catch (Exception e){
            showMessage("Внимание",e.getMessage(),FacesMessage.SEVERITY_INFO);
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ "Error powerReboot() admin: "+e.getMessage());
        }
    }

    public void powerShotdown() {
        showMessage("Внимание","Сервер будет выключен!",FacesMessage.SEVERITY_INFO);
        LogTool.getLogger().info("Admin: "+currentUser.getUname()+" poweroff server!");
        try {
            Process proc = Runtime.getRuntime().exec("sudo ./home/tomcat/scripts/poweroff");
            proc.waitFor();
        }catch (Exception e){
            showMessage("Внимание",e.getMessage(),FacesMessage.SEVERITY_INFO);
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ "Error powerShotdown() admin: "+e.getMessage());
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
            String studyId =  studiesIterator.next().getAsString();
            connection.deleteStudyFromOrthanc(studyId);
            i++;
            progress1 = (int) (dProgress * i);
        }
        PrimeFaces.current().executeScript("PF('statusDialog').hide()");
        showMessage("Сообщение", "Удаление завершено! Всего удалено: " + i, info);
        LogTool.getLogger().info("Admin "+ currentUser.getUname()+" start dateBaseFactoryReset()");
    }

    public void deleteDicomPeriod() throws IOException {

    }

    public void startRemoteSync(){
        PrimeFaces.current().executeScript("PF('startButtonRS').disable()");
        int i = 0;
        progress2 = 0;
        OrthancRestApi remoteCon = new OrthancRestApi(remoteaddr,remoteport,remotelogin,remotepass);
        OrthancSettings orthancSettings = new OrthancSettings(connection);
        JsonParser parserJson = new JsonParser();
        String remotestudies = remoteCon.makeGetConnectionAndStringBuilder("/studies/").toString();
        if(!remotestudies.equals("error")){
            LogTool.getLogger().info("Admin "+ currentUser.getUname()+" start startRemoteSync()");
            JsonArray studies = (JsonArray) parserJson.parse(remotestudies);
            Iterator<JsonElement> studiesIterator = studies.iterator();
            ArrayList<String> remoteStudyList = new ArrayList<>();
            while (studiesIterator.hasNext()) {
                remoteStudyList.add(studiesIterator.next().getAsString());
            }
            ArrayList<String> localStudyList = new ArrayList<>();

            remoteStudyList.removeAll(localStudyList);
            if(remoteStudyList.size()>0) {
                double dProgress = (double) 100 / studies.size();
                for (String bufId : remoteStudyList) {
                    remoteTransStatus = "Статус: передано "+ i + " из " + remoteStudyList.size() + " (" + progress2 + "%)";
                    try {
                        remoteCon.makePostConnectionAndStringBuilderWithIOE("/modalities/" +
                                orthancSettings.getDicomAet() + "/store", bufId);
                        remoteTransStatus ="Статус:  Передано "+ i + " из " + remoteStudyList.size() + " (" + progress2 + "%)";
                    } catch (IOException e) {
                        showMessage("Сообщение:", "Возникла ошибка при отправке! " + e.getMessage(), error);
                        remoteTransStatus = "Возникла ошибка при отправке!";
                    }
                    i++;
                    progress2 = (int) (dProgress * i);
                }
            }else{
                showMessage("Сообщение:", "Все данные синхронизированы! ", warning);
                LogTool.getLogger().info("Admin "+ currentUser.getUname()+" start startRemoteSync() - finish");
            }
            PrimeFaces.current().executeScript("PF('startButtonRS').enable()");
        }else{
            remoteTransStatus = "Статус: Ошибка соединения!";
            PrimeFaces.current().executeScript("PF('startButtonRS').enable()");
            showMessage("Сообщение:", "Проблемы с доступом! Проверьте настройки соединения!", warning);
        }
    }

    public void saveParam(){
        for(BitServerResources buf: bitServerResourcesList){
            switch (buf.getRname()){
                case "orthancaddress": buf.setRvalue(orthancAddress);
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
                case "OptionSend": buf.setRvalue(optSend);
                    break;
                case "OptionDownload": buf.setRvalue(optDownload);
                    break;
                case "ShowSeachTime": buf.setRvalue(showSeachTime);
                    break;
                case "showStat": buf.setRvalue(showStat);
                    break;
                case "hl7port": buf.setRvalue(hl7port);
                    break;
                case "workListLifeTime": buf.setRvalue(workListLifeTime);
                    break;
                case "ShowHelp": buf.setRvalue(showHelp);
                    break;
            }
            updateBitServiceResource(buf);
        }
        if(showSeachTime.equals("true")) {

            showMessage("Всего: 0", "SQL-запрос: 0"+ "\r\n" +
                    "Отображение: 0"+"\n\r", info);
        }
        PrimeFaces.current().ajax().update(":form:growl2");
        PrimeFaces.current().ajax().update(":form:accord:dt-studys");
        PrimeFaces.current().ajax().update(":form:accord:state");
        PrimeFaces.current().ajax().update(":form:accord:toolbar");
        PrimeFaces.current().ajax().update(":form");
    }

    public void initNewUser() {
        selectedUser = new BitServerUser();
    }

    public void initNewUsergroup() {
        //selectedBitServerGroup = new BitServerGroup();
    }

    public void addNewUser(){
        if((selectedUser.getUname()!=null)&(!selectedUser.getPassword().equals(""))&(!selectedUser.getUgroup().equals(""))
                &(!selectedUser.getRole().equals(""))&(!selectedUser.getRuFamily().equals(""))&(!selectedUser.getRuMiddleName().equals(""))
                &(!selectedUser.getRuName().equals("")))
        {
            boolean verifiUnical = true;
            for(BitServerUser bufUser: bitServerUserList) {
                if (bufUser.getUname().equals(selectedUser.getUname())) {
                    verifiUnical = false;
                    break;
                }
            }
            BitServerUser bufUser = getRealUserForBase(selectedUser);
            if(verifiUnical){
                addUser(bufUser);
            }else{
                updateUser(bufUser);
            }
            bitServerUserList = getAllBitServerUserList();
            PrimeFaces.current().executeScript("PF('manageUserDialog').hide()");
            PrimeFaces.current().ajax().update(":form:accord:dt-users");
            LogTool.getLogger().info("Admin: "+currentUser.getSignature()+" add new user "+bufUser.getUname());

        }else{
            showMessage("Внимание!","Все поля должны быть заполнены!",FacesMessage.SEVERITY_ERROR);
        }
    }

    public BitServerUser getRealUserForBase(BitServerUser sourseUser){
        for(BitServerGroup bufgroup: bitServerGroupList){
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
        deleteFromBitServerTable(selectedUser.getUid());
        bitServerUserList.remove(selectedUser);
        selectedUser = new BitServerUser();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Пользователь удален!"));
        PrimeFaces.current().ajax().update(":form:accord:dt-users");
    }

    public void addNewUsergroup(){
        if((!selectedBitServerGroup.getRuName().equals("")))
        {
            boolean verifiUnical = true;
            for(BitServerGroup bufBitServerGroup : bitServerGroupList){
                if (bufBitServerGroup.getRuName().equals(selectedBitServerGroup.getRuName())) {
                    verifiUnical = false;
                    break;
                }
            }
            if(verifiUnical) {
                bitServerGroupList.add(new BitServerGroup(selectedBitServerGroup.getId(), selectedBitServerGroup.getRuName(), selectedBitServerGroup.getUserList()));
                //saveNewUsergroup(selectedUsergroup);
                PrimeFaces.current().executeScript("PF('manageUsergroupDialog').hide()");
                PrimeFaces.current().ajax().update(":form:accord:dt-usergroup");
                //usergroupList = getBitServerUsergroupList();
            }else{
                //updateUsergroup(selectedUsergroup);
                //usergroupList = getBitServerUsergroupList();
                PrimeFaces.current().executeScript("PF('manageUsergroupDialog').hide()");
                PrimeFaces.current().ajax().update(":form:accord:dt-usergroup");
            }
        }else{
            showMessage("Внимание!","Все поля должны быть заполнены!",FacesMessage.SEVERITY_ERROR);
        }
    }

    public void deleteUsergroupSetting() {
        try {
            //deleteUsergroup(selectedUsergroup);
            bitServerGroupList.remove(selectedBitServerGroup);
            //selectedBitServerGroup = new BitServerGroup();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Группа удалена!"));
            PrimeFaces.current().ajax().update(":form:accord:dt-usergroup");
        }catch (Exception e){
            PrimeFaces.current().executeScript("PF('errorDeleteUsergroupDialog').show()");
            LogTool.getLogger().error("Error deleteUserGroupSettings SettingsBitServerBean: "+e.getMessage());
        }
    }

}