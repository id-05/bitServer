package ru.CustomOrthancWebMorda.beans;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import java.io.FileInputStream;
import java.io.IOException;

@ManagedBean(name = "settingsBean")
@SessionScoped
public class SettingsBean {

    public String ServerName;
    public boolean value1;
    public boolean firstload = false;

    public String getServerName() {
        return ServerName;
    }

    public void setServerName(String serverName) {
        ServerName = serverName;
    }

    public Boolean getValue1() {
        return value1;
    }

    public void setValue1(Boolean value1) {
        this.value1 = value1;
    }

    public void print(){
        System.out.println("print");
    }

    public void loadConfig(PhaseEvent evt){
        try {
            if (PhaseId.APPLY_REQUEST_VALUES.equals(evt.getPhaseId())) {
                System.out.println("Phase is " + PhaseId.APPLY_REQUEST_VALUES);
            }
            if (PhaseId.INVOKE_APPLICATION.equals(evt.getPhaseId())) {
                System.out.println("Phase is " + PhaseId.INVOKE_APPLICATION);
            }
            if (PhaseId.RENDER_RESPONSE.equals(evt.getPhaseId())) {
                System.out.println("Phase is " + PhaseId.RENDER_RESPONSE);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        if(!firstload) {
            firstload = true;
            StringBuilder sb = new StringBuilder();
            StringBuilder sbforbackup = new StringBuilder();
            //читаем файл
            try (FileInputStream fin = new FileInputStream("D://orthanc.json")) {
                int i = -1;
                while ((i = fin.read()) != -1) {
                    sb.append((char) i);
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            System.out.println(sb);
        }
        //json parser
       // System.out.println(sb);
    }

    public void saveConfig(){
        System.out.println("save");
    }

    public void resetServer(){
        System.out.println("reset");
    }

    public void restoreBackup(){
        System.out.println("restore");
    }

    public void showMessage2() {
        System.out.println("сработало");
        FacesMessage message = new FacesMessage("SettingsBean", "Всплывающее сообщение");
        message.setSeverity(FacesMessage.SEVERITY_INFO); //как выглядит окошко с сообщением
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
