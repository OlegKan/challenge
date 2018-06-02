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
    private final ProfileRepository repository;
    private final ProfileModelValidator validator;
    private final ErrorMessageFactory errorMessageFactory;
    private final DetailsContract.View view;
    private final DetailsContract.Navigator navigator;
    private final ProfileViewModel profile;

    private final CompositeDisposable disposables = new CompositeDisposable();

    DetailsPresenter(RxSchedulers rxSchedulers, ProfileRepository repository,
            ProfileModelValidator validator, ErrorMessageFactory errorMessageFactory,
            DetailsContract.View view, DetailsContract.Navigator navigator,
            ProfileViewModel profile) {
        this.rxSchedulers = rxSchedulers;
        this.repository = repository;
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
    }

    private void bindView() {
        Disposable saveProfile = view.onSaveProfileClick()
                .observeOn(rxSchedulers.getMainThreadScheduler())
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(o -> handleSaveProfileAction(), throwable -> handleUnknownError());
        disposables.add(saveProfile);
    }

    private void handleSaveProfileAction() {
        if (profile.isNew()) {
            profile.setAge(Integer.valueOf(view.getAge()));
            profile.setGender(view.getGender());
            profile.setHobbies(view.getHobbies());
            profile.setName(view.getName());

            Disposable addProfile = validator.validate(profile)
                    .flatMapCompletable(
                            profileViewModel -> repository.addProfile(profileViewModel.toProfile()))
                    .compose(rxSchedulers.getIoToMainTransformerCompletable())
                    .subscribe(this::handleModifyProfileSuccess, this::handleAddProfileError);
            disposables.add(addProfile);
        } else {
            if (profile.getHobbies().equals(view.getHobbies())) {
                handleModifyProfileSuccess();
                return;
            }

            profile.setHobbies(view.getHobbies());

            Disposable updateProfile = repository.updateProfile(profile.toProfile())
                    .compose(rxSchedulers.getIoToMainTransformerCompletable())
                    .subscribe(this::handleModifyProfileSuccess, this::handleUpdateProfileError);
            disposables.add(updateProfile);
        }
    }

    private void handleModifyProfileSuccess() {
        navigator.goBack();
    }

    private void handleAddProfileError(Throwable throwable) {
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
        view.showMessage(errorMessageFactory.getGenericErrorMessage());
    }

    private void handleUnknownError() {
        view.showMessage(errorMessageFactory.getGenericErrorMessage());
    }

    @Override
    public void destroy() {

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
        Disposable deleteProfile = repository.deleteProfile(profile.toProfile())
                .compose(rxSchedulers.getIoToMainTransformerCompletable())
                .subscribe(this::handleModifyProfileSuccess, this::handleDeleteProfileError);
        disposables.add(deleteProfile);
    }

    private void handleDeleteProfileError(Throwable throwable) {
        view.showMessage(errorMessageFactory.getGenericErrorMessage());
    }
}
