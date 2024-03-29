package foodcenter.service.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface MsgServiceAsync
{

	public void getMsgs(AsyncCallback<List<String>> callback);

	public void addMsg(String msg, AsyncCallback<Void> callback);

	public void removeMsg(String msg, AsyncCallback<Void> callback);
	
}
