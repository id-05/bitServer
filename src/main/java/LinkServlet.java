import com.google.common.net.HttpHeaders;
import com.google.gson.JsonObject;
import ru.bitServer.dao.BitServerResources;
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
        System.out.println("LinkServlet");
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

           // String originalUri = req.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
            String referrer = req.getHeader(HttpHeaders.REFERER);
            int i = referrer.indexOf("/bitServer/");
            int j = referrer.indexOf("://");
            String address = referrer.substring(j+3,i);
            if(address.contains(":")){
                BitServerResources bufResources = getBitServerResource("port");
                String port = bufResources.getRvalue();
                int k = address.indexOf(":");
                String addressCutPort = address.substring(0,k);
                resp.sendRedirect("http://" + mainServer.getLogin() + ":" + mainServer.getPassword() + "@" +addressCutPort+":"+port+ "/osimis-viewer/app/index.html?study=" + sid);
            }else{
                resp.sendRedirect("http://" + mainServer.getLogin() + ":" + mainServer.getPassword() + "@" +address+ "/osimis-viewer/app/index.html?study=" + sid);
            }
        }else{
            LogTool.getLogger().warn("Unrecognized AccessionNumber: "+sid);
            resp.sendRedirect("/bitServer/views/errorpage.xhtml");
        }
    }
}
