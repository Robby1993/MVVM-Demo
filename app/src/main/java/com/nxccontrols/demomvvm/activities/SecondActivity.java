package com.nxccontrols.demomvvm.activities;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.nxccontrols.demomvvm.R;
import com.nxccontrols.demomvvm.adapter.SecondAdapter;
import com.nxccontrols.demomvvm.databinding.ActivitySecondBinding;
import com.nxccontrols.demomvvm.interfaces.ProjectClickCallback;
import com.nxccontrols.demomvvm.models.Project;
import com.nxccontrols.demomvvm.viewmodels.SecondViewModel;

import java.util.List;

import javax.inject.Inject;

public class SecondActivity extends AppCompatActivity {

    private SecondAdapter projectAdapter;
    private ActivitySecondBinding binding;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Second Activity");

        binding = DataBindingUtil.setContentView(this, R.layout.activity_second);


        projectAdapter = new SecondAdapter(projectClickCallback);
        binding.rvproject.setAdapter(projectAdapter);
        binding.setIsLoading(true);

        SecondViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(SecondViewModel.class);

        viewModel.getProject("Google").observe(this, new Observer<List<Project>>() {
            @Override
            public void onChanged(@Nullable List<Project> projects) {
                if (projects != null) {
                    binding.setIsLoading(false);
                    projectAdapter.setProjectList(projects);
                }
            }
        });
    }

    private final ProjectClickCallback projectClickCallback = new ProjectClickCallback() {
        @Override
        public void onClick(Project project) {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                startActivity(new Intent(SecondActivity.this, ThirdActivity.class).putExtra("id", project.name));
            }


        }
    };

   /* @Override
    public boolean onSupportNavigateUp() {
        super.onSupportNavigateUp();
        onBackPressed();
        return true;

    }*/

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
