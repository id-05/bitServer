package ru.bitServer.beans;

import org.primefaces.PrimeFaces;
import ru.bitServer.dao.*;
import ru.bitServer.util.SessionUtils;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;
import java.util.*;
import static ru.bitServer.beans.AutoriseBean.showMessage;

@ManagedBean (name = "schedulerBean", eager = false)
@ViewScoped
public class SchedulerBean implements UserDao {

    Users currentUser;
    String currentUserId;
    BitServerScheduler selectedRule;
    List<BitServerScheduler> visibleRules = new ArrayList<>();
    List<BitServerScheduler> bufRules = new ArrayList<>();
    String selectedmodality;
    String selectsource;
    List<String> modalitylist = new ArrayList<>();
    List<String> sourcelist = new ArrayList<>();
    String selecttimecondition;
    public List<Usergroup> usergroupList;
    List<String> usergroupListRuName = new ArrayList<>();
    Usergroup selectedgroup;
    Date selectedtime;
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
        visibleRules = getAllBitServerSheduler();
        bufRules = getAllBitServerSheduler();

        modalitylist.clear();
        modalitylist.add("Все");
        modalitylist.add("CR");
        modalitylist.add("CT");
        modalitylist.add("MR");
        modalitylist.add("NM");
        modalitylist.add("PT");
        modalitylist.add("US");
        modalitylist.add("XA");
        modalitylist.add("CR");
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
        usergroupList = getRealBitServerUsergroupList();
        initNewRule();
        selectedtime = new Date();
        selectedRule.setTime(selectedtime);
    }

    public void initNewRule() {
        System.out.println("new rule");
        selectedRule = new BitServerScheduler();
    }

    public void addNewRule(){
//        selectedRule.setTime(new Date());
//        selectedRule.setTime(selectedtime);
//        System.out.println(selectedRule.getTimecondition());
//
//        if(!selectedRule.getModality().equals(""))
//        {
//            boolean verifiunical = true;
//            for(BitServerScheduler bufRule:bufRules){
//                if(selectedRule.getTime().equals(bufRule.getTime())){
//                    verifiunical = false;
//                    break;
//                }
//            }
//
//               if(verifiunical) {
//                   System.out.println("unical");
//                    BitServerScheduler bufRule = getRealRuleForBase(selectedRule);
//                    visibleRules.add(bufRule);
//                    //visibleRules.add(new BitServerScheduler(currentUserId, destinationgroup, selectedRule.getTimecondition(), selectedRule.getSource(), selectedRule.getTime(), selectedRule.getModality()));
//                    saveNewRule(bufRule);
//                    visibleRules = getAllBitServerSheduler();
//                    PrimeFaces.current().executeScript("PF('manageRuleDialog').hide()");
//                    PrimeFaces.current().ajax().update(":scheduler:dt-rules");
//               }else{
//                   System.out.println("ununical");
//                    BitServerScheduler bufRule = getRealRuleForBase(selectedRule);
//                    updateRule(bufRule);
//
//                   visibleRules = getAllBitServerSheduler();
//                   PrimeFaces.current().executeScript("PF('manageRuleDialog').hide()");
//                   PrimeFaces.current().ajax().update(":scheduler:dt-rules");
//               }
//
//        }else{
//            showMessage("Внимание!","Все поля должны быть заполнены!", FacesMessage.SEVERITY_ERROR);
//        }

        PrimeFaces.current().executeScript("PF('manageRuleDialog').hide()");
        //PrimeFaces.current().ajax().update(":scheduler");
    }

    public void deleteSelectedRule(){

    }

    public BitServerScheduler getRealRuleForBase(BitServerScheduler sourceRule){
        // buf = sourceRule.getDestinationgroup();
        return sourceRule;
    }

    public void timeselect(){
        System.out.println(selectedtime.toString());
    }
}
