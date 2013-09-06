package foodcenter.server;

import java.io.IOException;

import javax.jdo.Transaction;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import foodcenter.server.db.PMF;

public class PersistenceFilter implements Filter
{
    protected static final Logger log = LoggerFactory.getLogger(PersistenceFilter.class.getName());
    

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        
    }

    @Override
    public void destroy()
    {
        
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException
    {
    	PMF.initThreadLocal();    	
        Transaction tx = null;
        try
        {
        	tx = PMF.get().currentTransaction();
        	tx.begin();
            chain.doFilter(req, res);
            
            tx = PMF.get().currentTransaction();
            if (tx.isActive())
            {
            	tx.commit();
            }
        }
        catch (Exception e)
        {
        	log.error(e.getMessage(), e);
        }
        finally
        {
        	if (null != tx && tx.isActive())
        	{
        		tx.rollback();
        	}
            
            PMF.closeThreadLocal();
        }

    }
}