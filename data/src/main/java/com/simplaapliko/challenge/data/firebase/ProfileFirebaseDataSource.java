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

package com.simplaapliko.challenge.data.firebase;

import com.google.firebase.database.FirebaseDatabase;
import com.simplaapliko.challenge.data.datasource.ProfileDataSource;
import com.simplaapliko.challenge.data.firebase.rx.CreateProfileCompletable;
import com.simplaapliko.challenge.data.firebase.rx.CreateProfileObservable;
import com.simplaapliko.challenge.data.firebase.rx.DeleteProfileCompletable;
import com.simplaapliko.challenge.data.firebase.rx.DeleteUpdateProfileObservable;
import com.simplaapliko.challenge.data.firebase.rx.GetProfilesSingle;
import com.simplaapliko.challenge.data.firebase.rx.UpdateProfileCompletable;
import com.simplaapliko.challenge.domain.model.Filter;
import com.simplaapliko.challenge.domain.model.Pair;
import com.simplaapliko.challenge.domain.model.Profile;
import com.simplaapliko.challenge.domain.model.SortOrder;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class ProfileFirebaseDataSource implements ProfileDataSource {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    public Single<List<Profile>> getProfiles(Filter filter, SortOrder sortOrder) {
        return GetProfilesSingle.create(database, filter, sortOrder);
    }

    @Override
    public Observable<Pair<Profile, Integer>> observeProfilesChanges(Filter filter) {
        return Observable.merge(CreateProfileObservable.create(database, filter),
                DeleteUpdateProfileObservable.create(database, filter));
    }

    @Override
    public Completable addProfile(Profile profile) {
        return CreateProfileCompletable.create(database, new ProfileEntity(profile));
    }

    @Override
    public Completable deleteProfile(Profile profile) {
        return DeleteProfileCompletable.create(database, new ProfileEntity(profile));
    }

    @Override
    public Completable updateProfile(Profile profile) {
        return UpdateProfileCompletable.create(database, new ProfileEntity(profile));
    }
}
