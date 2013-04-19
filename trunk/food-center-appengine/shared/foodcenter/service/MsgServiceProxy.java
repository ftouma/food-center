package foodcenter.service;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

import foodcenter.service.proxies.MsgProxy;

@ServiceName(value="foodcenter.server.service.MsgService")
public interface MsgServiceProxy extends RequestContext
{

	Request<Void> createMsg(String msg);

	Request<Void> deleteMsg(String msg);

	Request<List<MsgProxy>> getMsgs();

}
