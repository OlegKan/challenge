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

import io.reactivex.Observable;

public interface DetailsContract {

    int REQUEST_CODE_PICK_IMAGE = 200;

    interface Navigator {
        void goBack();

        void goToSelectImageScreen();
    }

    interface Presenter {
        void init();

        void destroy();

        void bindMenu();
    }

    interface View {
        void hideProgress();

        void showProgress();

        void displayProfile(ProfileModel profile);

        void displayProfileImage(String path);

        void setFormEnabled(boolean enabled);

        String getAge();

        int getGender();

        String getHobbies();

        String getName();

        void showAgeError(String error);

        void showGenderError(String error);

        void showNameError(String error);

        void showMessage(String message);

        Observable<Object> onDeleteProfileClick();

        Observable<String> onImageSelected();

        Observable<Object> onSaveProfileClick();

        Observable<Object> onSelectImageClick();
    }
}
