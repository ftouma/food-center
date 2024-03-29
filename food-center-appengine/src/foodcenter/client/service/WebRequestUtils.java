package foodcenter.client.service;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.web.bindery.requestfactory.shared.RequestContext;

import foodcenter.server.db.modules.AbstractDbGeoObject;
import foodcenter.server.db.modules.DbCompany;
import foodcenter.server.db.modules.DbRestaurant;
import foodcenter.service.FoodCenterRequestFactory;
import foodcenter.service.enums.ServiceType;
import foodcenter.service.proxies.CompanyBranchProxy;
import foodcenter.service.proxies.CompanyProxy;
import foodcenter.service.proxies.CourseOrderProxy;
import foodcenter.service.proxies.CourseProxy;
import foodcenter.service.proxies.MenuCategoryProxy;
import foodcenter.service.proxies.MenuProxy;
import foodcenter.service.proxies.OrderProxy;
import foodcenter.service.proxies.RestaurantBranchProxy;
import foodcenter.service.proxies.RestaurantProxy;

public class WebRequestUtils
{
    private static FoodCenterRequestFactory requestFactory = null;
    
    private WebRequestUtils()
    {
        
    }
    
    public static FoodCenterRequestFactory getRequestFactory()
    {
        if (null == requestFactory)
        {
            final EventBus eventBus = new SimpleEventBus();
            requestFactory = GWT.create(FoodCenterRequestFactory.class);
            requestFactory.initialize(eventBus);
        }
        return requestFactory;
    }
        
    public static MenuProxy createMenuProxy(RequestContext rContext)
    {
    	MenuProxy res = rContext.create(MenuProxy.class);
    	res.setCategories(new ArrayList<MenuCategoryProxy>());
    	return res;
    }
    
    public static CourseProxy createCourseProxy(RequestContext rContext)
    {
        return rContext.create(CourseProxy.class);
    }

    public static CourseOrderProxy createCourseOrderProxy(RequestContext rContext, CourseProxy course, int cnt)
    {
        CourseOrderProxy res = rContext.create(CourseOrderProxy.class);
        
        res.setCnt(cnt);
        res.setCourseId(course.getId());
        res.setName(course.getName());
        res.setInfo(course.getInfo());
        res.setPrice(course.getPrice());
        
        return res;
    }

    public static MenuCategoryProxy createMenuCategoryProxy(RequestContext rContext)
    {
    	MenuCategoryProxy res = rContext.create(MenuCategoryProxy.class);
    	res.setCourses(new ArrayList<CourseProxy>());
    	return res;
    }
    
    public static RestaurantBranchProxy createRestaurantBranchProxy(RequestContext rContext)
    {
    	RestaurantBranchProxy res = rContext.create(RestaurantBranchProxy.class);
    	res.setMenu(createMenuProxy(rContext));

        res.setAdmins(new ArrayList<String>());
        res.setWaiters(new ArrayList<String>());
        res.setChefs(new ArrayList<String>());
        res.setServices(new ArrayList<ServiceType>());
        
    	res.setLat(AbstractDbGeoObject.GOOGLE_API_DEFAULT_LAT);
    	res.setLng(AbstractDbGeoObject.GOOGLE_API_DEFAULT_LNG);
    	res.setAddress(AbstractDbGeoObject.GOOGLE_API_DEFAULT_ADDR);
    	
    	res.setEditable(true);
    	
    	return res;
    }

    public static CompanyBranchProxy createCompanyBranchProxy(RequestContext rContext)
    {
        CompanyBranchProxy res = rContext.create(CompanyBranchProxy.class);
        
        res.setAdmins(new ArrayList<String>());
        res.setWorkers(new ArrayList<String>());
        
        res.setLat(AbstractDbGeoObject.GOOGLE_API_DEFAULT_LAT);
        res.setLng(AbstractDbGeoObject.GOOGLE_API_DEFAULT_LNG);
        res.setAddress(AbstractDbGeoObject.GOOGLE_API_DEFAULT_ADDR);
        
        res.setEditable(true);
        
        return res;
    }

    public static RestaurantProxy createRestaurantProxy(RequestContext rContext)
    {
    	RestaurantProxy res = rContext.create(RestaurantProxy.class);
    	res.setAdmins(new ArrayList<String>());
    	res.setBranches(new ArrayList<RestaurantBranchProxy>());
    	res.setMenu(createMenuProxy(rContext));
    	res.setServices(new ArrayList<ServiceType>());
    	res.setImageUrl(DbRestaurant.DEFAULT_ICON_PATH);

        res.setEditable(true);

    	return res;
    }

    public static CompanyProxy createCompanyProxy(RequestContext rContext)
    {
        CompanyProxy res = rContext.create(CompanyProxy.class);
        res.setAdmins(new ArrayList<String>());
        res.setBranches(new ArrayList<CompanyBranchProxy>());
        res.setServices(new ArrayList<ServiceType>());
        res.setImageUrl(DbCompany.DEFAULT_ICON_PATH);

        res.setEditable(true);

        return res;
    }

    public static OrderProxy createOrder(RequestContext rContext)
    {
    	OrderProxy res = rContext.create(OrderProxy.class);
    	res.setCourses(new ArrayList<CourseOrderProxy>());
    	return res;
    }
}
