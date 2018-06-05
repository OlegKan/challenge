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

package com.simplaapliko.challenge.data.firebase.rx;

import android.support.annotation.NonNull;

import com.simplaapliko.challenge.data.firebase.ProfileEntity;
import com.simplaapliko.challenge.domain.model.Filter;
import com.simplaapliko.challenge.domain.model.Profile;
import com.simplaapliko.challenge.domain.model.SortOrder;

final class Utils {

    public static final String PATH_IMAGES = "images";
    public static final String PATH_PROFILE = "profile";

    private Utils() { }

    static String getGenderPath(Filter filter) {
        String path;
        if (filter == Filter.FEMALE) {
            path = "female";
        } else {
            path = "male";
        }
        return path;
    }


    static String getGenderPath(ProfileEntity profile) {
        if (profile.gender == Profile.GENDER_FEMALE) {
            return "female";
        } else {
            return "male";
        }
    }

    @NonNull
    static String getSortOrder(SortOrder sortOrder) {
        String sortBy;
        switch (sortOrder) {
            case ID_ASC:
                sortBy = "id";
                break;
            case AGE_ASC:
                sortBy = "age";
                break;
            case AGE_DESC:
                sortBy = "age";
                break;
            case NAME_ASC:
                sortBy = "name";
                break;
            case NAME_DESC:
                sortBy = "name";
                break;
            default:
                sortBy = "id";
                break;
        }
        return sortBy;
    }
}
