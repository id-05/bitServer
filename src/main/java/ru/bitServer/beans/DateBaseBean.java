package ru.bitServer.beans;

import org.primefaces.PrimeFaces;
import ru.bitServer.dao.*;
import ru.bitServer.util.LogTool;
import ru.bitServer.util.SessionUtils;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.util.*;
import static ru.bitServer.beans.AutoriseBean.showMessage;

@ManagedBean(name = "datebaseBean")
@ViewScoped
public class DateBaseBean implements UserDao {

    List<BitServerResources> listResources = new ArrayList<>();
    BitServerResources selectedResource;
    //BitServerResources selectedResources;
    List<BitServerStudy> listStudys = new ArrayList<>();
    BitServerStudy selectedStudy;
    //BitServerStudy selectedStudys;
    List<BitServerScheduler> listSchedulers = new ArrayList<>();
    BitServerScheduler selectedScheduler;
    BitServerScheduler selectedSchedulers;
    //List<BitServerModality> listModalitys = new ArrayList<>();
    //BitServerModality selectedModality;
    //BitServerModality selectedModalitys;
    BitServerUser currentUser;
    String currentUserId;
    List<BitServerResources> bitServerResourcesList = new ArrayList<>();
    //final SimpleDateFormat FORMAT2 = new SimpleDateFormat("yyyy/MM/dd");

    public BitServerScheduler getSelectedSchedulers() {
        return selectedSchedulers;
    }

    public void setSelectedSchedulers(BitServerScheduler selectedSchedulers) {
        this.selectedSchedulers = selectedSchedulers;
    }

//    public BitServerStudy getSelectedStudys() {
//        return selectedStudys;
//    }

//    public void setSelectedStudys(BitServerStudy selectedStudys) {
//        this.selectedStudys = selectedStudys;
//    }

//    public BitServerResources getSelectedResources() {
//        return selectedResources;
//    }
//
//    public void setSelectedResources(BitServerResources selectedResources) {
//        this.selectedResources = selectedResources;
//    }
//    public BitServerModality getSelectedModality() {
//        return selectedModality;
//    }
//
//    public void setSelectedModality(BitServerModality selectedModality) {
//        this.selectedModality = selectedModality;
//    }

    public List<BitServerScheduler> getListSchedulers() {
        return listSchedulers;
    }

    public void setListSchedulers(List<BitServerScheduler> listSchedulers) {
        this.listSchedulers = listSchedulers;
    }

    public BitServerScheduler getSelectedScheduler() {
        return selectedScheduler;
    }

    public void setSelectedScheduler(BitServerScheduler selectedScheduler) {
        this.selectedScheduler = selectedScheduler;
    }

//    public List<BitServerStudy> getListStudys() {
//        return listStudys;
//    }
//
//    public void setListStudys(List<BitServerStudy> listStudys) {
//        this.listStudys = listStudys;
//    }

//    public BitServerStudy getSelectedStudy() {
//        return selectedStudy;
//    }
//
//    public void setSelectedStudy(BitServerStudy selectedStudy) {
//        this.selectedStudy = selectedStudy;
//    }

    public BitServerResources getSelectedResource() {
        return selectedResource;
    }

    public void setSelectedResource(BitServerResources selectedResource) {
        this.selectedResource = selectedResource;
    }

    public List<BitServerResources> getListResources() {
        return listResources;
    }

    public void setListResources(List<BitServerResources> listResources) {
        this.listResources = listResources;
    }

    @PostConstruct
    public void init() {
        HttpSession session = SessionUtils.getSession();
        currentUserId = session.getAttribute("userid").toString();
        currentUser = getUserById(currentUserId);

//        listSchedulers = getAllBitServerSheduler();
        listResources = getAllBitServerResource();
//        listStudys = getStudy (5, "all", new Date(), new Date(),"");//getAllBitServerStudy();
//        listModalitys = getAllBitServerModality();

        selectedResource = new BitServerResources();
        selectedStudy = new BitServerStudy();
        //selectedModality = new BitServerModality();
        selectedScheduler = new BitServerScheduler();
    }

    public void initNewResources(){
        selectedResource = new BitServerResources();
    }

