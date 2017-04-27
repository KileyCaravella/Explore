package cs460project.explore.Category;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import cs460project.explore.R;

/**
 * This is the Bucket List Activity. Here, the user can view a list of their saved categories. They can
 * add or delete the categories, and view the businesses inside of each category by clicking "View"
 */

public class BucketListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //MARK: - Private Variables

    private EditText dataEntry;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private int selectedItem;
    public String selectedCategory;
    private BroadcastReceiver receiver;
    private Notification notify;
    private NotificationManager notificationManager;
    private ImageView loadingIndicatorImageView;
    private AnimationDrawable frameAnimation;
    private View loadingIndicatorBackgroundView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bucket_list);

        setupVariables();
        setupArrayAdapter();
        setupBroadcastReceiverAndNotifications();
        setupActionBarIntent();
        setupLoadingIndicator();
    }

    //MARK: - Setup

    private void setupVariables() {
        dataEntry = (EditText) findViewById(R.id.enterText);
        listView = (ListView) findViewById(R.id.bucketListView);
        listView.setOnItemClickListener(this);
    }

    private void setupArrayAdapter() {
        //Array adapter created and setup with list view.
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, CategoryClient.sharedInstance.categoriesList);
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
        Intent notifyIntent = new Intent(this, BucketListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    /* This function sets up the loading indicator, its animation, and its background. This code is copied
    throughout all views that have a loading indicator. */
    private void setupLoadingIndicator() {
        loadingIndicatorImageView = (ImageView) findViewById(R.id.animation);
        loadingIndicatorImageView.setBackgroundResource(R.drawable.animation);
        loadingIndicatorImageView.setVisibility(View.INVISIBLE);

        loadingIndicatorBackgroundView = findViewById(R.id.animationBackground);
        loadingIndicatorBackgroundView.setVisibility(View.INVISIBLE);

        frameAnimation = (AnimationDrawable) loadingIndicatorImageView.getBackground();
    }

    //MARK: - Options Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    //MARK: - Adapter View

    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
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

    public void deleteCategory(View view) {
        String categoryName = dataEntry.getText().toString();
        removeCategory(categoryName);
    }

    public void newCategory(View view) {
        final String categoryName = dataEntry.getText().toString();
        addCategory(categoryName);
    }

    public void viewCategory(View view) {
        Log.i("Category View", "User Pressed Button to View a Catagory.");
        Intent viewCat = new Intent(BucketListActivity.this, CategoryViewActivity.class);
        startActivity(viewCat);
    }

    //TODO: - Need to discuss this. Doesn't really work with actionBar hidden / buttons on screen
    public boolean onOptionsItemSelected(MenuItem item) {

        System.out.println(item.getItemId());
        String updateText = dataEntry.getText().toString();

        switch (item.getItemId()) {
            //adds the entry from the Edit Text box into the Array List
            case R.id.addCategory:
                addCategory(updateText);
                return true;

            //updates the selected array list item from the edit text box
            case R.id.updateCategory:
                String oldName = (listView.getItemAtPosition(selectedItem).toString());
                String newName = dataEntry.getText().toString();
                updateCategory(oldName, newName);
                return true;

            //deletes the selected array list item
            case R.id.deleteCategory:
                removeCategory(updateText);
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
        public void onReceive(Context context, Intent intent) {
            String name = intent.getStringExtra("categoryName");
            Log.i("Category Created", name);
        }
    }

    //MARK: - Client Calls

    private void addCategory(final String categoryName) {

        //Starting the animation for the loading indicator and dismissing the keyboard
        dismissKeyboard();
        toggleLoadingIndicator(true);

        //Client call
        CategoryClient.sharedInstance.createNewCategory(categoryName, new CategoryClient.CompletionListenerWithArray() {
            @Override
            public void onSuccessful(ArrayList<String> arrayList) {
                toggleLoadingIndicator(false);
                createNotification(categoryName);
                updateList(arrayList);
            }

            @Override
            public void onFailed(String reason) {
                toggleLoadingIndicator(false);
                Log.i("Failure", reason);
                failedToAddNewCategory();
            }
        });
    }

    private void updateCategory(String oldName, String newName) {
        //Starting the animation for the loading indicator and dismissing the keyboard
        dismissKeyboard();
        toggleLoadingIndicator(true);

        //Client call
        CategoryClient.sharedInstance.updateCategoryName(oldName, newName, new CategoryClient.CompletionListenerWithArray() {
            @Override
            public void onSuccessful(ArrayList<String> arrayList) {
                toggleLoadingIndicator(false);
                updateList(arrayList);
            }

            @Override
            public void onFailed(String reason) {
                toggleLoadingIndicator(false);
                Log.i("Failure", reason);
                failedToUpdateCategory();
            }
        });
    }

    private void removeCategory(String categoryName) {

        //Starting the animation for the loading indicator and dismissing the keyboard
        dismissKeyboard();
        toggleLoadingIndicator(true);

        //Client call
        CategoryClient.sharedInstance.removeCategory(categoryName, new CategoryClient.CompletionListenerWithArray() {
            @Override
            public void onSuccessful(ArrayList<String> arrayList) {
                toggleLoadingIndicator(false);
                updateList(arrayList);
            }

            @Override
            public void onFailed(String reason) {
                toggleLoadingIndicator(false);
                Log.i("Failure", reason);
                failedToRemoveCategory();
            }
        });
    }

    //MARK: - Toggling Methods

    private void updateList(ArrayList<String> arrayList) {
        CategoryClient.sharedInstance.categoriesList.removeAll(CategoryClient.sharedInstance.categoriesList);
        CategoryClient.sharedInstance.categoriesList.addAll(arrayList);
        adapter.notifyDataSetChanged();
        dataEntry.setHint("@string/bucket_list_edit_text_hint");
    }

    private void toggleLoadingIndicator(Boolean makeVisible) {
        if (makeVisible) {
            loadingIndicatorImageView.setVisibility(View.VISIBLE);
            loadingIndicatorBackgroundView.setVisibility(View.VISIBLE);
            frameAnimation.start();
        } else {
            loadingIndicatorImageView.setVisibility(View.INVISIBLE);
            loadingIndicatorBackgroundView.setVisibility(View.INVISIBLE);
            frameAnimation.stop();
        }
    }

    private void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(dataEntry.getWindowToken(), 0);
    }

    //MARK: - Toasts

    private void failedToAddNewCategory() {
        Toast.makeText(this, "Error Adding Category", Toast.LENGTH_LONG).show();
    }

    private void failedToUpdateCategory() {
        Toast.makeText(this, "Error Updating Category", Toast.LENGTH_LONG).show();
    }

    private void failedToRemoveCategory() {
        Toast.makeText(this, "Error Removing Category", Toast.LENGTH_LONG).show();
    }
}
