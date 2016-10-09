package com.andmore.parkitmobile.activity;


import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.andmore.parkitmobile.entity.Seccion;
import com.andmore.parkitmobile.entity.SectionDrawerItem;
import com.andmore.parkitmobile.fragment.SectionContentFragment;
import com.andmore.parkitmobile.image.util.SectionDrawerListAdapter;
import com.andmore.parkitmobile.repository.SQLiteSeccionHandler;

@SuppressLint("NewApi")
public class SectionDetailActivity extends AppCompatActivity {
	
	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private String[] navMenuIds;
	private TypedArray navMenuIcons;

	private ArrayList<SectionDrawerItem> sectionDrawerItems;
	private SectionDrawerListAdapter adapter;
	
	private SQLiteSeccionHandler dbSeccion;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_section_detail);
		
		// nav drawer icons from resources
	   navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
	   mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	   mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		
		dbSeccion = new SQLiteSeccionHandler(getApplicationContext());
		Long ccId = 0L;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			ccId = extras.getLong("ccId");
		}
		mTitle = mDrawerTitle = getTitle();
		 
		List<Seccion> seccionList = dbSeccion.getSeccionByCC((ccId.intValue()));
		if(seccionList != null){
			sectionDrawerItems = new ArrayList<SectionDrawerItem>();
			navMenuTitles = new String[seccionList.size()];
			navMenuIds = new String[seccionList.size()];
			for (int i = 0; i < seccionList.size(); i++) {
				Seccion seccionObject = seccionList.get(i);
				navMenuTitles [i] = seccionObject.getNombre(); 
				navMenuIds [i] = seccionObject.getId()+""; 
				sectionDrawerItems.add(new SectionDrawerItem(navMenuTitles[i], navMenuIcons.getResourceId(seccionObject.getId(), -1), true, "50"));
				
			}
			
		}
		

		 
		   

		// nav drawer icons from resources
		//navMenuIcons = getResources()
			//	.obtainTypedArray(R.array.nav_drawer_icons);

		//mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		//mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		//sectionDrawerItems = new ArrayList<SectionDrawerItem>();
		
		// adding nav drawer items to array
        // Home
		//sectionDrawerItems.add(new SectionDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Find People
		//sectionDrawerItems.add(new SectionDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Photos
		//sectionDrawerItems.add(new SectionDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Communities, Will add a counter here
		//sectionDrawerItems.add(new SectionDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
        // Pages
		//sectionDrawerItems.add(new SectionDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // What's hot, We  will add a counter here
		//sectionDrawerItems.add(new SectionDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
         
 
        // Recycle the typed array
        navMenuIcons.recycle();
        
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        
        // setting the nav drawer list adapter
        adapter = new SectionDrawerListAdapter(getApplicationContext(),
                sectionDrawerItems);
        mDrawerList.setAdapter(adapter);
        
     // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
            	getSupportActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }
 
            public void onDrawerOpened(View drawerView) {
            	getSupportActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
        mDrawerLayout.openDrawer(mDrawerList);
    }
	
	/**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
        case R.id.action_settings:
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
 
    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
 
    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        android.support.v4.app.Fragment fragment = null;
        
   	    //fragment = new FindPeopleFragment();
        //fragment = new HomeFragment();
        //fragment = new PhotosFragment();
        //fragment = new CommunityFragment();
        //fragment = new PagesFragment();
        //fragment = new WhatsHotFragment();
       try {
        fragment = new SectionContentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("seccionId", Integer.parseInt(navMenuIds[position]) );
        fragment.setArguments(bundle);
        
        	android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();
            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
			
		} catch (Exception e) {
			 // error in creating fragment
            Log.e("SectionDetailActivity", "Error in creating fragment");
		}
    }
 
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }
 
    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
 
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
 
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
   
 
}