package ru.bitServer.beans;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.dcm4che3.imageio.plugins.dcm.DicomImageReadParam;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.shaded.commons.io.output.ByteArrayOutputStream;
import ru.bitServer.dao.BitServerStudy;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dao.Usergroup;
import ru.bitServer.dao.Users;
import ru.bitServer.util.OrthancRestApi;
import ru.bitServer.util.SessionUtils;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import static jdk.internal.org.objectweb.asm.commons.Method.getMethod;
import static ru.bitServer.beans.MainBean.mainServer;


@ManagedBean(name = "clientBean", eager = true)
@SessionScoped
public class ClientBean implements UserDao {

    boolean skip;
    boolean skipNext = true;
    int activeStep = 0;
    String currentStudyName;
    DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy_HH:mm");
    Users currentUser;
    ArrayList<byte[]> listUploadFile = new ArrayList<>();
    int uploadCount = 0;
    ArrayList<UploadedFile> files = new ArrayList<>();
    String anamnes;

    public String getAnamnes() {
        return anamnes;
    }

    public void setAnamnes(String anamnes) {
        this.anamnes = anamnes;
    }

    public int getUploadCount() {
        return uploadCount;
    }

    public void setUploadCount(int uploadCount) {
        this.uploadCount = uploadCount;
    }

    public int getActiveStep() {
        return activeStep;
    }

