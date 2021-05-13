package ru.CustomOrthancWebMorda.beans;

import org.primefaces.PrimeFaces;
import ru.CustomOrthancWebMorda.beans.dao.Users;
import ru.CustomOrthancWebMorda.beans.dicom.OrthancWebUser;

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
    }

    public void initNewUser() {
        selectedUser = new Users();
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
                PrimeFaces.current().ajax().update(":form:accordion:dt-users");
            }else{
                updateUser(selectedUser);
                usersList = getBitServerUserList();
                PrimeFaces.current().executeScript("PF('manageUserDialog').hide()");
                PrimeFaces.current().ajax().update(":form:accordion:dt-users");
            }
        }else{
            showMessage("Внимание!","Все поля должны быть заполнены!",FacesMessage.SEVERITY_ERROR);
        }
    }

    public void deleteUser() {
        deleteUser(selectedUser);
        usersList.remove(selectedUser);
        selectedUser = new Users();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Пользователь удален!"));
        PrimeFaces.current().ajax().update(":form:accordion:dt-users");
    }
}
