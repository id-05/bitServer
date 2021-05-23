package ru.bitServer.beans;

import org.primefaces.PrimeFaces;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dao.BitServerDBresources;
import ru.bitServer.dao.Usergroup;
import ru.bitServer.dao.Users;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

import static ru.bitServer.beans.AutoriseBean.showMessage;

@ManagedBean(name = "settingBitServerBean", eager = false)
@SessionScoped
public class SettingBitServerBean implements UserDao {

    public List<Users> usersList;
    public List<Users> selectedUsers;
    public Users selectedUser;
    public List<Usergroup> usergroupList;
    public List<String> usergroupListRuName;
    public List<Usergroup> selectedUsergroups;
    public Usergroup selectedUsergroup;
    public String externalAddress;
    public String orthancWebPort;
    public String orthancLogin;
    public String orthancPassword;
    public String orthancPathToJson;
    public String orthancPathToResultFile;
    public BitServerDBresources bitServerDBresources;
    public List<BitServerDBresources> bitServerDBresourcesList = new ArrayList<>();

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

    public String getExternalAddress() {
        return externalAddress;
    }

    public void setExternalAddress(String externalAddress) {
        this.externalAddress = externalAddress;
    }

    public List<String> getUsergroupListRuName() {
        usergroupListRuName = new ArrayList<String>();
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
    public void init(){
        System.out.println("settingBitServerBean page");
        usersList = getBitServerUserList();
        usergroupList = getBitServerUsergroupList();
            for(Usergroup buf:usergroupList){
                System.out.println("buf "+buf.getRuName());
            }
        initNewUser();
        initNewUsergroup();
        bitServerDBresourcesList = getAllBitServerResource();
        for(BitServerDBresources buf:bitServerDBresourcesList){
            switch (buf.getRname()){
                case "address": externalAddress = buf.getRvalue();
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
            }
        }

    }

    public void saveParam(){
        for(BitServerDBresources buf:bitServerDBresourcesList){
            switch (buf.getRname()){
                case "address": buf.setRvalue(externalAddress);
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
            }
            updateBitServiceDBresource(buf);
        }
        //bitServerDBresources.setRvalue(externalAddress);
    }

    public void initNewUser() {
        selectedUser = new Users();
    }

    public void initNewUsergroup() {
        selectedUsergroup = new Usergroup();
    }

    public void AddNewUser(){
        if((selectedUser.getUname()!=null)&(!selectedUser.getPassword().equals(""))&(!selectedUser.getGroupUser().equals(""))
                &(!selectedUser.getRole().equals(""))&(!selectedUser.getRuFamily().equals(""))&(!selectedUser.getRuMiddleName().equals(""))
                &(!selectedUser.getRuName().equals("")))
        {
            boolean verifiUnical = true;
            for(Users bufUser:usersList){
                if(bufUser.getUname().equals(selectedUser.getUname())){
                    verifiUnical = false;
                }
            }
            if(verifiUnical) {
                usersList.add(new Users(selectedUser.getUname(), selectedUser.getPassword(),
                        selectedUser.getRuName(), selectedUser.getRuMiddleName(),
                        selectedUser.getRuFamily(), selectedUser.getRole(), selectedUser.getGroupUser()));
                saveNewUser(selectedUser);
                PrimeFaces.current().executeScript("PF('manageUserDialog').hide()");
                PrimeFaces.current().ajax().update(":form:accord:dt-users");
            }else{
                updateUser(selectedUser);
                usersList = getBitServerUserList();
                PrimeFaces.current().executeScript("PF('manageUserDialog').hide()");
                PrimeFaces.current().ajax().update(":form:accord:dt-users");
            }
        }else{
            showMessage("Внимание!","Все поля должны быть заполнены!",FacesMessage.SEVERITY_ERROR);
        }
    }

    public void deleteUserSetting() {
        deleteUser(selectedUser);
        usersList.remove(selectedUser);
        selectedUser = new Users();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Пользователь удален!"));
        PrimeFaces.current().ajax().update(":form:accord:dt-users");
    }

    public void AddNewUsergroup(){
        if((selectedUsergroup.getgType()!=null)&(!selectedUsergroup.getRuName().equals(""))&(!selectedUsergroup.getStatus().equals("")))
        {
            boolean verifiUnical = true;
            for(Usergroup bufUsergroup:usergroupList){
                if(bufUsergroup.getRuName().equals(selectedUsergroup.getRuName())){
                    verifiUnical = false;
                }
            }
            if(verifiUnical) {
                usergroupList.add(new Usergroup(selectedUsergroup.getgType(), selectedUsergroup.getRuName(), selectedUsergroup.getStatus()));
                saveNewUsergroup(selectedUsergroup);
                PrimeFaces.current().executeScript("PF('manageUsergroupDialog').hide()");
                PrimeFaces.current().ajax().update(":form:accord:dt-usergroup");
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
        deleteUsergroup(selectedUsergroup);
        usergroupList.remove(selectedUsergroup);
        selectedUsergroup = new Usergroup();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Группа удалена!"));
        PrimeFaces.current().ajax().update(":form:accord:dt-usergroup");
    }
}
