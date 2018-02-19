package in.ahadi.luci.ahadi;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

/**
 * Created by Luci on 3/24/2017.
 */

public class OrderCustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Orders> movieItems;


    public OrderCustomListAdapter(Activity activity, List<Orders> movieItems) {
        this.activity = activity;
        this.movieItems = movieItems;
    }

    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public Object getItem(int location) {
        return movieItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.orderitems, null);


        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView quantity = (TextView) convertView.findViewById(R.id.quantity);
        TextView price = (TextView) convertView.findViewById(R.id.price);


        // getting movie data for the row
        Orders m = movieItems.get(position);



        String pname = m.getTitle().substring(0,1).toUpperCase() + m.getTitle().substring(1).toLowerCase();
        // title
        title.setText(pname);

        quantity.setText("Qantity: " + String.valueOf(m.getQantity()));


            price.setText("Price: " +String.valueOf(m.getPrice()));







        return convertView;
    }

}
