package com.customer;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private Client client;
    Toolbar mToolbar;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        client = new Client();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        showScreen(new FragmentList(), FragmentList.TAG, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    public void showScreen(Fragment fragment, String tag, boolean addToBackStack) {
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (addToBackStack) {
            ft.addToBackStack(String.valueOf(System.identityHashCode(fragment)));
        }
        ft.replace(R.id.frame_main, fragment, tag);
        ft.commitAllowingStateLoss();
        if (tag.equals(FragmentList.TAG)) {
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            if (fm.getFragments() instanceof  FragmentList) {
                mToolbar.setNavigationIcon(null);
            } else {
                mToolbar.setNavigationIcon(R.drawable.backbutton);
            }
            mToolbar.setNavigationIcon(null);
        } else {
            showIcon();
        }
    }

    public void showIcon() {
        mToolbar.setNavigationIcon(R.drawable.backbutton);
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
        if (fm.getBackStackEntryCount() == 0) {
            finish();
        } else fm.popBackStack();

    }

    public void deleteprofile(int id, String filename,String imagename ) {
        DatabaseAdapter db = new DatabaseAdapter(this);
        db.deleteContact(id);
        File file = new File(filename);
        file.delete();
        if (imagename!=null){
        File image=new File(Uri.parse(imagename).getPath());
        image.delete();}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean isAllPermissionsGranted = true;
        for (int result :
                grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                isAllPermissionsGranted = false;
            }
            }
        if(isAllPermissionsGranted){
            switch (requestCode){
                case 1:
                    ((OnPermissionsListener) getSupportFragmentManager().findFragmentByTag(FragmentList.TAG)).onPermissionsGranted(permissions);
                    break;
                case 2: ((OnPermissionsListener) getSupportFragmentManager().findFragmentByTag(FragmentMap.TAG)).onPermissionsGranted(permissions);
                    break;
                case 3: ((OnPermissionsListener) getSupportFragmentManager().findFragmentByTag(FragmentNew.TAG)).onPermissionsGranted(permissions);
                    break;
            }
        }
    }
}
