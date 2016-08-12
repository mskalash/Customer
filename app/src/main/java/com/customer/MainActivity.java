package com.customer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

public class MainActivity extends AppCompatActivity {
private Client client;
Toolbar mToolbar;
    FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
client=new Client();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        showScreen(new FirstFragment(),FirstFragment.TAG,false);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }
public void showScreen(Fragment fragment,String tag,boolean addToBackStack){
    fm =getSupportFragmentManager();
    FragmentTransaction ft = fm.beginTransaction();
    if (addToBackStack) {
        ft.addToBackStack(String.valueOf(System.identityHashCode(fragment)));
    }
    ft.replace(R.id.frame_main, fragment, tag);
    ft.commitAllowingStateLoss();
    fm.executePendingTransactions();
    if (tag.equals(FirstFragment.TAG)) {
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
    }
    else {
        showIcon();
    }
    }
    public void showIcon(){
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public Client getClient() {
        return client;
    }

    @Override
    public void onBackPressed() {
        if (fm.getBackStackEntryCount() == 0){
            getSupportActionBar().setHomeButtonEnabled(false);
            finish();}
        else fm.popBackStack();

    }
}
