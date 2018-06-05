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

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

public class DeleteImageCompletable implements CompletableOnSubscribe {

    private final FirebaseStorage firebaseStorage;
    private final String path;

    public static Completable create(FirebaseStorage firebaseStorage, String path) {
        return Completable.create(new DeleteImageCompletable(firebaseStorage, path));
    }

    private DeleteImageCompletable(FirebaseStorage firebaseStorage, String path) {
        this.firebaseStorage = firebaseStorage;
        this.path = path;
    }

    @Override
    public void subscribe(CompletableEmitter emitter) {
        if (path == null || path.equals("")) {
            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        }

        Uri file = Uri.parse(path);
        String path = file.getLastPathSegment();

        if (path == null) {
            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
            return;
        }

        StorageReference reference = firebaseStorage.getReference()
                .child(path);

        reference.delete()
                .addOnSuccessListener(aVoid -> {
                    if (!emitter.isDisposed()) {
                        emitter.onComplete();
                    }
                })
                .addOnFailureListener(exception -> {
                    if (exception instanceof StorageException) {
                        int errorCode = ((StorageException) exception).getErrorCode();
                        if (errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {
                            if (!emitter.isDisposed()) {
                                emitter.onComplete();
                            }
                        } else {
                            if (!emitter.isDisposed()) {
                                emitter.onError(exception);
                            }
                        }
                    } else {
                        if (!emitter.isDisposed()) {
                            emitter.onError(exception);
                        }
                    }
                });
    }
}
