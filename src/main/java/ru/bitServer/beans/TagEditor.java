package ru.bitServer.beans;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.io.DicomInputStream;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import ru.bitServer.dicom.DicomTag;
import ru.bitServer.dicom.OrthancStudy;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;

@ManagedBean(name = "tagEditorBean")
@ViewScoped
public class TagEditor {

    ArrayList<DicomTag> TagList = new ArrayList<>();
    DicomTag selectTag = new DicomTag();

    public ArrayList<DicomTag> getTagList() {
        return TagList;
    }

    public void setTagList(ArrayList<DicomTag> tagList) {
        TagList = tagList;
    }

    public DicomTag getSelectTag() {
        return selectTag;
    }

    public void setSelectTag(DicomTag selectTag) {
        this.selectTag = selectTag;
    }

    @PostConstruct
    public void init() {

    }

    public void handleFileUpload(FileUploadEvent event) throws IOException, SQLException {
        UploadedFile file = event.getFile();
        file.getContent();
        DicomInputStream din = new DicomInputStream(new ByteArrayInputStream(file.getContent()));
        Attributes attributes = din.readDataset(-1, -1);
        Attributes fmi = din.readFileMetaInformation();

        Tag tag = new Tag();
        Field[] fields = tag.getClass().getDeclaredFields();
        for (Field field : fields){
            try {
                String buf = attributes.getString(field.getInt(field.getName()),"none");
                if(!buf.equals("none")){
                    DicomTag bufTag = new DicomTag(field.getName(),attributes.getString(field.getInt(field.getName()),"none"));
                    TagList.add(bufTag);
                }
                //System.out.println(field.getName()+" = "+attributes.getString(field.getInt(field.getName()),"none"));
            }catch (Exception e){
                //System.out.println(e.getMessage());
            }

        }
        String PatientName = attributes.getString(Tag.PatientName , "");
        System.out.println(" PatientName = "+PatientName);
        String PatientID = attributes.getString(Tag.PatientID, "");
        System.out.println(" PatientName = "+PatientID);
        PrimeFaces.current().executeScript("PF('addDICOM').hide()");
        PrimeFaces.current().ajax().update(":tageditor:dt-tags");
    }
}
