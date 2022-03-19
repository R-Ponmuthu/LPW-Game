package com.it.lpw.game.ui.activity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.it.lpw.game.ui.fragments.FragmentMain;
import com.it.lpw.game.ui.fragments.RatingDialogFragment;
import com.it.lpw.game.util.Constant_Api;
import com.it.lpw.game.util.Method;
import com.it.lpw.game.R;
import com.it.lpw.game.util.Session;
import com.applovin.sdk.AppLovinSdk;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.it.lpw.game.Config.Config;
import com.it.lpw.game.databinding.ActivityMainBinding;
import com.it.lpw.game.ui.fragments.Coins;
import com.it.lpw.game.ui.fragments.HomeNew;
import com.it.lpw.game.ui.fragments.Profile;
import com.it.lpw.game.ui.fragments.Invite;
import com.google.firebase.messaging.FirebaseMessaging;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    public static TextView textpoints;
    private boolean doubleBackToExitPressedOnce = false;
    Session session;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseMessaging.getInstance().subscribeToTopic("all");

        StartAppSDK.init(this, Constant_Api.STARTAPP_ID, false);
        StartAppAd.disableSplash();

        AppLovinSdk.getInstance(this).setMediationProvider("max");
        AppLovinSdk.initializeSdk(this, configuration -> {
        });

        session = new Session(this);

        session.setBoolean(session.ENABLE_SESSION, true);
        session.save(this);


        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {

            switch (menuItem.getItemId()) {
                default:
                    loadFragment(new FragmentMain());
                case R.id.navigation_home:
                    loadFragment(new FragmentMain());
                    return true;
                case R.id.navigation_invite:
                    loadFragment(new Invite());
                    return true;
                case R.id.navigation_coin:
                    loadFragment(new Coins());
                    return true;
                case R.id.navigation_profile:
                    loadFragment(new Profile());
                    return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
//
//        header.findViewById(R.id.history).setOnClickListener(view -> { loadFragment(new Coins());             binding.drawerLayout.closeDrawer(GravityCompat.START);
//        });
//        header.findViewById(R.id.contact).setOnClickListener(view -> { startActivity(new Intent(MainActivity.this,AboutusActivity.class));             binding.drawerLayout.closeDrawer(GravityCompat.START);
//        });
//        header.findViewById(R.id.about).setOnClickListener(view -> { startActivity(new Intent(MainActivity.this,AboutusActivity.class));             binding.drawerLayout.closeDrawer(GravityCompat.START);
//        });
//
//        header.findViewById(R.id.privacy).setOnClickListener(view -> {
//            try {
//                String url = Constant_Api.PRIVACY_POLICY_URL;
//                if (!url.startsWith("http://") && !url.startsWith("https://")) {
//                    url = "http://" + url;
//                }
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(browserIntent);
//                binding.drawerLayout.closeDrawer(GravityCompat.START);
//            } catch (Exception e) {
//                Method.alert(this,getResources().getString(R.string.wrong));}
//        });

//        header.findViewById(R.id.rateus).setOnClickListener(view -> {
//            final String appName = getPackageName();
//            try {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));
//            } catch (ActivityNotFoundException anfe) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
//            }
//            binding.drawerLayout.closeDrawer(GravityCompat.START);
//        });
//        header.findViewById(R.id.logoutbtn).setOnClickListener(view -> {
//            Logout();
//            binding.drawerLayout.closeDrawer(GravityCompat.START);
//        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        RatingDialogFragment dialog = RatingDialogFragment.newInstance();
        dialog.show(getSupportFragmentManager(), "rating_dialog");
    }

//    private void Logout() {
//        LayoutInflater layoutInflater = LayoutInflater.from(this);
//        View promptView = layoutInflater.inflate(R.layout.dialog_logout, null);
//        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
//        alert.setView(promptView);
//        alert.setPositiveButton(getString(R.string.confirm), (dialog, whichButton) -> {
//            session.Logout();
//            session.setBoolean(session.LOGIN,false);
//                startActivity(new Intent(this,FrontLogin.class));
//        });
//        alert.setNegativeButton("CANCEL",
//                (dialog, whichButton) -> {
//                });
//        AlertDialog alert1 = alert.create();
//        alert1.show();
//    }


}
