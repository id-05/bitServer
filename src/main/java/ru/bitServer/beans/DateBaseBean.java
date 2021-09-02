package ru.bitServer.beans;

import org.primefaces.PrimeFaces;
import ru.bitServer.dao.*;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

import static ru.bitServer.beans.AutoriseBean.showMessage;

@ManagedBean(name = "datebaseBean", eager = false)
@ViewScoped
public class DateBaseBean implements UserDao {

    List<BitServerResources> listResources = new ArrayList<>();

    BitServerResources selectedResource;

    List<BitServerStudy> listStudys = new ArrayList<>();

    BitServerStudy selectedStudy;

    List<BitServerScheduler> listSchedulers = new ArrayList<>();

    BitServerScheduler selectedScheduler;

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
        System.out.println("datebase bean");

        listSchedulers = getAllBitServerSheduler();
        listResources = getAllBitServerResource();
        listStudys = getAllBitServerStudy();

        selectedResource = new BitServerResources();
        if(listStudys.size()>0){
            selectedStudy = listStudys.get(0);
        }else{
            selectedStudy = new BitServerStudy();
        }

        if(listSchedulers.size()>0){
            selectedScheduler = listSchedulers.get(0);
        }else{
            selectedScheduler = new BitServerScheduler();
        }

    }

    public void initNewResources(){
        selectedResource = new BitServerResources();
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
                listResources = getAllBitServerResource();
                PrimeFaces.current().executeScript("PF('manageResourceDialog').hide()");
                PrimeFaces.current().ajax().update(":form:accord:dt-resources");
            }else{
                updateBitServiceResource(selectedResource);
                listResources = getAllBitServerResource();
                PrimeFaces.current().executeScript("PF('manageResourceDialog').hide()");
                PrimeFaces.current().ajax().update(":form:accord:dt-resources");
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
        deleteBitServiceResource(selectedResource);
        listResources.remove(selectedResource);
        selectedResource = new BitServerResources();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Ресурс удален!"));
        PrimeFaces.current().ajax().update(":form:accord:dt-resources");
    }

    public void updateStudyInBase(){
        if((selectedStudy.getId() != null) & (!selectedStudy.getModality().equals("")))
        {
            updateStudy(selectedStudy);
            listStudys = getAllBitServerStudy();
            PrimeFaces.current().executeScript("PF('manageStudyDialog').hide()");
            PrimeFaces.current().ajax().update(":form:accord:dt-study");
        }else{
            showMessage("Внимание!","Все поля должны быть заполнены!",FacesMessage.SEVERITY_ERROR);
        }
    }

    public void deleteStudyFromBase(){
        deleteStudy(selectedStudy);
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
        listStudys.clear();
        selectedStudy = new BitServerStudy();
        showMessage("Внимание!","Все данные удалены!",FacesMessage.SEVERITY_ERROR);
        PrimeFaces.current().ajax().update(":form:accord:dt-study");
    }
}
