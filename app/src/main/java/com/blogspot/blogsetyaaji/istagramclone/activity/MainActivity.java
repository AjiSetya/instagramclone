package com.blogspot.blogsetyaaji.istagramclone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import com.blogspot.blogsetyaaji.istagramclone.AddFragment;
import com.blogspot.blogsetyaaji.istagramclone.HomeFragment;
import com.blogspot.blogsetyaaji.istagramclone.ProfileFragment;
import com.blogspot.blogsetyaaji.istagramclone.R;
import com.blogspot.blogsetyaaji.istagramclone.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        openFragment(new HomeFragment());
                        return true;
                    case R.id.navigation_add:
                        openFragment(new AddFragment());
                        return true;
                    case R.id.navigation_profile:
                        openFragment(new ProfileFragment());
                        return true;
                }
                return false;
            };

    public void openFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //new SessionManager(MainActivity.this).checkLogin();
        if (!new SessionManager(MainActivity.this).is_login()) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }

        openFragment(new HomeFragment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navlogout:
                new SessionManager(MainActivity.this).logout();
                finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new SessionManager(MainActivity.this).checkLogin();
    }
}
