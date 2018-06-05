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
import com.simplaapliko.challenge.domain.repository.ImageRepository;
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
        private final ProfileModel profile;

        public Module(DetailsActivity activity, ProfileModel profile) {
            this.activity = activity;
            this.profile = safeCheck(profile);
        }

        private ProfileModel safeCheck(ProfileModel profile) {
            if (profile == null) {
                return new ProfileModel();
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
                ImageRepository imageRepository, ProfileRepository profileRepository,
                ProfileModelValidator validator, ErrorMessageFactory errorMessageFactory,
                DetailsContract.View view, DetailsContract.Navigator navigator) {
            return new DetailsPresenter(rxSchedulers, imageRepository, profileRepository, validator,
                    errorMessageFactory, view, navigator, profile);
        }

        @Provides
        @ActivityScope
        DetailsContract.Navigator provideDetailsNavigator() {
            return new DetailsNavigator(activity);
        }
    }
}

