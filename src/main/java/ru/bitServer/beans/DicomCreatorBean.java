package ru.bitServer.beans;


import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import ru.bitServer.dicom.UploadDicomFile;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;

@ManagedBean(name = "dicomcreatorBean")
@SessionScoped
public class DicomCreatorBean {

    private UploadedFile originalImageFile;

    public UploadedFile getOriginalImageFile() {
        return originalImageFile;
    }

    public void setOriginalImageFile(UploadedFile originalImageFile) {
        this.originalImageFile = originalImageFile;
    }

    @PostConstruct
    private void init() {

    }

    public void handleFileUpload(FileUploadEvent event) {
        this.originalImageFile = null;

        //    if (file != null && file.getContent() != null && file.getContent().length > 0 && file.getFileName() != null) {
            this.originalImageFile = event.getFile();
            System.out.println("yes");
            PrimeFaces.current().ajax().update(":dicomcreator:tabview1:img1");
//            FacesMessage msg = new FacesMessage("Successful", this.originalImageFile.getFileName() + " is uploaded.");
//            FacesContext.getCurrentInstance().addMessage(null, msg);
     //   }
    }

    public StreamedContent getImage() {
        return DefaultStreamedContent.builder()
                .contentType(originalImageFile == null ? null : originalImageFile.getContentType())
                .stream(() -> {
                    if (originalImageFile == null
                            || originalImageFile.getContent() == null
                            || originalImageFile.getContent().length == 0) {
                        return null;
                    }

                    try {
                        return new ByteArrayInputStream(originalImageFile.getContent());
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .build();
    }
}
