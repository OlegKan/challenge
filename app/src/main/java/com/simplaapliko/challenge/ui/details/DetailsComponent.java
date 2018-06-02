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

import com.simplaapliko.challenge.di.ActivityScope;
import com.simplaapliko.challenge.domain.repository.ProfileRepository;
import com.simplaapliko.challenge.rx.RxSchedulers;
import com.simplaapliko.challenge.ui.details.validation.ErrorMessageFactory;
import com.simplaapliko.challenge.ui.details.validation.ProfileModelValidator;

import dagger.Provides;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = DetailsComponent.Module.class)
public interface DetailsComponent {

    void inject(DetailsActivity activity);

    @dagger.Module
    class Module {

        private final DetailsActivity activity;
        private final ProfileViewModel profile;

        public Module(DetailsActivity activity, ProfileViewModel profile) {
            this.activity = activity;
            this.profile = safeCheck(profile);
        }

        private ProfileViewModel safeCheck(ProfileViewModel profile) {
            if (profile == null) {
                return new ProfileViewModel();
            }
            return profile;
        }

        @Provides
        @ActivityScope
        DetailsContract.View provideDetailsView() {
            return activity;
        }

        @Provides
        @ActivityScope
        DetailsContract.Presenter provideDetailsPresenter(RxSchedulers rxSchedulers,
                ProfileRepository repository, ProfileModelValidator validator,
                ErrorMessageFactory errorMessageFactory, DetailsContract.View view,
                DetailsContract.Navigator navigator) {
            return new DetailsPresenter(rxSchedulers, repository, validator, errorMessageFactory,
                    view, navigator, profile);
        }

        @Provides
        @ActivityScope
        DetailsContract.Navigator provideDetailsNavigator() {
            return new DetailsNavigator(activity);
        }
    }
}
