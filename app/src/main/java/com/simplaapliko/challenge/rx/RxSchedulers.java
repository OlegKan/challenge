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

package com.simplaapliko.challenge.rx;

import io.reactivex.CompletableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public abstract class RxSchedulers {

    abstract public Scheduler getMainThreadScheduler();

    abstract public Scheduler getIoScheduler();

    abstract public Scheduler getComputationScheduler();

    public <T> ObservableTransformer<T, T> getIoToMainTransformer() {
        return observable -> observable.subscribeOn(getIoScheduler())
                .observeOn(getMainThreadScheduler());
    }

    public CompletableTransformer getIoToMainTransformerCompletable() {
        return completable -> completable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public <T> SingleTransformer<T, T> getIoToMainTransformerSingle() {
        return single -> single.subscribeOn(getIoScheduler())
                .observeOn(getMainThreadScheduler());
    }

    public <T> ObservableTransformer<T, T> getComputationToMainTransformer() {
        return observable -> observable.subscribeOn(getComputationScheduler())
                .observeOn(getMainThreadScheduler());
    }

    public <T> SingleTransformer<T, T> getComputationToMainTransformerSingle() {
        return single -> single.subscribeOn(getComputationScheduler())
                .observeOn(getMainThreadScheduler());
    }
}
