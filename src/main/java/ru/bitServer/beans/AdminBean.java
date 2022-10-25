package ru.bitServer.beans;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import ru.bitServer.dao.UserDao;
//import ru.bitServer.dicomviewer.Instance;
//import ru.bitServer.dicomviewer.Patient;
//import ru.bitServer.dicomviewer.Series;
//import ru.bitServer.dicomviewer.Study;
import ru.bitServer.util.LogTool;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Iterator;
import java.util.Map;

@ManagedBean(name = "adminBean", eager = true)
@RequestScoped
public class AdminBean implements UserDao {
    public String buf = "buffer";

    @PostConstruct
    public void init(){
        System.out.println("admin page");
    }

    public void loadDicomFile(){
   //     File dicomFile = new File("D:\\dicom\\IM10");
   ///     BufferedImage bufImage = createBufferedImgdFromDICOMfile(dicomFile);


        //BufferedImage blackWhite = get16bitBuffImage(bufImage);
//        addDicomInstance(new FileDataSource(dicomFile));
    }

//    private void addDicomInstance(DataSource dataSource) {
//        LoadDicomInstanceTask task = new LoadDicomInstanceTask(dataSource);
//        Thread th = new Thread(task);
//        th.setDaemon(true);
//        th.start();
//        //Application.getInstance().getContext().getTaskService().execute(task);
//    }

    public StreamedContent getDicomImage() {
        try {
            return DefaultStreamedContent.builder()
                    .contentType("image/png")
                    .stream(() -> {
                        try {
                            BufferedImage bufferedImg = new BufferedImage(100, 25, BufferedImage.TYPE_INT_RGB);
                            Graphics2D g2 = bufferedImg.createGraphics();
                            g2.drawString("This is a text", 0, 10);
                            //File dicomFile = new File("/resourses//IM12");
                            //BufferedImage bufferedImg = createBufferedImgdFromDICOMfile(dicomFile);
                            ByteArrayOutputStream os = new ByteArrayOutputStream();
                            ImageIO.write(bufferedImg, "png", os);
                            return new ByteArrayInputStream(os.toByteArray());
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public BufferedImage createBufferedImgdFromDICOMfile(File dicomFile) {
        Raster raster = null ;
        System.out.println("Input: " + dicomFile.getName());

        //Open the DICOM file and get its pixel data
        try {
            Iterator iter = ImageIO.getImageReadersByFormatName("DICOM");
            ImageReader reader = (ImageReader) iter.next();
           // DicomImageReadParam param = (DicomImageReadParam) reader.getDefaultReadParam();
            ImageInputStream iis = ImageIO.createImageInputStream(dicomFile);
            reader.setInput(iis, false);
            //Returns a new Raster (rectangular array of pixels) containing the raw pixel data from the image stream
//            raster = reader.readRaster(0, param);
//            if (raster == null)
//                System.out.println("Error: couldn't read Dicom image!");
//            iis.close();
        }
        catch(Exception e) {
            System.out.println("Error: couldn't read dicom image! "+ e.getMessage());
            e.printStackTrace();
        }
        return get16bitBuffImage(raster);
    }

    public BufferedImage get16bitBuffImage(Raster raster) {
        //System.out.println("8");
        short[] pixels = ((DataBufferUShort) raster.getDataBuffer()).getData();
        //System.out.println("9");
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
//
//    private class LoadDicomInstanceTask extends Task<Void> {
//        private final DataSource dataSource;
//
//        public LoadDicomInstanceTask(DataSource dataSource) {
//
//            this.dataSource = dataSource;
//        }
//
//        protected Void doInBackground() throws Exception {
//            DicomObject dcmObj = null;
//            DicomInputStream din = new DicomInputStream(this.dataSource.getInputStream());
//            din.setHandler(new StopTagInputHandler(2145386511));
//            dcmObj = new BasicDicomObject();
//            din.readDicomObject(dcmObj, -1);
//            din.close();
//            String sopClassUID = dcmObj.getString(524310);
//            //Plugin plugin = FileController.this.applicationContext.getPluginContext().getPlugin(sopClassUID);
//            //if (plugin != null)
//            {
//                String patientId = dcmObj.getString(1048608);
//                Patient patient = (Patient) patientMap.get(patientId);
//                if (patient == null) {
//                    patient = new Patient();
//                    patient.setPatientId(patientId);
//                    patient.setPatientName(dcmObj.getString(1048592));
//                    patient.setPatientBirthDate(dcmObj.getDate(1048624));
//                    patient.setPatientSex(dcmObj.getString(1048640));
//                    patientMap.put(patientId, patient);
//                }
//
//                String studyUID = dcmObj.getString(2097165);
//                Study study = (Study) studyMap.get(studyUID);
//                if (study == null) {
//                    study = new Study();
//                    study.setStudyInstanceUID(studyUID);
//                    study.setStudyId(dcmObj.getString(2097168));
//                    study.setStudyDateTime(dcmObj.getDate(524320, 524336));
//                    study.setAccessionNumber(dcmObj.getString(524368));
//                    study.setStudyDescription(dcmObj.getString(528432));
//                    study.setPatient(patient);
//                    patient.addStudy(study);
//                    studyMap.put(studyUID, study);
//                }
//
//                String seriesUID = dcmObj.getString(2097166);
//                Series series = (Series) seriesMap.get(seriesUID);
//                if (series == null) {
//                    series = new Series();
//                    series.setSeriesInstanceUID(dcmObj.getString(2097166));
//                    series.setSeriesNumber(dcmObj.getString(2097169));
//                    series.setModality(dcmObj.getString(524384));
//                    series.setInstitutionName(dcmObj.getString(524416));
//                    series.setSeriesDescription(dcmObj.getString(528446));
//                    series.setStudy(study);
//                    study.addSeries(series);
//                    seriesMap.put(seriesUID, series);
//                }
//
//                String instanceUID = dcmObj.getString(524312);
//                Instance instance = (Instance) instanceMap.get(instanceUID);
//                if (instance == null) {
//                   // instance = plugin.createInstance(this.dataSource);
//                    instance.setSeries(series);
//                    series.addInstance(instance);
//                    instanceMap.put(instanceUID, instance);
//                }
//
////                FileController.this.applicationContext.getViewContext().getDicomThumbsPane().getDicomThumbsModel().addInstance(instance);
////                if (FileController.this.applicationContext.getViewContext().getDicomThumbsPane().getDicomThumbsModel().getSize() == 1) {
////                    FileController.this.applicationContext.getViewContext().getDicomThumbsPane().setSelectedIndex(0);
////                }
//            }
//
//            return null;
//        }
//
//        @Override
//        protected Void call() throws Exception {
//            return null;
//        }
//    }
}
