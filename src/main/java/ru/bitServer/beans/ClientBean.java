package ru.bitServer.beans;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.dcm4che3.imageio.plugins.dcm.DicomImageReadParam;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.shaded.commons.io.output.ByteArrayOutputStream;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.*;
import java.util.Iterator;

@ManagedBean(name = "clientBean", eager = true)
@SessionScoped
public class ClientBean {

    @PostConstruct
    public void init() {
        System.out.println("client bean");
        //        ByteArrayOutputStream os = new ByteArrayOutputStream();
        File file = new File("D:\\dicom\\IM11.dcm");
        BufferedImage bufferedImg = createBufferedImgdFromDICOMfile(file);

//            ImageIO.write(bufferedImg, "dcm", os);
        outputJpegImage(bufferedImg, "D:\\dicom\\dicom.jpg");

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
}
