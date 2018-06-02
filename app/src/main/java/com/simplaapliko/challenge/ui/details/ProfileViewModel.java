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

import android.os.Parcel;
import android.os.Parcelable;

import com.simplaapliko.challenge.domain.model.Profile;

public class ProfileViewModel implements Parcelable {

    public static final Creator<ProfileViewModel> CREATOR = new Creator<ProfileViewModel>() {
        @Override
        public ProfileViewModel createFromParcel(Parcel in) {
            return new ProfileViewModel(in);
        }

        @Override
        public ProfileViewModel[] newArray(int size) {
            return new ProfileViewModel[size];
        }
    };

    private boolean isNew;
    private int id;
    private int gender;
    private String name;
    private int age;
    private String imagePath;
    private String hobbies;

    public ProfileViewModel() {
        isNew = true;
        name = "";
        imagePath = "";
        hobbies = "";
    }

    public ProfileViewModel(Profile profile) {
        isNew = false;
        id = profile.getId();
        gender = profile.getGender();
        name = profile.getName();
        age = profile.getAge();
        imagePath = profile.getImagePath();
        hobbies = profile.getHobbies();
    }

    protected ProfileViewModel(Parcel in) {
        isNew = in.readByte() != 0;
        id = in.readInt();
        gender = in.readInt();
        name = in.readString();
        age = in.readInt();
        imagePath = in.readString();
        hobbies = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isNew ? 1 : 0));
        dest.writeInt(id);
        dest.writeInt(gender);
        dest.writeString(name);
        dest.writeInt(age);
        dest.writeString(imagePath);
        dest.writeString(hobbies);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public boolean isNew() {
        return isNew;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProfileViewModel that = (ProfileViewModel) o;

        if (isNew != that.isNew) return false;
        if (id != that.id) return false;
        if (gender != that.gender) return false;
        if (age != that.age) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (imagePath != null ? !imagePath.equals(that.imagePath) : that.imagePath != null)
            return false;
        return hobbies != null ? hobbies.equals(that.hobbies) : that.hobbies == null;
    }

    @Override
    public int hashCode() {
        int result = (isNew ? 1 : 0);
        result = 31 * result + id;
        result = 31 * result + gender;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + age;
        result = 31 * result + (imagePath != null ? imagePath.hashCode() : 0);
        result = 31 * result + (hobbies != null ? hobbies.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProfileViewModel{" + "isNew=" + isNew + ", id=" + id + ", gender=" + gender + ", " +
                "name='" + name + '\'' + ", age=" + age + ", imagePath='" + imagePath + '\'' + "," +
                " hobbies='" + hobbies + '\'' + '}';
    }
}
