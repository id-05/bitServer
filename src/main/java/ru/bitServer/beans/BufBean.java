package ru.bitServer.beans;

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
import java.io.File;
import java.util.Iterator;
import org.dcm4che3.imageio.plugins.dcm.DicomImageReadParam;

@ManagedBean(name = "BufBean")
@RequestScoped
public class BufBean {

    @PostConstruct
    public void init() {

    }





}
