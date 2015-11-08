package group1.se3345.contactmanager;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity
    implements ContactListFragment.ContactListFragmentListener,
        DetailsFragment.DetailsFragmentListener,
        AddEditFragment.AddEditFragmentListener
  {
      public static final String ROW_ID = "row_id";
      ContactListFragment contactListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null)
            return;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
         */
        //CODE
        //if (findViewById(R.id.fragmentContainer) != null)
        //{
            contactListFragment = new ContactListFragment();

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(R.id.fragmentContainer, contactListFragment);
            transaction.commit();

    }

    @Override
    protected void onResume()
    {
        super.onResume();

    }

    @Override
    public void onContactSelected(long rowID)
    {
        //if(findViewById(R.id.fragmentContainer) != null) //phone
        displayContact(rowID, R.id.fragmentContainer);

    }

      private void displayContact(long rowID, int viewID)
      {
          DetailsFragment detailsFragment = new DetailsFragment();
          Bundle arguments = new Bundle();
          arguments.putLong(ROW_ID, rowID);
          detailsFragment.setArguments(arguments);

          FragmentTransaction transaction =
                  getFragmentManager().beginTransaction();
          transaction.replace(viewID, detailsFragment);
          transaction.addToBackStack(null);
          transaction.commit();
      }
    @Override
    public void onAddContact()
    {
        //if (findViewById(R.id.fragmentContainer)!= null)
        displayAddEditFragment(R.id.fragmentContainer, null);

    }
      private void displayAddEditFragment(int viewID, Bundle arguments)
      {
          AddEditFragment addEditFragment = new AddEditFragment();
          addEditFragment.setArguments(arguments);

          FragmentTransaction transaction =
                  getFragmentManager().beginTransaction();
          transaction.replace(viewID, addEditFragment);
          transaction.addToBackStack(null);
          transaction.commit();
      }

      @Override
      public void onContactDeleted()
      {
          getFragmentManager().popBackStack();

      }

      @Override
      public void onEditContact(Bundle arguments)
      {
          displayAddEditFragment(R.id.fragmentContainer, arguments);
      }

      @Override
      public void onAddEditCompleted(long rowID)
      {
          getFragmentManager().popBackStack();
      }



}
