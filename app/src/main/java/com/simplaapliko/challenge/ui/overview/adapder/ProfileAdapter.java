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

package com.simplaapliko.challenge.ui.overview.adapder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simplaapliko.challenge.R;
import com.simplaapliko.challenge.domain.model.Profile;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileViewHolder> {

    @NonNull private List<Profile> data = new ArrayList<>();

    private ProfileViewHolder.ClickListener clickListener = new ProfileViewHolder.ClickListener() {
        @Override
        public void onItemClicked(int position) {
            onClickSubject.onNext(data.get(position));
        }
    };

    private PublishSubject<Profile> onClickSubject = PublishSubject.create();

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_profile, parent, false);
        return new ProfileViewHolder(parent.getContext(), view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Profile model = data.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(@NonNull List<Profile> data) {
        if (data == null) {
            this.data = new ArrayList<>();
        } else {
            this.data = new ArrayList<>(data);
        }
    }

    public Observable<Profile> getClickObservable() {
        return onClickSubject;
    }
}
