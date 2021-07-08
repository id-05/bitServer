package ru.bitServer.component;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@FacesComponent(value = "components.UserStaticComponent", createTag = true)
public class UserStaticComponent extends UIComponentBase {
    @Override
    public String getFamily() {
        return "Return family";
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        String message = (String) getAttributes().get("message");
        LocalDateTime time = (LocalDateTime) getAttributes().get("time");
        String formattedTime = time.format(
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));

        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("p", this);
        writer.write("Message: " + message);
        writer.endElement("p");

        writer.startElement("p", this);
        writer.write("Time: " + formattedTime);
        writer.endElement("p");
    }
}
