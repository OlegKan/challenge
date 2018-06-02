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

import android.content.Intent;

import com.simplaapliko.challenge.domain.model.Profile;
import com.simplaapliko.challenge.ui.details.DetailsActivity;
import com.simplaapliko.challenge.ui.details.ProfileModel;

public class OverviewNavigator implements OverviewContract.Navigator {

    private final OverviewActivity activity;

    OverviewNavigator(OverviewActivity activity) {
        this.activity = activity;
    }

    @Override
    public void goToAddProfileScreen() {
        Intent intent = DetailsActivity.getStartIntent(activity);
        activity.startActivity(intent);
    }

    @Override
    public void goToEditProfileScreen(Profile model) {
        Intent intent = DetailsActivity.getStartIntent(activity, new ProfileModel(model));
        activity.startActivity(intent);
    }
}
