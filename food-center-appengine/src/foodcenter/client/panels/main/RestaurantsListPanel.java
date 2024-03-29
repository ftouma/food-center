package foodcenter.client.panels.main;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import foodcenter.client.callbacks.OnClickServiceCheckBox;
import foodcenter.client.callbacks.PanelCallback;
import foodcenter.client.callbacks.RedrawablePanel;
import foodcenter.client.callbacks.SearchPanelCallback;
import foodcenter.client.callbacks.search.RestaurantSearchOptions;
import foodcenter.client.panels.common.EditableImage;
import foodcenter.service.enums.ServiceType;
import foodcenter.service.proxies.RestaurantProxy;
import foodcenter.service.requset.RestaurantAdminServiceRequest;

public class RestaurantsListPanel extends VerticalPanel implements RedrawablePanel
{
    private final static int COLUMN_IMAGE = 0;
    private final static int COLUMN_NAME = 1;
    private final static int COLUMN_DELIVERY = 2;
    private final static int COLUMN_TAKEAWAY = 3;
    private final static int COLUMN_TABLE = 4;
    private final static int COLUMN_NEW_BUTTON = 5;
    private final static int COLUMN_VIEW_BUTTON = 5;
    private final static int COLUMN_EDIT_BUTTON = 6;
    private final static int COLUMN_DELETE_BUTTON = 7;

    private final List<RestaurantProxy> rests;
    private final PanelCallback<RestaurantProxy, RestaurantAdminServiceRequest> callback;
    private final SearchPanelCallback<RestaurantSearchOptions> searchCallback;
    private final boolean isAdmin;

    private final RestaurantSearchOptions searchOptions;
    private final RestCallback restCallback;
    private final Panel optionsPanel;
    private final FlexTable restsTable;

    public RestaurantsListPanel(List<RestaurantProxy> rests,
                                PanelCallback<RestaurantProxy, RestaurantAdminServiceRequest> callback,
                                SearchPanelCallback<RestaurantSearchOptions> searchCallback)
    {
        this(rests, callback, searchCallback, false);
    }

    public RestaurantsListPanel(List<RestaurantProxy> rests,
                                PanelCallback<RestaurantProxy, RestaurantAdminServiceRequest> callback,
                                SearchPanelCallback<RestaurantSearchOptions> searchCallback,
                                boolean isAdmin)
    {
        super();

        this.rests = rests;
        this.callback = callback;
        this.searchCallback = searchCallback;
        this.isAdmin = isAdmin;

        searchOptions = new RestaurantSearchOptions();
        restCallback = new RestCallback();
        optionsPanel = createOptionsPannel();
        add(optionsPanel);

        restsTable = new FlexTable();
        add(restsTable);
        restsTable.setStyleName("one-column-emphasis");

        redraw();
    }

    @Override
    public void redraw()
    {
        // Clear all the rows of this table
        restsTable.removeAllRows();

        // Print the header of this table
        printRestaurantsTableHeader();

        // Print all the branches if exits
        int row = restsTable.getRowCount();
        if (null != rests)
        {
            for (RestaurantProxy rp : rests)
            {
                printRestaurantTableRow(rp, row);
                row++;
            }
        }
    }

    @Override
    public void close()
    {
        // This is not supported by this internal panel
        callback.error(this, null, "Not Supported!");
    }

    private void printRestaurantTableRow(RestaurantProxy rest, int row)
    {
        EditableImage img = new EditableImage(rest.getImageUrl());
        restsTable.setWidget(row, COLUMN_IMAGE, img);

        String name = rest.getName();
        restsTable.setText(row, COLUMN_NAME, name);

        String delivery = rest.getServices().contains(ServiceType.DELIVERY) ? "yes" : "no";
        restsTable.setText(row, COLUMN_DELIVERY, delivery);

        String takeAway = rest.getServices().contains(ServiceType.TAKE_AWAY) ? "yes" : "no";
        restsTable.setText(row, COLUMN_TAKEAWAY, takeAway);

        String table = rest.getServices().contains(ServiceType.TABLE) ? "yes" : "no";
        restsTable.setText(row, COLUMN_TABLE, table);

        Button view = new Button("View");
        view.addClickHandler(new OnClickViewRestaurant(rest));
        restsTable.setWidget(row, COLUMN_VIEW_BUTTON, view);

        if (rest.isEditable())
        {
            Button edit = new Button("Edit");
            edit.addClickHandler(new OnClickEditRestaurant(rest));
            restsTable.setWidget(row, COLUMN_EDIT_BUTTON, edit);

            if (isAdmin)
            {
                Button delete = new Button("Delete");
                delete.addClickHandler(new OnClickDeleteRestaurant(rest));
                restsTable.setWidget(row, COLUMN_DELETE_BUTTON, delete);
            }
        }
        
        restsTable.getRowFormatter().setStyleName(row, "td");
    }

    private void printRestaurantsTableHeader()
    {
        restsTable.setText(0, COLUMN_NAME, "Name");
        restsTable.setText(0, COLUMN_DELIVERY, "Delivery");
        restsTable.setText(0, COLUMN_TAKEAWAY, "Take Away");
        restsTable.setText(0, COLUMN_TABLE, "Table");
        
        if (isAdmin)
        {
            Button newButton = new Button("New");
            newButton.addClickHandler(new OnClickNewRestaurant());
            restsTable.setWidget(0, COLUMN_NEW_BUTTON, newButton);
        }
        restsTable.getRowFormatter().setStyleName(0, "th");
    }

