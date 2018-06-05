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

import com.google.firebase.database.FirebaseDatabase;
import com.simplaapliko.challenge.data.firebase.ProfileEntity;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

public class CreateProfileCompletable implements CompletableOnSubscribe {

    private final FirebaseDatabase firebaseDatabase;
    private final ProfileEntity profile;

    public static Completable create(FirebaseDatabase firebaseDatabase, ProfileEntity profile) {
        return Completable.create(new CreateProfileCompletable(firebaseDatabase, profile));
    }

    private CreateProfileCompletable(FirebaseDatabase firebaseDatabase, ProfileEntity profile) {
        this.firebaseDatabase = firebaseDatabase;
        this.profile = profile;
    }

    @Override
    public void subscribe(CompletableEmitter emitter) {
        String uid = String.valueOf(profile.id);

        Map<String, Object> data = new HashMap<>();
        data.put(Utils.PATH_PROFILE + "/" + uid, profile);
        data.put(Utils.getGenderPath(profile) + "/" + Utils.PATH_PROFILE + "/" + uid,
                profile);

        firebaseDatabase.getReference()
                .updateChildren(data, (databaseError, databaseReference) -> {
                    if (databaseError == null) {
                        if (!emitter.isDisposed()) {
                            emitter.onComplete();
                        }
                    } else {
                        if (!emitter.isDisposed()) {
                            emitter.onError(databaseError.toException());
                        }
                    }
                });
    }
}
