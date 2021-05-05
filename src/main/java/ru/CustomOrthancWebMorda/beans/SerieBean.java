package ru.CustomOrthancWebMorda.beans;


import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "serieBean", eager = true)
@ViewScoped
public class SerieBean {

    public String serieID;

    public String getStudyID() {
        return studyID;
    }

    public void setStudyID(String studyID) {
        this.studyID = studyID;
    }

    public String studyID;

    public String getSerieID() {
        return serieID;
    }

    public void setSerieID(String serieID) {
        this.serieID = serieID;
    }

    @PostConstruct
    public void init(){
        System.out.println("patientInfoPanel");
        studyID = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("serieID").toString();
        System.out.println(studyID);
    }
}
