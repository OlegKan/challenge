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
import android.view.MenuItem;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.simplaapliko.challenge.R;
import com.simplaapliko.challenge.di.ApplicationComponent;
import com.simplaapliko.challenge.domain.model.Filter;
import com.simplaapliko.challenge.domain.model.Profile;
import com.simplaapliko.challenge.domain.model.SortOrder;
import com.simplaapliko.challenge.ui.base.BaseActivity;
import com.simplaapliko.challenge.ui.overview.adapder.ProfileAdapter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class OverviewActivity extends BaseActivity implements OverviewContract.View {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, OverviewActivity.class);
    }

    @Inject OverviewContract.Presenter presenter;

    private final PublishSubject<Filter> filterSubject = PublishSubject.create();
    private final PublishSubject<SortOrder> sortOrderSubject = PublishSubject.create();

    private FloatingActionButton addFab;
    private ProfileAdapter adapter;
    private RecyclerView recyclerView;
    private Button filterButton;
    private Button sortButton;

    private Filter filter = Filter.ALL;
    private SortOrder sortOrder = SortOrder.ID_ASC;

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

        filterButton = findViewById(R.id.filter_button);
        sortButton = findViewById(R.id.sort_button);

        filterButton.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this, filterButton);
            popupMenu.inflate(R.menu.menu_filter);
            popupMenu.setOnMenuItemClickListener(item -> {
                updateFilterText(item);
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_filter_show_all:
                        filter = Filter.ALL;
                        break;
                    case R.id.action_filter_female:
                        filter = Filter.FEMALE;
                        break;
                    case R.id.action_filter_male:
                        filter = Filter.MALE;
                        break;
                }
                filterSubject.onNext(filter);
                return true;
            });
            popupMenu.show();
        });

        sortButton.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this, sortButton);
            popupMenu.inflate(R.menu.menu_sort);
            popupMenu.setOnMenuItemClickListener(item -> {
                updateSortText(item);
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_sort_by_id_asc:
                        sortOrder = SortOrder.ID_ASC;
                        break;
                    case R.id.action_sort_by_age_asc:
                        sortOrder = SortOrder.AGE_ASC;
                        break;
                    case R.id.action_sort_by_age_desc:
                        sortOrder = SortOrder.AGE_DESC;
                        break;
                    case R.id.action_sort_by_name_asc:
                        sortOrder = SortOrder.NAME_ASC;
                        break;
                    case R.id.action_sort_by_name_desc:
                        sortOrder = SortOrder.NAME_DESC;
                        break;
                }
                sortOrderSubject.onNext(sortOrder);
                return true;
            });
            popupMenu.show();
        });
    }

    private void updateFilterText(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_filter_show_all:
                filterButton.setText(R.string.action_filter);
                break;
            case R.id.action_filter_female:
                filterButton.setText(R.string.action_filter_female);
                break;
            case R.id.action_filter_male:
                filterButton.setText(R.string.action_filter_male);
                break;
        }
    }

    private void updateSortText(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sort_by_id_asc:
                sortButton.setText(R.string.action_sort);
                break;
            case R.id.action_sort_by_age_asc:
                sortButton.setText(R.string.action_sort_by_age_asc);
                break;
            case R.id.action_sort_by_age_desc:
                sortButton.setText(R.string.action_sort_by_age_desc);
                break;
            case R.id.action_sort_by_name_asc:
                sortButton.setText(R.string.action_sort_by_name_asc);
                break;
            case R.id.action_sort_by_name_desc:
                sortButton.setText(R.string.action_sort_by_name_desc);
                break;
        }
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
        //recyclerView.smoothScrollToPosition(adapter.getItemCount());
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
    public Filter getSelectedFilter() {
        return filter;
    }

    @Override
    public SortOrder getSelectedSortOrder() {
        return sortOrder;
    }

    @Override
    public Observable<Object> onAddProfileClick() {
        return RxView.clicks(addFab);
    }

    @Override
    public Observable<Filter> onFilterChange() {
        return filterSubject;
    }

    @Override
    public Observable<SortOrder> onSortOrderChange() {
        return sortOrderSubject;
    }

    @Override
    public Observable<Profile> onShowProfileClick() {
        return adapter.getClickObservable();
    }
}
