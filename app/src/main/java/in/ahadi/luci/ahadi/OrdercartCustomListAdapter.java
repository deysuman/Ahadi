package in.ahadi.luci.ahadi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Luci on 3/26/2017.
 */
public class OrdercartCustomListAdapter extends BaseAdapter {
    Context context;
    private Activity activity;
    private LayoutInflater inflater;
    private List<Carts> cartiems;

    public OrdercartCustomListAdapter(Activity activity, List<Carts> cartItems) {
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
            convertView = inflater.inflate(R.layout.cartitems, null);


        TextView orderid = (TextView) convertView.findViewById(R.id.orderid);
        TextView orderdate = (TextView) convertView.findViewById(R.id.cartiddate);
        TextView orderstatus = (TextView) convertView.findViewById(R.id.process);


        // getting movie data for the row
        final Carts m = cartiems.get(position);


        orderid.setText(m.getTitle());


        orderdate.setText("Order date - " + m.getDate());

        orderstatus.setText("Order status -"+ m.getProcess());
/*

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getalert(String.valueOf(position));

                Intent i = new Intent(getBaseContext(), singleorder.class);
                i.putExtra("orderno",m.getTitle());
                i.putExtra("token",m.getToken());
                context.startActivity(i);




            }
        });
*/
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(v.getContext(),singleorder.class);
                i.putExtra("orderno",m.getTitle());
                i.putExtra("token",m.getToken());
                i.putExtra("process",m.getProcess());
                v.getContext().startActivity(i);

            }
        });




        return convertView;
    }

    public void  getalert(String getalertdata){
        AlertDialog alertDialog = new AlertDialog.Builder(
                context).create();
        alertDialog.setMessage(getalertdata);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();

    }
}
