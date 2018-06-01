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

public class Profile {

    public static final int AGE_MIN = 0;
    public static final int AGE_MAX = 125;

    public static final int GENDER_FEMALE = 0;
    public static final int GENDER_MALE = 1;

    private final int id;
    private final int gender;
    private final String name;
    private final int age;
    private final String imagePath;
    private final String hobbies;

    public Profile(int id, int gender, String name, int age, String imagePath, String hobbies) {
        validateAge(age);
        validateGender(gender);

        this.id = id;
        this.gender = gender;
        this.name = name;
        this.age = age;
        this.imagePath = imagePath;
        this.hobbies = hobbies;
    }

    public int getId() {
        return id;
    }

    public int getGender() {
        return gender;
    }

    public boolean isFemale() {
        return gender == GENDER_FEMALE;
    }

    public boolean isMale() {
        return gender == GENDER_MALE;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getHobbies() {
        return hobbies;
    }

    private void validateAge(int value) {
        if (value < AGE_MIN || value > AGE_MAX) {
            throw new IllegalArgumentException("invalid age: " + value);
        }
    }

    private void validateGender(int value) {
        if (value < GENDER_FEMALE || value > GENDER_MALE) {
            throw new IllegalArgumentException("invalid gender: " + value);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Profile profile = (Profile) o;

        if (id != profile.id) return false;
        if (gender != profile.gender) return false;
        if (age != profile.age) return false;
        if (name != null ? !name.equals(profile.name) : profile.name != null) return false;
        return imagePath != null ? imagePath.equals(profile.imagePath) : profile.imagePath == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + gender;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + age;
        result = 31 * result + (imagePath != null ? imagePath.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Profile{" + "id=" + id + ", gender=" + gender + ", name='" + name + '\'' + ", " +
                "age=" + age + ", imagePath='" + imagePath + '\'' + ", hobbies='" + hobbies +
                '\'' + '}';
    }
}
