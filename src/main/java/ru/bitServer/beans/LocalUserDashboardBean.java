package ru.bitServer.beans;

import ru.bitServer.dao.UserDao;
import ru.bitServer.dao.Users;
import ru.bitServer.util.LogTool;
import ru.bitServer.util.SessionUtils;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "localDashboard")
@ViewScoped
public class LocalUserDashboardBean implements UserDao {
    Users currentUser;

    public Users getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Users currentUser) {
        this.currentUser = currentUser;
    }

    @PostConstruct
    public void init() {
        try {
            HttpSession session = SessionUtils.getSession();
            currentUser = getUserById(session.getAttribute("userid").toString());
        }catch (Exception e){
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            try{
                ec.redirect(ec.getRequestContextPath()
                        + "/views/errorpage.xhtml");
            }catch (Exception e2){
                LogTool.getLogger().warn("Error init() LocalUserDashboardBean: "+e2.getMessage());
            }
        }
    }
}
