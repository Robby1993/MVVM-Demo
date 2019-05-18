package com.nxccontrols.demomvvm.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.nxccontrols.demomvvm.R;
import com.nxccontrols.demomvvm.databinding.ActivityThirdBinding;
import com.nxccontrols.demomvvm.models.Project;
import com.nxccontrols.demomvvm.viewmodels.SecondViewModel;

import javax.inject.Inject;

public class ThirdActivity extends AppCompatActivity {

    private ActivityThirdBinding binding;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private String TAG = "ThirdActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_third);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Third Activity");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_third);

        String project_id = getIntent().getStringExtra("id");
        SecondViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(SecondViewModel.class);

        binding.setIsLoading(true);

        viewModel.getProjectDetails("Google", project_id).observe(this, new Observer<Project>() {
            @Override
            public void onChanged(@Nullable Project project) {
                if (project != null) {
                    binding.setIsLoading(false);
                    binding.setProject(project);
                    binding.setUser(project.owner);
                    Log.d(TAG, "DETAILS----" + new Gson().toJson(project));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}