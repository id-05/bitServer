package ru.bitServer.beans;

import org.primefaces.PrimeFaces;
import ru.bitServer.dao.BitServerUser;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dicom.DicomModaliti;
import ru.bitServer.dicom.OrthancSettings;
import ru.bitServer.service.TimetableTask;
import ru.bitServer.util.LogTool;
import ru.bitServer.util.OrthancRestApi;
import ru.bitServer.util.SessionUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ru.bitServer.beans.MainBean.mainServer;

@ManagedBean(name = "timetableBean")
@ViewScoped
public class TimetableBean implements UserDao {

    TimetableTask selectedTask;
    ArrayList<TimetableTask> tasks = new ArrayList<>();
    BitServerUser currentUser;
    String currentUserId;
    LocalTime timeTask;
    List<DicomModaliti> modalities;

    public List<DicomModaliti> getModalities() {
        return modalities;
    }

    public void setModalities(List<DicomModaliti> modalities) {
        this.modalities = modalities;
    }

    public LocalTime getTimeTask() {
        return timeTask;
    }

    public void setTimeTask(LocalTime timeTask) {
        this.timeTask = timeTask;
    }

    public TimetableTask getSelectedTask() {
        return selectedTask;
    }

    public void setSelectedTask(TimetableTask selectedTask) {
        this.selectedTask = selectedTask;
    }

    public ArrayList<TimetableTask> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<TimetableTask> tasks) {
        this.tasks = tasks;
    }

    @PostConstruct
    public void init() {
        HttpSession session = SessionUtils.getSession();
        currentUserId = session.getAttribute("userid").toString();
        currentUser = getUserById(currentUserId);


        OrthancRestApi connection = new OrthancRestApi(mainServer.getIpaddress(),mainServer.getPort(),mainServer.getLogin(),mainServer.getPassword());
        OrthancSettings orthancSettings = new OrthancSettings(connection);
        modalities = orthancSettings.getDicomModalitis();
    }

    public void initNewTask(){
        selectedTask = new TimetableTask();
        selectedTask.setAction("send");
    }

    public void addNewTask(){
//        if(){
//
//        }
        tasks.add(selectedTask);
        saveTask(selectedTask);

        System.out.println(selectedTask.getTimeTask()+" "+selectedTask.getAction()+" "+selectedTask.getSource()+" "+selectedTask.getDestination());
        PrimeFaces.current().executeScript("PF('manageTask').hide()");
        PrimeFaces.current().ajax().update(":timetable:dt-tasks");
        LogTool.getLogger().debug("Admin: "+currentUser.getSignature()+" add new route "+selectedTask.getId());
    }

    public void changeTime(){
        System.out.println("changetime "+selectedTask.getTimeTask());
    }

    public void changeDialogForm(){
        System.out.println(selectedTask.getAction());
        PrimeFaces.current().ajax().update(":timetable:manage-task");
    }

    public void deleteTaskFromList(){
        tasks.remove(selectedTask);
        PrimeFaces.current().ajax().update(":timetable:dt-tasks");
    }
}
