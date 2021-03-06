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
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatSpinner;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxMenuItem;
import com.jakewharton.rxbinding2.view.RxView;
import com.simplaapliko.challenge.R;
import com.simplaapliko.challenge.di.ApplicationComponent;
import com.simplaapliko.challenge.ui.base.BaseActivity;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class DetailsActivity extends BaseActivity implements DetailsContract.View {

    public static final String EXTRA_PROFILE = "simplaapliko.extra.PROFILE";

    private boolean isNew = true;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, DetailsActivity.class);
    }

    public static Intent getStartIntent(Context context, ProfileModel profile) {
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

    private Observable<Object> deleteProfileObservable;
    private PublishSubject<String> imageSelectedObservable = PublishSubject.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        ProfileModel profile = getIntent().getParcelableExtra(EXTRA_PROFILE);
        applicationComponent.plus(new DetailsComponent.Module(this, profile))
                .inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!isNew) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_details, menu);

            MenuItem deleteMenuItem = menu.findItem(R.id.action_delete);
            deleteProfileObservable = RxMenuItem.clicks(deleteMenuItem);
            presenter.bindMenu();
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == DetailsContract.REQUEST_CODE_PICK_IMAGE) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                imageSelectedObservable.onNext(imageUri.toString());
            }
        }
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
    public void hideProgress() {
        setProgressVisibility(false);
    }

    @Override
    public void showProgress() {
        setProgressVisibility(true);
    }

    @Override
    public void displayProfile(ProfileModel profile) {
        ageText.setText(profile.getFormattedAge());
        hobbiesText.setText(profile.getHobbies());
        hobbiesText.setSelection(profile.getHobbies().length(), profile.getHobbies().length());
        nameText.setText(profile.getName());
        nameText.setSelection(profile.getName().length(), profile.getName().length());
        genderSpinner.setSelection(profile.getGender());
    }

    @Override
    public void displayProfileImage(String path) {
        Picasso.get()
                .load(path)
                .placeholder(R.drawable.ic_account)
                .error(R.drawable.ic_account)
                .resize(R.dimen.details_image_size, R.dimen.details_image_size)
                .onlyScaleDown()
                .centerCrop()
                .into(imageView);
    }

    @Override
    public void setFormEnabled(boolean enabled) {
        isNew = enabled;

        imageView.setEnabled(enabled);
        ageText.setEnabled(enabled);
        nameText.setEnabled(enabled);
        genderSpinner.setEnabled(enabled);

        invalidateOptionsMenu();
    }

    @Override
    public String getAge() {
        return ageText.getText().toString();
    }

    @Override
    public int getGender() {
        return genderSpinner.getSelectedItemPosition();
    }

    @Override
    public String getHobbies() {
        return hobbiesText.getText().toString();
    }

    @Override
    public String getName() {
        return nameText.getText().toString();
    }

    @Override
    public void showAgeError(String error) {
        ageText.setError(error);
    }

    @Override
    public void showGenderError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNameError(String error) {
        nameText.setError(error);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Observable<Object> onDeleteProfileClick() {
        return deleteProfileObservable;
    }

    @Override
    public Observable<String> onImageSelected() {
        return imageSelectedObservable;
    }

    @Override
    public Observable<Object> onSaveProfileClick() {
        return RxView.clicks(saveFab);
    }

    @Override
    public Observable<Object> onSelectImageClick() {
        return RxView.clicks(imageView);
    }
}
