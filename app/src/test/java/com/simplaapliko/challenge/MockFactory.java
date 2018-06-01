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

package com.simplaapliko.challenge;

import com.simplaapliko.challenge.domain.model.Profile;

public class MockFactory {

    public static Profile newProfile() {
        String hobbies = "running, reading, travelling, photography, coding, music, fishing, " +
                "cycling, yoga, hiking, scuba diving";

        return new Profile(1, Profile.GENDER_FEMALE, "Anna", 40, "", hobbies);
    }
}
