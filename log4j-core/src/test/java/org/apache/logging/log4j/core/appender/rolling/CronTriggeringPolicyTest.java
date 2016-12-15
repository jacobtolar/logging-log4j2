/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */

package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.NullConfiguration;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.junit.Assert;
import org.junit.Test;

public class CronTriggeringPolicyTest {

    private static final NullConfiguration NULL_CONFIGURATION = new NullConfiguration();

    private CronTriggeringPolicy createPolicy() {
        return CronTriggeringPolicy.createPolicy(NULL_CONFIGURATION, Boolean.TRUE.toString(), "0 0 0 * * ?");
    }

    private DefaultRolloverStrategy createStrategy() {
        return DefaultRolloverStrategy.createStrategy("7", "1", "max", null, null, false, NULL_CONFIGURATION);
    }

    /**
     * Tests LOG4J2-1474 CronTriggeringPolicy raise exception and fail to rollover log file when evaluateOnStartup is
     * true.
     */
    @Test
    public void testBuilder() {
        testBuilder(NULL_CONFIGURATION);
    }

    public void testBuilder(final Configuration configuration) {
        // @formatter:off
        final RollingFileAppender raf = RollingFileAppender.newBuilder()
            .withName("test")
            .withFileName("testcmd.log")
            .withFilePattern("testcmd.log.%d{yyyy-MM-dd}")
            .withPolicy(createPolicy())
            .withStrategy(createStrategy())
            .setConfiguration(configuration)
            .build();
        // @formatter:on
        Assert.assertNotNull(raf);
    }

    /**
     * Tests LOG4J2-1474 CronTriggeringPolicy raise exception and fail to rollover log file when evaluateOnStartup is
     * true.
     */
    @Test
    public void testBuilderSequence() {
        testBuilder(NULL_CONFIGURATION);
        testBuilder(NULL_CONFIGURATION);
    }

    /**
     * Tests LOG4J2-1474 CronTriggeringPolicy raise exception and fail to rollover log file when evaluateOnStartup is
     * true.
     */
    @Test
    public void testFactoryMethod() {
        final CronTriggeringPolicy triggerPolicy = createPolicy();
        final DefaultRolloverStrategy rolloverStrategy = createStrategy();

        try (RollingFileManager fileManager = RollingFileManager.getFileManager("testcmd.log",
                "testcmd.log.%d{yyyy-MM-dd}", true, true, triggerPolicy, rolloverStrategy, null,
                PatternLayout.createDefaultLayout(), 0, true, false, null)) {
            // trigger rollover
            fileManager.initialize();
            fileManager.rollover();
        }
    }
}