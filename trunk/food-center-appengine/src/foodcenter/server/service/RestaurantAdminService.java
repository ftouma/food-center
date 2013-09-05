package foodcenter.server.service;

import java.util.ArrayList;
import java.util.List;

import foodcenter.server.db.DbHandler;
import foodcenter.server.db.modules.DbRestaurant;
import foodcenter.server.db.modules.DbRestaurantBranch;

public class RestaurantAdminService extends RestaurantBranchAdminService
{

//    private static UserService userService = UserServiceFactory.getUserService();
//    private static Logger logger = LoggerFactory.getLogger(UserService.class);
    
        
    public static void addRestaurantBranch(DbRestaurant rest, DbRestaurantBranch branch)
	{
		List<DbRestaurantBranch> branches = rest.getBranches();
		if (null == branches)
		{
			branches = new ArrayList<DbRestaurantBranch>();
			rest.setBranches(branches);
		}
		branches.add(branch);
	}
    
    public static void removeRestaurantBranch(DbRestaurant rest, DbRestaurantBranch branch)
	{
        List<DbRestaurantBranch> branches= rest.getBranches();
		if (null == branches)
		{
			return;
		}
		if (branches.contains(branch))
		{
		    branches.remove(branch);
//		    branch.setRestaurant(null);
		}
	}
    
//    public static void addRestaurantAdmin(DbRestaurant rest, String admin)
//	{
//		rest.getAdmins().add(admin);
//	}
//	
//	public static void removeRestaurantAdmin(DbRestaurant rest, String admin)
//	{
//		rest.getAdmins().remove(admin);
//	}
    	
    public static DbRestaurant saveRestaurant(DbRestaurant rest)
	{
		DbRestaurant res = DbHandler.save(rest);
		
		return res;
	}

    
}
