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

package com.simplaapliko.challenge.ui.details.validation;

import android.content.Context;

import com.simplaapliko.challenge.R;

public class ErrorMessageFactory {

    private final Context context;

    public ErrorMessageFactory(Context context) {
        this.context = context;
    }

    public String getErrorMessage(ProfileValidationErrorModel errorModel) {
        String errorMessage;
        if (errorModel.getField() == ProfileValidationErrorModel.Field.AGE) {
            errorMessage = context.getString(R.string.validation_error_profile_age);
        } else if (errorModel.getField() == ProfileValidationErrorModel.Field.GENDER) {
            errorMessage = context.getString(R.string.validation_error_profile_gender);
        } else if (errorModel.getField() == ProfileValidationErrorModel.Field.NAME) {
            errorMessage = context.getString(R.string.validation_error_profile_name);
        } else {
            errorMessage = context.getString(R.string.error_unknown);
        }
        return errorMessage;
    }

    public String getGenericErrorMessage() {
        return context.getString(R.string.error_unknown);
    }
}