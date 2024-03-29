package foodcenter.service.requset;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

import foodcenter.service.enums.ServiceType;
import foodcenter.service.proxies.CompanyProxy;
import foodcenter.service.proxies.OrderProxy;
import foodcenter.service.proxies.RestaurantProxy;
import foodcenter.service.proxies.TableReservationProxy;
import foodcenter.service.proxies.UserProxy;

@ServiceName(value = "foodcenter.server.service.ClientService")
public interface ClientServiceRequest extends RequestContext
{
	public Request<Void> logout();
    public Request<UserProxy> login(String gcmKey);
    public Request<OrderProxy> makeOrder(OrderProxy order);
    public Request<List<OrderProxy>> getOrders(Integer startIdx, Integer endIdx);
    public Request<TableReservationProxy> reserveTable(TableReservationProxy reservation);
    
    public Request<List<String>> getCoworkers();
    
    //services the user needs for restaurant
    public Request<List<RestaurantProxy>> getDefaultRestaurants();
    public Request<List<RestaurantProxy>> findRestaurant(String pattern, List<ServiceType> services);
    public Request<RestaurantProxy> getRestaurantById(String id);
    
    
    // services the user needs for companies    
    public Request<List<CompanyProxy>> getDefaultCompanies();
    public Request<List<CompanyProxy>> findCompany(String pattern, List<ServiceType> services);
    public Request<CompanyProxy> getCompanyById(String id);
    
    
}
