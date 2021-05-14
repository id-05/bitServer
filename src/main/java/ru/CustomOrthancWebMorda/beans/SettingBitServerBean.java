package ru.CustomOrthancWebMorda.beans;

import org.primefaces.PrimeFaces;
import ru.CustomOrthancWebMorda.beans.dao.Usergroup;
import ru.CustomOrthancWebMorda.beans.dao.Users;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

import static ru.CustomOrthancWebMorda.beans.AutoriseBean.showMessage;


@ManagedBean(name = "settingBitServerBean", eager = false)
@SessionScoped
public class SettingBitServerBean implements UserDao{

    public List<Users> usersList;
    public List<Users> selectedUsers;
    public Users selectedUser;

    public List<Usergroup> usergroupList;
    public List<String> usergroupListRuName;
    public List<Usergroup> selectedUsergroups;
    public Usergroup selectedUsergroup;
    public String externalAdress;
    public BitServiceDBresources bitServiceDBresources;

    public String getExternalAdress() {
        return externalAdress;
    }

    public void setExternalAdress(String externalAdress) {
        this.externalAdress = externalAdress;
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
        bitServiceDBresources = getBitServerResource("address");
        externalAdress = bitServiceDBresources.getRvalue();
    }

    public void saveAddress(){
        bitServiceDBresources.setRvalue(externalAdress);
        updateBitServiceDBresource(bitServiceDBresources);
        System.out.println("save");
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
        if((selectedUsergroup.getgName()!=null)&(!selectedUsergroup.getRuName().equals(""))&(!selectedUsergroup.getStatus().equals("")))
        {
            boolean verifiUnical = true;
            for(Usergroup bufUsergroup:usergroupList){
                if(bufUsergroup.getRuName().equals(selectedUsergroup.getRuName())){
                    verifiUnical = false;
                }
            }
            if(verifiUnical) {
                usergroupList.add(new Usergroup(selectedUsergroup.getgName(), selectedUsergroup.getRuName(), selectedUsergroup.getStatus()));
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
