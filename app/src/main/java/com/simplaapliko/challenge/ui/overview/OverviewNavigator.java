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

package com.simplaapliko.challenge.ui.overview;

import android.widget.Toast;

import com.simplaapliko.challenge.domain.model.Profile;

public class OverviewNavigator implements OverviewContract.Navigator {

    private final OverviewActivity activity;

    OverviewNavigator(OverviewActivity activity) {
        this.activity = activity;
    }

    @Override
    public void goToAddProfileScreen() {
        Toast.makeText(activity, "add profile clicked", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void goToEditProfileScreen(Profile model) {
        Toast.makeText(activity, "profile clicked", Toast.LENGTH_SHORT)
                .show();
    }
}
