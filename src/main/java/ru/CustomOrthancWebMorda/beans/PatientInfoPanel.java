package ru.CustomOrthancWebMorda.beans;

import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "patientInfoPanel", eager = true)
@ViewScoped
public class PatientInfoPanel {

    public String param1;

    public String patientId;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    @PostConstruct
    public void init() {
        System.out.println("postconstruct");
//        System.out.println("patient info panel");
//        String param1 = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("parentPatientID");
//        System.out.println(param1);
//        patientId = param1;
    }

    public void clickOpenPatient(){
        System.out.println("click open patient");
        param1 = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("parentPatientID");
        System.out.println(param1);
        patientId = param1;
        PrimeFaces.current().ajax().update("patientinfo");
    }

    public void printParam(){
        System.out.println(param1);
    }

}
