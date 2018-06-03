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

package com.simplaapliko.challenge.data.firebase.rx;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simplaapliko.challenge.data.datasource.ProfileDataSource;
import com.simplaapliko.challenge.data.firebase.ProfileEntity;
import com.simplaapliko.challenge.domain.model.Filter;
import com.simplaapliko.challenge.domain.model.Profile;
import com.simplaapliko.challenge.domain.model.ProfileComparator;
import com.simplaapliko.challenge.domain.model.SortOrder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

public class GetProfilesSingle implements SingleOnSubscribe<List<Profile>> {

    private final FirebaseDatabase firebaseDatabase;
    private final Filter filter;
    private final SortOrder sortOrder;

    public static Single<List<Profile>> create(FirebaseDatabase firebaseDatabase, Filter filter,
            SortOrder sortOrder) {
        return Single.create(new GetProfilesSingle(firebaseDatabase, filter, sortOrder));
    }

    private GetProfilesSingle(FirebaseDatabase firebaseDatabase, Filter filter,
            SortOrder sortOrder) {
        this.firebaseDatabase = firebaseDatabase;
        this.filter = filter;
        this.sortOrder = sortOrder;
    }

    @Override
    public void subscribe(SingleEmitter<List<Profile>> emitter) {
        String orderBy = Utils.getSortOrder(sortOrder);

        DatabaseReference reference;

        if (filter == Filter.ALL) {
            reference = firebaseDatabase.getReference()
                    .child(ProfileDataSource.PATH_PROFILE);
        } else {
            String genderPath = Utils.getGenderPath(filter);
            reference = firebaseDatabase.getReference()
                    .child(genderPath)
                    .child(ProfileDataSource.PATH_PROFILE);
        }

        reference.orderByChild(orderBy)
                .addValueEventListener(new ValueEventCallback(emitter));
    }

    private class ValueEventCallback implements ValueEventListener {

        private final SingleEmitter<List<Profile>> emitter;

        ValueEventCallback(SingleEmitter<List<Profile>> emitter) {
            this.emitter = emitter;
        }

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (!emitter.isDisposed()) {

                List<Profile> profiles = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ProfileEntity profile = snapshot.getValue(ProfileEntity.class);

                    if (profile != null) {
                        profiles.add(profile.toProfile());
                    }
                }

                if (sortOrder == SortOrder.AGE_DESC) {
                    Collections.sort(profiles, new ProfileComparator.AgeDesc());
                } else if (sortOrder == SortOrder.NAME_DESC) {
                    Collections.sort(profiles, new ProfileComparator.NameDesc());
                }

                if (!emitter.isDisposed()) {
                    emitter.onSuccess(profiles);
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }
}
