package foodcenter.android.activities.main;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import foodcenter.android.R;
import foodcenter.android.service.AndroidRequestUtils;

public class ActionBarDrawerAdapter extends BaseAdapter
{
    private Activity activity;

    private DisplayImageOptions options;

    public static final int PROFILE_POSITION = 0;
    public static final int HISTORY_POSITION = 1;
    public static final int COWORKERS_POSITION = 2;

    private String txt[] = { "Profile", "History", "Co-Workers" };
    private int imgs[] = { R.drawable.ic_person, R.drawable.ic_history, R.drawable.ic_group };

    public ActionBarDrawerAdapter(Activity activity, DisplayImageOptions options)
    {
        super();

        this.activity = activity;
        this.options = options;
        if (null == this.options)
        {
            this.options = AndroidRequestUtils.getDefaultDisplayImageOptions(activity);
        }

        txt[PROFILE_POSITION] = AndroidRequestUtils.getSharedPreferences(activity)
            .getString(AndroidRequestUtils.PREF_ACCOUNT_NAME, "Unknown Account");

    }

    @Override
    public void notifyDataSetChanged()
    {
        txt[PROFILE_POSITION] = AndroidRequestUtils.getSharedPreferences(activity)
            .getString(AndroidRequestUtils.PREF_ACCOUNT_NAME, "Unknown Account");
        
        super.notifyDataSetChanged();
    }
    @Override
    public int getCount()
    {
        return txt.length;
    }

    @Override
    public String getItem(int position)
    {
        if (position > getCount())
        {
            return null;
        }
        return txt[position];
    }

    // Require for structure, not really used in my code. Can
    // be used to get the id of an item in the adapter for
    // manual control.
    public long getItemId(int position)
    {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View view, ViewGroup parent)
    {
        if (view == null)
        {
            view = activity.getLayoutInflater()
                .inflate(R.layout.main_view_drawer_list_item, parent, false);
            
            // findViewById is expensive, reusing with ViewHolder
            ViewHolder holder = new ViewHolder();
            holder.img = (ImageView) view.findViewById(R.id.main_view_drawer_list_item_img);
            holder.txt = (TextView) view.findViewById(R.id.main_view_drawer_list_item_txt);
            view.setTag(holder);
        }
        
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.txt.setText(txt[position]);
        holder.img.setImageResource(imgs[position]);

        return view;
    }

    private class ViewHolder
    {
        private ImageView img;
        private TextView txt;
    }

}
