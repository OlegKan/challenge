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

package com.simplaapliko.challenge.ui.overview;

import com.simplaapliko.challenge.domain.model.Filter;
import com.simplaapliko.challenge.domain.model.Profile;
import com.simplaapliko.challenge.domain.model.SortOrder;
import com.simplaapliko.challenge.domain.repository.ProfileRepository;
import com.simplaapliko.challenge.rx.RxSchedulers;
import com.simplaapliko.challenge.rx.TestRxSchedulers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OverviewPresenterTest {

    @Mock private ProfileRepository mockRepository;
    @Mock private OverviewContract.View mockView;
    @Mock private OverviewContract.Navigator mockNavigator;
    private OverviewPresenter presenter;

    @Before
    public void setUp() {
        when(mockView.getSelectedFilter()).thenReturn(Filter.ALL);
        when(mockView.onAddProfileClick()).thenReturn(Observable.never());
        when(mockView.onFilterChange()).thenReturn(Observable.never());
        when(mockView.onShowProfileClick()).thenReturn(Observable.never());
        when(mockView.onSortOrderChange()).thenReturn(Observable.never());

        RxSchedulers rxSchedulers = new TestRxSchedulers();
        presenter = new OverviewPresenter(rxSchedulers, mockRepository, mockView, mockNavigator);
    }

    @Test
    public void init() {
        List<Profile> profiles = new ArrayList<>();
        when(mockRepository.getProfiles(any(Filter.class), any(SortOrder.class))).thenReturn(
                Single.just(profiles));
        when(mockRepository.observeProfilesChanges(any(Filter.class))).thenReturn(
                Observable.never());

        presenter.init();

        // verify default filter and sort order
        verify(mockRepository, times(1)).getProfiles(Filter.ALL, SortOrder.ID_ASC);
        verify(mockView, times(1)).displayProfiles(profiles);
        verify(mockView, never()).showMessage(any());
    }
}
