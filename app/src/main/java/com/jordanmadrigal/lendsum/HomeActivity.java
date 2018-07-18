package com.jordanmadrigal.lendsum;

import android.content.Intent;
import android.drm.DrmStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;
import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;

public class HomeActivity extends AppCompatActivity{

    private static final String LOG_TAG = HomeActivity.class.getName();

    private ActionBar mActionBar;
    private FloatingActionButton mFAB;
    private ViewPager mViewPager;
    private CategoryAdapter mAdapter;
    private TabLayout mTabLayout;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ImageView mImageView;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        mImageView =findViewById(R.id.userProfImage);
        mViewPager = findViewById(R.id.viewPager);
        mDrawerLayout = findViewById(R.id.drawerLayout);


        mNavigationView = findViewById(R.id.nav_view);
        mTabLayout = findViewById(R.id.sliding_tabs);

        //Sliding fragments with tabs
        mAdapter = new CategoryAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        //Menu icon in app bar
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_34dp);
        }

        //Animate fab
        mFAB = findViewById(R.id.mainFab);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        mFAB.setImageResource(R.drawable.ic_handshake_white_54dp);
                        break;
                    case 1:
                        mFAB.setImageResource(R.drawable.ic_mail_outline_24dp);
                        break;
                    case 2:
                        mFAB.setImageResource(R.drawable.ic_edit_white_24dp);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == SCROLL_STATE_DRAGGING){
                    mFAB.hide();
                }else{
                    mFAB.show();
                }
            }
        });




    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();




        //Drawer event handler
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                menuItem.setChecked(true);

                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();

                navigateDrawerEvents(id);

                return true;
            }
        });

    }

    //Handles Navdrawer click events
    private void navigateDrawerEvents(int id){

        Intent intent;

        switch (id) {
            case R.id.navProf:
                intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.navFriendSearch:
                intent = new Intent(HomeActivity.this, FriendSearchActivity.class);
                startActivity(intent);
                break;
            case R.id.navManagePayment:
                intent = new Intent(HomeActivity.this, ManagePaymentActivity.class);
                startActivity(intent);
                break;
            case R.id.navSettings:
                intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.navInviteFriends:
                intent = new Intent(HomeActivity.this, InviteFriends.class);
                startActivity(intent);
                break;
            case R.id.navHelp:
                intent = new Intent(HomeActivity.this, HelpActivity.class);
                startActivity(intent);
                break;
            case R.id.navLogout:
                mAuth.signOut();
                intent = new Intent(HomeActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();
                break;
        }

    }

}
