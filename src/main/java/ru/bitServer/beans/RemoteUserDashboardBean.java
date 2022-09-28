package ru.bitServer.beans;

import ru.bitServer.dao.BitServerUser;
import ru.bitServer.dao.UserDao;
import ru.bitServer.util.SessionUtils;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;


@ManagedBean(name = "remoteDashboard")
@ViewScoped
public class RemoteUserDashboardBean implements UserDao {


    BitServerUser currentUser;

    public BitServerUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(BitServerUser currentUser) {
        this.currentUser = currentUser;
    }

    @PostConstruct
    public void init()  {
        HttpSession session = SessionUtils.getSession();
        currentUser = getUserById(session.getAttribute("userid").toString());
    }


}
