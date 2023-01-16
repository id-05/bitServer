package ru.bitServer.beans;

import org.primefaces.PrimeFaces;
import ru.bitServer.dao.BitServerUser;
import ru.bitServer.dao.UserDao;
import ru.bitServer.service.TimetableTask;
import ru.bitServer.util.LogTool;
import ru.bitServer.util.SessionUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;

@ManagedBean(name = "timetableBean")
@ViewScoped
public class TimetableBean implements UserDao {

    TimetableTask selectedTask;
    ArrayList<TimetableTask> tasks = new ArrayList<>();
    BitServerUser currentUser;
    String currentUserId;
    Date timeTask;

    public Date getTimeTask() {
        return timeTask;
    }

    public void setTimeTask(Date timeTask) {
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
    }

    public void initNewTask(){
        selectedTask = new TimetableTask();
        selectedTask.setAction("send");
    }

    public void addNewTask(){
        tasks.add(selectedTask);
        PrimeFaces.current().executeScript("PF('manageTask').hide()");
        PrimeFaces.current().ajax().update(":timetable:dt-tasks");
        LogTool.getLogger().debug("Admin: "+currentUser.getSignature()+" add new route "+selectedTask.getId());
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
