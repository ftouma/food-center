package foodcenter.server.db.modules;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.validation.constraints.NotNull;

import com.google.appengine.api.users.User;

import foodcenter.server.db.security.UsersManager;
import foodcenter.service.enums.ServiceType;

@PersistenceCapable (detachable="true")
public class DbRestaurant extends AbstractDbObject
{

    /**
	 * 
	 */
    private static final long serialVersionUID = -9053099508081246189L;

    public static final String DEFAULT_ICON_PATH = "/images/default_restaurant_icon.png";

    @Persistent
    @NotNull
    private String name = "";

    @Persistent
    private DbMenu menu = new DbMenu();

    @Persistent
    private String phone = "";

    @Persistent (mappedBy = "restaurant")
    @Element(dependent = "true")
    private List<DbRestaurantBranch> branches = new ArrayList<DbRestaurantBranch>();

    @Persistent
    private List<String> admins = new ArrayList<String>(); // emails

    @Persistent
    private List<ServiceType> services = new ArrayList<ServiceType>();

    @Persistent
    private String info = "";
    
    public DbRestaurant()
    {
        super();
    }

    public DbRestaurant(String name)
    {
        this();
        this.name = name;
    }

    @Override
    public void jdoPostLoad()
    {
        super.jdoPostLoad();
        setPermission();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public DbMenu getMenu()
    {
        return menu;
    }

    public void setMenu(DbMenu menu)
    {
        this.menu = menu;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public List<DbRestaurantBranch> getBranches()
    {
        return branches;
    }

    public void setBranches(List<DbRestaurantBranch> branches)
    {
        this.branches = branches;
    }

    public List<String> getAdmins()
    {
        return admins;
    }

    public void setAdmins(List<String> admins)
    {
        this.admins = admins;
    }

    public List<ServiceType> getServices()
    {
        return services;
    }

    public void setServices(List<ServiceType> services)
    {
        this.services = services;
    }

    public String getInfo()
    {
        return info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }

    /**
     * called on postLoad
     */
    private void setPermission()
    {
        User user = UsersManager.getUser();
        
        // Set edit permission (this happens in post load before any changes to admins)
        setEditable(UsersManager.isAdmin() || admins.contains(user.getEmail().toLowerCase()));
        
        // Make sure image can be shown!
        if (0 == getImageUrl().length())
        {
            setImageUrl(DEFAULT_ICON_PATH);
        }

    }
}