    public void setActiveStep(int activeStep) {
        this.activeStep = activeStep;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public String getCurrentStudyName() {
        return currentStudyName;
    }

    public void setCurrentStudyName(String currentStudyName) {
        this.currentStudyName = currentStudyName;
    }

    public boolean isSkipNext() {
        return skipNext;
    }

    @PostConstruct
    public void init() {
        HttpSession session = SessionUtils.getSession();
        currentUser = getUserById(session.getAttribute("userid").toString());
        Usergroup usergroup = getUsergroupById(currentUser.getUgroup());
        anamnes = "";
        //currentStudyName = usergroup.getRuName()+"_"+formatter.format(new Date());
//        System.out.println("client bean");
//        File file = new File("D:\\dicom\\IM11.dcm");
//        BufferedImage bufferedImg = createBufferedImgdFromDICOMfile(file);
//        outputJpegImage(bufferedImg, "D:\\dicom\\dicom.jpg");

    }

    public StreamedContent getDicomimage(){
        try {
            return DefaultStreamedContent.builder()
                    .contentType("image")
                    .stream(() -> {


                        try {
                            ByteArrayOutputStream os = new ByteArrayOutputStream();
                            File file = new File("D:\\dicom\\IM11.dcm");

                            BufferedImage bufferedImg = createBufferedImgdFromDICOMfile(file);
                            ImageIO.write(bufferedImg, "DICOM", os);
                            //outputJpegImage(bufferedImg, "D:\\dicom\\dicom.jpg");
                            return new ByteArrayInputStream(os.toByteArray());
                        }
                        catch (Exception e) {
                            System.out.println("dicomfile error create "+e.getMessage());
                            return null;
                        }
                    })
                    .build();
        } catch (Exception e) {
            System.out.println("dicomfile error "+e.getMessage());
            return null;
        }
    }

    static BufferedImage createBufferedImgdFromDICOMfile(File dicomFile) {
        Raster raster = null ;
        System.out.println("Input: " + dicomFile.getName());

        try {
            Iterator iter = ImageIO.getImageReadersByFormatName("DICOM");
            ImageReader reader = (ImageReader) iter.next();
            DicomImageReadParam param = (DicomImageReadParam) reader.getDefaultReadParam();
            ImageInputStream iis = ImageIO.createImageInputStream(dicomFile);
            reader.setInput(iis, false);
            raster = reader.readRaster(0, param);
            if (raster == null)
                System.out.println("Error: couldn't read Dicom image!");
            iis.close();
        }
        catch(Exception e) {
            System.out.println("Error: couldn't read dicom image! "+ e.getMessage());
            e.printStackTrace();
        }
        return get16bitBuffImage(raster);
    }

    public static BufferedImage get16bitBuffImage(Raster raster) {
        short[] pixels = ((DataBufferUShort) raster.getDataBuffer()).getData();
        ColorModel colorModel = new ComponentColorModel(
                ColorSpace.getInstance(ColorSpace.CS_GRAY),
                new int[]{16},
                false,
                false,
                Transparency.OPAQUE,
                DataBuffer.TYPE_USHORT);
        DataBufferUShort db = new DataBufferUShort(pixels, pixels.length);
        WritableRaster outRaster = Raster.createInterleavedRaster(
                db,
                raster.getWidth(),
                raster.getHeight(),
                raster.getWidth(),
                1,
                new int[1],
                null);
        return new BufferedImage(colorModel, outRaster, false, null);
    }

    private static void outputJpegImage(BufferedImage outputImage, String outputPath) {
        try {
            System.out.println("output image");
            File outputJpegFile = new File(outputPath);
            OutputStream output = new BufferedOutputStream(new FileOutputStream(outputJpegFile));
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(output);
            encoder.encode(outputImage);
            output.close();
        } catch (IOException e) {
            System.out.println("Error: couldn't write jpeg image! "+ e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Output: " + outputPath);
    }

    public void onPreviewClick() {
        if(activeStep>0)
            activeStep--;
        if(activeStep==0)
                skip = false;
        if(activeStep <3)
            skipNext = true;
        //PrimeFaces.current().executeScript("PF('visibleStudy').unselectAllRows();");
        PrimeFaces.current().ajax().update("stepbystep");
    }

    public void onNextClick() throws NoSuchMethodException {
       if(activeStep<3)
       {
           switch (activeStep){
               case 0:{
                   skip = true;
                   activeStep++;
                   break;
               }

               case 1:{
                   if(!anamnes.equals("")){
                       activeStep++;
                       if (activeStep==3){
                           skipNext = false;
                       }
                   }
                   break;
               }

               case 2:{
                   if(uploadCount>0){
                       activeStep++;
                       skipNext = false;
                   }
                   break;
               }
           }

       }

       //PrimeFaces.current().executeScript("PF('visibleStudy').unselectAllRows();");
       PrimeFaces.current().ajax().update("stepbystep");
    }

    public boolean selectcard(int i){
        if(i==activeStep)
            return true;
        else return false;
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        UploadedFile f = event.getFile();
        listUploadFile.add(f.getContent());
        uploadCount++;
        PrimeFaces.current().ajax().update(":stepbystep:count");
    }

    public void clearfileList(){
        listUploadFile.clear();
        uploadCount = 0;
        PrimeFaces.current().ajax().update(":stepbystep:count");
    }

    public void aprove() throws IOException {
        PrimeFaces.current().executeScript("PF('statusDialog').show()");
        OrthancRestApi connection = new OrthancRestApi(mainServer.getIpaddress(),mainServer.getPort(),mainServer.getLogin(),mainServer.getPassword());
        for(byte[] bufInstance:listUploadFile){
            HttpURLConnection conn = connection.sendDicom("/instances", bufInstance);
            String buf = conn.getResponseMessage();
            System.out.println(buf);
            conn.disconnect();
        }

        BitServerStudy newStudy = new BitServerStudy();
        newStudy.setAnamnes(anamnes);
        //newStudy.
        //addStudyInBitServerStudyTable(newStudy);

        PrimeFaces.current().executeScript("PF('statusDialog').hide()");
    }

    public void oncomplete(){
        System.out.println("oncomplete");
    }

    public void onadd(){
        System.out.println("onadd");
    }

    public void onupload(){
        System.out.println("onupload");
    }

    public void onstart(){
        System.out.println("onstart(");
    }

}
