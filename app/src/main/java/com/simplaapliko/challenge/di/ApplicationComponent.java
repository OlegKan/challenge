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

import com.simplaapliko.challenge.App;
import com.simplaapliko.challenge.ui.details.DetailsComponent;
import com.simplaapliko.challenge.ui.overview.OverviewComponent;

import dagger.Component;

@ApplicationScope
@Component(modules = {ApplicationModule.class, DataModule.class, UtilsModule.class})
public interface ApplicationComponent {
    void inject(App app);

    DetailsComponent plus(DetailsComponent.Module module);

    OverviewComponent plus(OverviewComponent.Module module);
}