    public void initDefaultResources(){
        for(BitServerResources bufResources:listResources){
            deleteFromBitServerTable(bufResources.getId());
        }
        listResources.clear();
        selectedResource = new BitServerResources();

        bitServerResourcesList = getAllBitServerResource();
        ArrayList<String> bufResourceName = new ArrayList<>();
        for (BitServerResources buf : bitServerResourcesList) {
            bufResourceName.add(buf.getRname());
        }

        if(!bufResourceName.contains("orthancaddress")){saveBitServiceResource(new BitServerResources("orthancaddress","127.0.0.1")); }
        if(!bufResourceName.contains("port")){saveBitServiceResource(new BitServerResources("port","8042")); }
        if(!bufResourceName.contains("login")){saveBitServiceResource(new BitServerResources("login","doctor")); }
        if(!bufResourceName.contains("password")){saveBitServiceResource(new BitServerResources("password","doctor")); }
        if(!bufResourceName.contains("pathtojson")){saveBitServiceResource(new BitServerResources("pathtojson","/etc/orthanc/")); }
        if(!bufResourceName.contains("pathtoresultfile")){saveBitServiceResource(new BitServerResources("pathtoresultfile","/dataimage/results/")); }
        if(!bufResourceName.contains("networksetpathfile")){saveBitServiceResource(new BitServerResources("networksetpathfile","/etc/network/interfaces")); }
        if(!bufResourceName.contains("httpmode")){saveBitServiceResource(new BitServerResources("httpmode","false")); }
        if(!bufResourceName.contains("luascriptpathfile")){saveBitServiceResource(new BitServerResources("luascriptpathfile","/usr/share/orthanc/lua/route.lua")); }
        if(!bufResourceName.contains("updateafteropen")){saveBitServiceResource(new BitServerResources("updateafteropen","false")); }
        if(!bufResourceName.contains("showStat")){saveBitServiceResource(new BitServerResources("showStat","true")); }
        if(!bufResourceName.contains("logpath")){saveBitServiceResource(new BitServerResources("logpath","/dataimage/results/")); }
        if(!bufResourceName.contains("colstatus")){saveBitServiceResource(new BitServerResources("colstatus","false")); }
        if(!bufResourceName.contains("colpreview")){saveBitServiceResource(new BitServerResources("colpreview","false")); }
        if(!bufResourceName.contains("colDateBirth")){saveBitServiceResource(new BitServerResources("colDateBirth","false")); }
        if(!bufResourceName.contains("colDate")){saveBitServiceResource(new BitServerResources("colDate","true")); }
        if(!bufResourceName.contains("colDescription")){saveBitServiceResource(new BitServerResources("colDescription","false")); }
        if(!bufResourceName.contains("colModality")){saveBitServiceResource(new BitServerResources("colModality","false")); }
        if(!bufResourceName.contains("colWhereSend")){saveBitServiceResource(new BitServerResources("colWhereSend","false")); }
        if(!bufResourceName.contains("datastorage")){saveBitServiceResource(new BitServerResources("datastorage","/dataimage/")); }
        if(!bufResourceName.contains("workListLifeTime")){saveBitServiceResource(new BitServerResources("workListLifeTime","1")); }
        if(!bufResourceName.contains("hl7port")){saveBitServiceResource(new BitServerResources("hl7port","4043")); }
        if(!bufResourceName.contains("WorkListPath")){saveBitServiceResource(new BitServerResources("WorkListPath","/dataimage/results/")); }
        if(!bufResourceName.contains("debug")){saveBitServiceResource(new BitServerResources("debug","false")); }
        if(!bufResourceName.contains("OptionSend")){saveBitServiceResource(new BitServerResources("OptionSend","false")); }
        if(!bufResourceName.contains("OptionDownload")){saveBitServiceResource(new BitServerResources("OptionDownload","true")); }
        if(!bufResourceName.contains("ShowSeachTime")){saveBitServiceResource(new BitServerResources("ShowSeachTime","true")); }
        if(!bufResourceName.contains("ShowHelp")){saveBitServiceResource(new BitServerResources("ShowHelp","true")); }
        if(!bufResourceName.contains("PeriodUpdate")){saveBitServiceResource(new BitServerResources("PeriodUpdate","5")); }

        listResources = getAllBitServerResource();

        showMessage("Внимание!","Все данные были удалены и заполнены значениями по умолчанию!",FacesMessage.SEVERITY_ERROR);
        PrimeFaces.current().ajax().update(":form:accord:dt-resources");
        LogTool.getLogger().info("Admin: "+currentUser.getSignature()+" select default resources");
    }

