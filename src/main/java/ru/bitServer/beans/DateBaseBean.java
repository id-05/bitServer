package ru.bitServer.beans;

import org.primefaces.PrimeFaces;
import ru.bitServer.dao.BitServerResources;
import ru.bitServer.dao.BitServerStudy;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dao.Users;

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
    List<BitServerResources> selectedResources = new ArrayList<>();
    BitServerResources selectedResource;
    List<BitServerStudy> listStudys = new ArrayList<>();
    List<BitServerStudy> selectedStudys = new ArrayList<>();
    BitServerStudy selectedStudy;

    public List<BitServerStudy> getListStudys() {
        return listStudys;
    }

    public void setListStudys(List<BitServerStudy> listStudys) {
        this.listStudys = listStudys;
    }

    public List<BitServerStudy> getSelectedStudys() {
        return selectedStudys;
    }

    public void setSelectedStudys(List<BitServerStudy> selectedStudys) {
        this.selectedStudys = selectedStudys;
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

    public List<BitServerResources> getSelectedResources() {
        return selectedResources;
    }

    public void setSelectedResources(List<BitServerResources> selectedResources) {
        this.selectedResources = selectedResources;
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
        selectedResource = new BitServerResources();
        listResources = getAllBitServerResource();
        listStudys = getAllBitServerStudy();
        selectedStudy = new BitServerStudy();
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

    public void deleteResource(){
        deleteBitServiceResource(selectedResource);
        listResources.remove(selectedResource);
        selectedResource = new BitServerResources();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Ресурс удален!"));
        PrimeFaces.current().ajax().update(":form:accord:dt-resources");
    }

    public void updateStudyInBase(){
        if((selectedStudy.getId()!=null)&(!selectedStudy.getModality().equals(""))
            &(true))
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
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Исследование удалено!"));
        PrimeFaces.current().ajax().update(":form:accord:dt-study");

        //добавить удаление из базы Orthanc

    }
}
