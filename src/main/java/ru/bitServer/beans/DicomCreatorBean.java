package ru.bitServer.beans;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.UID;
import org.dcm4che3.data.VR;
import org.dcm4che3.io.DicomInputStream;
import org.dcm4che3.io.DicomOutputStream;
import org.dcm4che3.util.UIDUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.CommonsUploadedFile;
import org.primefaces.model.file.UploadedFile;
import ru.bitServer.util.OrthancRestApi;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

import static ru.bitServer.beans.MainBean.mainServer;

@ManagedBean(name = "dicomcreatorBean")
@SessionScoped
public class DicomCreatorBean {

    static byte[] createImageData;
    UploadedFile file;
    static String patientName;
    static String patientId;
    static String patientSex;
    static String patientDateBirth;
    static String studyDescription;
    static String accessionNumber;
    static String studyModality;
    static Date dateBirth;
    static Date dateStudy;
    static boolean fileEnable = false;
    SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMdd");

    public boolean isFileEnable() {
        return fileEnable;
    }

    public void setFileEnable(boolean fileEnable) {
        DicomCreatorBean.fileEnable = fileEnable;
    }

    public Date getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(Date dateBirth) {
        DicomCreatorBean.dateBirth = dateBirth;
    }

    public Date getDateStudy() {
        return dateStudy;
    }

    public void setDateStudy(Date dateStudy) {
        DicomCreatorBean.dateStudy = dateStudy;
    }

    public String getStudyModality() {
        return studyModality;
    }

    public void setStudyModality(String studyModality) {
        DicomCreatorBean.studyModality = studyModality;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        DicomCreatorBean.patientName = patientName;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        DicomCreatorBean.patientId = patientId;
    }

    public String getPatientSex() {
        return patientSex;
    }

    public void setPatientSex(String patientSex) {
        DicomCreatorBean.patientSex = patientSex;
    }

    public String getStudyDescription() {
        return studyDescription;
    }

