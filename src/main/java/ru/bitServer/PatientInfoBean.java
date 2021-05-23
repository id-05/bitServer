package ru.bitServer;

import org.primefaces.event.SelectEvent;
import ru.bitServer.dicom.Patient;
import ru.bitServer.dicom.Serie;
import ru.bitServer.dicom.Study;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "patientInfoBean", eager = true)
@ViewScoped
public class PatientInfoBean {

    public static String patientId;
    public static Patient currentPatient;
    public static List<Study> studyList = new ArrayList<>();
    public List<Study> selectstudyList = new ArrayList<>();
    public Study bufStudy = new Study("1","1","1");

    public Study getBufStudy() {
        return bufStudy;
    }

    public void setBufStudy(Study bufStudy) {
        this.bufStudy = bufStudy;
    }

    public List<Study> getSelectstudyList() {
        return selectstudyList;
    }

    public void setSelectstudyList(ArrayList<Study> selectstudyList) {
        this.selectstudyList = selectstudyList;
    }

    public List<Study> getStudyList() {
        return studyList;
    }

    public void setStudyList(ArrayList<Study> studyList) {
        PatientInfoBean.studyList = studyList;
    }

    public Patient getCurrentPatient() {
        return currentPatient;
    }

    public void setCurrentPatient(Patient currentPatient) {
        PatientInfoBean.currentPatient = currentPatient;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        PatientInfoBean.patientId = patientId;
    }

    @PostConstruct
    public void init(){
        System.out.println("patientInfoPanel");
        patientId = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("patientID").toString();

        for(Patient bufPatient:SearchBean.patients){
            if(bufPatient.getPatientOrthancId().equals(patientId)){
                currentPatient = bufPatient;
            }
        }

        studyList.clear();
        studyList.addAll(currentPatient.getChildStudies().values());
        for(Study buf:studyList){
            List<Serie> buflist = new ArrayList<>();
            for(int i=0; i<10; i++){
                Serie serie = new Serie(String.valueOf(i),String.valueOf(i),String.valueOf(i));
                buflist.add(serie);
            }
            buf.setSeries(buflist);
        }
    }

    public void redirectToSeries(SelectEvent<Study> event) throws IOException {
        String buf = event.getObject().getOrthancId();
        System.out.println("study id for redirect "+buf);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("patientID", patientId);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("studyID", buf);
        FacesContext.getCurrentInstance().getExternalContext().redirect("serie.xhtml");
    }

}