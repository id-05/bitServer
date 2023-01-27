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
    BitServerStudy selectedStudy;
    List<BitServerScheduler> listSchedulers = new ArrayList<>();
    BitServerScheduler selectedScheduler;
    BitServerScheduler selectedSchedulers;
    BitServerUser currentUser;
    String currentUserId;
    List<BitServerResources> bitServerResourcesList = new ArrayList<>();
    boolean debug;

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public BitServerScheduler getSelectedSchedulers() {
        return selectedSchedulers;
    }

    public void setSelectedSchedulers(BitServerScheduler selectedSchedulers) {
        this.selectedSchedulers = selectedSchedulers;
    }

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
        listResources = getAllBitServerResource();
        selectedResource = new BitServerResources();
        selectedStudy = new BitServerStudy();
        selectedScheduler = new BitServerScheduler();
        debug = getBitServerResource("debug").getRvalue().equals("true");
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
        //bitServerResourcesList = getAllBitServerResource();
        checkResources();
        listResources = getAllBitServerResource();
        showMessage("Внимание!","Все данные были удалены и заполнены значениями по умолчанию!",FacesMessage.SEVERITY_ERROR);
        PrimeFaces.current().ajax().update(":form:accord:dt-resources");
        LogTool.getLogger().info("Admin: "+currentUser.getSignature()+" select default resources");
    }

    public void checkResources(){
        ArrayList<String> bufResourceName = new ArrayList<>();
        for (BitServerResources buf:getAllBitServerResource()) {
            bufResourceName.add(buf.getRname());
        }
        StringBuilder sb = new StringBuilder();
        if(!bufResourceName.contains("orthancaddress")){saveBitServiceResource(new BitServerResources("orthancaddress","127.0.0.1")); sb.append("orthancaddress;").append("\n");}
        if(!bufResourceName.contains("port")){saveBitServiceResource(new BitServerResources("port","8042")); sb.append("port;").append("\n");}
        if(!bufResourceName.contains("login")){saveBitServiceResource(new BitServerResources("login","doctor")); sb.append("login;").append("\n");}
        if(!bufResourceName.contains("password")){saveBitServiceResource(new BitServerResources("password","doctor")); sb.append("password;").append("\n");}
        if(!bufResourceName.contains("pathtojson")){saveBitServiceResource(new BitServerResources("pathtojson","/etc/orthanc/")); sb.append("pathtojson;").append("\n");}
        if(!bufResourceName.contains("pathtoresultfile")){saveBitServiceResource(new BitServerResources("pathtoresultfile","/dataimage/results/")); sb.append("pathtoresultfile;").append("\n");}
        if(!bufResourceName.contains("networksetpathfile")){saveBitServiceResource(new BitServerResources("networksetpathfile","/etc/network/interfaces")); sb.append("networksetpathfile;").append("\n");}
        if(!bufResourceName.contains("httpmode")){saveBitServiceResource(new BitServerResources("httpmode","false")); sb.append("httpmode;").append("\n");}
        if(!bufResourceName.contains("luascriptpathfile")){saveBitServiceResource(new BitServerResources("luascriptpathfile","/usr/share/orthanc/lua/route.lua")); sb.append("luascriptpathfile;").append("\n");}
        if(!bufResourceName.contains("updateafteropen")){saveBitServiceResource(new BitServerResources("updateafteropen","false")); sb.append("updateafteropen;").append("\n");}
        if(!bufResourceName.contains("showStat")){saveBitServiceResource(new BitServerResources("showStat","true")); sb.append("showStat;").append("\n");}
        if(!bufResourceName.contains("logpath")){saveBitServiceResource(new BitServerResources("logpath","/dataimage/results/")); sb.append("logpath;").append("\n");}
        if(!bufResourceName.contains("colstatus")){saveBitServiceResource(new BitServerResources("colstatus","false")); sb.append("colstatus;").append("\n");}
        if(!bufResourceName.contains("colpreview")){saveBitServiceResource(new BitServerResources("colpreview","false")); sb.append("colpreview;").append("\n");}
        if(!bufResourceName.contains("colDateBirth")){saveBitServiceResource(new BitServerResources("colDateBirth","false")); sb.append("colDateBirth;").append("\n");}
        if(!bufResourceName.contains("colDate")){saveBitServiceResource(new BitServerResources("colDate","true")); sb.append("colDate;").append("\n");}
        if(!bufResourceName.contains("colDescription")){saveBitServiceResource(new BitServerResources("colDescription","false")); sb.append("colDescription;").append("\n");}
        if(!bufResourceName.contains("colModality")){saveBitServiceResource(new BitServerResources("colModality","false")); sb.append("colModality;").append("\n");}
        if(!bufResourceName.contains("colWhereSend")){saveBitServiceResource(new BitServerResources("colWhereSend","false")); sb.append("colWhereSend;").append("\n");}
        if(!bufResourceName.contains("datastorage")){saveBitServiceResource(new BitServerResources("datastorage","/dataimage/")); sb.append("datastorage;").append("\n");}
        if(!bufResourceName.contains("workListLifeTime")){saveBitServiceResource(new BitServerResources("workListLifeTime","1")); sb.append("workListLifeTime;").append("\n");}
        if(!bufResourceName.contains("hl7port")){saveBitServiceResource(new BitServerResources("hl7port","4043")); sb.append("hl7port;").append("\n");}
        if(!bufResourceName.contains("WorkListPath")){saveBitServiceResource(new BitServerResources("WorkListPath","/dataimage/results/")); sb.append("WorkListPath;").append("\n");}
        if(!bufResourceName.contains("debug")){saveBitServiceResource(new BitServerResources("debug","false")); sb.append("debug;").append("\n");}
        if(!bufResourceName.contains("OptionSend")){saveBitServiceResource(new BitServerResources("OptionSend","false")); sb.append("OptionSend;").append("\n");}
        if(!bufResourceName.contains("OptionDownload")){saveBitServiceResource(new BitServerResources("OptionDownload","true")); sb.append("OptionDownload;").append("\n");}
        if(!bufResourceName.contains("ShowSeachTime")){saveBitServiceResource(new BitServerResources("ShowSeachTime","true")); sb.append("ShowSeachTime;").append("\n");}
        if(!bufResourceName.contains("ShowHelp")){saveBitServiceResource(new BitServerResources("ShowHelp","true")); sb.append("ShowHelp;").append("\n");}
        if(!bufResourceName.contains("PeriodUpdate")){saveBitServiceResource(new BitServerResources("PeriodUpdate","5")); sb.append("PeriodUpdate;").append("\n");}
        if(!bufResourceName.contains("colInstitution")){saveBitServiceResource(new BitServerResources("colInstitution","false")); sb.append("colInstitution;").append("\n");}
        if(!bufResourceName.contains("colStation")){saveBitServiceResource(new BitServerResources("colStation","false")); sb.append("colStation;").append("\n");}
        if(!bufResourceName.contains("colSource")){saveBitServiceResource(new BitServerResources("colSource","false")); sb.append("colSource;").append("\n");}
        if(!bufResourceName.contains("timerEnable")){saveBitServiceResource(new BitServerResources("timerEnable","false")); sb.append("timerEnable;").append("\n");}
        if(!bufResourceName.contains("luaRead")){saveBitServiceResource(new BitServerResources("luaRead","true")); sb.append("luaRead;").append("\n");}
        if(sb.toString().length()>0){
            showMessage("Внимание!","Были добавлены: "+ sb.toString(),FacesMessage.SEVERITY_INFO);
            PrimeFaces.current().ajax().update(":form:accord:dt-resources");
            PrimeFaces.current().ajax().update(":form:growl2");
        }else{
            showMessage("Внимание!","В таблице есть все актуальный параметры!",FacesMessage.SEVERITY_INFO);
            PrimeFaces.current().ajax().update(":form:growl2");
        }
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
                PrimeFaces.current().executeScript("PF('manageSchedulerDialog').hide()");
                PrimeFaces.current().ajax().update(":form:accord:dt-scheduler");
            }else{
                PrimeFaces.current().executeScript("PF('manageSchedulerDialog').hide()");
                PrimeFaces.current().ajax().update(":form:accord:dt-scheduler");
            }
        }else{
            showMessage("Внимание!","Все поля должны быть заполнены!",FacesMessage.SEVERITY_ERROR);
        }
    }

    public void deleteScheduler(){
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

}
