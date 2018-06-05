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

package com.simplaapliko.challenge.di;

import com.simplaapliko.challenge.data.datasource.ImageDataSource;
import com.simplaapliko.challenge.data.datasource.ProfileDataSource;
import com.simplaapliko.challenge.data.firebase.ImageFirebaseDataSource;
import com.simplaapliko.challenge.data.firebase.ProfileFirebaseDataSource;
import com.simplaapliko.challenge.data.repository.ImageDataRepository;
import com.simplaapliko.challenge.data.repository.ProfileDataRepository;
import com.simplaapliko.challenge.domain.repository.ImageRepository;
import com.simplaapliko.challenge.domain.repository.ProfileRepository;

import dagger.Module;
import dagger.Provides;

@Module
class DataModule {

    @Provides
    @ApplicationScope
    ImageDataSource provideImageDataSource() {
        return new ImageFirebaseDataSource();
    }

    @Provides
    @ApplicationScope
    ImageRepository provideImageRepository(ImageDataSource dataSource) {
        return new ImageDataRepository(dataSource);
    }

    @Provides
    @ApplicationScope
    ProfileDataSource provideProfileDataSource() {
        return new ProfileFirebaseDataSource();
    }

    @Provides
    @ApplicationScope
    ProfileRepository provideProfileRepository(ProfileDataSource dataSource) {
        return new ProfileDataRepository(dataSource);
    }
}
