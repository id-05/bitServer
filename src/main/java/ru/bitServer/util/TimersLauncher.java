package ru.bitServer.util;

import ru.bitServer.dao.BitServerStudy;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dicom.DicomModaliti;
import ru.bitServer.dicom.OrthancSettings;
import ru.bitServer.service.TimetableTask;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.*;

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
                        try {
                            visibleStudiesList = getStudyFromOrthanc(0, "today", new Date(), new Date(), getAETbyNameModaliti(bufTask.getAltSource()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
                        LogTool.getLogger().info("Delete old study start for timer");
                        int i=0;
                        ArrayList<BitServerStudy> bitServerStudies =  getDateFromMaindicomTagsLong();
                        OrthancRestApi connection = new OrthancRestApi(mainServer.getIpaddress(),mainServer.getPort(),mainServer.getLogin(),mainServer.getPassword());
                        for(BitServerStudy bufStudy:bitServerStudies){
                            Calendar calendar = new GregorianCalendar();
                            calendar.add(Calendar.YEAR,-5);
                            calendar.add(Calendar.MONTH,-1);
                            Date fiveYeasAgo = calendar.getTime();
                            if(bufStudy.getSdate().before(fiveYeasAgo)){
                                try {
                                    connection.deleteStudyFromOrthanc(bufStudy.getSid());
                                    i++;
                                }catch (Exception e){
                                    LogTool.getLogger().error(this.getClass().getSimpleName() + ": Error during delete old file. " + e.getMessage());
                                }
                            }
                        }
                        LogTool.getLogger().info("Delete old study finish. Count delete study: "+i);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public String getAETbyNameModaliti(String nameModaliti) throws IOException {
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

//    ArrayList<TimetableTask> getAllTasks() {
//        ArrayList<TimetableTask> resultList = new ArrayList<>();
//        try {
//            Connection conn = getConnection();
//            String resultSQL = "SELECT internalid, rvalue FROM bitserver WHERE rtype = '11'";
//            Statement statement = conn.createStatement();
//            ResultSet rs = statement.executeQuery(resultSQL);
//            while (rs.next()) {
//                TimetableTask bufTask = new TimetableTask(rs.getString(1),rs.getString(2));
//                resultList.add(bufTask);
//            }
//            conn.close();
//        } catch (Exception  e) {
//            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
//        }
//        return resultList;
//    }
}
