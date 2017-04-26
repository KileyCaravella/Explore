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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import cs460project.explore.R;

/**
 * Created by HARDY_NATH on 3/26/2017.
 * Current idea is to have the description text view change to have the particular
 * bucket list name displayed when viewing that category.
 * IE. click on mexican, text view says mexican while you view locations. Will be implemented in sprint 3.
 * Class handles clicking on various elements, viewing, adding, updating, deleting categories
 */

public class BucketListActivity extends Activity implements AdapterView.OnItemClickListener {


    private EditText dataEntry;
    private ListView listView;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    private int selectedItem;
    private String temp;
    private Button ViewBtn, UpdateBtn, AddBtn;
    private ImageView mvpView;
    public String selectedCategory;
    private BroadcastReceiver receiver;
    private Notification notify;
    private NotificationManager notificationManager;
    private final String Burger="Burger", Mexican = "Mexican", Italian="Italian", Chinese="Chinese", Entertainment="Entertainment";

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_OPTIONS_PANEL);
        setTheme(R.style.BucketTheme);
        //setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);
        setContentView(R.layout.bucket_list);


        dataEntry = (EditText)findViewById(R.id.enterText);
        listView = (ListView)findViewById(R.id.bucketListView);

        list = new ArrayList<>();

        listView.setOnItemClickListener(this);
        //^attaches the listener to the listView

        //sets up the array adapter
        adapter = new ArrayAdapter<> (
                this,
                android.R.layout.simple_list_item_1,  //android supplid list item
                list);

        //connects the array adapter to the list view
        listView.setAdapter(adapter);  //connect the Array Adapter to the list view


        //assigns the buttons to the layout
        AddBtn = (Button)findViewById(R.id.newBtn);
        //AddBtn.setOnClickListener(this);
        ViewBtn = (Button)findViewById(R.id.viewBtn);
        UpdateBtn = (Button)findViewById(R.id.updateBtn);


        //adds a couple hard coded bucket list items to be displayed
        list.add(Burger);
        list.add(Mexican);
        list.add(Italian);
        list.add(Chinese);
        list.add(Entertainment);

        //Nathaniel
        //register and define the filter for the BroadCast Receiver
        IntentFilter mainIntentFilter = new IntentFilter("CategoryCreated");
        receiver = new mainReceiver();
        registerReceiver(receiver, mainIntentFilter);

        //Nathaniel
        //instanciate the notification manager
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //Nathaniel
        //create intent for action bar
        Intent notifyIntent = new Intent(this,BucketListActivity.class);
        //Create pending intent so that intent will work when notification selected
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    }//on create

    //Nathaniel: creates the options menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }


    //Nathaniel: handles the user clicking on the array list
    //takes the selected item and displays it in the edit text field for editing
    public void onItemClick(AdapterView<?> parent, View v, int position, long id){
        dataEntry.setText("");
        String realTemp = (listView.getItemAtPosition(position).toString());
        selectedItem = position;
        selectedCategory = realTemp;
        dataEntry.setText(realTemp);
    }//On Item Click

    // set

    public String getCategory(){
        return selectedCategory;
    }

    //Couldnt use OnClickListener since the list view is using an onItemClickListener
    //handles the user clicking the update button
    public void updateCategory(View view){
        String updateText = dataEntry.getText().toString();
        list.remove(selectedItem);
        list.add(selectedItem, updateText);
        adapter.notifyDataSetChanged();
        dataEntry.setHint("@string/bucket_list_edit_text_hint");

    }

    //handles creating new category
    public void newCategory(View view){
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
    }

    //handles viewing the category
    //Category View Activity creates custom list view, populates custom list view, creates adapter, sets adapter,

     public void viewCategory(View view){
         Log.i("Category View", "User Pressed Button to View a Catagory.");
         Intent viewCat = new Intent(BucketListActivity.this,CategoryViewActivity.class);
         startActivity(viewCat);
     }


    // handles the user clicking on the various menu items
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
/*
            //adds the entry from the Edit Text box into the Array List
            case R.id.addCategory:


            //updates the selected array list item from the edit text box
            case R.id.updateCategory:

*/
            //deletes the selected array list item
            case R.id.deleteCategory:
                list.remove(selectedItem);
                adapter.notifyDataSetChanged();
                dataEntry.setHint("@string/bucket_list_edit_text_hint");
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }



    }//menu options

    //Nathaniel
    //local receiver for Broadcast receiver
    public class mainReceiver extends BroadcastReceiver{
        public void onReceive(Context context, Intent intent){

            String name = intent.getStringExtra("categoryName");
            Log.i("Category Created", name);
        }//on receive

    }//main receiver

}//class
