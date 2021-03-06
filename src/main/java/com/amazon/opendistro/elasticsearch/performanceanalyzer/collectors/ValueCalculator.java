/*
 * Copyright <2019> Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.amazon.opendistro.elasticsearch.performanceanalyzer.collectors;

import org.elasticsearch.action.admin.indices.stats.ShardStats;

@FunctionalInterface
interface ValueCalculator {
    long calculateValue(ShardStats shardStats);

    default long calculate(ShardStats shardStats, CachedStats cachedStats, String statsName) {
        long calculatedVal = calculateValue(shardStats);
        long cachedValue = 0;
        if (CachedStats.getCachableValues().contains(statsName)) {
            cachedValue = cachedStats.getValue(statsName);
            cachedStats.putValue(statsName, calculatedVal);
        }
        return calculatedVal - cachedValue;
    }
}
