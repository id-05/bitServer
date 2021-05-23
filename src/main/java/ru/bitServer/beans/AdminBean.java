package ru.bitServer.beans;

import javafx.application.Application;
import javafx.concurrent.Task;
import org.dcm4che2.data.BasicDicomObject;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.io.StopTagInputHandler;
import ru.bitServer.dicomviewer.Instance;
import ru.bitServer.dicomviewer.Patient;
import ru.bitServer.dicomviewer.Series;
import ru.bitServer.dicomviewer.Study;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import java.io.File;
import java.util.Map;

@ManagedBean(name = "adminBean", eager = true)
@SessionScoped
public class AdminBean {
    public String buf = "buffer";
    private Map<String, Patient> patientMap;
    private Map<String, Study> studyMap;
    private Map<String, Series> seriesMap;
    private Map<String, Instance> instanceMap;

    public String getBuf() {
        return buf;
    }

    public void setBuf(String buf) {
        this.buf = buf;
    }

    @PostConstruct
    public void init(){
        System.out.println("admin page");
    }

    public void loadDicomFile(){
        File dicomFile = new File("/resources/images/IM10");
        addDicomInstance(new FileDataSource(dicomFile));
    }

    private void addDicomInstance(DataSource dataSource) {
        LoadDicomInstanceTask task = new LoadDicomInstanceTask(dataSource);
        //Application.getInstance().getContext().getTaskService().execute(task);
    }

    private class LoadDicomInstanceTask extends Task<Void> {
        private final DataSource dataSource;

        public LoadDicomInstanceTask(DataSource dataSource) {

            this.dataSource = dataSource;
        }

        protected Void doInBackground() throws Exception {
            DicomObject dcmObj = null;
            DicomInputStream din = new DicomInputStream(this.dataSource.getInputStream());
            din.setHandler(new StopTagInputHandler(2145386511));
            dcmObj = new BasicDicomObject();
            din.readDicomObject(dcmObj, -1);
            din.close();
            String sopClassUID = dcmObj.getString(524310);
            //Plugin plugin = FileController.this.applicationContext.getPluginContext().getPlugin(sopClassUID);
            //if (plugin != null)
            {
                String patientId = dcmObj.getString(1048608);
                Patient patient = (Patient) patientMap.get(patientId);
                if (patient == null) {
                    patient = new Patient();
                    patient.setPatientId(patientId);
                    patient.setPatientName(dcmObj.getString(1048592));
                    patient.setPatientBirthDate(dcmObj.getDate(1048624));
                    patient.setPatientSex(dcmObj.getString(1048640));
                    patientMap.put(patientId, patient);
                }

                String studyUID = dcmObj.getString(2097165);
                Study study = (Study) studyMap.get(studyUID);
                if (study == null) {
                    study = new Study();
                    study.setStudyInstanceUID(studyUID);
                    study.setStudyId(dcmObj.getString(2097168));
                    study.setStudyDateTime(dcmObj.getDate(524320, 524336));
                    study.setAccessionNumber(dcmObj.getString(524368));
                    study.setStudyDescription(dcmObj.getString(528432));
                    study.setPatient(patient);
                    patient.addStudy(study);
                    studyMap.put(studyUID, study);
                }

                String seriesUID = dcmObj.getString(2097166);
                Series series = (Series) seriesMap.get(seriesUID);
                if (series == null) {
                    series = new Series();
                    series.setSeriesInstanceUID(dcmObj.getString(2097166));
                    series.setSeriesNumber(dcmObj.getString(2097169));
                    series.setModality(dcmObj.getString(524384));
                    series.setInstitutionName(dcmObj.getString(524416));
                    series.setSeriesDescription(dcmObj.getString(528446));
                    series.setStudy(study);
                    study.addSeries(series);
                    seriesMap.put(seriesUID, series);
                }

                String instanceUID = dcmObj.getString(524312);
                Instance instance = (Instance) instanceMap.get(instanceUID);
                if (instance == null) {
                   // instance = plugin.createInstance(this.dataSource);
                    instance.setSeries(series);
                    series.addInstance(instance);
                    instanceMap.put(instanceUID, instance);
                }

//                FileController.this.applicationContext.getViewContext().getDicomThumbsPane().getDicomThumbsModel().addInstance(instance);
//                if (FileController.this.applicationContext.getViewContext().getDicomThumbsPane().getDicomThumbsModel().getSize() == 1) {
//                    FileController.this.applicationContext.getViewContext().getDicomThumbsPane().setSelectedIndex(0);
//                }
            }

            return null;
        }

        @Override
        protected Void call() throws Exception {
            return null;
        }
    }
}
