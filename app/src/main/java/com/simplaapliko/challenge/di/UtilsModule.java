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

package com.simplaapliko.challenge.di;

import android.content.Context;

import com.simplaapliko.challenge.rx.AppRxSchedulers;
import com.simplaapliko.challenge.rx.RxSchedulers;
import com.simplaapliko.challenge.ui.details.validation.ErrorMessageFactory;
import com.simplaapliko.challenge.ui.details.validation.ProfileModelValidator;

import dagger.Module;
import dagger.Provides;

@Module
public class UtilsModule {

    @Provides
    @ApplicationScope
    static ErrorMessageFactory provideErrorMessageFactory(Context context) {
        return new ErrorMessageFactory(context);
    }

    @Provides
    @ApplicationScope
    static ProfileModelValidator provideProfileModelValidator() {
        return new ProfileModelValidator();
    }

    @Provides
    @ApplicationScope
    static RxSchedulers provideRxSchedulers() {
        return new AppRxSchedulers();
    }
}
