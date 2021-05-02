package ru.CustomOrthancWebMorda.beans;

import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "patientInfoPanel", eager = true)
@ViewScoped
public class PatientInfoPanel {

    public static String patientId;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }



    public void clickOpenPatient(){
        System.out.println("click open patient");
        patientId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("parentPatientID");
        System.out.println(patientId);
        PrimeFaces.current().ajax().update("patientinfo");
    }

    public void printParam(){
        //patientId = "987654321";
        System.out.println(patientId);
    }

}