package foodcenter.server.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import foodcenter.server.db.DbHandler;
import foodcenter.server.db.DbHandler.DeclaredParameter;
import foodcenter.server.db.DbHandler.SortOrder;
import foodcenter.server.db.DbHandler.SortOrderDirection;
import foodcenter.server.db.modules.DbCompanyBranch;
import foodcenter.server.db.modules.DbOrder;

public class CompanyBranchAdminService
{
    
    private static Logger logger = LoggerFactory.getLogger(CompanyAdminService.class);
    
    public static DbCompanyBranch saveCompanyBranch(DbCompanyBranch branch)
    {
        return DbHandler.save(branch);
    }
    
    
    public static List<DbOrder> getOrders(String branchId, Date from, Date to)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(from);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        from = calendar.getTime();
        
        calendar.setTime(to);
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        to = calendar.getTime();
        
        logger.info("get orders, comp branchId=" + branchId
                    + ", from: "
                    + from.toString()
                    + ", to: "
                    + to.toString());
        if (!isBranchAdmin(branchId))
        {
            return null;
        }

        String query = "compBranchId == branchIdP && date >= fromP && date <= toP";

        ArrayList<DeclaredParameter> params = new ArrayList<DeclaredParameter>();
        params.add(new DeclaredParameter("branchIdP", branchId));
        params.add(new DeclaredParameter("fromP", from));
        params.add(new DeclaredParameter("toP", to));

        ArrayList<SortOrder> sort = new ArrayList<SortOrder>();
        sort.add(new SortOrder("date", SortOrderDirection.DESC));

        return DbHandler.find(DbOrder.class, query, params, sort, Integer.MAX_VALUE);
    }
    
    private static boolean isBranchAdmin(String branchId)
    {
        if (null == branchId)
        {
            return false;
        }

        DbCompanyBranch branch = DbHandler.find(DbCompanyBranch.class, branchId);
        return ((null != branch) && branch.isEditable());
    }


}