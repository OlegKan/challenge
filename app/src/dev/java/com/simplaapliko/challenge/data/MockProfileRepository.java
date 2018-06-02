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

package com.simplaapliko.challenge.data;

import android.support.annotation.NonNull;

import com.simplaapliko.challenge.domain.model.Pair;
import com.simplaapliko.challenge.domain.model.Profile;
import com.simplaapliko.challenge.domain.repository.ProfileRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public class MockProfileRepository implements ProfileRepository {

    @NonNull private List<Profile> profilesCache = new ArrayList<>();

    @Override
    public Single<List<Profile>> getAllProfiles() {
        return Maybe.concat(getCachedProfiles(), getLiveProfiles())
                .filter(profiles -> !profiles.isEmpty())
                .first(new ArrayList<>());
    }

    @Override
    public Observable<Pair<Profile, Integer>> observeProfilesChanges() {
        return Observable.merge(observeAddedProfiles(), observeDeletedProfiles(),
                observeUpdatedProfiles());
    }

    private Observable<Pair<Profile, Integer>> observeAddedProfiles() {
        return Observable.intervalRange(27, 100, 0, 5, TimeUnit.SECONDS)
                .map(aLong -> Pair.create(generateNewProfile(aLong.intValue()), 0))
                .doOnNext(pair -> {
                    profilesCache.add(pair.first);
                });
    }

    private Observable<Pair<Profile, Integer>> observeDeletedProfiles() {
        return Observable.intervalRange(27, 100, 0, 9, TimeUnit.SECONDS)
                .map(aLong -> Pair.create(generateNewProfile(aLong.intValue()), 1))
                .doOnNext(pair -> {
                    profilesCache.remove(pair.first);
                });
    }

    private Observable<Pair<Profile, Integer>> observeUpdatedProfiles() {
        return Observable.intervalRange(27, 100, 0, 7, TimeUnit.SECONDS)
                .map(aLong -> Pair.create(generateUpdatedProfile(aLong.intValue()), 2))
                .doOnNext(pair -> {
                    if (profilesCache.contains(pair.first)) {
                        int index = profilesCache.indexOf(pair.first);
                        profilesCache.remove(index);
                        profilesCache.add(index, pair.first);
                    }
                });
    }

    private Maybe<List<Profile>> getCachedProfiles() {
        if (profilesCache.isEmpty()) {
            return Maybe.empty();
        } else {
            return Maybe.just(profilesCache);
        }
    }

    private Maybe<List<Profile>> getLiveProfiles() {
        return Maybe.fromCallable(() -> generateProfiles(25))
                .doOnSuccess(profiles -> profilesCache = new ArrayList<>(profiles));
    }

    private List<Profile> generateProfiles(int count) {
        List<Profile> profiles = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            Profile profile = generateProfile(i);
            profiles.add(profile);
        }
        return profiles;
    }

    private Profile generateProfile(int id) {
        int gender = id % 2 + 1;

        String name;
        if (gender == Profile.GENDER_FEMALE) {
            name = "Anna";
        } else {
            name = "Jack";
        }

        String hobbies = "running, reading, travelling, photography, coding, music, fishing, " +
                "cycling, yoga, hiking, scuba diving";

        return new Profile(id, gender, name, 40, "", hobbies);
    }

    private Profile generateNewProfile(int id) {

        String hobbies = "running";

        return new Profile(id, Profile.GENDER_MALE, "New", 40, "", hobbies);
    }

    private Profile generateUpdatedProfile(int id) {

        String hobbies = "reading, travelling";

        return new Profile(id, Profile.GENDER_MALE, "New", 40, "", hobbies);
    }
}
