import com.google.gson.JsonObject;
import ru.bitServer.dao.UserDao;
import ru.bitServer.util.LogTool;
import ru.bitServer.util.OrthancRestApi;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.bitServer.beans.MainBean.mainServer;

public class LinkServlet extends HttpServlet implements UserDao {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String sid = req.getParameter("id");
        if(sid.length()==6) {
            OrthancRestApi connection = new OrthancRestApi(mainServer.getIpaddress(), mainServer.getPort(), mainServer.getLogin(), mainServer.getPassword());
            JsonObject query = new JsonObject();
            query.addProperty("Level", "Study");
            JsonObject queryDetails = new JsonObject();
            queryDetails.addProperty("AccessionNumber", sid);
            query.add("Query", queryDetails);
            StringBuilder sb = connection.makePostConnectionAndStringBuilder("/tools/find", query.toString());
            sid = sb.toString();
            sid = sid.replace("[", "").replace("]", "").replace(" ", "").replace("\"", "");
            resp.sendRedirect("http://" + mainServer.getLogin() + ":" + mainServer.getPassword() + "@" + mainServer.getIpaddress() + ":" + mainServer.getPort() + "/osimis-viewer/app/index.html?study=" + sid);
        }else{
            LogTool.getLogger().warn("Unrecognized AccessionNumber: "+sid);
            resp.sendRedirect("/bitServer/views/errorpage.xhtml");
        }
    }
}
