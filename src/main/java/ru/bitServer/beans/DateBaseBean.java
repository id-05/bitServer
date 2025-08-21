package ru.bitServer.beans;

import org.primefaces.PrimeFaces;
import ru.bitServer.dao.*;
import ru.bitServer.service.Maindicomtags;
import ru.bitServer.util.LogTool;
import ru.bitServer.util.SessionUtils;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.util.*;
import static ru.bitServer.beans.AuthoriseBean.showMessage;

@ManagedBean(name = "datebaseBean")
@ViewScoped
public class DateBaseBean implements UserDao {

    List<BitServerResources> listResources = new ArrayList<>();

    List<Maindicomtags> listMaindicomtags = new ArrayList<>();
    BitServerResources selectedResource;
    BitServerStudy selectedStudy;
    BitServerUser currentUser;
    String currentUserId;
    boolean debug;

    public List<Maindicomtags> getListMaindicomtags() {
        return listMaindicomtags;
    }

    public void setListMaindicomtags(List<Maindicomtags> listMaindicomtags) {
        this.listMaindicomtags = listMaindicomtags;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
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
        //System.out.println("init");
        HttpSession session = SessionUtils.getSession();
        currentUserId = session.getAttribute("userid").toString();
        currentUser = getUserById(currentUserId);
        listResources = getAllBitServerResource();
        //LogTool.getLogger().info("Получили данные из таблицы bitserver в колличестве: "+listResources.size());
        //listMaindicomtags = getTableMaindicomtags();
        selectedResource = new BitServerResources();
        selectedStudy = new BitServerStudy();
        debug = false;//getBitServerResource("debug").getRvalue().equals("true");
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
        if(!bufResourceName.contains("isoPath")){saveBitServiceResource(new BitServerResources("isoPath","/dataimage/results/")); sb.append("isoPath;").append("\n");}
        if(!bufResourceName.contains("cdViewerInclude")){saveBitServiceResource(new BitServerResources("cdViewerInclude","true")); sb.append("cdViewerInclude;").append("\n");}
        if(!bufResourceName.contains("firewallpathfile")){saveBitServiceResource(new BitServerResources("firewallpathfile","/result")); sb.append("firewallpathfile").append("\n");}
        if(!bufResourceName.contains("cdviewerpath")){saveBitServiceResource(new BitServerResources("cdviewerpath","/result")); sb.append("cdviewerpath").append("\n");}
        if(!bufResourceName.contains("logpathorthanc")){saveBitServiceResource(new BitServerResources("logpathorthanc","/var/log/orthanc")); sb.append("logpathorthanc").append("\n");}
        if(!bufResourceName.contains("QueueGetType")){saveBitServiceResource(new BitServerResources("QueueGetType","false")); sb.append("QueueGetType;").append("\n");}
        if(!bufResourceName.contains("CT")){saveBitServiceResource(new BitServerResources("CT","CT")); sb.append("CT;").append("\n");}
        if(!bufResourceName.contains("MR")){saveBitServiceResource(new BitServerResources("MR","MR")); sb.append("MR;").append("\n");}
        if(!bufResourceName.contains("DX")){saveBitServiceResource(new BitServerResources("DX","DX")); sb.append("DX;").append("\n");}
        if(!bufResourceName.contains("CR")){saveBitServiceResource(new BitServerResources("CR","CR")); sb.append("CR;").append("\n");}
        if(!bufResourceName.contains("MG")){saveBitServiceResource(new BitServerResources("MG","MG")); sb.append("MG;").append("\n");}
        if(!bufResourceName.contains("XA")){saveBitServiceResource(new BitServerResources("XA","XA")); sb.append("XA;").append("\n");}
        if(!bufResourceName.contains("US")){saveBitServiceResource(new BitServerResources("US","US")); sb.append("US;").append("\n");}
        if(!bufResourceName.contains("RF")){saveBitServiceResource(new BitServerResources("RF","RF")); sb.append("RF;").append("\n");}
        if(!bufResourceName.contains("worklistsamplefile")){saveBitServiceResource(new BitServerResources("worklistsamplefile","/dataimage/sampleworklist")); sb.append("worklistsamplefile;").append("\n");}
        if(!bufResourceName.contains("FontForPdfFilePath")){saveBitServiceResource(new BitServerResources("FontForPdfFilePath","/dataimage/font/ArialRegular.ttf")); sb.append("FontForPdfFilePath;").append("\n");}
        if(sb.toString().length()>0){
            showMessage("Внимание!","Были добавлены: "+ sb,FacesMessage.SEVERITY_INFO);
            PrimeFaces.current().ajax().update(":form:accord:dt-resources");
        }else{
            showMessage("Внимание!","В таблице есть все актуальный параметры!",FacesMessage.SEVERITY_INFO);
        }
        PrimeFaces.current().ajax().update(":form:accord:dt-resources");
        PrimeFaces.current().ajax().update(":form:growl2");

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

    public void deleteResource(){
        deleteFromBitServerTable(selectedResource.getId());
        listResources.remove(selectedResource);
        LogTool.getLogger().info("Admin: "+currentUser.getSignature()+" delete Resource: "+selectedResource.getRname());
        selectedResource = new BitServerResources();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Ресурс удален!"));
        PrimeFaces.current().ajax().update(":form:accord:dt-resources");
    }
}
