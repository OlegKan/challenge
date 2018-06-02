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

package com.simplaapliko.challenge.domain.model;

import java.util.Comparator;

public final class ProfileComparator {

    private ProfileComparator () {}

    public static final class IdAsc implements Comparator<Profile> {
        @Override
        public int compare(Profile p1, Profile p2) {
            return p1.getId() - p2.getId();
        }
    }

    public static final class AgeAsc implements Comparator<Profile> {
        @Override
        public int compare(Profile p1, Profile p2) {
            return p1.getAge() - p2.getAge();
        }
    }

    public static final class AgeDesc implements Comparator<Profile> {
        @Override
        public int compare(Profile p1, Profile p2) {
            return p2.getAge() - p1.getAge();
        }
    }

    public static final class NameAsc implements Comparator<Profile> {
        @Override
        public int compare(Profile p1, Profile p2) {
            return p1.getName().compareTo(p2.getName());
        }
    }

    public static final class NameDesc implements Comparator<Profile> {
        @Override
        public int compare(Profile p1, Profile p2) {
            return p2.getName().compareTo(p1.getName());
        }
    }
}
