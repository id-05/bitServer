package ru.bitServer.util;

import ru.bitServer.dao.BitServerStudy;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dicom.DicomModaliti;
import ru.bitServer.dicom.OrthancSettings;
import ru.bitServer.service.TimetableTask;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static ru.bitServer.beans.MainBean.mainServer;

public class TimersLauncher implements UserDao, Runnable {

    ArrayList<TimetableTask> tasks = new ArrayList<>();
    List<BitServerStudy> visibleStudiesList = new ArrayList<>();
    OrthancRestApi connection;
    OrthancSettings orthancSettings;
    List<DicomModaliti> allModalities = new ArrayList<>();

    @Override
    public void run() {
        tasks = getAllTasks();
        connection = new OrthancRestApi(mainServer.getIpaddress(), mainServer.getPort(), mainServer.getLogin(), mainServer.getPassword());
        StringBuilder sb;
        for (TimetableTask bufTask:tasks){
            LocalTime nTime = LocalTime.now();
            if( (bufTask.getTimeTask().getHour()*60+bufTask.getTimeTask().getMinute()) == (nTime.getHour()*60 + nTime.getMinute()) ) {
                switch (bufTask.getAction()) {
                    case "send":
                        LogTool.getLogger().info(this.getClass().getSimpleName() + ": " + "Start send task, info: source: " + bufTask.getSource() + " destination: " + bufTask.getDestination());
                        System.out.println("Start send task, info: source: " + bufTask.getSource() + " destination: " + bufTask.getDestination());
                        //поиск всех записей соответствующих условию
                        visibleStudiesList = getStudyFromOrthanc(0, "today", new Date(), new Date(), getAETbyNameModaliti(bufTask.getAltSource()));
                        LogTool.getLogger().info(this.getClass().getSimpleName() + ": " + "Start send task, info: StudiesListSize: " + visibleStudiesList.size());
                        System.out.println("Start send task, info: StudiesListSize: " + visibleStudiesList.size());
                        //отправка
                        for (BitServerStudy bufStudy : visibleStudiesList) {
                            try {
                                sb = connection.makePostConnectionAndStringBuilderWithIOE("/modalities/" + bufTask.getDestination() + "/store", bufStudy.getSid());
                                LogTool.getLogger().info(this.getClass().getSimpleName() + ": Ответ на оптравку:" + sb.toString());
                                System.out.println("Ответ на оптравку:" + sb.toString());
                            } catch (IOException e) {
                                LogTool.getLogger().error(this.getClass().getSimpleName() + ": Ошибка во время выполенения задания по расписанию:" + e.getMessage());
                            }
                        }
                        break;

                    case "reset":
                        LogTool.getLogger().info("Server restart, timer command!");
                        try {
                            LogTool.getLogger().info(this.getClass().getSimpleName() + ": Reset command from TimersLauncher - start");
                            Process proc = Runtime.getRuntime().exec("sudo ./home/tomcat/scripts/reboot");
                            proc.waitFor();
                        } catch (Exception e) {
                            LogTool.getLogger().error(this.getClass().getSimpleName() + ": " + "Error powerReboot() timer command: " + e.getMessage());
                        }
                        break;

                    case "deleteOld":

                        break;

                    default:
                        break;
                }
            }
        }
    }

    public String getAETbyNameModaliti(String nameModaliti){
        String bufAET = "";
        connection = new OrthancRestApi(mainServer.getIpaddress(),mainServer.getPort(),mainServer.getLogin(),mainServer.getPassword());
        orthancSettings = new OrthancSettings(connection);
        allModalities = orthancSettings.getDicomModalitis();
        for(DicomModaliti bufModaliti:allModalities){
            if(bufModaliti.getDicomName().equals(nameModaliti)){
                bufAET = bufModaliti.getDicomTitle();
                break;
            }
        }
        return bufAET;
    }
}