    public void addNewResource(){
        if((selectedResource.getRname()!=null)&(!selectedResource.getRvalue().equals("")))
        {
            boolean verifiUnical = true;
            for(BitServerResources bufResource:listResources){
                if (bufResource.getRname().equals(selectedResource.getRname())) {
                    verifiUnical = false;
                    break;
                }
            }
            if(verifiUnical) {
                saveBitServiceResource(selectedResource);
                LogTool.getLogger().info("Admin: "+currentUser.getSignature()+" add new resource");
                listResources = getAllBitServerResource();
                PrimeFaces.current().executeScript("PF('manageResourceDialog').hide()");
                PrimeFaces.current().ajax().update(":form:accord:dt-resources");
            }else{
                updateBitServiceResource(selectedResource);
                LogTool.getLogger().info("Admin: "+currentUser.getSignature()+" update resource");
                listResources = getAllBitServerResource();
                PrimeFaces.current().executeScript("PF('manageResourceDialog').hide()");
                PrimeFaces.current().ajax().update(":form:accord:dt-resources");
                if(selectedResource.getRname().equals("hl7port")){
                    MainBean.HL7service();
                }
            }
        }else{
            showMessage("Внимание!","Все поля должны быть заполнены!",FacesMessage.SEVERITY_ERROR);
        }
    }

//    public void addNewModality(){
//        if(!selectedModality.getName().equals(""))
//        {
//            ///updateBitServerModality(selectedModality);
//            //listModalitys = getAllBitServerModality();
//            PrimeFaces.current().executeScript("PF('manageModalityDialog').hide()");
//            PrimeFaces.current().ajax().update(":form:accord:dt-modality");
//            LogTool.getLogger().info("Admin: "+currentUser.getSignature()+" add new modality "+selectedModality.getName());
//        }else{
//            showMessage("Внимание!","Поле должно быть заполнено!",FacesMessage.SEVERITY_ERROR);
//        }
//    }

    public void addNewScheduler(){
        if((!selectedScheduler.getModality().equals(""))&(!selectedScheduler.getSource().equals("")))
        {
            boolean verifiUnical = true;
            for(BitServerScheduler bufScheduler:listSchedulers){
                if (bufScheduler.getModality().equals(selectedScheduler.getModality())) {
                    verifiUnical = false;
                    break;
                }
            }
            if(verifiUnical) {
                //saveNewRule(selectedScheduler);
                //listSchedulers = getAllBitServerSheduler();
                PrimeFaces.current().executeScript("PF('manageSchedulerDialog').hide()");
                PrimeFaces.current().ajax().update(":form:accord:dt-scheduler");
            }else{
                //pdateRule(selectedScheduler);
                //listSchedulers = getAllBitServerSheduler();
                PrimeFaces.current().executeScript("PF('manageSchedulerDialog').hide()");
                PrimeFaces.current().ajax().update(":form:accord:dt-scheduler");
            }
        }else{
            showMessage("Внимание!","Все поля должны быть заполнены!",FacesMessage.SEVERITY_ERROR);
        }
    }

    public void deleteScheduler(){
        //deleteRule(selectedScheduler);
        listSchedulers.remove(selectedScheduler);
        selectedScheduler = new BitServerScheduler();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Правило удалено!"));
        PrimeFaces.current().ajax().update(":form:accord:dt-scheduler");
    }

    public void deleteResource(){
        deleteFromBitServerTable(selectedResource.getId());
        listResources.remove(selectedResource);
        LogTool.getLogger().info("Admin: "+currentUser.getSignature()+" delete Resource: "+selectedResource.getRname());
        selectedResource = new BitServerResources();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Ресурс удален!"));
        PrimeFaces.current().ajax().update(":form:accord:dt-resources");
    }

//    public void deleteModality(){
//        LogTool.getLogger().info("Admin: "+currentUser.getSignature()+" delete Modality "+selectedModality.getName());
//        //deleteBitServerModality(selectedModality);
//        listModalitys.remove(selectedModality);
//        selectedModality = new BitServerModality();
//        showMessage("Внимание!","Модальность удалена!",FacesMessage.SEVERITY_ERROR);
//        PrimeFaces.current().ajax().update(":form:accord:dt-modality");
//    }

//    public void deleteAllStudy() throws SQLException {
//        //deleteAllStudyJDBC();
//        LogTool.getLogger().info("Admin: "+currentUser.getSignature()+" delete all study from base");
//        listStudys.clear();
//        selectedStudy = new BitServerStudy();
//        showMessage("Внимание!","Все данные удалены!",FacesMessage.SEVERITY_ERROR);
//        PrimeFaces.current().ajax().update(":form:accord:dt-study");
//        LogTool.getLogger().info("Admin "+ currentUser.getUname()+" delete all record from bitserverstudy");
//    }
}
