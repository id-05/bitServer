package ru.bitServer.beans;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.shaded.commons.io.output.ByteArrayOutputStream;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

@ManagedBean
@SessionScoped
public class ClientBean {

    @PostConstruct
    public void init(){
        System.out.println("client bean");
    }

    public StreamedContent getGraphicText() {
        try {
            return DefaultStreamedContent.builder()
                    .contentType("image/png")
                    .stream(() -> {
                        try {
                            BufferedImage bufferedImg = new BufferedImage(100, 25, BufferedImage.TYPE_INT_RGB);
                            Graphics2D g2 = bufferedImg.createGraphics();
                            g2.drawString("This is a text", 0, 10);
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
}
