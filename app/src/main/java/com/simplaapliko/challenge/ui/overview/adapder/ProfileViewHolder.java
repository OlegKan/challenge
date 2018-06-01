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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.simplaapliko.challenge.R;
import com.simplaapliko.challenge.domain.model.Profile;

class ProfileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public interface ClickListener {
        void onItemClicked(int position);
    }

    private final Context context;
    private final ClickListener clickListener;
    private View root;
    private ImageView image;
    private TextView profileInfo;
    private TextView hobbies;

    ProfileViewHolder(Context context, View itemView, ClickListener clickListener) {
        super(itemView);
        itemView.setOnClickListener(this);

        this.context = context;
        this.clickListener = clickListener;

        root = itemView.findViewById(R.id.root);
        image = itemView.findViewById(R.id.image);
        profileInfo = itemView.findViewById(R.id.profile_info);
        hobbies = itemView.findViewById(R.id.hobbies);
    }

    @Override
    public void onClick(View v) {
        clickListener.onItemClicked(getAdapterPosition());
    }

    void bind(Profile model) {
        if (model.isFemale()) {
            root.setBackgroundColor(context.getResources()
                    .getColor(R.color.color_female));
        } else {
            root.setBackgroundColor(context.getResources()
                    .getColor(R.color.color_male));
        }
        profileInfo.setText(String.format("%s (%s)", model.getName(), model.getAge()));
        hobbies.setText(model.getHobbies());
    }
}
