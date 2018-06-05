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

package com.simplaapliko.challenge.domain;

import com.simplaapliko.challenge.domain.model.Profile;
import com.simplaapliko.challenge.domain.model.ProfileComparator;
import com.simplaapliko.challenge.domain.model.SortOrder;

import java.util.Comparator;

import io.reactivex.annotations.NonNull;

public final class Utils {

    private Utils() { }

    @NonNull
    public static Comparator<Profile> getComparator(SortOrder sortOrder) {
        Comparator<Profile> comparator;
        switch (sortOrder) {
            case ID_ASC:
                comparator = new ProfileComparator.IdAsc();
                break;
            case AGE_ASC:
                comparator = new ProfileComparator.AgeAsc();
                break;
            case AGE_DESC:
                comparator = new ProfileComparator.AgeDesc();
                break;
            case NAME_ASC:
                comparator = new ProfileComparator.NameAsc();
                break;
            case NAME_DESC:
                comparator = new ProfileComparator.NameDesc();
                break;
            default:
                comparator = new ProfileComparator.IdAsc();
                break;
        }
        return comparator;
    }
}
