package ru.bitServer.beans;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;

@ManagedBean(name = "terminalBean")
@ViewScoped
public class TerminalBean implements Serializable {

    @PostConstruct
    public void init() {
        System.out.println("terminalbean");
    }

    public String handleCommand(String command, String[] params) {
        String outString = "";

        try {

            Process proc = Runtime.getRuntime().exec(command);
            System.out.println(command+" "+params);
            proc.waitFor();

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(proc.getErrorStream()));

            System.out.println("Here is the standard output of the command:\n");
            String s = null;
            StringBuilder sb = new StringBuilder();
            while ((s = stdInput.readLine()) != null) {
                sb.append(s).append("/n");
                System.out.println(sb.toString());
                outString = "error = "+sb.toString();
            }
        }catch (Exception e){
            System.out.println("e = "+e.getMessage());
            outString = "error = "+e.getMessage();
        }

        return outString;
    }
}
