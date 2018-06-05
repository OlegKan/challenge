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

package com.simplaapliko.challenge.ui.details;

import android.content.Intent;

import static com.simplaapliko.challenge.ui.details.DetailsContract.REQUEST_CODE_PICK_IMAGE;

public class DetailsNavigator implements DetailsContract.Navigator {

    private final DetailsActivity activity;

    DetailsNavigator(DetailsActivity activity) {
        this.activity = activity;
    }

    @Override
    public void goBack() {
        activity.finish();
    }

    @Override
    public void goToSelectImageScreen() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        activity.startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }
}
