package foodcenter.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import foodcenter.client.handlers.AddMsgHandler;
import foodcenter.client.handlers.RemoveMsgHandler;
import foodcenter.client.service.RequestUtils;
import foodcenter.service.FoodCenterRequestFactory;
import foodcenter.service.common.LoginInfoProxy;
import foodcenter.service.common.RestaurantBaseInfoProxy;
import foodcenter.service.msg.MsgProxy;

public class FoodCenterImp implements EntryPoint, FoodCenter
{

	private static final String GWT_MENU_CONTAINER = "gwtMenuContainer";
	private static final String GWT_CONTINER = "gwtContainer";
	private FoodCenterRequestFactory requestFactory = new RequestUtils().getRequestFactory();

	/**************************************************************************
	 * Data Objects
	 **************************************************************************/
	private ArrayList<RestaurantBaseInfoProxy> restaurants = new ArrayList<RestaurantBaseInfoProxy>(); 
	
	/**************************************************************************
	 * Panels																 	
	 **************************************************************************/
	private StackPanel mainStackPanel = new StackPanel();
	
	// restaurants pannel
	private VerticalPanel restVerticalPanel = new VerticalPanel();
	private HorizontalPanel searchRestHorizonalPanel = new HorizontalPanel();
	private TextBox searchRestTextBox = new TextBox();
	private CheckBox searchRestDeliveryCheckBox = new CheckBox("delivery");
	private CheckBox searchRestTakeAwayCheckBox = new CheckBox("take away");
	private CheckBox searchRestTableCheckBox = new CheckBox("table");
	private Button searchRestButton = new Button("?");
	private FlexTable restaurantsTable = new FlexTable();
	
	// companies pannel
	private VerticalPanel compVerticalPanel = new VerticalPanel();
	private HorizontalPanel searchCompHorizonalPanel = new HorizontalPanel();
	private TextBox searchCompTextBox = new TextBox();
	private CheckBox searchCompDeliveryCheckBox = new CheckBox("delivery");
	private CheckBox searchCompTakeAwayCheckBox = new CheckBox("take away");
	private CheckBox searchCompTableCheckBox = new CheckBox("table");
	private Button searchCompButton = new Button("?");
	private FlexTable companiesTable = new FlexTable();
	
	
	
	// private static final int REFRESH_INTERVAL = 5000; // ms
	private VerticalPanel verticalMainPanel = new VerticalPanel();
	private FlexTable msgFlexTable = new FlexTable();

	// private HorizontalPanel horizonalHelloePanel = new HorizontalPanel();

	private HorizontalPanel horizonalAddMsgPanel = new HorizontalPanel();
	private TextBox newMsgTextBox = new TextBox();
	private Button addMsgButton = new Button("Add");

	private ArrayList<String> msgs = new ArrayList<String>();


	/**
	 * Entry point method.
	 */
	@Override
	public void onModuleLoad()
	{
		mainStackPanel.setWidth("95%");

		// restaurants load
		searchRestDeliveryCheckBox.setValue(true);
		searchRestTakeAwayCheckBox.setValue(true);
		searchRestTableCheckBox.setValue(true);
		
		searchRestHorizonalPanel.add(searchRestTextBox);
		searchRestHorizonalPanel.add(searchRestDeliveryCheckBox);
		searchRestHorizonalPanel.add(searchRestTakeAwayCheckBox);
		searchRestHorizonalPanel.add(searchRestTableCheckBox);
		searchRestHorizonalPanel.add(searchRestButton);
		
		restVerticalPanel.add(searchRestHorizonalPanel);
		restVerticalPanel.add(restaurantsTable);
		
		mainStackPanel.add(restVerticalPanel, "Restaurants");
		
		//companies load 
		//TODO companies were copied from restaurants just to understand how it is going to look like
		searchCompDeliveryCheckBox.setValue(true);
		searchCompTakeAwayCheckBox.setValue(true);
		searchCompTableCheckBox.setValue(true);
		
		searchCompHorizonalPanel.add(searchCompTextBox);
		searchCompHorizonalPanel.add(searchCompDeliveryCheckBox);
		searchCompHorizonalPanel.add(searchCompTakeAwayCheckBox);
		searchCompHorizonalPanel.add(searchCompTableCheckBox);
		searchCompHorizonalPanel.add(searchCompButton);
		
		compVerticalPanel.add(searchCompHorizonalPanel);
		compVerticalPanel.add(companiesTable);
		
		mainStackPanel.add(compVerticalPanel, "Companies");
		
		
		RootPanel.get(FoodCenterImp.GWT_CONTINER).add(mainStackPanel);
		
		
		
		// deprecated
		// Assemble Add Msg panel.
		horizonalAddMsgPanel.add(newMsgTextBox);
		horizonalAddMsgPanel.add(addMsgButton);

		// Assemble Main panel.
		verticalMainPanel.add(msgFlexTable);
		verticalMainPanel.add(horizonalAddMsgPanel);

		// Associate the Main panel with the HTML host page.
		RootPanel.get(FoodCenterImp.GWT_MENU_CONTAINER).add(getMenu());
		RootPanel.get(FoodCenterImp.GWT_CONTINER).add(verticalMainPanel);

		// Move cursor focus to the input box.
		newMsgTextBox.setFocus(true);

		// Listen for mouse events on the Add button.
		addMsgButton.addClickHandler(new AddMsgHandler(this));
		newMsgTextBox.addKeyPressHandler(new AddMsgHandler(this));

		// Create table for msgs in the db.
		updateMsgsTableFromDb();
		// Window.Location.assign("test");

		// Assemble the Hellow panel
		requestFactory.getLoginService().getLoginInfo().fire(new LoginInfoReciever(this));

	}

	
	public void getDefaultRestaurantsFromRPC()
	{

		restaurantsTable.removeAllRows();
		msgs.clear();
		msgFlexTable.setText(0, 0, "Msg");
		msgFlexTable.setText(0, 1, "Remove");

		requestFactory.msgService().getMsgs().fire(new Receiver<List<MsgProxy>>()
		{
			@Override
			public void onSuccess(List<MsgProxy> response)
			{
				for (MsgProxy m : response)
				{
					addMsgToTable(m.getMsg());
				}

			}

			@Override
			public void onFailure(ServerFailure error)
			{
				Window.alert("[FAIL] service connection error: " + error.getMessage());
			}
		});

	}

	
	@Override
	public void setHeader(String header)
	{
		HTML h = new HTML(header);
		verticalMainPanel.add(h);
	}
	
