package com.jordanmadrigal.lendsum.View;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jordanmadrigal.lendsum.Adapter.ViewPagerAdapter;
import com.jordanmadrigal.lendsum.Interfaces.OnActivityToFragmentListener;
import com.jordanmadrigal.lendsum.Model.User;
import com.jordanmadrigal.lendsum.R;
import com.jordanmadrigal.lendsum.Utility.CustomViewPager;

import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;
import static android.support.v4.view.ViewPager.SCROLL_STATE_SETTLING;

public class HomeActivity extends AppCompatActivity implements OnActivityToFragmentListener {

    private static final String LOG_TAG = HomeActivity.class.getSimpleName();

    private ActionBar mActionBar;
    private TextView mNavProfText;
    private FloatingActionButton mFAB;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private View mHeaderView;
    private FirebaseFirestore database;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FragmentManager mFragmentManager;
    private CustomViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mFragmentManager = getSupportFragmentManager();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        database = FirebaseFirestore.getInstance();
        mNavProfText = findViewById(R.id.navUserName);

        mDrawerLayout = findViewById(R.id.drawerLayout);

        mNavigationView = findViewById(R.id.nav_view);
        mHeaderView = mNavigationView.getHeaderView(0);
        mNavProfText = mHeaderView.findViewById(R.id.navUserName);

        //Sliding fragments with tabs
        mViewPager = findViewById(R.id.viewPager);
        mViewPager.setSwipeEnabled(true);

        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);

        //Menu icon in app bar
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_34dp);
        }

        mFAB = findViewById(R.id.mainFab);

        mViewPager.addOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {

            Fragment fragment = null;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(final int position) {

                if(position == 2){
                    mFAB.hide();
                }else{
                    mFAB.show();
                }

                switch (position){
                    case 0:
                        mFAB.setImageResource(R.drawable.ic_handshake_white_54dp);
                        mFAB.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                mActionBar.setTitle("New Package");

                                mFAB.hide();

                                mViewPager.setSwipeEnabled(false);

                                fragment = new AddPackageFragment();

                                mFragmentManager.beginTransaction().add(R.id.contentFrame,fragment).addToBackStack("HomeFrag").commit();

                            }

                        });

                        break;
                    case 1:
                        mFAB.setImageResource(R.drawable.ic_mail_outline_24dp);
                        mFAB.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                mActionBar.setTitle("New Message");

                                mViewPager.setSwipeEnabled(false);

                                fragment = new NewMessageFragment();

                                mFragmentManager.beginTransaction().add(R.id.contentFrame,fragment).addToBackStack(null).commit();

                            }
                        });
                        break;


                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == SCROLL_STATE_DRAGGING){
                    mFAB.hide();
                }

                if(state == SCROLL_STATE_SETTLING){
                    mFAB.show();
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mActionBar.setTitle(R.string.app_name);
        mFAB.show();
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

        getFirestoreNameDisplay();

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

    public void getFirestoreNameDisplay(){
        DocumentReference document = database.collection("users").document(mUser.getUid());

        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists()) {

                    User user = documentSnapshot.toObject(User.class);

                    String firstName = user.getFirstName();
                    String lastName = user.getLastName();
                    String fullName = firstName + " " + lastName;

                    mNavProfText.setText(fullName);

                }
            }
        });
    }

    //Handles Navdrawer click events
    private void navigateDrawerEvents(int id){

        Intent intent;

        switch (id) {
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
                intent = new Intent(HomeActivity.this, InviteFriendsActivity.class);
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

    @Override
    public void onActionBarListener(int title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onFragmentVisible(boolean isVisible) {
        if(!isVisible){
            mViewPager.setSwipeEnabled(true);
            mFAB.show();
        }
    }
}
