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

import com.simplaapliko.challenge.domain.model.Profile;
import com.simplaapliko.challenge.ui.details.ProfileViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.exceptions.Exceptions;

public class ProfileModelValidator {

    public Single<ProfileViewModel> validate(ProfileViewModel model) {
        return Single.just(model)
                .map(this::validateProfileModel)
                .map(validatedProfileModel -> {
                    if (!validatedProfileModel.getErrors()
                            .isEmpty()) {
                        throw Exceptions.propagate(
                                new ProfileValidationException(validatedProfileModel.getErrors()));
                    } else {
                        return validatedProfileModel.getProfileModel();
                    }
                });

    }

    private ValidatedProfileModel validateProfileModel(ProfileViewModel model) {
        List<ProfileValidationErrorModel> errors = new ArrayList<>();

        ProfileValidationErrorModel ageError = validateAge(model.getAge());
        if (ageError != null) {
            errors.add(ageError);
        }

        ProfileValidationErrorModel genderError = validateGender(model.getGender());
        if (genderError != null) {
            errors.add(genderError);
        }

        ProfileValidationErrorModel nameError = validateName(model.getName());
        if (nameError != null) {
            errors.add(nameError);
        }

        return new ValidatedProfileModel(model, errors);
    }

    private ProfileValidationErrorModel validateAge(int value) {
        try {
            Profile.validateAge(value);
            return null;
        } catch (IllegalArgumentException e) {
            return new ProfileValidationErrorModel(ProfileValidationErrorModel.Field.AGE);
        }
    }

    private ProfileValidationErrorModel validateGender(int value) {
        try {
            Profile.validateGender(value);
            return null;
        } catch (IllegalArgumentException e) {
            return new ProfileValidationErrorModel(ProfileValidationErrorModel.Field.GENDER);
        }
    }

    private ProfileValidationErrorModel validateName(String value) {
        if (value == null || value.isEmpty()) {
            return new ProfileValidationErrorModel(ProfileValidationErrorModel.Field.NAME);
        } else {
            return null;
        }
    }
}
