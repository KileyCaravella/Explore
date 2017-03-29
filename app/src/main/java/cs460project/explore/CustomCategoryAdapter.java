package cs460project.explore;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by HARDY_NATH on 3/29/2017.
 * Class is here simply to be used to fill the array list items to display what is stored
 * in the bucket list categories
 */
/*
public class CustomCategoryAdapter extends ArrayAdapter<CustomListView> {

    int resource;
    String response;
    Context context;




    public CustomCategoryAdapter(Context context, int resource, List<CustomListView> items){
        super(context, resource, items);
        this.resource=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LinearLayout locationView;
        CustomListView custListView = getItem(position);
        if(convertView==null) {
            locationView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater inflate;
            inflate = (LayoutInflater)getContext().getSystemService(inflater);
            inflate.inflate(resource, locationView, true);
        } else {
            locationView = (LinearLayout) convertView;
        }
        TextView locationName =(TextView)locationView.findViewById(R.id.locationNameView);
        TextView locationAddress = (TextView) locationView.findViewById(R.id.locationAddressView);
        ImageView locationPic = (ImageView) locationView.findViewById(R.id.locationPicView);


        locationName.setText(custListView.getLocationName());
        locationAddress.setText(custListView.getAddress());

        Picasso.with(context)
                .load(custListView.getImageURL())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .fit()
                .centerInside()
                .into(locationPic);



        return locationView;
    }

}
*/
