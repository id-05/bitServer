package ru.bitServer.beans;

import org.primefaces.PrimeFaces;
import ru.bitServer.dao.*;
import ru.bitServer.util.SessionUtils;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.time.LocalTime;
import java.util.*;
import static ru.bitServer.beans.AutoriseBean.showMessage;

@ManagedBean (name = "schedulerBean", eager = false)
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
    public List<Usergroup> usergroupList;
    List<String> usergroupListRuName = new ArrayList<>();
    Usergroup selectedgroup;
    LocalTime selectedtime;
    List<BitServerScheduler> selectedRules = new ArrayList<>();

    public List<BitServerScheduler> getSelectedRules() {
        return selectedRules;
    }

    public void setSelectedRules(List<BitServerScheduler> selectedRules) {
        this.selectedRules = selectedRules;
    }

    public Usergroup getSelectedgroup() {
        return selectedgroup;
    }

    public void setSelectedgroup(Usergroup selectedgroup) {
        this.selectedgroup = selectedgroup;
    }

    public LocalTime getSelectedtime() {
        return selectedtime;
    }

    public void setSelectedtime(LocalTime selectedtime) {
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
        usergroupListRuName = new ArrayList<>();
        for(Usergroup bufgroup:usergroupList){
            usergroupListRuName.add(bufgroup.getRuName());
        }
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
        usergroupList = getRealBitServerUsergroupList();
        visibleRules = getPrepareBitServiceScheduler();
        modalitylist.clear();
        modalitylist.add("Все");
        modalitylist.add("CR");
        modalitylist.add("CT");
        modalitylist.add("MR");
        modalitylist.add("NM");
        modalitylist.add("PT");
        modalitylist.add("US");
        modalitylist.add("XA");
        modalitylist.add("MG");
        modalitylist.add("DX");
        sourcelist.clear();
        sourcelist.add("Все");
        List<String> buflist = new ArrayList<>();
        List<BitServerStudy> bufStudyList = getAllBitServerStudy();
        for(BitServerStudy bufStudy:bufStudyList){
            buflist.add(bufStudy.getSource());
        }
        Set<String> set = new LinkedHashSet<>(buflist);
        sourcelist.addAll(set);
        selectedtime = LocalTime.now();
        selectedRule = new BitServerScheduler();
    }

    public void initNewRule() {
        selectedRule = new BitServerScheduler();
    }

    public void addNewRule(){
        selectedRule.setMinute(String.valueOf(selectedtime.getMinute()));
        selectedRule.setClock(String.valueOf(selectedtime.getHour()));
        if(!selectedRule.getModality().equals("")&&!selectedRule.getSource().equals(""))
        {
            System.out.println("unical");
            BitServerScheduler bufRule = getRealRuleForBase(selectedRule);
            visibleRules.add(bufRule);
            saveNewRule(bufRule);
            visibleRules = getPrepareBitServiceScheduler();
            PrimeFaces.current().executeScript("PF('manageRuleDialog').hide()");
            PrimeFaces.current().ajax().update(":form:dt-rules");
        }else{
            showMessage("Внимание!","Все поля должны быть заполнены!", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void deleteSelectedRule(){
        deleteRule(selectedRule);
        visibleRules.remove(selectedRule);
        selectedRule = new BitServerScheduler();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Правило удалено!"));
        PrimeFaces.current().ajax().update(":form:dt-rules");
    }

    public BitServerScheduler getRealRuleForBase(BitServerScheduler sourceRule){
        for(Usergroup bufgroup:usergroupList) {
            if (bufgroup.getRuName().equals(selectedRule.getDestinationgroup())) {
                selectedRule.setDestinationgroup(String.valueOf(bufgroup.getId()));
            }
        }
        return sourceRule;
    }

    public void setTimeToDialog(){
        selectedtime = LocalTime.of(Integer.parseInt(selectedRule.getClock()),Integer.parseInt(selectedRule.getMinute()));
    }

    public List<BitServerScheduler> getPrepareBitServiceScheduler(){
        List<BitServerScheduler> outputList = getAllBitServerSheduler();
        for(BitServerScheduler bufRule:outputList){
            int buf = Integer.parseInt(bufRule.getDestinationgroup());
            bufRule.setDestinationgroup(getVisibleNameOfGroup(buf));
            if(bufRule.getClock().length()<2){
                bufRule.setClock("0"+bufRule.getClock());
            }
            if(bufRule.getMinute().length()<2){
                bufRule.setMinute("0"+bufRule.getMinute());
            }
        }
        return outputList;
    }

    public String getVisibleNameOfGroup(int i){
        String buf = null;
        for(Usergroup bufgroup:usergroupList){
            if(bufgroup.getId()==i){
                buf = bufgroup.getRuName();
                break;
            }
        }
        return buf;
    }
}