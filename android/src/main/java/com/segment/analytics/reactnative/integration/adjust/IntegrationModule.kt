/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Segment, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.segment.analytics.reactnative.integration.adjust

import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustAttribution
import com.adjust.sdk.AdjustConfig
import com.adjust.sdk.AdjustConfig.ENVIRONMENT_PRODUCTION
import com.adjust.sdk.AdjustConfig.ENVIRONMENT_SANDBOX
import com.facebook.react.bridge.*
import com.segment.analytics.reactnative.core.RNAnalytics
import com.segment.analytics.android.integrations.adjust.AdjustIntegration

class RNAnalyticsIntegration_AdjustModule(context: ReactApplicationContext): ReactContextBaseJavaModule(context) {

    private val ATTRIBUTION_TRACKER_TOKEN = "trackerToken"
    private val ATTRIBUTION_TRACKER_NAME = "trackerName"
    private val ATTRIBUTION_NETWORK = "network"
    private val ATTRIBUTION_CAMPAIGN = "campaign"
    private val ATTRIBUTION_ADGROUP = "adgroup"
    private val ATTRIBUTION_CREATIVE = "creative"
    private val ATTRIBUTION_CLICK_LABEL = "clickLabel"
    private val ATTRIBUTION_ADID = "adid"
    override fun getName() = "RNAnalyticsIntegration_Adjust"

    @ReactMethod
    fun setup() {
        RNAnalytics.addIntegration(AdjustIntegration.FACTORY)
    }

    @ReactMethod
    fun setupListener(token: String, environment: String, callback: Callback) {
        val adjustConfigEnv = if (environment === "production") ENVIRONMENT_PRODUCTION else ENVIRONMENT_SANDBOX
        val adjustConfig = AdjustConfig(reactApplicationContext, token, adjustConfigEnv)
        adjustConfig.setOnAttributionChangedListener { attribution -> callback.invoke(attributionToMap(attribution)) }
        Adjust.onCreate(adjustConfig)
    }

    @ReactMethod
    fun getAttribution(callback: Callback) {
        callback.invoke(attributionToMap(Adjust.getAttribution()))
    }
    
    private fun attributionToMap(attribution: AdjustAttribution?): WritableMap {
        val map = Arguments.createMap()
        if (null == attribution) {
            return map
        }

        val analytics = Analytics.with(reactApplicationContext);
        val campaign = ValueMap()
                .putValue("source", attribution.network)
                .putValue("name", attribution.campaign)
                .putValue("content", attribution.clickLabel)
                .putValue("adCreative", attribution.creative)
                .putValue("adGroup", attribution.adgroup)

        analytics.track("Install Attributed", Properties()
                .putValue("provider", "Adjust") //
                .putValue("trackerToken", attribution.trackerToken)
                .putValue("trackerName", attribution.trackerName)
                .putValue("campaign", campaign))


        map.putString(ATTRIBUTION_TRACKER_TOKEN, if (null != attribution.trackerToken) attribution.trackerToken else "")
        map.putString(ATTRIBUTION_TRACKER_NAME, if (null != attribution.trackerName) attribution.trackerName else "")
        map.putString(ATTRIBUTION_NETWORK, if (null != attribution.network) attribution.network else "")
        map.putString(ATTRIBUTION_CAMPAIGN, if (null != attribution.campaign) attribution.campaign else "")
        map.putString(ATTRIBUTION_ADGROUP, if (null != attribution.adgroup) attribution.adgroup else "")
        map.putString(ATTRIBUTION_CREATIVE, if (null != attribution.creative) attribution.creative else "")
        map.putString(ATTRIBUTION_CLICK_LABEL, if (null != attribution.clickLabel) attribution.clickLabel else "")
        map.putString(ATTRIBUTION_ADID, if (null != attribution.adid) attribution.adid else "")
        return map
    }
}
