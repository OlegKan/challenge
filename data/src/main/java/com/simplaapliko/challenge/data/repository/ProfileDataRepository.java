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

package com.simplaapliko.challenge.data.repository;

import com.simplaapliko.challenge.data.datasource.ProfileDataSource;
import com.simplaapliko.challenge.domain.model.Filter;
import com.simplaapliko.challenge.domain.model.Pair;
import com.simplaapliko.challenge.domain.model.Profile;
import com.simplaapliko.challenge.domain.model.SortOrder;
import com.simplaapliko.challenge.domain.repository.ProfileRepository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class ProfileDataRepository implements ProfileRepository {

    private final ProfileDataSource dataSource;

    public ProfileDataRepository(ProfileDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Single<List<Profile>> getProfiles(Filter filter, SortOrder sortOrder) {
        return dataSource.getProfiles(filter, sortOrder);
    }

    @Override
    public Observable<Pair<Profile, Integer>> observeProfilesChanges(Filter filter) {
        return dataSource.observeProfilesChanges(filter);
    }

    @Override
    public Completable addProfile(Profile profile) {
        return dataSource.addProfile(profile);
    }

    @Override
    public Completable deleteProfile(Profile profile) {
        return dataSource.deleteProfile(profile);
    }

    @Override
    public Completable updateProfile(Profile profile) {
        return dataSource.updateProfile(profile);
    }
}
