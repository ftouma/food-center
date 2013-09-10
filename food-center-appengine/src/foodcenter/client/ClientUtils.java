package foodcenter.client;

import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.ValueBoxBase;

public class ClientUtils
{
    public final static String GOOGLE_API_MAPS_KEY = "AIzaSyD304OJtRcgd-t5L6hsDDJxDgYXOdVzQVw"; 
    public final static String GOOGLE_API_MAPS_VER = "2";
            
    public static void setNotNullText(ValueBoxBase<String> w, String s)
    {
        if (null != s)
        {
            w.setText(s);
        }
    }
    
    public static void setNotNullHtml(HasSafeHtml w, String html)
    {
        if (null == html)
        {
            return;
        }
        
        SafeHtmlBuilder builder = new SafeHtmlBuilder();
        builder.appendHtmlConstant(html);
        w.setHTML(builder.toSafeHtml());
    }

}
