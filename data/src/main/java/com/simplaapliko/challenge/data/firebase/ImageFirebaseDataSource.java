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

import com.google.firebase.storage.FirebaseStorage;
import com.simplaapliko.challenge.data.datasource.ImageDataSource;
import com.simplaapliko.challenge.data.firebase.rx.DeleteImageCompletable;
import com.simplaapliko.challenge.data.firebase.rx.UploadImageSingle;

import io.reactivex.Completable;
import io.reactivex.Single;

public class ImageFirebaseDataSource implements ImageDataSource {

    private FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    public Completable deleteImage(String path) {
        return DeleteImageCompletable.create(storage, path);
    }

    @Override
    public Single<String> uploadImage(String path) {
        return UploadImageSingle.create(storage, path);
    }
}
