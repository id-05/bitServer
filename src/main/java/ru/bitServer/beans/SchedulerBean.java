package ru.bitServer.beans;

import ru.bitServer.dao.BitServerScheduler;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dao.Usergroup;
import ru.bitServer.dao.Users;
import ru.bitServer.util.OrthancRestApi;
import ru.bitServer.util.SessionUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ru.bitServer.beans.MainBean.mainServer;

@ManagedBean (name = "schedulerBean")
@ViewScoped
public class SchedulerBean implements UserDao {

    Users currentUser;
    String currentUserId;
    BitServerScheduler selectedRule;
    List<BitServerScheduler> visibleRules = new ArrayList<>();
    String selectedmodality;
    String selectsource;
    List<String> modalitylist = new ArrayList<>();
    List<String> sourcelist = new ArrayList<>();
    String selecttimecondition;
    List<String> usergroupListRuName = new ArrayList<>();
    Usergroup selectedgroup;
    Date selectedtime;

    public Usergroup getSelectedgroup() {
        return selectedgroup;
    }

    public void setSelectedgroup(Usergroup selectedgroup) {
        this.selectedgroup = selectedgroup;
    }

    public Date getSelectedtime() {
        return selectedtime;
    }

    public void setSelectedtime(Date selectedtime) {
        this.selectedtime = selectedtime;
    }

    public String getSelectedmodality() {
        return selectedmodality;
    }

    public void setSelectedmodality(String selectedmodality) {
        this.selectedmodality = selectedmodality;
    }

    public String getSelectsource() {
        return selectsource;
    }

    public void setSelectsource(String selectsource) {
        this.selectsource = selectsource;
    }

    public List<String> getModalitylist() {
        return modalitylist;
    }

    public void setModalitylist(List<String> modalitylist) {
        this.modalitylist = modalitylist;
    }

    public List<String> getSourcelist() {
        return sourcelist;
    }

    public void setSourcelist(List<String> sourcelist) {
        this.sourcelist = sourcelist;
    }

    public String getSelecttimecondition() {
        return selecttimecondition;
    }

    public void setSelecttimecondition(String selecttimecondition) {
        this.selecttimecondition = selecttimecondition;
    }

    public List<String> getUsergroupListRuName() {
        return usergroupListRuName;
    }

    public void setUsergroupListRuName(List<String> usergroupListRuName) {
        this.usergroupListRuName = usergroupListRuName;
    }

    public List<BitServerScheduler> getVisibleRules() {
        return visibleRules;
    }

    public void setVisibleRules(List<BitServerScheduler> visibleRules) {
        this.visibleRules = visibleRules;
    }

    public BitServerScheduler getSelectedRule() {
        return selectedRule;
    }

    public void setSelectedRule(BitServerScheduler selectedRule) {
        this.selectedRule = selectedRule;
    }

    public Users getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Users currentUser) {
        this.currentUser = currentUser;
    }

    @PostConstruct
    public void init() {
        System.out.println("lucurrenttaskBean");
        HttpSession session = SessionUtils.getSession();
        currentUserId = session.getAttribute("userid").toString();
        currentUser = getUserById(currentUserId);
        visibleRules.clear();
        visibleRules = getAllBitServerSheduler();
    }

    public void initNewRule() {
        System.out.println("new rule");
        selectedRule = new BitServerScheduler();
    }

    public void addNewRule(){

    }

    public void deleteSelectedRule(){

    }
}
