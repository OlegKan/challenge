/*
 * Copyright (C) 2018 Oleg Kan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.simplaapliko.challenge.ui.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatSpinner;
import android.util.SparseArray;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.jakewharton.rxbinding2.view.RxView;
import com.simplaapliko.challenge.R;
import com.simplaapliko.challenge.di.ApplicationComponent;
import com.simplaapliko.challenge.ui.base.BaseActivity;

import javax.inject.Inject;

import io.reactivex.Observable;

public class DetailsActivity extends BaseActivity implements DetailsContract.View {

    public static final String EXTRA_PROFILE = "simplaapliko.extra.PROFILE";

    private static final String BUNDLE_SCREEN_SESSION_ID = "screenSessionId";
    private static final SparseArray<DetailsComponent> SCREEN_COMPONENT_CACHE = new SparseArray<>();
    private static int sScreenSessionCounter;
    private int screenSessionId;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, DetailsActivity.class);
    }

    public static Intent getStartIntent(Context context, ProfileViewModel profile) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(EXTRA_PROFILE, profile);
        return intent;
    }

    @Inject DetailsContract.Presenter presenter;

    private ImageView imageView;
    private AppCompatSpinner genderSpinner;
    private TextInputEditText ageText;
    private TextInputEditText hobbiesText;
    private TextInputEditText nameText;
    private FloatingActionButton saveFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            screenSessionId = savedInstanceState.getInt(BUNDLE_SCREEN_SESSION_ID, 1);
        } else {
            sScreenSessionCounter++;
            screenSessionId = sScreenSessionCounter;
        }

        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        bindViews();

        presenter.init();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_details;
    }

    @Override
    protected void injectDependencies(ApplicationComponent applicationComponent) {
        DetailsComponent screenComponent = SCREEN_COMPONENT_CACHE.get(screenSessionId);
        if (screenComponent == null) {
            ProfileViewModel profile = getIntent().getParcelableExtra(EXTRA_PROFILE);

            screenComponent = applicationComponent.plus(new DetailsComponent.Module(this, profile));
            SCREEN_COMPONENT_CACHE.put(screenSessionId, screenComponent);
        }
        screenComponent.inject(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        clearScreenComponent();
    }

    private void clearScreenComponent() {
        SCREEN_COMPONENT_CACHE.remove(screenSessionId);
    }

    private void bindViews() {
        imageView = findViewById(R.id.image);
        ageText = findViewById(R.id.age);
        hobbiesText = findViewById(R.id.hobbies);
        nameText = findViewById(R.id.name);
        saveFab = findViewById(R.id.save_fab);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.genders,
                android.R.layout.simple_spinner_dropdown_item);
        genderSpinner = findViewById(R.id.gender);
        genderSpinner.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        presenter.destroy();
        super.onDestroy();
    }

    @Override
    public void displayProfile(ProfileViewModel profile) {
        ageText.setText(String.valueOf(profile.getAge()));
        hobbiesText.setText(profile.getHobbies());
        nameText.setText(profile.getName());
        nameText.setSelection(profile.getName().length(), profile.getName().length());
        genderSpinner.setSelection(profile.getGender());
    }

    @Override
    public void setFormEnabled(boolean enabled) {
        imageView.setEnabled(enabled);
        ageText.setEnabled(enabled);
        nameText.setEnabled(enabled);
        genderSpinner.setEnabled(enabled);
    }

    @Override
    public Observable<Object> onSaveProfileClick() {
        return RxView.clicks(saveFab);
    }

    @Override
    public Observable<Object> onSelectImageClick() {
        return null;
    }
}
