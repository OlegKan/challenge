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

import com.simplaapliko.challenge.data.datasource.ImageDataSource;
import com.simplaapliko.challenge.domain.repository.ImageRepository;

import io.reactivex.Completable;
import io.reactivex.Single;

public class ImageDataRepository implements ImageRepository {

    private final ImageDataSource dataSource;

    public ImageDataRepository(ImageDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Completable deleteImage(String path) {
        return dataSource.deleteImage(path);
    }

    @Override
    public Single<String> uploadImage(String path) {
        return dataSource.uploadImage(path);
    }
}
