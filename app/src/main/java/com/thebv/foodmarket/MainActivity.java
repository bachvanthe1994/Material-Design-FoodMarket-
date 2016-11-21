package com.thebv.foodmarket;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private static MainActivity instance;

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportPostponeEnterTransition();
        setContentView(R.layout.activity_main);
        instance = this;

        pushFragment(new MainFragment());
    }

    public void pushFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager()
                .beginTransaction();
        ft.replace(R.id.container, fragment);
        ft.addToBackStack(fragment.getClass().getName());
        ft.commit();
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
    }

    @Override
    public void onBackPressed() {
        back();
    }

    protected void back() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int backStackEntryCount = fragmentManager.getBackStackEntryCount();
        if (backStackEntryCount > 1) {
            fragmentManager.popBackStack();
        } else {
            finish();
        }
    }


}
