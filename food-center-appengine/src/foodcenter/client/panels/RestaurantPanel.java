package foodcenter.client.panels;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.web.bindery.requestfactory.shared.RequestContext;

import foodcenter.client.panels.restaurant.BranchesFlexTable;
import foodcenter.client.panels.restaurant.MenuFlexTable;
import foodcenter.client.panels.restaurant.ProfilePannel;
import foodcenter.client.panels.restaurant.UsersPannel;
import foodcenter.service.proxies.MenuProxy;
import foodcenter.service.proxies.RestaurantBranchProxy;
import foodcenter.service.proxies.RestaurantProxy;
import foodcenter.service.proxies.UserProxy;

public class RestaurantPanel extends VerticalPanel
{

    private final RequestContext requestContext;
    private final RestaurantProxy rest;
    private final Boolean isAdmin;
    private final Runnable onSave;
    private final Runnable onDiscard;
    
    private final Panel hPanel;
    private final Panel sPanel;

    public RestaurantPanel(RequestContext requestContext, RestaurantProxy rest, Boolean isAdmin, Runnable onSave, Runnable onDiscard)
    {
        super();
        this.requestContext = requestContext;
        this.rest = rest;
        this.isAdmin = isAdmin;
        this.onSave = onSave;
        this.onDiscard = onDiscard;

        
        this.hPanel = createHorizonalPanel();
        add(hPanel);
        
        this.sPanel = createStackPanel();
        add(sPanel);
    }

    private Panel createHorizonalPanel()
    {
        HorizontalPanel hpanel = new HorizontalPanel();
        
        Button saveButton = new Button("save");
        saveButton.addClickHandler(new SaveRestClickHandler());
        saveButton.setEnabled(isAdmin);
        hpanel.add(saveButton);
       
        Button discardButton = new Button("discard");
        discardButton.addClickHandler(new DiscardRestClickHandler());
        discardButton.setEnabled(isAdmin);
        hpanel.add(discardButton);
        
        return hpanel;
    }
    
    private Panel createStackPanel()
    {
        StackPanel stackPanel = new StackPanel();

        // profile pannel
        Panel profilePanel = new ProfilePannel(rest, isAdmin);
        Panel menuPanel = createMenuPannel();
        Panel adminsPanel = createAdminPannel();
        Panel branchesPanel = createBranchesPanel();

        // TODO fix order
        stackPanel.add(profilePanel, "Profile");
        stackPanel.add(menuPanel, "Menu");
        stackPanel.add(adminsPanel, "Admins");
        stackPanel.add(branchesPanel, "Braches");

        // requestFactory.getUserCommonService().getRestaurant(0L); //TODO get id from somewhere

        return stackPanel;

    }
    
    private Panel createBranchesPanel()
    {
        List<RestaurantBranchProxy> branches = rest.getBranches();
        if (null == branches)
        {
            branches = new LinkedList<RestaurantBranchProxy>();
            rest.setBranches(branches);
        }
        BranchesFlexTable res = new BranchesFlexTable(requestContext, branches, isAdmin);
        return res;
    }
    
    private Panel createAdminPannel()
    {
        List<UserProxy> admins = rest.getAdmins();
        if (null == admins)
        {
            admins = new LinkedList<UserProxy>();
            rest.setAdmins(admins);
        }
        return new UsersPannel(admins, isAdmin);

    }

    private Panel createMenuPannel()
    {
        MenuProxy menuProxy = rest.getMenu();
        if (null == menuProxy)
        {
            menuProxy = requestContext.create(MenuProxy.class);
            rest.setMenu(menuProxy);
        }
        return new MenuFlexTable(requestContext, menuProxy, isAdmin);
    }
    
    
    class SaveRestClickHandler implements ClickHandler
    {
        @Override
        public void onClick(ClickEvent event)
        {
//            UserCommonServiceProxy service = RequestUtils.getRequestFactory().getUserCommonService();
//            requestContext.append(service);
//            service.saveRestaurant(rest).to(new AddRestaurantReciever());
//            requestContext.fire();
            if (null != onSave)
            {
                onSave.run();
            }
        }
    }

    class DiscardRestClickHandler implements ClickHandler
    {
        @Override
        public void onClick(ClickEvent event)
        {
            if (null != onDiscard)
            {
                onDiscard.run();
            }
        }
    }

//    class AddRestaurantReciever extends Receiver<Boolean>
//    {
//
//        @Override
//        public void onSuccess(Boolean response)
//        {
//            if (null != onSave)
//            {
//                onSave.run();
//            }
//        }
//
//        @Override
//        public void onFailure(ServerFailure error)
//        {
//            Window.alert("exception: " + error.getMessage());
//        }
//
//    }

}