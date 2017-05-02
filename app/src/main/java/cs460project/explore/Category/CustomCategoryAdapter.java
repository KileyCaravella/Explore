package cs460project.explore.Category;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import cs460project.explore.R;

/**
 * Created by HARDY_NATH on 3/29/2017.
 * Class is here simply to be used to fill the array list items to display what is stored
 * in the bucket list categories
 */

public class CustomCategoryAdapter extends ArrayAdapter<CustomListView> {

    int resource;
    String response;
    Context context;
    private Typeface customTypeFace;
    private TextView locationName, locationDistance;
    private ImageView locationPic;


    public CustomCategoryAdapter(Context context, int resource, List<CustomListView> items){
        super(context, resource, items);
        this.resource = resource;
        this.context = context;
        //initialize the typeface
        this.customTypeFace = Typeface.createFromAsset(context.getAssets(),
                "fonts/DroidSans.ttf");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout locationView;
        CustomListView customListView = getItem(position);

        if (convertView == null) {
            locationView = new LinearLayout(getContext());

            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater inflate = (LayoutInflater) getContext().getSystemService(inflater);
            inflate.inflate(resource, locationView, true);

        } else {
            locationView = (LinearLayout) convertView;
        }

        //Setup functions called
        setupVariablesForView(locationView);
        setTextForTextView(locationName, customListView.getLocationName());

        String distance = String.format(Locale.US, "%.2f",customListView.getDistance()) + " miles";
        setTextForTextView(locationDistance, distance);

        setImageForImageView(locationPic, customListView.getImageURL());

        return locationView;
    }

    //MARK: - Setup

    private void setupVariablesForView(View locationView) {
        locationName = (TextView) locationView.findViewById(R.id.locationNameView);
        locationDistance = (TextView) locationView.findViewById(R.id.locationDistanceView);
        locationPic = (ImageView) locationView.findViewById(R.id.locationPicView);

        setTypefaceForTextView(locationName);
        setTypefaceForTextView(locationDistance);
    }

    private void setTypefaceForTextView(TextView textView) {
        textView.setTypeface(customTypeFace);
    }

    private void setTextForTextView(TextView textView, String text) {
        textView.setText(text);
    }

    //sets the picture for the list view layout item
    private void setImageForImageView(ImageView imageView, String imageURL) {
        Picasso.with(context)
                .load(imageURL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .fit()
                .centerInside()
                .into(imageView);
    }

}

