package ru.bitServer.beans;

import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.CommonsUploadedFile;
import org.primefaces.model.file.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import java.io.ByteArrayInputStream;

@ManagedBean(name = "dicomcreatorBean")
@SessionScoped
public class DicomCreatorBean {

    static byte[] createImageData;
    UploadedFile file;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    @PostConstruct
    public void init() {
        //PrimeFaces.current().ajax().update(":dicomcreator:imgpanel1");
        file = new CommonsUploadedFile();
    }

    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();
        createImageData = file.getContent();//event.getFile().getContent();
        System.out.println("handleFileUpload");
        PrimeFaces.current().ajax().update(":dicomcreator:img1");
    }

    public void onstart(){
        System.out.println("start");
        PrimeFaces.current().ajax().update(":dicomcreator:img1");
    }

    public static void onClearForm(){
        createImageData = null;
        PrimeFaces.current().ajax().update(":dicomcreator:img1");
    }

    public void onUpdate(){
        //createImageData = null;
        //System.out.println("fname "+ file.getFileName());
        PrimeFaces.current().ajax().update(":dicomcreator:img1:");
    }

    public StreamedContent getImage() {
        if (createImageData == null) {
            return null;
        } else {
            return DefaultStreamedContent.builder()
                    .contentType("image/jpeg")
                    .name("test")
                    .stream(() -> {
                        try {
                            return new ByteArrayInputStream(createImageData);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }).build();
        }
    }
}
