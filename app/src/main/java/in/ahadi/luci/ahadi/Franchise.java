package in.ahadi.luci.ahadi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


/**
 * Created by Luci on 3/26/2017.
 */
public class Franchise extends BaseAdapter {
    Context context;
    private Activity activity;
    private LayoutInflater inflater;
    private List<FranchaiseModel> cartiems;

    public Franchise(Activity activity, List<FranchaiseModel> cartItems) {
        this.activity = activity;
        this.cartiems = cartItems;
    }

    @Override
    public int getCount() {
        return cartiems.size();
    }

    @Override
    public Object getItem(int location) {
        return cartiems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.raw_franchaise, null);

        // getting movie data for the row
        final FranchaiseModel m = cartiems.get(position);

        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(m.getName());


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + m.getMobile()));
                if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                activity.startActivity(intent);
            }
        });




        return convertView;
    }

}
