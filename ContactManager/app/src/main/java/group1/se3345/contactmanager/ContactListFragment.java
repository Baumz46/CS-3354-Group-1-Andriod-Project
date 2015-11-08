package group1.se3345.contactmanager;

import android.app.Activity;
import android.app.ListFragment;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by Sharad on 11/6/15.
 */
public class ContactListFragment extends ListFragment {

    public interface ContactListFragmentListener
    {
        public void onContactSelected(long rowID);

        public void onAddContact();
    }

    private ContactListFragmentListener listener;

    private ListView contactListView;
    private CursorAdapter contactAdapter;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        listener = (ContactListFragmentListener) activity;
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        setEmptyText(getResources().getString(R.string.no_contacts));

        contactListView = getListView();
        contactListView.setOnItemClickListener(viewContactListener);
        contactListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //map
        String[] from = new String[]{"name"};
        int[] to = new int[]{android.R.id.text1};
        contactAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_1, null, from, to, 0);
        setListAdapter(contactAdapter);
    }

    AdapterView.OnItemClickListener viewContactListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            listener.onContactSelected(id);
        }
    };

    @Override
    public void onResume()
    {
        super.onResume();
        new GetContactsTask().execute((Object[]) null);
    }

    // performs database query outside GUI thread
    private class GetContactsTask extends AsyncTask<Object, Object, Cursor>
    {
        DatabaseConnector databaseConnector =
                new DatabaseConnector(getActivity());

        // open database and return Cursor for all contacts
        @Override
        protected Cursor doInBackground(Object... params)
        {
               databaseConnector.open();
               return databaseConnector.getAllContacts();
        }

        // use the Cursor returned from the doInBackground method
        @Override
        protected void onPostExecute(Cursor result)
        {
           contactAdapter.changeCursor(result); // set the adapter's Cursor
           databaseConnector.close();
        }
     } // end class GetContactsTask

    @Override
    public void onStop()
    {
        Cursor cursor = contactAdapter.getCursor(); // get current Cursor
        contactAdapter.changeCursor(null); // adapter now has no Cursor

        if (cursor != null)
           cursor.close(); // release the Cursor's resources

        super.onStop();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
       super.onCreateOptionsMenu(menu, inflater);
       inflater.inflate(R.menu.fragment_contact_list_menu, menu);
    }



    // handle choice from options menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
       switch (item.getItemId())
       {
          case R.id.action_add:
             listener.onAddContact();
             return true;
       }
       return super.onOptionsItemSelected(item); // call super's method
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.action_add) {
            listener.onAddContact();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/


    public void updateContactList()
    {
        new GetContactsTask().execute((Object[]) null);
    }

}
