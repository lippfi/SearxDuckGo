/*
 * Copyright (c) 2017 DuckDuckGo
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

package com.duckduckgo.app.di

import com.duckduckgo.app.autocomplete.api.SearxAutoCompleteResultJsonAdapter
import com.duckduckgo.app.trackerdetection.api.ActionJsonAdapter
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.httpsupgrade.impl.HttpsFalsePositivesJsonAdapter
import com.duckduckgo.privacy.config.impl.network.JSONObjectAdapter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module
object JsonModule {

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun moshi(): Moshi = Moshi.Builder()
        .add(ActionJsonAdapter())
        .add(SearxAutoCompleteResultJsonAdapter())
        // FIXME we should not access HttpsFalsePositivesJsonAdapter directly here because it's in impl module
        .add(HttpsFalsePositivesJsonAdapter())
        .add(JSONObjectAdapter())
        .build()
}
