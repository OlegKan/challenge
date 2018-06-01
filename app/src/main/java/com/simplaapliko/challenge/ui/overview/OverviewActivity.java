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

package com.simplaapliko.challenge.ui.overview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.simplaapliko.challenge.R;
import com.simplaapliko.challenge.di.ApplicationComponent;
import com.simplaapliko.challenge.domain.model.Profile;
import com.simplaapliko.challenge.ui.base.BaseActivity;
import com.simplaapliko.challenge.ui.overview.adapder.ProfileAdapter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class OverviewActivity extends BaseActivity implements OverviewContract.View {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, OverviewActivity.class);
    }

    @Inject OverviewContract.Presenter presenter;

    private FloatingActionButton addFab;
    private ProfileAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindViews();

        presenter.init();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_overview;
    }

    @Override
    protected void injectDependencies(ApplicationComponent applicationComponent) {
        applicationComponent.plus(new OverviewComponent.Module(this))
                .inject(this);
    }

    private void bindViews() {
        addFab = findViewById(R.id.add_fab);

        adapter = new ProfileAdapter();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        presenter.destroy();
        super.onDestroy();
    }

    @Override
    public void displayProfiles(List<Profile> profiles) {
        adapter.setItems(profiles);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addProfile(Profile profile) {
        adapter.addItem(profile);
        recyclerView.smoothScrollToPosition(adapter.getItemCount());
    }

    @Override
    public void deleteProfile(Profile profile) {
        adapter.deleteItem(profile);
    }

    @Override
    public void updateProfile(Profile profile) {
        adapter.updateItem(profile);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public Observable<Object> onAddProfileClick() {
        return RxView.clicks(addFab);
    }

    @Override
    public Observable<Profile> onShowProfileClick() {
        return adapter.getClickObservable();
    }
}
