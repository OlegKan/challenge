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
import com.google.firebase.storage.StorageReference;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

public class UploadImageSingle implements SingleOnSubscribe<String> {

    private final FirebaseStorage firebaseStorage;
    private final String path;

    public static Single<String> create(FirebaseStorage firebaseStorage, String path) {
        return Single.create(new UploadImageSingle(firebaseStorage, path));
    }

    private UploadImageSingle(FirebaseStorage firebaseStorage, String path) {
        this.firebaseStorage = firebaseStorage;
        this.path = path;
    }

    @Override
    public void subscribe(SingleEmitter<String> emitter) {
        if (path == null || path.equals("")) {
            if (!emitter.isDisposed()) {
                emitter.onSuccess("");
                return;
            }
        }

        Uri file = Uri.parse(path);
        StorageReference reference = firebaseStorage.getReference()
                .child(Utils.PATH_IMAGES + "/" + file.getLastPathSegment());

        reference.putFile(file)
                .continueWithTask(task -> reference.getDownloadUrl())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        if (!emitter.isDisposed()) {
                            emitter.onSuccess(downloadUri.toString());
                        }
                    } else {
                        if (!emitter.isDisposed()) {
                            emitter.onError(task.getException());
                        }
                    }
                });
    }
}
