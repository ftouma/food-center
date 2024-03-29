package foodcenter.service.proxies;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;

import foodcenter.service.proxies.interfaces.AbstractEntityInterface;

@ProxyForName(value = "foodcenter.server.db.modules.DbCourse", locator = "foodcenter.server.db.DbObjectLocator")
public interface CourseProxy extends EntityProxy, AbstractEntityInterface
{
    public String getName();

    public void setName(String name);

    public String getInfo();

    public void setInfo(String info);
    
    public Double getPrice();

    public void setPrice(Double price);
}
