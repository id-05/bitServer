package ru.bitServer.dao;

import org.primefaces.PrimeFaces;
import javax.faces.context.FacesContext;
import static ru.bitServer.beans.MainBean.mainServer;

public interface DataAction extends UserDao{

    public default void redirectToOsimis(String sid) {
        String HttpOrHttps;
        if(mainServer.getHttpmode().equals("true")){
            HttpOrHttps = "https";
        }else{
            HttpOrHttps = "http";
        }
        String referrer = FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap().get("referer");
        int i = referrer.indexOf("/bitServer/");
        int j = referrer.indexOf("://");
        String address = referrer.substring(j+3,i);
        if(address.contains(":")){
            BitServerResources bufResources = getBitServerResource("port");
            String port = bufResources.getRvalue();
            int k = address.indexOf(":");
            String addressCutPort = address.substring(0,k);
            PrimeFaces.current().executeScript("window.open('"+HttpOrHttps+"://"+mainServer.getLogin()+":"+mainServer.getPassword()+"@"+addressCutPort+":"+port+"/osimis-viewer/app/index.html?study="+sid+"','_blank')");
        }else{
            PrimeFaces.current().executeScript("window.open('"+HttpOrHttps+"://"+mainServer.getLogin()+":"+mainServer.getPassword()+"@"+address+"/viewer/osimis-viewer/app/index.html?study="+sid+"','_blank')");
        }
    }

}
