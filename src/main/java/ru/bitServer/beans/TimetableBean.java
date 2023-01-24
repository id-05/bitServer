package ru.bitServer.beans;

import com.google.gson.JsonObject;
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
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static ru.bitServer.beans.MainBean.*;

@ManagedBean(name = "timetableBean")
@ViewScoped
public class TimetableBean implements UserDao {

    TimetableTask selectedTask;
    ArrayList<TimetableTask> tasks = new ArrayList<>();
    BitServerUser currentUser;
    String currentUserId;
    List<DicomModaliti> modalities;

    public List<DicomModaliti> getModalities() {
        return modalities;
    }

    public void setModalities(List<DicomModaliti> modalities) {
        this.modalities = modalities;
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
        tasks = getAllTasks();
        PrimeFaces.current().ajax().update(":timetable:dt-tasks");
        OrthancRestApi connection = new OrthancRestApi(mainServer.getIpaddress(),mainServer.getPort(),mainServer.getLogin(),mainServer.getPassword());
        OrthancSettings orthancSettings = new OrthancSettings(connection);
        modalities = orthancSettings.getDicomModalitis();
    }

    public void initNewTask(){
        selectedTask = new TimetableTask();
        selectedTask.setAction("send");
        selectedTask.setTimeTask(LocalTime.now());
        selectedTask.setSource("");
        selectedTask.setDestination("");
    }

    public void addNewTask(){
        JsonObject jsonSelectTask = new JsonObject();
        jsonSelectTask.addProperty("time", selectedTask.getStrTime());
        jsonSelectTask.addProperty("action", selectedTask.getAction());
        jsonSelectTask.addProperty("source", selectedTask.getAltSource());
        jsonSelectTask.addProperty("destination", selectedTask.getDestination());

        boolean unical = true;
        for (TimetableTask bufTask : tasks) {
            JsonObject jsonBufTask = new JsonObject();
            jsonBufTask.addProperty("time", bufTask.getStrTime());
            jsonBufTask.addProperty("action", bufTask.getAction());
            jsonBufTask.addProperty("source", bufTask.getAltSource());
            jsonBufTask.addProperty("destination", bufTask.getDestination());

            if (jsonSelectTask.toString().equals(jsonBufTask.toString())) {
                unical = false;
                break;
            }
        }

        if (unical | selectedTask.getId() != 0) {
            saveTask(selectedTask);
            tasks = getAllTasks();
            PrimeFaces.current().executeScript("PF('manageTask').hide()");
            PrimeFaces.current().ajax().update(":timetable:dt-tasks");
            LogTool.getLogger().debug("Admin: " + currentUser.getSignature() + " add new route " + selectedTask.getId());
        } else {
            PrimeFaces.current().executeScript("PF('manageTask').hide()");
            showMessage("Внимание!", "Такое правило уже есть в списке!", warning);
            PrimeFaces.current().ajax().update(":timetable:growl");
        }
    }

    public void showMessage(String title, String note, FacesMessage.Severity type) {
        FacesMessage message = new FacesMessage(title, note);
        message.setSeverity(type);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void changeDialogForm(){
        PrimeFaces.current().ajax().update(":timetable:manage-task");
    }

    public void deleteTaskFromList(){
        deleteFromBitServerTable( (long) selectedTask.getId() );
        tasks = getAllTasks();
        PrimeFaces.current().ajax().update(":timetable:dt-tasks");
    }
}
