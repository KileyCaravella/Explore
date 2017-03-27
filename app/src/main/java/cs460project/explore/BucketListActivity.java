package cs460project.explore;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by HARDY_NATH on 3/26/2017.
 * Current idea is to have the description text view change to have the particular
 * bucket list name displayed when viewing that category.
 * IE. click on mexican, text view says mexican while you view locations
 */

public class BucketListActivity extends Activity implements AdapterView.OnItemClickListener {


    private EditText dataEntry;
    private ListView listView;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    private int selectedItem;
    private String temp;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
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
        dataEntry.setText(realTemp);
    }//On Item Click

    //handles the user clicking on the various menu items
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){

            //adds the entry from the Edit Text box into the Array List
            case R.id.addCategory:
                temp = dataEntry.getText().toString();
                list.add(temp);
                adapter.notifyDataSetChanged();
                dataEntry.setHint("@string/bucket_list_edit_text_hint");
                return true;

            //updates the selected array list item from the edit text box
            case R.id.updateCategory:
                temp = dataEntry.getText().toString();
                String updateText;
                int tempInt, tempPos;
                tempInt = selectedItem+1;
                tempPos = selectedItem ;
                updateText = tempInt+". "+temp;
                list.remove(tempPos);
                list.add(tempPos, updateText);
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



    }//menu options


}//class
