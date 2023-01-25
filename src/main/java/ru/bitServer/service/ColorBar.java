package ru.bitServer.service;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static javafx.scene.paint.Color.rgb;

public class ColorBar {

    ArrayList<Color> colorList = new ArrayList<>();

    public ColorBar(){
        colorList.add(rgb(128,0,0));
        colorList.add(rgb(255,99,71));
        colorList.add(rgb(255,69,0));
        colorList.add(rgb(255,140,0));
        colorList.add(rgb(184,134,11));
        colorList.add(rgb(238,232,170));
        colorList.add(rgb(128,128,0));
        colorList.add(rgb(154,205,50));
        colorList.add(rgb(50,205,50));
        colorList.add(rgb(95,158,160));
        colorList.add(rgb(138,43,226));
        colorList.add(rgb(75,0,130));
        colorList.add(rgb(139,0,139));
        colorList.add(rgb(219,112,147));
        colorList.add(rgb(245,222,179));
        colorList.add(rgb(139,69,19));
        colorList.add(rgb(210,105,30));
        colorList.add(rgb(169,169,169));
        colorList.add(rgb(112,128,144));
        colorList.add(rgb(176,196,222));
    }

    public String getColor(){
        int i = ThreadLocalRandom.current().nextInt(1, colorList.size());
        Color color = colorList.get(i);
        String buf = "rgb("+color.getRed()*255+","+ color.getGreen()*255+","+color.getBlue()*255+")";
        colorList.remove(i);
        return buf;
    }
}
