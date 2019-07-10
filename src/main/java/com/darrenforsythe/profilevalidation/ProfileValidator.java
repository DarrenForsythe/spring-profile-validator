package com.darrenforsythe.profilevalidation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProfileValidator implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileValidator.class);

    private static final Set<String> DB_PROFILES = new HashSet<>(Arrays.asList("DB1", "DB2", "DB3"));

    private static final Set<String> ENVIRONMENT_PROFILES = new HashSet<>(Arrays.asList("dev", "test"));


    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent applicationEnvironmentPreparedEvent) {
        List<String> activeProfiles = Arrays.asList(applicationEnvironmentPreparedEvent.getEnvironment().getActiveProfiles());

        LOG.info("Validating Allowed Profiles - {}", activeProfiles);

        if (activeProfiles.size() > 1) {
            long count = activeProfiles.stream().filter(profile -> DB_PROFILES.contains(profile) || ENVIRONMENT_PROFILES.contains(profile)).count();

            LOG.debug("Counted {} profiles", count);

            if (count != 0 && activeProfiles.size() - 2 != (activeProfiles.size() - count)) {
                throw new IllegalArgumentException(String.format("Invalid Profiles detected for %s", activeProfiles.toString()));
            }
        }
    }
}