    private Panel createOptionsPannel()
    {
        HorizontalPanel result = new HorizontalPanel();

        TextBox searchBox = new TextBox();
        searchBox.addKeyUpHandler(new SearchKeyUpHandler());
        result.add(searchBox);

        CheckBox delivery = createServiceCheckBox(ServiceType.DELIVERY);
        result.add(delivery);

        CheckBox takeAway = createServiceCheckBox(ServiceType.TAKE_AWAY);
        result.add(takeAway);

        CheckBox table = createServiceCheckBox(ServiceType.TABLE);
        result.add(table);

        Button searchButton = new Button("Search");
        searchButton.addClickHandler(new OnClickSearchRests());
        result.add(searchButton);

        return result;
    }

    private CheckBox createServiceCheckBox(ServiceType service)
    {
        CheckBox res = new CheckBox(service.getName());
        res.setValue(true);
        res.addClickHandler(new OnClickServiceCheckBox(searchOptions.getServices()));
        
        return res;
    }

    /* **************************************************************** */
    /* **************** private classes ********************* */

    private class RestCallback implements
                              PanelCallback<RestaurantProxy, RestaurantAdminServiceRequest>
    {

        @Override
        public void close(RedrawablePanel panel, RestaurantProxy proxy)
        {
            // super gets this panel, and not the requested panel
            if (null != panel)
            {
                panel.close();
            }
            RestaurantsListPanel.this.callback.close(RestaurantsListPanel.this, proxy);
        }

        @Override
        public void save(RedrawablePanel panel,
                         RestaurantProxy proxy,
                         PanelCallback<RestaurantProxy, RestaurantAdminServiceRequest> callback,
                         RestaurantAdminServiceRequest service)
        {
            close(panel, proxy);
            RestaurantsListPanel.this.callback.save(RestaurantsListPanel.this,
                                                    proxy,
                                                    callback,
                                                    service);
        }

        @Override
        public void view(RedrawablePanel panel,
                         RestaurantProxy proxy,
                         PanelCallback<RestaurantProxy, RestaurantAdminServiceRequest> callback)
        {
            close(panel, proxy);
            RestaurantsListPanel.this.callback.view(RestaurantsListPanel.this, proxy, callback);
        }

        @Override
        public void edit(RedrawablePanel panel,
                         RestaurantProxy proxy,
                         PanelCallback<RestaurantProxy, RestaurantAdminServiceRequest> callback)
        {
            close(panel, proxy);
            RestaurantsListPanel.this.callback.edit(RestaurantsListPanel.this, proxy, callback);
        }

        @Override
        public void
            createNew(RedrawablePanel panel,
                      PanelCallback<RestaurantProxy, RestaurantAdminServiceRequest> callback)
        {
            RestaurantsListPanel.this.callback.createNew(RestaurantsListPanel.this, callback);
        }

        @Override
        public void del(RedrawablePanel panel, RestaurantProxy proxy)
        {
            close(panel, proxy); // Close the edit panel
            RestaurantsListPanel.this.callback.del(RestaurantsListPanel.this, proxy);
        }

        @Override
        public void error(RedrawablePanel panel, RestaurantProxy proxy, String reason)
        {
            callback.error(panel, proxy, reason);
        }

    }

    /* ************************************************************************** */

    private class OnClickViewRestaurant implements ClickHandler
    {
        private final RestaurantProxy rest;

        public OnClickViewRestaurant(RestaurantProxy rest)
        {
            super();

            this.rest = rest;
        }

        @Override
        public void onClick(ClickEvent event)
        {
            callback.view(RestaurantsListPanel.this, rest, restCallback);
        }
    }

    /* ************************************************************************** */

    private class OnClickEditRestaurant implements ClickHandler
    {
        private final RestaurantProxy rest;

        public OnClickEditRestaurant(RestaurantProxy rest)
        {
            super();
            this.rest = rest;
        }

        @Override
        public void onClick(ClickEvent event)
        {
            callback.edit(RestaurantsListPanel.this, rest, restCallback);
        }
    }

    /* ************************************************************************** */

    private class OnClickDeleteRestaurant implements ClickHandler
    {
        private final RestaurantProxy rest;

        public OnClickDeleteRestaurant(RestaurantProxy rest)
        {
            this.rest = rest;
        }

        @Override
        public void onClick(ClickEvent event)
        {
            callback.del(RestaurantsListPanel.this, rest);
        }
    }

    /* ************************************************************************** */

    private class OnClickNewRestaurant implements ClickHandler
    {
        @Override
        public void onClick(ClickEvent event)
        {
            callback.createNew(RestaurantsListPanel.this, restCallback);
        }
    }

    /* ************************************************************************** */

    private class OnClickSearchRests implements ClickHandler
    {
        @Override
        public void onClick(ClickEvent event)
        {
            searchCallback.search(searchOptions);
        }
    }

    /* ************************************************************************** */

    private class SearchKeyUpHandler implements KeyUpHandler
    {
        @Override
        public void onKeyUp(KeyUpEvent event)
        {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
            {
                searchCallback.search(searchOptions);
            }
            TextBox tb = (TextBox)event.getSource();
            searchOptions.setPattern(tb.getText());
            
        }

    }

}
