package com.f1x.mtcdtools.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.adapters.InstalledPackagesArrayAdapter;
import com.f1x.mtcdtools.adapters.PackageEntry;
import com.f1x.mtcdtools.adapters.PackageEntryArrayAdapter;

import java.util.ArrayList;

public class EditModePackagesActivity extends ServiceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mode_packages);

        InstalledPackagesArrayAdapter installedPackagesArrayAdapter = new InstalledPackagesArrayAdapter(this);
        final Spinner packagesListSpinner = (Spinner)findViewById(R.id.spinnerInstalledPackagesModePackages);
        packagesListSpinner.setAdapter(installedPackagesArrayAdapter);
        //-------------------------------------------------------------------------------------

        Button saveButton = (Button)findViewById(R.id.buttonSaveModePackages);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> modePackagesList = new ArrayList<>();

                for(int i = 0; i < mModePackagesArrayAdapter.getCount(); ++i) {
                    PackageEntry packageEntry = mModePackagesArrayAdapter.getItem(i);
                    modePackagesList.add(packageEntry.getName());
                }

                if(mServiceBinder != null && mServiceBinder.saveModePackages(modePackagesList)) {
                    finish();
                } else {
                    Toast.makeText(EditModePackagesActivity.this, getString(R.string.ModePackagesSaveFailure), Toast.LENGTH_LONG).show();
                }
            }
        });
        //-------------------------------------------------------------------------------------

        Button cancelButton = (Button)findViewById(R.id.buttonCancelModePackages);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //-------------------------------------------------------------------------------------

        mModePackagesArrayAdapter = new PackageEntryArrayAdapter(this);
        ListView modePackagesListView = (ListView)findViewById(R.id.listViewModePackages);
        modePackagesListView.setAdapter(mModePackagesArrayAdapter);
        modePackagesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                PackageEntry packageEntry = mModePackagesArrayAdapter.getItem(position);
                if(packageEntry != null) {
                    mModePackagesArrayAdapter.remove(packageEntry);
                }

                return true;
            }
        });
        //-------------------------------------------------------------------------------------

        Button addButton = (Button)findViewById(R.id.buttonAddModePackage);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageEntry packageEntry = (PackageEntry)packagesListSpinner.getSelectedItem();
                mModePackagesArrayAdapter.add(packageEntry);
            }
        });
        //-------------------------------------------------------------------------------------
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mServiceBinder != null) {
            mModePackagesArrayAdapter.reset(mServiceBinder.getModePackages());
        }
    }

    @Override
    protected void onServiceConnected() {
        mModePackagesArrayAdapter.reset(mServiceBinder.getModePackages());
    }

    private PackageEntryArrayAdapter mModePackagesArrayAdapter;
}