package in.ahadi.luci.ahadi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Luci on 3/6/2017.
 */

public class Settingsarrayadapter  extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public Settingsarrayadapter(Context context, String[] values) {
        super(context, R.layout.single_column, values);
        this.context = context;
        this.values = values;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.single_column, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.itemname);

        textView.setText(values[position]);




        return rowView;
    }
}
