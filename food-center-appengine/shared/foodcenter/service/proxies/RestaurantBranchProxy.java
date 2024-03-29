package foodcenter.service.proxies;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;

import foodcenter.service.enums.ServiceType;
import foodcenter.service.proxies.interfaces.AbstractGeoLocationInterface;

@ProxyForName(value = "foodcenter.server.db.modules.DbRestaurantBranch", locator = "foodcenter.server.db.DbObjectLocator")
public interface RestaurantBranchProxy extends AbstractGeoLocationInterface, EntityProxy
{
    public final static String[] BRANCH_WITH = { "menu",
                                               "menu.categories",
                                               "menu.categories.courses"};

    public List<String> getAdmins();

    public void setAdmins(List<String> admins);

    public List<String> getWaiters();

    public void setWaiters(List<String> waiters);

    public List<String> getChefs();

    public void setChefs(List<String> chefs);

    public boolean isChef();
    
    public List<TableProxy> getTables();

    public void setTables(List<TableProxy> tables);

    public MenuProxy getMenu();

    public void setMenu(MenuProxy menu);

    public List<ServiceType> getServices();

    public void setServices(List<ServiceType> services);

    public String getPhone();

    public void setPhone(String phone);
}