    public void setStudyDescription(String studyDescription) {
        DicomCreatorBean.studyDescription = studyDescription;
    }


    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        DicomCreatorBean.accessionNumber = accessionNumber;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    @PostConstruct
    public void init() {
        file = new CommonsUploadedFile();
    }

    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();
        createImageData = file.getContent();
        fileEnable = true;
        PrimeFaces.current().ajax().update(":dicomcreator:saveselected");
        PrimeFaces.current().ajax().update(":dicomcreator:img1");
    }

    public StreamedContent convertFile() throws IOException, IllegalAccessException {
        File jpgSource = new File("D:\\source.jpg");
        File dcmDestination = new File("D:\\result.dcm");

        BufferedImage jpegImage = ImageIO.read(jpgSource);

        int colorComponents = jpegImage.getColorModel().getNumColorComponents();
        int bitsPerPixel = jpegImage.getColorModel().getPixelSize();
        int bitsAllocated = (bitsPerPixel / colorComponents);
        int samplesPerPixel = colorComponents;

        Attributes dicom = new Attributes();
        dicom.setString(Tag.SpecificCharacterSet, VR.CS, "ISO_IR 100");
        dicom.setString(Tag.PhotometricInterpretation, VR.CS, samplesPerPixel == 3 ? "YBR_FULL_422" : "MONOCHROME2");

        dicom.setInt(Tag.SamplesPerPixel, VR.US, samplesPerPixel);
        dicom.setInt(Tag.Rows, VR.US, jpegImage.getHeight());
        dicom.setInt(Tag.Columns, VR.US, jpegImage.getWidth());
        dicom.setInt(Tag.BitsAllocated, VR.US, bitsAllocated);
        dicom.setInt(Tag.BitsStored, VR.US, bitsAllocated);
        dicom.setInt(Tag.HighBit, VR.US, bitsAllocated-1);
        dicom.setInt(Tag.PixelRepresentation, VR.US, 0);

        dicom.setDate(Tag.InstanceCreationDate, VR.DA, new Date());
        dicom.setDate(Tag.InstanceCreationTime, VR.TM, new Date());

        dicom.setString(Tag.StudyInstanceUID, VR.UI, UIDUtils.createUID());
        dicom.setString(Tag.SeriesInstanceUID, VR.UI, UIDUtils.createUID());
        dicom.setString(Tag.SOPInstanceUID, VR.UI, UIDUtils.createUID());
        dicom.createFileMetaInformation(UID.BasicColorImageBox );//UID.//////////////////////////////////CHANGE IT

        //FileOutputStream fos = new FileOutputStream(dcmDestination);
        //BufferedOutputStream bos = new BufferedOutputStream(fos);
        DicomOutputStream dos = new DicomOutputStream(dcmDestination);
        dicom.writeTo(dos);

        dos.writeHeader(Tag.PixelData, VR.OB, -1);
        dos.writeHeader(Tag.Item, null, 0);
        int jpgLen = (int) jpgSource.length();
        dos.writeHeader(Tag.Item, null, (jpgLen+1)&~1);

        FileInputStream fis = new FileInputStream(jpgSource);
        BufferedInputStream bis = new BufferedInputStream(fis);
        DataInputStream dis = new DataInputStream(bis);

        byte[] buffer = new byte[65536];
        int b;
        while ((b = dis.read(buffer)) > 0) {
            dos.write(buffer, 0, b);
        }

        if ((jpgLen&1) != 0) dos.write(0);
        dos.writeHeader(Tag.SequenceDelimitationItem, null, 0);
        dos.close();

        //jpg2Dcm.convert(jpgFileOS, dcmFileOS);
//        String[] args = new String[4];
//        args[0] = "D:\\source.jpg";
//        args[1] = "no-app";
//        args[2] = "f";
//        args[3] = "D:\\";
//        Jpg2Dcm.main(args);
//        System.out.println("convertFile");
//        BufferedImage jpg = ImageIO.read(new ByteArrayInputStream(createImageData));
//        //Convert the image to a byte array
//        DataBufferByte buff = (DataBufferByte) jpg.getData().getDataBuffer();
//        //buff.getData(0);
//        byte[] data = buff.getData(0);
//        ByteBuffer byteBuf = ByteBuffer.allocate(2*data.length);
//        int i = 0;
//        while (data.length > i) {
//            byteBuf.put(data[i]);
//            i++;
//        }
//        //buff.getData(numOfBank)
//        //Copy a header

        File fd = new File("D:\\blank.dcm");
        DicomInputStream dis2 = new DicomInputStream(fd);
        Attributes meta = dis2.readFileMetaInformation();
        //Attributes attributes = new Attributes();// dis.readDataset(-1, Tag.PixelData);
        Attributes attributes = dis2.readDataset();
        dis.close();

        Tag tag = new Tag();
        Field[] fields = tag.getClass().getDeclaredFields();
        Field bufField = getField("PatientName", fields);
        attributes.setString(bufField.getInt(bufField.getName()), VR.PN, patientName);
        bufField = getField("PatientID", fields);
        attributes.setString(bufField.getInt(bufField.getName()), VR.SH, patientId);
        bufField = getField("PatientBirthDate", fields);
        attributes.setString(bufField.getInt(bufField.getName()), VR.DA, FORMAT.format(dateBirth));
        bufField = getField("PatientSex", fields);
        attributes.setString(bufField.getInt(bufField.getName()), VR.CS, patientSex);
        bufField = getField("Modality", fields);
        attributes.setString(bufField.getInt(bufField.getName()), VR.CS, studyModality);
        bufField = getField("StudyDescription", fields);
        attributes.setString(bufField.getInt(bufField.getName()), VR.LO, studyDescription);
        bufField = getField("AccessionNumber", fields);
        attributes.setString(bufField.getInt(bufField.getName()), VR.SH, accessionNumber);
        bufField = getField("StudyDate", fields);
        attributes.setString(bufField.getInt(bufField.getName()), VR.DA, FORMAT.format(dateStudy));

        //Change the rows and columns
//        attributes.setInt(Tag.Rows, VR.US, jpg.getHeight());
//        attributes.setInt(Tag.Columns, VR.US, jpg.getWidth());
//        //Attributes attribs = new Attributes();
//
//        //Write the file
//        attributes.setBytes(Tag.PixelData, VR.OW, byteBuf.array());
        File f = new File("myDicom.dcm");
        DicomOutputStream dcmo = new DicomOutputStream(f);
        dcmo.writeFileMetaInformation(meta);
        attributes.writeTo(dcmo);
        dcmo.close();

        OrthancRestApi connection = new OrthancRestApi(mainServer.getIpaddress(), mainServer.getPort(), mainServer.getLogin(), mainServer.getPassword());
        String buf = connection.sendDicom("/instances", Files.readAllBytes(f.toPath()));
        System.out.println("buf = "+buf);

        byte[] bytes = Files.readAllBytes(f.toPath());
        InputStream inputStream = new ByteArrayInputStream(bytes);
        return DefaultStreamedContent.builder()
                .name("test.dcm")
                .contentType("application/dcm")
                .stream(() -> inputStream)
                .build();
    }

    public Field getField(String TagName, Field[] fields){
        Field buf = null;
        for(Field bufField:fields){
            if(bufField.getName().equals(TagName)){
                return bufField;
            }
        }
        return buf;
    }

    public static void onUpdate(){
        fileEnable = false;
        createImageData = null;
        patientName = "";
        patientId = "";
        patientDateBirth = "";
        dateStudy = null;
        dateBirth = null;
        studyDescription = "";
        studyModality = "";
        accessionNumber = "";
        PrimeFaces.current().ajax().update(":dicomcreator:");
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
