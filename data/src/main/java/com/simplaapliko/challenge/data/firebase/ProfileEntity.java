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

package com.simplaapliko.challenge.data.firebase;

import com.simplaapliko.challenge.domain.model.Profile;

public class ProfileEntity {

    public String key;
    public int id;
    public int gender;
    public String name;
    public int age;
    public String imagePath;
    public String hobbies;

    public ProfileEntity() {
    }

    public ProfileEntity(Profile profile) {
        id = profile.getId();
        gender = profile.getGender();
        name = profile.getName();
        age = profile.getAge();
        imagePath = profile.getImagePath();
        hobbies = profile.getHobbies();
    }

    public Profile toProfile() {
        return new Profile(id, gender, name, age, imagePath, hobbies);
    }
}
