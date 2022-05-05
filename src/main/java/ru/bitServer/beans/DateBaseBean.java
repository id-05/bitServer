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
import java.util.ArrayList;
import java.util.List;

import static ru.bitServer.beans.AutoriseBean.showMessage;

@ManagedBean(name = "datebaseBean")
@ViewScoped
public class DateBaseBean implements UserDao {

    List<BitServerResources> listResources = new ArrayList<>();
    BitServerResources selectedResource;
    BitServerResources selectedResources;
    List<BitServerStudy> listStudys = new ArrayList<>();
    BitServerStudy selectedStudy;
    BitServerStudy selectedStudys;
    List<BitServerScheduler> listSchedulers = new ArrayList<>();
    BitServerScheduler selectedScheduler;
    BitServerScheduler selectedSchedulers;
    List<BitServerModality> listModalitys = new ArrayList<>();
    BitServerModality selectedModality;
    BitServerModality selectedModalitys;
    Users currentUser;
    String currentUserId;

    public BitServerModality getSelectedModalitys() {
        return selectedModalitys;
    }

    public void setSelectedModalitys(BitServerModality selectedModalitys) {
        this.selectedModalitys = selectedModalitys;
    }

    public BitServerScheduler getSelectedSchedulers() {
        return selectedSchedulers;
    }

    public void setSelectedSchedulers(BitServerScheduler selectedSchedulers) {
        this.selectedSchedulers = selectedSchedulers;
    }

    public BitServerStudy getSelectedStudys() {
        return selectedStudys;
    }

    public void setSelectedStudys(BitServerStudy selectedStudys) {
        this.selectedStudys = selectedStudys;
    }

    public BitServerResources getSelectedResources() {
        return selectedResources;
    }

    public void setSelectedResources(BitServerResources selectedResources) {
        this.selectedResources = selectedResources;
    }

    public List<BitServerModality> getListModalitys() {
        return listModalitys;
    }

    public void setListModalitys(List<BitServerModality> listModalitys) {
        this.listModalitys = listModalitys;
    }

    public BitServerModality getSelectedModality() {
        return selectedModality;
    }

