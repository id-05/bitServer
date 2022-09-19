package ru.bitServer.dao;

import org.primefaces.convert.DateTimeConverter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter("myDateTimeConverter")
public class MyDateConvertion extends DateTimeConverter {

    public MyDateConvertion() {
        setPattern("dd.MM.yyyy");
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && value.length() != getPattern().length()) {
            throw new ConverterException("Invalid format");
        }
        return super.getAsObject(context, component, value);
    }

}