	/**
	 * Add msg to FlexTable. Executed when the user clicks the addMsgButton or
	 * presses enter in the newMsgTextBox.
	 */

	@Override
	public void updateMsgsTableFromDb()
	{

		msgFlexTable.removeAllRows();
		msgs.clear();
		msgFlexTable.setText(0, 0, "Msg");
		msgFlexTable.setText(0, 1, "Remove");

		requestFactory.msgService().getMsgs().fire(new Receiver<List<MsgProxy>>()
		{
			@Override
			public void onSuccess(List<MsgProxy> response)
			{
				for (MsgProxy m : response)
				{
					addMsgToTable(m.getMsg());
				}

			}

			@Override
			public void onFailure(ServerFailure error)
			{
				Window.alert("[FAIL] service connection error: " + error.getMessage());
			}
		});

	}

	/**
	 * adds the msg to the table, doesn't update/use any service.
	 * 
	 * @param msg is the msg to add.
	 */
	@Override
	public void addMsgToTable(String msg)
	{
		// Add the msg to the list, so user can't add it again.
		msgs.add(msg);

		int row = msgFlexTable.getRowCount();

		// Add a button to remove this stock from the table.
		Button removeMsgButton = new Button("remove msg");
		removeMsgButton.addClickHandler(new RemoveMsgHandler(this, msg));

		msgFlexTable.setText(row, 0, msg);
		msgFlexTable.setWidget(row, 1, removeMsgButton);
	}

	/**
	 * deletes the msg from the table, doesn't update/use any service.
	 * 
	 * @param msg is the msg to delete.
	 */
	@Override
	public void deleteMsgFromTable(String msg)
	{
		int removedIndex = msgs.indexOf(msg);
		msgs.remove(removedIndex);
		msgFlexTable.removeRow(removedIndex + 1);
	}

	@Override
	public final List<String> getMsgs()
	{
		return msgs;
	}

	@Override
	public TextBox getMsgTextBox()
	{
		return newMsgTextBox;
	}

	private MenuBar getMenu()
	{
		// Create a menu bar
		MenuBar menu = new MenuBar();
		menu.setAutoOpen(true);
		menu.setWidth("100px");
		menu.setAnimationEnabled(true);

		// Create the profile menu
		MenuBar profile = new MenuBar();
		profile.setAnimationEnabled(true);

		profile.addItem("My Profile", new Command()
		{
			@Override
			public void execute()
			{
				Window.Location.assign("/profile.jsp");
			}
		});
		
		profile.addItem("Logout", new Command()
		{
			@Override
			public void execute()
			{
				Window.Location.assign("/user_profile.jsp");
			}
		});

		menu.addItem(new MenuItem("Profile", profile));
		menu.addItem(new MenuItem("Profile2", profile));
		return menu;
	}
}

class LoginInfoReciever extends Receiver<LoginInfoProxy>
{

	private final FoodCenter module;

	public LoginInfoReciever(FoodCenter module)
	{
		this.module = module;
	}

	@Override
	public void onSuccess(LoginInfoProxy response)
	{
		StringBuilder builder = new StringBuilder();
		builder.append("hellow ");
		builder.append(response.getNickName());
		builder.append(" <a href=\"");
		builder.append(response.getLogoutUrl());
		builder.append("\">logout</a>");
		module.setHeader(builder.toString());
	}

	@Override
	public void onFailure(ServerFailure error)
	{
		Window.alert("[FAIL] service connection error: " + error.getMessage());
	}
}