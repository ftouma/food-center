package foodcenter.service.msg;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@ServiceName("foodcenter.service.msg.MsgService")
public interface MsgServiceRequest extends RequestContext
{

	Request<Void> createMsg(String msg);

	Request<Void> deleteMsg(String msg);

	Request<List<String>> getMsgs();

}