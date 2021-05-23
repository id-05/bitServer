package ru.bitServer.beans;



import org.primefaces.PrimeFaces;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dao.Users;
import ru.bitServer.util.SessionUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@ManagedBean(name = "autoriseBean", eager = false)
@SessionScoped
public class AutoriseBean implements UserDao {

    FacesMessage.Severity info = FacesMessage.SEVERITY_INFO;
    FacesMessage.Severity error = FacesMessage.SEVERITY_ERROR;
    FacesMessage.Severity warning = FacesMessage.SEVERITY_WARN;

    String inputUserName;
    String inputPassword;

    public Users getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Users currentUser) {
        this.currentUser = currentUser;
    }

    public Users currentUser;

    public String currentuserTheme = "saga";

    public String getCurrentuserTheme() {
        return currentuserTheme;
    }

    public void setCurrentuserTheme(String currentuserTheme) {
        this.currentuserTheme = currentuserTheme;
    }

    public String getInputUserName() {
        return inputUserName;
    }

    public void setInputUserName(String inputUserName) {
        this.inputUserName = inputUserName;
    }

    public String getInputPassword() {
        return inputPassword;
    }

    public void setInputPassword(String inputPassword) {
        this.inputPassword = inputPassword;
    }

    @PostConstruct
    public void init() {
       System.out.println("autorise");
       initialHibernate();
    }

    public void setTheme(String tName){
        currentuserTheme = tName;
        currentUser.setuTheme(currentuserTheme);
        updateUser(currentUser);
        PrimeFaces.current().executeScript("location.reload();");
    }

    public void validateUsernamePassword() throws IOException {
        currentUser = validateUserAndGetIfExist(inputUserName,inputPassword);
        System.out.println(currentUser.getUname());
        if (currentUser.getUname()!=null) {
            HttpSession session = SessionUtils.getSession();
            session.setAttribute("userid", currentUser.getUid());
            if(!currentUser.getuTheme().equals("")){
            currentuserTheme = currentUser.getuTheme();
            }else{
                currentuserTheme = "saga";
            }
            switch (currentUser.getRole()){
                case "localuser":
                        FacesContext.getCurrentInstance().getExternalContext().redirect("/views/localuser.xhtml");
                    break;
                case "remoteuser":
                        FacesContext.getCurrentInstance().getExternalContext().redirect("/views/remoteuser.xhtml");
                        break;
                case "admin":
                        FacesContext.getCurrentInstance().getExternalContext().redirect("/views/admin.xhtml");
                        break;
                default:
                        break;
            }

        } else {
            showMessage("Ошибка авторизации", "Неверное имя пользователя или пароль", error );
        }
    }

    public void logout() throws IOException {
        HttpSession session = SessionUtils.getSession();
        session.invalidate();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/views/login.xhtml");
    }

    public static void showMessage(String title, String note, FacesMessage.Severity type) {
        FacesMessage message = new FacesMessage(title, note);
        message.setSeverity(type);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
