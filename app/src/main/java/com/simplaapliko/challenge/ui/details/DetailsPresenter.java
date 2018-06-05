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

import com.simplaapliko.challenge.domain.repository.ImageRepository;
import com.simplaapliko.challenge.domain.repository.ProfileRepository;
import com.simplaapliko.challenge.rx.RxSchedulers;
import com.simplaapliko.challenge.ui.details.validation.ErrorMessageFactory;
import com.simplaapliko.challenge.ui.details.validation.ProfileModelValidator;
import com.simplaapliko.challenge.ui.details.validation.ProfileValidationErrorModel;
import com.simplaapliko.challenge.ui.details.validation.ProfileValidationException;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class DetailsPresenter implements DetailsContract.Presenter {

    private final RxSchedulers rxSchedulers;
    private final ImageRepository imageRepository;
    private final ProfileRepository profileRepository;
    private final ProfileModelValidator validator;
    private final ErrorMessageFactory errorMessageFactory;
    private final DetailsContract.View view;
    private final DetailsContract.Navigator navigator;
    private final ProfileModel profile;

    private final CompositeDisposable disposables = new CompositeDisposable();

    DetailsPresenter(RxSchedulers rxSchedulers, ImageRepository imageRepository,
            ProfileRepository profileRepository, ProfileModelValidator validator,
            ErrorMessageFactory errorMessageFactory, DetailsContract.View view,
            DetailsContract.Navigator navigator, ProfileModel profile) {
        this.rxSchedulers = rxSchedulers;
        this.imageRepository = imageRepository;
        this.profileRepository = profileRepository;
        this.validator = validator;
        this.errorMessageFactory = errorMessageFactory;
        this.view = view;
        this.navigator = navigator;
        this.profile = profile;
    }

    @Override
    public void init() {
        bindView();

        view.setFormEnabled(profile.isNew());
        view.displayProfile(profile);
        if (!"".equals(profile.getImagePath())) {
            view.displayProfileImage(profile.getImagePath());
        }
    }

    private void bindView() {
        Disposable imageSelected = view.onImageSelected()
                .observeOn(rxSchedulers.getMainThreadScheduler())
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(this::handleImageSelectedAction, throwable -> handleUnknownError());
        disposables.add(imageSelected);

        Disposable saveProfile = view.onSaveProfileClick()
                .observeOn(rxSchedulers.getMainThreadScheduler())
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(o -> handleSaveProfileAction(), throwable -> handleUnknownError());
        disposables.add(saveProfile);

        Disposable selectImage = view.onSelectImageClick()
                .observeOn(rxSchedulers.getMainThreadScheduler())
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(o -> handleSelectImageAction(), throwable -> handleUnknownError());
        disposables.add(selectImage);
    }

    private void handleImageSelectedAction(String path) {
        view.displayProfileImage(path);
        profile.setTempImagePath(path);
    }

    private void handleSelectImageAction() {
        navigator.goToSelectImageScreen();
    }

    private void handleSaveProfileAction() {
        view.showProgress();
        if (profile.isNew()) {
            profile.setAge(view.getAge());
            profile.setGender(view.getGender());
            profile.setHobbies(view.getHobbies());
            profile.setName(view.getName());

            Disposable addProfile = validator.validate(profile)
                    .flatMap(p -> imageRepository.uploadImage(p.getTempImagePath()))
                    .flatMapCompletable(path -> {
                        profile.setImagePath(path);
                        return profileRepository.addProfile(profile.toProfile());
                    })
                    .compose(rxSchedulers.getIoToMainTransformerCompletable())
                    .subscribe(this::handleModifyProfileSuccess, this::handleAddProfileError);
            disposables.add(addProfile);
        } else {
            if (profile.getHobbies()
                    .equals(view.getHobbies())) {
                handleModifyProfileSuccess();
                return;
            }

            profile.setHobbies(view.getHobbies());

            Disposable updateProfile = profileRepository.updateProfile(profile.toProfile())
                    .compose(rxSchedulers.getIoToMainTransformerCompletable())
                    .subscribe(this::handleModifyProfileSuccess, this::handleUpdateProfileError);
            disposables.add(updateProfile);
        }
    }

    private void handleModifyProfileSuccess() {
        view.hideProgress();
        navigator.goBack();
    }

    private void handleAddProfileError(Throwable throwable) {
        view.hideProgress();
        if (throwable instanceof ProfileValidationException) {
            handleValidationErrors(((ProfileValidationException) throwable).getErrors());
        } else {
            view.showMessage(errorMessageFactory.getGenericErrorMessage());
        }
    }

    private void handleValidationErrors(List<ProfileValidationErrorModel> errors) {
        for (ProfileValidationErrorModel error : errors) {
            handleValidationError(error);
        }
    }

    private void handleValidationError(ProfileValidationErrorModel error) {
        if (error.getField() == ProfileValidationErrorModel.Field.AGE) {
            view.showAgeError(errorMessageFactory.getErrorMessage(error));
        } else if (error.getField() == ProfileValidationErrorModel.Field.GENDER) {
            view.showGenderError(errorMessageFactory.getErrorMessage(error));
        } else if (error.getField() == ProfileValidationErrorModel.Field.NAME) {
            view.showNameError(errorMessageFactory.getErrorMessage(error));
        } else {
            view.showMessage(errorMessageFactory.getGenericErrorMessage());
        }
    }

    private void handleUpdateProfileError(Throwable throwable) {
        view.hideProgress();
        view.showMessage(errorMessageFactory.getGenericErrorMessage());
    }

    private void handleUnknownError() {
        view.showMessage(errorMessageFactory.getGenericErrorMessage());
    }

    @Override
    public void destroy() {
        disposables.dispose();
    }

    @Override
    public void bindMenu() {
        Disposable deleteProfile = view.onDeleteProfileClick()
                .observeOn(rxSchedulers.getMainThreadScheduler())
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(o -> handleDeleteProfileAction(), throwable -> handleUnknownError());
        disposables.add(deleteProfile);
    }

    private void handleDeleteProfileAction() {
        view.showProgress();
        Disposable deleteProfile = imageRepository.deleteImage(profile.getImagePath())
                .andThen(profileRepository.deleteProfile(profile.toProfile()))
                .compose(rxSchedulers.getIoToMainTransformerCompletable())
                .subscribe(this::handleModifyProfileSuccess, this::handleDeleteProfileError);
        disposables.add(deleteProfile);
    }

    private void handleDeleteProfileError(Throwable throwable) {
        view.hideProgress();
        view.showMessage(errorMessageFactory.getGenericErrorMessage());
    }
}
