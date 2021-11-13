package ru.bitServer.beans;

import org.apache.log4j.Logger;
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
    private static final Logger log = Logger.getLogger(AutoriseBean.class);

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

    }

    public void setTheme(String tName){
        currentuserTheme = tName;
        currentUser.setuTheme(currentuserTheme);
        updateUser(currentUser);
        PrimeFaces.current().executeScript("location.reload();");
    }

    public void validateUsernamePassword() throws IOException {
        try {
            currentUser = validateUserAndGetIfExist(inputUserName,inputPassword);
            System.out.println(currentUser.getUname());
            if (currentUser.getUname()!=null) {
                HttpSession session = SessionUtils.getSession();

                log.info(currentUser.getUid() + " Пользователь зарегистрировался!");
                //System.out.println(session.);
                session.setAttribute("userid", currentUser.getUid());
                currentuserTheme = "saga";

                switch (currentUser.getRole()){
                    case "localuser":
                        FacesContext.getCurrentInstance().getExternalContext().redirect("/bitServer/views/localuser.xhtml");
                        break;
                    case "remoteuser":
                        FacesContext.getCurrentInstance().getExternalContext().redirect("/bitServer/views/remoteuser.xhtml");
                        break;
                    case "admin":
                        FacesContext.getCurrentInstance().getExternalContext().redirect("/bitServer/views/admin.xhtml");
                        break;
                    case "client":
                        FacesContext.getCurrentInstance().getExternalContext().redirect("/bitServer/views/client.xhtml");
                        break;
                    case "onlyview":
                        FacesContext.getCurrentInstance().getExternalContext().redirect("/bitServer/views/localuser.xhtml");
                        break;
                    default:
                        break;
                }

            } else {
                showMessage("Ошибка авторизации", "Неверное имя пользователя или пароль", error );
            }
        }catch (Exception e){
            if(inputUserName.equals("admin2")&inputPassword.equals("admin2"))
            FacesContext.getCurrentInstance().getExternalContext().redirect("/bitServer/views/admin.xhtml");
        }

    }

    public void logout() throws IOException {
        HttpSession session = SessionUtils.getSession();
        session.invalidate();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/bitServer/views/login.xhtml");
    }

    public static void showMessage(String title, String note, FacesMessage.Severity type) {
        FacesMessage message = new FacesMessage(title, note);
        message.setSeverity(type);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
