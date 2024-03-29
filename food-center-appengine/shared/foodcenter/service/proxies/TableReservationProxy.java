package foodcenter.service.proxies;

import java.util.Date;
import java.util.List;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;

import foodcenter.service.enums.TableReservationStatus;
import foodcenter.service.proxies.interfaces.AbstractOrderProxy;

@ProxyForName(value = "foodcenter.server.db.modules.DbTableReservation", locator = "foodcenter.server.db.DbObjectLocator")
public interface TableReservationProxy extends EntityProxy, AbstractOrderProxy
{

    public final static String[] TABLE_RESERVATION_WITH = { "fromDate" , "toDate", "confirmationDate" };

    public Date getFromDate();

    public void setFromDate(Date fromDate);

    public Date getToDate();

    public void setToDate(Date toDate);

    public Date getConfirmationDate();
    
    public TableReservationStatus getStatus();
    
    public List<String> getUsers();

    public void setUsers(List<String> users);

}
