package foodcenter.android.activities.branch;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import foodcenter.android.ObjectStore;
import foodcenter.android.R;
import foodcenter.android.activities.coworkers.CoworkersActivity;
import foodcenter.android.activities.order.OrderActivity;
import foodcenter.android.activities.rest.RestActivity;
import foodcenter.android.data.MenuSavedState;
import foodcenter.android.data.OrderData;
import foodcenter.service.enums.ServiceType;
import foodcenter.service.proxies.RestaurantBranchProxy;

/**
 * Displays the branch menu and location dialogs. <br>
 * Allow user to make an order / table reservation.
 */
public class BranchActivity extends FragmentActivity
                                                    implements
                                                    BranchSwipeListViewTouchListener.OnSwipeCallback,
                                                    OnItemClickListener
{

    public final static int REQ_CODE_ORDER = 1;
    public final static String EXTRA_BRANCH_ID = "foodcenter.android.EXTRA_BRANCH_ID";

    private final static String TAG = BranchActivity.class.getSimpleName();

    private RestaurantBranchProxy branch = null;
    private String restId = null;
    private List<ServiceType> services = null;

    private ListView lv;

    private BranchMenuListAdapter adapter;

    /** Note that this may be null if the Google Play services APK is not available. */
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.branch_view);

        lv = (ListView) findViewById(R.id.branch_menu_list);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lv.setMultiChoiceModeListener(new ModeCallback());

        BranchSwipeListViewTouchListener touchListener = new BranchSwipeListViewTouchListener(lv,
                                                                                              this,
                                                                                              false,
                                                                                              false);
        lv.setOnTouchListener(touchListener);
        lv.setOnItemClickListener(this);

        initActionBar();
        handleIntent(getIntent());
    }

    /** Initialize the action bar of this activity */
    private void initActionBar()
    {
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        getActionBar().setSubtitle(getString(R.string.press_to_select));
    }

    @Override
    public void onSwipeLeft(ListView lv, int[] reverseSortedPositions)
    {
        int pos = reverseSortedPositions[0];

        if (null != adapter.getItem(pos))
        {
            boolean newState = adapter.decreaseCounter(pos);
            lv.setItemChecked(pos, newState);
        }
    }

    @Override
    public void onSwipeRight(ListView lv, int[] reverseSortedPositions)
    {
        int pos = reverseSortedPositions[0];
        if (null != adapter.getItem(pos))
        {
            adapter.increaseCounter(pos);
            lv.setItemChecked(pos, true);
        }
    }

    
    @Override
    public Object onRetainCustomNonConfigurationInstance()
    {
        final MenuSavedState savedState = adapter.getSavedState();
        return savedState;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if (null == branch)
        {
            return true;
        }

        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.branch_menu, menu);

        boolean isTable = services.contains(ServiceType.TABLE);
        menu.findItem(R.id.branch_menu_table).setVisible(isTable);
        return true;
    }

    @Override
    public void setTitle(CharSequence title)
    {
        super.setTitle(title);
        getActionBar().setTitle(title);
    }

    public void onClickActionModeDoneButton(View view)
    {
        adapter.clearCounters();
    }

    @Override
    public void onBackPressed()
    {
        adapter.clearCounters();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.branch_menu_table:
                Intent i = new Intent(this, CoworkersActivity.class);
                i.putExtra(EXTRA_BRANCH_ID, branch.getId());
                i.putExtra(RestActivity.EXTRA_REST_ID, restId);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id)
    {
        onSwipeRight(lv, new int[] { position });

    }

    /**
     * Opens the order verification activity
     * 
     * @param service - is the type of the order
     * @see {@link ServiceType} - for available service types
     */
    private void openOrderVerification(ServiceType service)
    {
        Intent intent = new Intent(this, OrderActivity.class);

        // restId received from intent
        OrderData data = adapter.getOrderConfData(service);
        data.setRestBranchId(branch.getId());
        data.setRestId(restId);
        ObjectStore.put(OrderData.class, OrderData.CACHE_KEY, data);

        startActivity(intent);
    }

    /**
     * handle the intent used to create this activity.<br>
     * parse branchId and restId from this intent.
     * 
     * @param intent
     */
    private void handleIntent(Intent intent)
    {
        // Get the ids from the intent
        String branchId = intent.getExtras().getString(EXTRA_BRANCH_ID);
        restId = intent.getExtras().getString(RestActivity.EXTRA_REST_ID);

        if (null == branchId || null == restId)
        {
            setTitle("Can't find branch id");
            Log.e(TAG, "Can't find branch id - null");
            return;
        }

        // Get
        branch = ObjectStore.get(RestaurantBranchProxy.class, branchId);
        if (null == branch)
        {
            setTitle("Can't find branch");
            Log.e(TAG, "Can't find branch id in ObjectStore: " + branchId);
            return;
        }

        services = branch.getServices();
        if (null == services)
        {
            services = new ArrayList<ServiceType>();
        }

        MenuSavedState savedState = (MenuSavedState) getLastCustomNonConfigurationInstance();
        adapter = new BranchMenuListAdapter(this, branch.getMenu(), savedState);
        lv.setAdapter(adapter);

        TextView phone = (TextView) findViewById(R.id.branch_drawer_phone);
        phone.setText(branch.getPhone());

        setUpMapIfNeeded();

    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded()
    {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null)
        {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

            // Check if we were successful in obtaining the map.
            if (mMap != null)
            {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap()
    {
        UiSettings mUiSettings = mMap.getUiSettings();
        mUiSettings.setAllGesturesEnabled(false);

        LatLng l = new LatLng(branch.getLat(), branch.getLng());
        Marker marker = mMap.addMarker(new MarkerOptions().position(l).title(branch.getAddress()));
        marker.showInfoWindow();

        CameraUpdate center = CameraUpdateFactory.newLatLng(l);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(13);
        mMap.moveCamera(center);
        mMap.moveCamera(zoom);

    }

    /* ***************************************************************************************** */
    /* ***************************************************************************************** */
    /* ***************************************************************************************** */

    /**
     * Callback for the ActionMode.<br>
     * (After selecting an item in the list, ActionMode is used instead of ActionBar)
     */
    private class ModeCallback implements ListView.MultiChoiceModeListener
    {
        private final DecimalFormat df = new DecimalFormat("#.0");

        @Override
        public boolean onCreateActionMode(final ActionMode mode, Menu menu)
        {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.branch_course_list_select_menu, menu);

            menu.findItem(android.R.id.home);
            boolean isTakeAway = services.contains(ServiceType.TAKE_AWAY);
            menu.findItem(R.id.branch_menu_takeaway).setVisible(isTakeAway);

            boolean isDelivery = services.contains(ServiceType.DELIVERY);
            menu.findItem(R.id.branch_menu_delivery).setVisible(isDelivery);

            mode.setTitle("Select Items");
            showTotalPrice(mode);

            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode)
        {
            adapter.clearCounters();
        }
        
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu)
        {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item)
        {
            switch (item.getItemId())
            {
                case R.id.branch_view_list_item:
                    // mode.finish();
                    break;
                case R.id.branch_menu_delivery:
                    // mode.finish();
                    openOrderVerification(ServiceType.DELIVERY);
                    break;
                case R.id.branch_menu_takeaway:
                    // mode.finish();
                    openOrderVerification(ServiceType.TAKE_AWAY);
                    break;
                default:
                    break;
            }
            return true;
        }

        @Override
        public void onItemCheckedStateChanged(ActionMode mode,
                                              int position,
                                              long id,
                                              boolean checked)
        {

            // Select & add 1 to counter
            if (checked && 0 == adapter.getCounter(position))
            {
                adapter.increaseCounter(position);
            }
            else if (!checked)
            {
                adapter.clearCounter(position);
            }

            // Update the price!
            showTotalPrice(mode);

        }

        /** Shows the total price in the action mode subtitle */
        private void showTotalPrice(ActionMode mode)
        {
            String s = getString(R.string.total_price) + " " + df.format(adapter.getTotalPrice());

            // Show total price on action bar
            mode.setSubtitle(s);
        }
    }    
}
