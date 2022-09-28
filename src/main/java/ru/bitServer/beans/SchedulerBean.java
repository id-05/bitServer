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

@ManagedBean (name = "schedulerBean")
@ViewScoped
public class SchedulerBean implements UserDao {

    BitServerUser currentUser;
    String currentUserId;
    BitServerScheduler selectedRule;
    List<BitServerScheduler> visibleRules = new ArrayList<>();
    String selectsource;
    List<String> modalitylist = new ArrayList<>();
    List<String> sourcelist = new ArrayList<>();
    List<BitServerGroup> bitServerGroupList;
    List<String> usergroupListRuName = new ArrayList<>();
    LocalTime selectedtime;
    List<BitServerScheduler> selectedRules = new ArrayList<>();

    public List<BitServerScheduler> getSelectedRules() {
        return selectedRules;
    }

    public void setSelectedRules(List<BitServerScheduler> selectedRules) {
        this.selectedRules = selectedRules;
    }

    public LocalTime getSelectedtime() {
        return selectedtime;
    }

    public void setSelectedtime(LocalTime selectedtime) {
        this.selectedtime = selectedtime;
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

    public List<String> getUsergroupListRuName() {
        usergroupListRuName = new ArrayList<>();
        for(BitServerGroup bufgroup: bitServerGroupList){
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

    public BitServerUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(BitServerUser currentUser) {
        this.currentUser = currentUser;
    }

    @PostConstruct
    public void init() {
        System.out.println("lucurrenttaskBean");
        HttpSession session = SessionUtils.getSession();
        currentUserId = session.getAttribute("userid").toString();
        currentUser = getUserById(currentUserId);
        visibleRules.clear();
        //usergroupList = getRealBitServerUsergroupList();
        //visibleRules = getPrepareBitServiceScheduler();
        modalitylist.clear();
        modalitylist.add("Все");
        //List<BitServerModality> modalityFromBase = getAllBitServerModality();
//        for(BitServerModality bufModality:modalityFromBase){
//            modalitylist.add(bufModality.getName());
//        }

        sourcelist.clear();
        sourcelist.add("Все");
        List<String> buflist = new ArrayList<>();
        //List<BitServerStudy> bufStudyList = getAllBitServerStudy();
//        for(BitServerStudy bufStudy:bufStudyList){
//            buflist.add(bufStudy.getSource());
//        }
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
            BitServerScheduler bufRule = getRealRuleForBase(selectedRule);
            visibleRules.add(bufRule);
            //saveNewRule(bufRule);
            //visibleRules = getPrepareBitServiceScheduler();
            PrimeFaces.current().executeScript("PF('manageRuleDialog').hide()");
            PrimeFaces.current().ajax().update(":form:dt-rules");
        }else{
            showMessage("Внимание!","Все поля должны быть заполнены!", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void deleteSelectedRule(){
        //deleteRule(selectedRule);
        visibleRules.remove(selectedRule);
        selectedRule = new BitServerScheduler();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Правило удалено!"));
        PrimeFaces.current().ajax().update(":form:dt-rules");
    }

    public BitServerScheduler getRealRuleForBase(BitServerScheduler sourceRule){
        for(BitServerGroup bufgroup: bitServerGroupList) {
            if (bufgroup.getRuName().equals(selectedRule.getDestinationgroup())) {
                selectedRule.setDestinationgroup(String.valueOf(bufgroup.getId()));
            }
        }
        return sourceRule;
    }

    public void setTimeToDialog(){
        selectedtime = LocalTime.of(Integer.parseInt(selectedRule.getClock()),Integer.parseInt(selectedRule.getMinute()));
    }

//    public List<BitServerScheduler> getPrepareBitServiceScheduler(){
//        List<BitServerScheduler> outputList = getAllBitServerSheduler();
//        for(BitServerScheduler bufRule:outputList){
//            int buf = Integer.parseInt(bufRule.getDestinationgroup());
//            bufRule.setDestinationgroup(getVisibleNameOfGroup(buf));
//            if(bufRule.getClock().length()<2){
//                bufRule.setClock("0"+bufRule.getClock());
//            }
//            if(bufRule.getMinute().length()<2){
//                bufRule.setMinute("0"+bufRule.getMinute());
//            }
//        }
//        return outputList;
//    }

    public String getVisibleNameOfGroup(int i){
        String buf = null;
        for(BitServerGroup bufgroup: bitServerGroupList){
            if(bufgroup.getId()==i){
                buf = bufgroup.getRuName();
                break;
            }
        }
        return buf;
    }
}