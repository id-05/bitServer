package ru.bitServer.util;

import ru.bitServer.dao.UserDao;
import ru.bitServer.service.TimetableTask;

import javax.faces.application.FacesMessage;
import java.util.ArrayList;

import static ru.bitServer.beans.AutoriseBean.showMessage;

public class TimersLauncher implements UserDao, Runnable {

    ArrayList<TimetableTask> tasks = new ArrayList<>();

    @Override
    public void run() {
        tasks = getAllTasks();
        for (TimetableTask bufTask:tasks){
            switch(bufTask.getAction()){
                case "send":
                    //поиск всех записей соответствующих условию
                    if(bufTask.getSource().equals("all")){

                    }
                    //отправка
                    break;

                case "reset":
                    LogTool.getLogger().info("Server restart, timer command!");
                    try {
                        Process proc = Runtime.getRuntime().exec("sudo ./home/tomcat/scripts/reboot");
                        proc.waitFor();
                    }catch (Exception e){
                        LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ "Error powerReboot() timer command: "+e.getMessage());
                    }
                    break;

                case "deleteOld":
                    break;

                default: break;
            }
        }
    }
}
