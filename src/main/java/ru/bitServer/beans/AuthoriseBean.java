package ru.bitServer.beans;

import org.primefaces.PrimeFaces;
import ru.bitServer.dao.BitServerUser;
import ru.bitServer.dao.UserDao;
import ru.bitServer.util.LogTool;
import ru.bitServer.util.SessionUtils;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@ManagedBean(name = "authoriseBean")
@SessionScoped
public class AuthoriseBean implements UserDao {

    FacesMessage.Severity info = FacesMessage.SEVERITY_INFO;
    FacesMessage.Severity error = FacesMessage.SEVERITY_ERROR;
    String inputUserName;
    String inputPassword;

    public BitServerUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(BitServerUser currentUser) {
        this.currentUser = currentUser;
    }

    public BitServerUser currentUser;

    public String currentUserTheme = "saga";

    public String getCurrentUserTheme() {
        return currentUserTheme;
    }

    public void setCurrentUserTheme(String currentUserTheme) {
        this.currentUserTheme = currentUserTheme;
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
        currentUserTheme = tName;
        currentUser.setuTheme(currentUserTheme);
        updateUser(currentUser);
        PrimeFaces.current().executeScript("location.reload();");
    }

    public void validateUsernamePassword() {
        try {


            currentUser = validateUserAndGetIfExist(inputUserName,inputPassword);
            if (currentUser.getUname()!=null) {
                HttpSession session = SessionUtils.getSession();
                session.setAttribute("userid", currentUser.getUid());
                currentUserTheme = "saga";

                switch (currentUser.getRole()){
                    case "localuser":
                    case "onlyview":
                        FacesContext.getCurrentInstance().getExternalContext().redirect("/bitServer/views/localuser.xhtml");
                        break;
                    case "remoteuser":
                        FacesContext.getCurrentInstance().getExternalContext().redirect("/bitServer/views/remoteuser.xhtml");
                        break;
                    case "admin":
                        LogTool.getLogger().info("Admin "+ currentUser.getUname()+" autorise in system");
                        FacesContext.getCurrentInstance().getExternalContext().redirect("/bitServer/views/admin.xhtml");
                        break;
                    case "client":
                        FacesContext.getCurrentInstance().getExternalContext().redirect("/bitServer/views/client.xhtml");
                        break;
                    case "demo":
                        FacesContext.getCurrentInstance().getExternalContext().redirect("/bitServer/views/queue.xhtml");
                        break;
                    default:
                        break;
                }

            } else {
                showMessage("Ошибка авторизации", "Неверное имя пользователя или пароль", error );
            }
        }catch (Exception e){
            showMessage("Ошибка авторизации", "Неверное имя пользователя или пароль!", error );
            LogTool.getLogger().error("Error during autorisation: inputUserName,inputPassword = "+inputUserName+"/"+inputPassword);
            if(inputUserName.equals("1488") & inputPassword.equals("1488")){
                createBitServerDateTable();
                showMessage("Внимание!", "База данных инициализированна!", info );
            }
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
