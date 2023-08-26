package ru.bitServer.beans;

import ru.bitServer.dao.BitServerResources;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dao.BitServerUser;
import ru.bitServer.util.LogTool;
import ru.bitServer.util.SessionUtils;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


@ManagedBean(name = "localDashboard")
@ViewScoped
public class LocalUserDashboardBean implements UserDao {

    String optSend;
    BitServerUser currentUser;
    List<BitServerResources> bitServerResourcesList = new ArrayList<>();

    public String getOptSend() {
        return optSend;
    }

    public void setOptSend(String optSend) {
        this.optSend = optSend;
    }

    public BitServerUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(BitServerUser currentUser) {
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

        bitServerResourcesList = getAllBitServerResource();
        for(BitServerResources buf: bitServerResourcesList) {
            switch (buf.getRname()) {
                case "OptionSend":
                    optSend = buf.getRvalue();
                    break;
            }
        }
    }
}
