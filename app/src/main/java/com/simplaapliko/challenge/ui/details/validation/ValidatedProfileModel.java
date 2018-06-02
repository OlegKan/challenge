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

import com.simplaapliko.challenge.ui.details.ProfileModel;

import java.util.List;

public class ValidatedProfileModel {
    private ProfileModel profileModel;
    private List<ProfileValidationErrorModel> errors;

    public ValidatedProfileModel(ProfileModel profileModel,
            List<ProfileValidationErrorModel> errors) {
        this.profileModel = profileModel;
        this.errors = errors;
    }

    public ProfileModel getProfileModel() {
        return profileModel;
    }

    public List<ProfileValidationErrorModel> getErrors() {
        return errors;
    }
}
