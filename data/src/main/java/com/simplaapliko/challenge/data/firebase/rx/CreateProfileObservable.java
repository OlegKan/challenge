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
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.simplaapliko.challenge.data.firebase.ProfileEntity;
import com.simplaapliko.challenge.domain.model.Filter;
import com.simplaapliko.challenge.domain.model.Pair;
import com.simplaapliko.challenge.domain.model.Profile;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class CreateProfileObservable implements ObservableOnSubscribe<Pair<Profile, Integer>> {

    private final FirebaseDatabase firebaseDatabase;
    private final Filter filter;

    public static Observable<Pair<Profile, Integer>> create(FirebaseDatabase firebaseDatabase,
            Filter filter) {
        return Observable.create(new CreateProfileObservable(firebaseDatabase, filter));
    }

    private CreateProfileObservable(FirebaseDatabase firebaseDatabase, Filter filter) {
        this.firebaseDatabase = firebaseDatabase;
        this.filter = filter;
    }

    @Override
    public void subscribe(ObservableEmitter<Pair<Profile, Integer>> emitter) {

        DatabaseReference reference;

        if (filter == Filter.ALL) {
            reference = firebaseDatabase.getReference()
                    .child(Utils.PATH_PROFILE);
        } else {
            String genderPath = Utils.getGenderPath(filter);
            reference = firebaseDatabase.getReference()
                    .child(genderPath)
                    .child(Utils.PATH_PROFILE);
        }

        // observe only newly added profiles
        int startAt = (int) (System.currentTimeMillis() / 1000);
        reference.orderByChild("id")
                .startAt(startAt)
                .addChildEventListener(new ValueEventCallback(emitter));
    }

    private class ValueEventCallback implements ChildEventListener {

        private final ObservableEmitter<Pair<Profile, Integer>> emitter;

        ValueEventCallback(ObservableEmitter<Pair<Profile, Integer>> emitter) {
            this.emitter = emitter;
        }

        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            if (!emitter.isDisposed()) {
                ProfileEntity profile = dataSnapshot.getValue(ProfileEntity.class);

                if (profile != null) {
                    emitter.onNext(new Pair<>(profile.toProfile(), 0));
                }
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    }
}
