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

import com.simplaapliko.challenge.domain.repository.ProfileRepository;
import com.simplaapliko.challenge.rx.RxSchedulers;

import io.reactivex.disposables.CompositeDisposable;

public class DetailsPresenter implements DetailsContract.Presenter {

    private final RxSchedulers rxSchedulers;
    private final ProfileRepository repository;
    private final DetailsContract.View view;
    private final DetailsContract.Navigator navigator;
    private final ProfileViewModel profile;

    private final CompositeDisposable disposables = new CompositeDisposable();

    DetailsPresenter(RxSchedulers rxSchedulers, ProfileRepository repository,
            DetailsContract.View view, DetailsContract.Navigator navigator,
            ProfileViewModel profile) {
        this.rxSchedulers = rxSchedulers;
        this.repository = repository;
        this.view = view;
        this.navigator = navigator;
        this.profile = profile;
    }

    @Override
    public void init() {
        view.setFormEnabled(profile.isNew());
        view.displayProfile(profile);
    }

    @Override
    public void destroy() {

    }
}
