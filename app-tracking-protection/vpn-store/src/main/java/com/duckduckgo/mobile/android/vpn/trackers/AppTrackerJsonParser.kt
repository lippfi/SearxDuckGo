/*
 * Copyright (c) 2021 DuckDuckGo
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

package com.duckduckgo.mobile.android.vpn.trackers

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

class AppTrackerJsonParser {

    companion object {

        fun parseAppTrackerJson(moshi: Moshi, json: String): AppTrackerBlocklist {
            val adapter: JsonAdapter<JsonAppBlockingList> =
                moshi.adapter(JsonAppBlockingList::class.java)
            val parsed = adapter.fromJson(json)
            val version = parseBlocklistVersion(parsed)
            val appTrackers = parseAppTrackers(parsed)
            val appPackages = parseAppPackages(parsed)
            val entities = parseTrackerEntities(parsed)
            return AppTrackerBlocklist(version, appTrackers, appPackages, entities)
        }

        private fun parseBlocklistVersion(parsed: JsonAppBlockingList?) = parsed?.version.orEmpty()

        fun parseAppTrackers(parsed: JsonAppBlockingList?) =
            parsed
                ?.trackers
                .orEmpty()
                .filter { it.value.defaultAction == "block" }
                .mapValues {
                    AppTracker(
                        hostname = it.key,
                        trackerCompanyId = it.value.owner.name.hashCode(),
                        owner = it.value.owner,
                        app = it.value.app,
                    )
                }
                .map { it.value }

        fun parseAppPackages(response: JsonAppBlockingList?): List<AppTrackerPackage> {
            return response
                ?.packageNames
                .orEmpty()
                .mapValues { AppTrackerPackage(packageName = it.key, entityName = it.value) }
                .map { it.value }
        }

        fun parseTrackerEntities(response: JsonAppBlockingList?): List<AppTrackerEntity> {
            return response
                ?.entities
                .orEmpty()
                .mapValues {
                    AppTrackerEntity(
                        trackerCompanyId = it.key.hashCode(),
                        entityName = it.key,
                        score = it.value.score,
                        signals = it.value.signals,
                    )
                }
                .map { it.value }
                .sortedBy { it.score }
        }
    }
}
