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

import com.simplaapliko.challenge.domain.Utils;
import com.simplaapliko.challenge.domain.model.Filter;
import com.simplaapliko.challenge.domain.model.Pair;
import com.simplaapliko.challenge.domain.model.Profile;
import com.simplaapliko.challenge.domain.model.SortOrder;
import com.simplaapliko.challenge.domain.repository.ProfileRepository;
import com.simplaapliko.challenge.rx.RxSchedulers;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class OverviewPresenter implements OverviewContract.Presenter {

    private final RxSchedulers rxSchedulers;
    private final ProfileRepository repository;
    private final OverviewContract.View view;
    private final OverviewContract.Navigator navigator;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private Disposable getProfilesDisposable;
    private Disposable profilesChangesDisposable;

    OverviewPresenter(RxSchedulers rxSchedulers, ProfileRepository repository,
            OverviewContract.View view, OverviewContract.Navigator navigator) {
        this.rxSchedulers = rxSchedulers;
        this.repository = repository;
        this.view = view;
        this.navigator = navigator;
    }

    @Override
    public void init() {
        bindView();

        refreshData(Filter.ALL, SortOrder.ID_ASC);
    }

    private void refreshData(Filter filter, SortOrder sortOrder) {
        view.showProgress();
        if (getProfilesDisposable != null && !getProfilesDisposable.isDisposed()) {
            getProfilesDisposable.dispose();
            disposables.delete(getProfilesDisposable);
        }
        getProfilesDisposable = repository.getProfiles(filter, sortOrder)
                .compose(rxSchedulers.getComputationToMainTransformerSingle())
                .doFinally(this::startObservingProfilesChanges)
                .subscribe(this::handleGetAllProfileSuccess, this::handleGetProfileError);
        disposables.add(getProfilesDisposable);
    }

    private void handleGetAllProfileSuccess(List<Profile> data) {
        view.hideProgress();
        view.displayProfiles(data);
    }

    private void startObservingProfilesChanges() {
        if (profilesChangesDisposable != null && !profilesChangesDisposable.isDisposed()) {
            profilesChangesDisposable.dispose();
            disposables.delete(profilesChangesDisposable);
        }
        profilesChangesDisposable = repository.observeProfilesChanges(view.getSelectedFilter())
                .compose(rxSchedulers.getComputationToMainTransformer())
                .subscribe(this::handleObserveChangesSuccess, this::handleGetProfileError);
        disposables.add(profilesChangesDisposable);
    }

    private void handleObserveChangesSuccess(Pair<Profile, Integer> data) {
        if (data.second == 0) {
            handleAddProfile(data.first);
        } else if (data.second == 1) {
            handleDeleteProfile(data.first);
        } else {
            handleUpdateProfile(data.first);
        }
    }

    private void handleAddProfile(Profile profile) {
        SortOrder sortOrder = view.getSelectedSortOrder();
        if (sortOrder == SortOrder.ID_ASC) {
            view.addProfile(profile);
        } else {
            List<Profile> profiles = view.getProfiles();
            profiles.add(profile);
            Collections.sort(profiles, Utils.getComparator(sortOrder));
            view.displayProfiles(profiles);
        }
    }

    private void handleDeleteProfile(Profile profile) {
        view.deleteProfile(profile);
    }

    private void handleUpdateProfile(Profile profile) {
        view.updateProfile(profile);
    }

    private void handleGetProfileError(Throwable throwable) {
        view.hideProgress();
        view.showMessage(throwable.getLocalizedMessage());
    }

    private void bindView() {
        Disposable filter = view.onFilterChange()
                .observeOn(rxSchedulers.getMainThreadScheduler())
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(this::handleSelectFilterAction, throwable -> handleUnknownError());
        disposables.add(filter);

        Disposable listChange = view.onProfileListChange()
                .observeOn(rxSchedulers.getMainThreadScheduler())
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(this::handleBeerListCountChange, throwable -> handleUnknownError());
        disposables.add(listChange);

        Disposable sort = view.onSortOrderChange()
                .observeOn(rxSchedulers.getMainThreadScheduler())
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(this::handleSelectSortOrderAction, throwable -> handleUnknownError());
        disposables.add(sort);

        Disposable addProfile = view.onAddProfileClick()
                .observeOn(rxSchedulers.getMainThreadScheduler())
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(o -> handleAddProfileAction(), throwable -> handleUnknownError());
        disposables.add(addProfile);

        Disposable showProfile = view.onShowProfileClick()
                .observeOn(rxSchedulers.getMainThreadScheduler())
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(this::handleShowProfileAction, throwable -> handleUnknownError());
        disposables.add(showProfile);
    }

    private void handleSelectFilterAction(Filter filter) {
        refreshData(filter, view.getSelectedSortOrder());
    }

    private void handleBeerListCountChange(Integer count) {
        if (count == 0) {
            view.setEmptyMessageVisibility(true);
        } else {
            view.setEmptyMessageVisibility(false);
        }
    }

    private void handleSelectSortOrderAction(SortOrder sortOrder) {
        refreshData(view.getSelectedFilter(), sortOrder);
    }

    @Override
    public void destroy() {
        disposables.dispose();
    }

    private void handleAddProfileAction() {
        navigator.goToAddProfileScreen();
    }

    private void handleShowProfileAction(Profile profile) {
        navigator.goToEditProfileScreen(profile);
    }

    private void handleUnknownError() {
    }
}
