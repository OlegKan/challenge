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

import com.simplaapliko.challenge.domain.model.Profile;
import com.simplaapliko.challenge.domain.repository.ProfileRepository;
import com.simplaapliko.challenge.rx.RxSchedulers;

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

        Disposable disposable = repository.getAllProfiles()
                .compose(rxSchedulers.getComputationToMainTransformerSingle())
                .subscribe(this::handleGetAllProfileSuccess, this::handleGetAllProfileError);
        disposables.add(disposable);
    }

    private void handleGetAllProfileSuccess(List<Profile> data) {
        view.displayProfiles(data);
    }

    private void handleGetAllProfileError(Throwable throwable) {
        view.showMessage(throwable.getLocalizedMessage());
    }

    private void bindView() {
        Disposable addProfile = view.addProfile()
                .observeOn(rxSchedulers.getMainThreadScheduler())
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(o -> handleAddProfileAction(), throwable -> handleUnknownError());
        disposables.add(addProfile);

        Disposable showProfile = view.showProfile()
                .observeOn(rxSchedulers.getMainThreadScheduler())
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(this::handleShowProfileAction, throwable -> handleUnknownError());
        disposables.add(showProfile);
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
