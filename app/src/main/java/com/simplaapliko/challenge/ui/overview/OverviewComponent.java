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

import com.simplaapliko.challenge.di.ActivityScope;
import com.simplaapliko.challenge.domain.repository.ProfileRepository;
import com.simplaapliko.challenge.rx.RxSchedulers;

import dagger.Provides;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = OverviewComponent.Module.class)
public interface OverviewComponent {

    void inject(OverviewActivity activity);

    @dagger.Module
    class Module {

        private final OverviewActivity activity;

        public Module(OverviewActivity activity) {
            this.activity = activity;
        }

        @Provides
        @ActivityScope
        OverviewContract.View provideOverviewView() {
            return activity;
        }

        @Provides
        @ActivityScope
        OverviewContract.Presenter provideOverviewPresenter(RxSchedulers rxSchedulers,
                ProfileRepository repository, OverviewContract.View view,
                OverviewContract.Navigator navigator) {
            return new OverviewPresenter(rxSchedulers, repository, view, navigator);
        }

        @Provides
        @ActivityScope
        OverviewContract.Navigator provideOverviewNavigator() {
            return new OverviewNavigator(activity);
        }
    }
}
