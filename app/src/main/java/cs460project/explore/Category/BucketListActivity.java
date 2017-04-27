package cs460project.explore.Category;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cs460project.explore.Category.CategoryViewActivity;
import cs460project.explore.R;

/**
 * This is the Bucket List Activity. Here, the user can view a list of their saved categories. They can
 * add or delete the categories, and view the businesses inside of each category by clicking "View"
 */

public class BucketListActivity extends Activity implements AdapterView.OnItemClickListener {

    //MARK: - Private Variables

    private EditText dataEntry;
    private ListView listView;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    private int selectedItem;
    public String selectedCategory;
    private BroadcastReceiver receiver;
    private Notification notify;
    private NotificationManager notificationManager;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bucket_list);

        setupIntent();
        setupVariables();
        setupArrayAdapter();
        setupBroadcastReceiverAndNotifications();
        setupActionBarIntent();
    }

    //MARK: - Setup

    private void setupIntent() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            list = extras.getStringArrayList("Categories");
        } else {
            Log.e("Error Yelp Business", "Did not receive yelpBusiness");
            list = new ArrayList<>();
        }
    }

    private void setupVariables() {
        dataEntry = (EditText)findViewById(R.id.enterText);
        listView = (ListView)findViewById(R.id.bucketListView);
        listView.setOnItemClickListener(this);
    }

    private void setupArrayAdapter() {
        //Array adapter created and setup with list view.
        adapter = new ArrayAdapter<> (this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
    }

    private void setupBroadcastReceiverAndNotifications() {
        //Register and define the filter for the BroadCast Receiver
        receiver = new mainReceiver();
        IntentFilter mainIntentFilter = new IntentFilter("CategoryCreated");
        registerReceiver(receiver, mainIntentFilter);

        //Instantiate the notification manager
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private void setupActionBarIntent() {
        //Creating intenet for action bar and for when a notification is selected
        Intent notifyIntent = new Intent(this,BucketListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    //MARK: - Options Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    //MARK: - Adapter View

    public void onItemClick(AdapterView<?> parent, View v, int position, long id){
        dataEntry.setText("");
        String realTemp = (listView.getItemAtPosition(position).toString());
        selectedItem = position;
        selectedCategory = realTemp;
        dataEntry.setText(realTemp);
    }

    //MARK: - OnClick Functions

    public String getCategory() {
        return selectedCategory;
    }

    public void deleteCategory(View view){
        //TODO: - Connect delete category code here.

        list.remove(selectedItem);
        adapter.notifyDataSetChanged();
        dataEntry.setHint("@string/bucket_list_edit_text_hint");
    }

    public void newCategory(View view){
        //TODO: - Include call to backend to create a new category... then call get categories again.
        String categoryName = dataEntry.getText().toString();
        createNotification(categoryName);

        //TODO: - Maybe have backend send back new list of categories??
        list.add(categoryName);
        adapter.notifyDataSetChanged();
        dataEntry.setHint("@string/bucket_list_edit_text_hint");

        //instanciates the notification
        notify = new Notification.Builder(this)
                .setContentTitle("Category "+updateText+" created")
                .setSmallIcon(R.drawable.notification_icon)
                .build();

        //calls the notification
        notificationManager.notify(1,
                notify);

        //Nathaniel
        //creates the intent for the broadcast receiver and sends it
        Intent senderIntent = new Intent("CategoryCreated");
        senderIntent.putExtra("categoryName", updateText);
        sendBroadcast(senderIntent);
    }

    //handles viewing the category
    //Category View Activity creates custom list view, populates custom list view, creates adapter, sets adapter,

    public void viewCategory(View view){
        Log.i("Category View", "User Pressed Button to View a Catagory.");
        Intent viewCat = new Intent(BucketListActivity.this,CategoryViewActivity.class);
        startActivity(viewCat);
    }

    //TODO: - Need to discuss this. Doesn't really work with actionBar hidden / buttons on screen
    public boolean onOptionsItemSelected(MenuItem item){

        System.out.println(item.getItemId());
        switch (item.getItemId()){


            //adds the entry from the Edit Text box into the Array List
            case R.id.addCategory:
                        String updateText = dataEntry.getText().toString();
                        list.add(updateText);
                        adapter.notifyDataSetChanged();
                        dataEntry.setHint("@string/bucket_list_edit_text_hint");

        //instanciates the notification
        notify = new Notification.Builder(this)
                .setContentTitle("Category "+updateText+" created")
                .setSmallIcon(R.drawable.notification_icon)
                .build();

        //calls the notification
        notificationManager.notify(1,
                notify);

        //Nathaniel
        //creates the intent for the broadcast receiver and sends it
        Intent senderIntent = new Intent("CategoryCreated");
        senderIntent.putExtra("categoryName", updateText);
        sendBroadcast(senderIntent);
                return true;
            //updates the selected array list item from the edit text box
            case R.id.updateCategory:
                 updateText = dataEntry.getText().toString();
                list.remove(selectedItem);
                list.add(selectedItem, updateText);
                adapter.notifyDataSetChanged();
                dataEntry.setHint("@string/bucket_list_edit_text_hint");
                return true;

            //deletes the selected array list item
            case R.id.deleteCategory:
                list.remove(selectedItem);
                adapter.notifyDataSetChanged();
                dataEntry.setHint("@string/bucket_list_edit_text_hint");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //MARK: - Notification

    private void createNotification(String categoryName) {
        //Instantiates notification
        notify = new Notification.Builder(this)
                .setContentTitle("Category " + categoryName + " created")
                .setSmallIcon(R.drawable.notification_icon)
                .build();

        //Calls notification
        notificationManager.notify(1, notify);

        //Sends broadcast for notification
        Intent senderIntent = new Intent("CategoryCreated");
        senderIntent.putExtra("categoryName", categoryName);
        sendBroadcast(senderIntent);
    }

    //MARK: - Broadcast Receiver Extension

    public class mainReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent){
            String name = intent.getStringExtra("categoryName");
            Log.i("Category Created", name);
        }
    }
}
