package foodcenter.server.service;

import foodcenter.server.db.DbHandler;
import foodcenter.server.db.PMF;
import foodcenter.server.db.modules.DbCourse;
import foodcenter.server.db.modules.DbMenu;
import foodcenter.server.db.modules.DbMenuCategory;

public class MenuAdminService
{
    
    public static DbMenu saveMenu(DbMenu menu)
    {
        return DbHandler.save(menu);
    }

	/**
	 * add the course to the category
	 * @param cat
	 * @param course
	 */
	public static void addCategoryCourse(DbMenuCategory cat, DbCourse course)
	{
        PMF.makeTransactional();

		cat.getCourses().add(course);
	}
	
	/**
	 * remove the course from the category
	 * @param cat
	 * @param course
	 */
	public static void removeCategoryCourse(DbMenuCategory cat, DbCourse course)
	{
        PMF.makeTransactional();

		cat.getCourses().remove(course);
	}
	
	/**
	 * add the category to the menu
	 * @param menu
	 * @param category
	 */
	public static void addMenuCategory(DbMenu menu, DbMenuCategory category)
	{
        PMF.makeTransactional();

		menu.getCategories().add(category);
	}
	
	/**
	 * remove the category from the menu
	 * @param menu
	 * @param category
	 */
	public static void removeMenuCategory(DbMenu menu, DbMenuCategory category)
	{
        PMF.makeTransactional();

	    if (menu.getCategories().contains(category))
	    {
	        menu.getCategories().remove(category);
	    }
	}
}