    public void setSelectedModality(BitServerModality selectedModality) {
        this.selectedModality = selectedModality;
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

    public List<BitServerStudy> getListStudys() {
        return listStudys;
    }

    public void setListStudys(List<BitServerStudy> listStudys) {
        this.listStudys = listStudys;
    }

    public BitServerStudy getSelectedStudy() {
        return selectedStudy;
    }

    public void setSelectedStudy(BitServerStudy selectedStudy) {
        this.selectedStudy = selectedStudy;
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

        listSchedulers = getAllBitServerSheduler();
        listResources = getAllBitServerResource();
        listStudys = getAllBitServerStudy();
        listModalitys = getAllBitServerModality();

        selectedResource = new BitServerResources();
        selectedStudy = new BitServerStudy();
        selectedModality = new BitServerModality();
        selectedScheduler = new BitServerScheduler();
    }

    public void initNewResources(){
        selectedResource = new BitServerResources();
    }

    public void initDefaultResources(){
        for(BitServerResources bufResources:listResources){
            deleteBitServerResource(bufResources);
        }
        listResources.clear();
        selectedResource = new BitServerResources();

        saveBitServiceResource(new BitServerResources("orthancaddress","127.0.0.1"));
        saveBitServiceResource(new BitServerResources("port","8042"));
        saveBitServiceResource(new BitServerResources("login","doctor"));
        saveBitServiceResource(new BitServerResources("password","doctor"));
        saveBitServiceResource(new BitServerResources("pathtojson","/etc/orthanc/"));
        saveBitServiceResource(new BitServerResources("pathtoresultfile","/dataimage/result/"));
        saveBitServiceResource(new BitServerResources("demontimerupdate","5"));
        saveBitServiceResource(new BitServerResources("syncdate","2015-11-01"));
        saveBitServiceResource(new BitServerResources("networksetpathfile","/etc/network/interfaces"));
        saveBitServiceResource(new BitServerResources("httpmode","false"));
        saveBitServiceResource(new BitServerResources("luascriptpathfile","/usr/share/orthanc/lua/route.lua"));
        saveBitServiceResource(new BitServerResources("updateafteropen","true"));
        saveBitServiceResource(new BitServerResources("logpath","/dataimage/results/"));

        listResources.add(new BitServerResources("orthancaddress","127.0.0.1"));
        listResources.add(new BitServerResources("port","8042"));
        listResources.add(new BitServerResources("login","doctor"));
        listResources.add(new BitServerResources("password","doctor"));
        listResources.add(new BitServerResources("pathtojson","/etc/orthanc/"));
        listResources.add(new BitServerResources("pathtoresultfile","/dataimage/result/"));
        listResources.add(new BitServerResources("demontimerupdate","5"));
        listResources.add(new BitServerResources("syncdate","2015-11-01"));
        listResources.add(new BitServerResources("networksetpathfile","/etc/network/interfaces"));
        listResources.add(new BitServerResources("httpmode","false"));
        listResources.add(new BitServerResources("luascriptpathfile","/usr/share/orthanc/lua/route.lua"));
        listResources.add(new BitServerResources("updateafteropen","true"));
        listResources.add(new BitServerResources("logpath","/dataimage/results/"));

        showMessage("Внимание!","Все данные были удалены и заполнены значениями по умолчанию!",FacesMessage.SEVERITY_ERROR);
        PrimeFaces.current().ajax().update(":form:accord:dt-resources");
        LogTool.getLogger().debug("Admin: "+currentUser.getSignature()+" select default resources");
    }

    public void initNewModality(){
        selectedModality = new BitServerModality();
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
                LogTool.getLogger().debug("Admin: "+currentUser.getSignature()+" add new resource");
                listResources = getAllBitServerResource();
                PrimeFaces.current().executeScript("PF('manageResourceDialog').hide()");
                PrimeFaces.current().ajax().update(":form:accord:dt-resources");
            }else{
                updateBitServiceResource(selectedResource);
                LogTool.getLogger().debug("Admin: "+currentUser.getSignature()+" update resource");
                listResources = getAllBitServerResource();
                PrimeFaces.current().executeScript("PF('manageResourceDialog').hide()");
                PrimeFaces.current().ajax().update(":form:accord:dt-resources");
            }
        }else{
            showMessage("Внимание!","Все поля должны быть заполнены!",FacesMessage.SEVERITY_ERROR);
        }
    }

    public void addNewModality(){
        if(!selectedModality.getName().equals(""))
        {
            updateBitServerModality(selectedModality);
            listModalitys = getAllBitServerModality();
            PrimeFaces.current().executeScript("PF('manageModalityDialog').hide()");
            PrimeFaces.current().ajax().update(":form:accord:dt-modality");
            LogTool.getLogger().debug("Admin: "+currentUser.getSignature()+" add new modality "+selectedModality.getName());
        }else{
            showMessage("Внимание!","Поле должно быть заполнено!",FacesMessage.SEVERITY_ERROR);
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
                saveNewRule(selectedScheduler);
                listSchedulers = getAllBitServerSheduler();
                PrimeFaces.current().executeScript("PF('manageSchedulerDialog').hide()");
                PrimeFaces.current().ajax().update(":form:accord:dt-scheduler");
            }else{
                updateRule(selectedScheduler);
                listSchedulers = getAllBitServerSheduler();
                PrimeFaces.current().executeScript("PF('manageSchedulerDialog').hide()");
                PrimeFaces.current().ajax().update(":form:accord:dt-scheduler");
            }
        }else{
            showMessage("Внимание!","Все поля должны быть заполнены!",FacesMessage.SEVERITY_ERROR);
        }
    }

    public void deleteScheduler(){
        deleteRule(selectedScheduler);
        listSchedulers.remove(selectedScheduler);
        selectedScheduler = new BitServerScheduler();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Правило удалено!"));
        PrimeFaces.current().ajax().update(":form:accord:dt-scheduler");
    }

    public void deleteResource(){
        deleteBitServerResource(selectedResource);
        listResources.remove(selectedResource);
        selectedResource = new BitServerResources();
        LogTool.getLogger().debug("Admin: "+currentUser.getSignature()+" delete Resource "+selectedResource.getRname());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Ресурс удален!"));
        PrimeFaces.current().ajax().update(":form:accord:dt-resources");
    }

    public void deleteModality(){
        LogTool.getLogger().debug("Admin: "+currentUser.getSignature()+" delete Modality "+selectedModality.getName());
        deleteBitServerModality(selectedModality);
        listModalitys.remove(selectedModality);
        selectedModality = new BitServerModality();
        showMessage("Внимание!","Модальность удалена!",FacesMessage.SEVERITY_ERROR);
        PrimeFaces.current().ajax().update(":form:accord:dt-modality");
    }

    public void updateStudyInBase(){
        if((selectedStudy.getId() != null) & (!selectedStudy.getModality().equals("")))
        {
            updateStudy(selectedStudy);
            LogTool.getLogger().debug("Admin: "+currentUser.getSignature()+" update study in base "+selectedStudy.getSid());
            listStudys = getAllBitServerStudy();
            PrimeFaces.current().executeScript("PF('manageStudyDialog').hide()");
            PrimeFaces.current().ajax().update(":form:accord:dt-study");
        }else{
            showMessage("Внимание!","Все поля должны быть заполнены!",FacesMessage.SEVERITY_ERROR);
        }
    }

    public void deleteStudyFromBase(){
        deleteStudy(selectedStudy);
        LogTool.getLogger().debug("Admin: "+currentUser.getSignature()+" delete study from base "+selectedStudy.getSid());
        listStudys.remove(selectedStudy);
        selectedStudy = new BitServerStudy();
        showMessage("Внимание!","Исследование удалено!",FacesMessage.SEVERITY_ERROR);
        PrimeFaces.current().ajax().update(":form:accord:dt-study");

        //добавить удаление из базы Orthanc
    }

    public void deleteAllStudy(){
        for(BitServerStudy bufStudy:listStudys){
            deleteStudy(bufStudy);
        }
        LogTool.getLogger().debug("Admin: "+currentUser.getSignature()+" delete all study from base");
        listStudys.clear();
        selectedStudy = new BitServerStudy();
        showMessage("Внимание!","Все данные удалены!",FacesMessage.SEVERITY_ERROR);
        PrimeFaces.current().ajax().update(":form:accord:dt-study");
    }
}
