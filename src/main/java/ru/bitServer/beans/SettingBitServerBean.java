package ru.bitServer.beans;

import org.primefaces.PrimeFaces;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dao.BitServerResources;
import ru.bitServer.dao.Usergroup;
import ru.bitServer.dao.Users;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static ru.bitServer.beans.AutoriseBean.showMessage;

@ManagedBean(name = "settingBitServerBean", eager = false)
@ViewScoped
public class SettingBitServerBean implements UserDao {

    public List<Users> usersList;
    public List<Users> selectedUsers;
    public Users selectedUser;
    public List<Usergroup> usergroupList;
    public List<String> usergroupListRuName;
    public List<Usergroup> selectedUsergroups;
    public Usergroup selectedUsergroup;
    public String osimisAddress;
    public String orthancAddress;
    public String orthancWebPort;
    public String orthancLogin;
    public String orthancPassword;
    public String orthancPathToJson;
    public String orthancPathToResultFile;
    public List<BitServerResources> bitServerResourcesList = new ArrayList<>();
    public Date syncdate;

    public Date getSyncdate() {
        return syncdate;
    }

    public void setSyncdate(Date syncdate) {
        this.syncdate = syncdate;
    }

    public String getOsimisAddress() {
        return osimisAddress;
    }

    public void setOsimisAddress(String osimisAddress) {
        this.osimisAddress = osimisAddress;
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


    @PostConstruct
    public void init() {
        System.out.println("settingBitServerBean page");
        usergroupList = getBitServerUsergroupList();
        usersList = prepareUserList();
        initNewUser();
        initNewUsergroup();
        bitServerResourcesList = getAllBitServerResource();
        for(BitServerResources buf: bitServerResourcesList){
            switch (buf.getRname()){
                case "orthancaddress": orthancAddress = buf.getRvalue();
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
                case "syncdate": {
                    try {
                        syncdate = format.parse(buf.getRvalue());
                    }catch (Exception e){
                        System.out.println("Ошибка преобразования даты "+e.getMessage());
                    }
                }
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
            }
            updateBitServiceResource(buf);
        }
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
            boolean verifiUnical = true;
            for(Users bufUser:usersList){
                if (bufUser.getUname().equals(selectedUser.getUname())) {
                    verifiUnical = false;
                    break;
                }
            }
            if(verifiUnical) {
                Users bufUser = getRealUserForBase(selectedUser);
                saveNewUser(bufUser);
                usersList = prepareUserList();
                PrimeFaces.current().executeScript("PF('manageUserDialog').hide()");
                PrimeFaces.current().ajax().update(":form:accord:dt-users");
            }else{
                Users bufUser = getRealUserForBase(selectedUser);
                updateUser(bufUser);
                usersList = prepareUserList();
                PrimeFaces.current().executeScript("PF('manageUserDialog').hide()");
                PrimeFaces.current().ajax().update(":form:accord:dt-users");
            }
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
                if(bufUsergroup.getRuName().equals(selectedUsergroup.getRuName())){
                    verifiUnical = false;
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
            System.out.println("error delete = "+e.getMessage());
        }
    }

    public void setsyncdate(){
        System.out.println("asdqwe");
    }
}