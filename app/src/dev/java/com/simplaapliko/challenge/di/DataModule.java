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

import com.simplaapliko.challenge.data.repository.MockProfileRepository;
import com.simplaapliko.challenge.domain.repository.ImageRepository;
import com.simplaapliko.challenge.domain.repository.ProfileRepository;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Completable;
import io.reactivex.Single;

@Module
class DataModule {

    @Provides
    @ApplicationScope
    ImageRepository provideImageRepository() {
        return new ImageRepository() {
            @Override
            public Completable deleteImage(String path) {
                return Completable.complete();
            }

            @Override
            public Single<String> uploadImage(String path) {
                return Single.just("");
            }
        };
    }

    @Provides
    @ApplicationScope
    ProfileRepository provideProfileRepository() {
        return new MockProfileRepository();
    }
}
