package com.customer;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;

import com.customer.map.FragmentMap;

import java.io.File;

public class ActivityMain extends AppCompatActivity {
    private ClientItem clientItem;
    public Toolbar toolbar;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        clientItem = new ClientItem();
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(this.getResources().getColor(R.color.background));
        }
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
            if (fm.getFragments() instanceof FragmentList) {
                toolbar.setNavigationIcon(null);
            } else {
                toolbar.setNavigationIcon(R.drawable.backbutton);
            }
            toolbar.setNavigationIcon(null);
        } else {
            showIcon();
        }
    }
    public void deleteProfile(int id, String filename, String imagename) {
        DatabaseAdapter db = new DatabaseAdapter(this);
        db.deleteContact(id);
        File file = new File(filename);
        if (file.exists())
            file.delete();
        if (imagename != null) {
            File image = new File(Uri.parse(imagename).getPath());
            if (image.exists())
                image.delete();
        }
    }

    public void showIcon() {
        toolbar.setNavigationIcon(R.drawable.backbutton);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public ClientItem getClientItem() {
        return clientItem;
    }

    @Override
    public void onBackPressed() {
        if (fm.getBackStackEntryCount() == 0) {
            finish();
        } else fm.popBackStack();

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
        if (isAllPermissionsGranted) {
            switch (requestCode) {
                case 1:
                    ((OnPermissionsListener) getSupportFragmentManager().findFragmentByTag(FragmentNew.TAG)).onPermissionsGranted(permissions);
                    break;
                case 2:
                    ((OnPermissionsListener) getSupportFragmentManager().findFragmentByTag(FragmentList.TAG)).onPermissionsGranted(permissions);
                    break;
                case 3:
                    ((OnPermissionsListener) getSupportFragmentManager().findFragmentByTag(FragmentMap.TAG)).onPermissionsGranted(permissions);
                    break;
                case 4:
                    ((OnPermissionsListener) getSupportFragmentManager().findFragmentByTag(FragmentList.TAG)).onPermissionsGranted(permissions);
                    break;

            }
        }
    }

}
