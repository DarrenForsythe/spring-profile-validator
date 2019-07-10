package com.darrenforsythe.profilevalidation;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;

public class ProfileValidatorTest {


    private ProfileValidator profileValidator;
    private ApplicationEnvironmentPreparedEvent event;
    private MockEnvironment environment;

    @Before
    public void setUp() throws Exception {
        profileValidator = new ProfileValidator();
        environment = new MockEnvironment();
        event = new ApplicationEnvironmentPreparedEvent(mock(SpringApplication.class), null, environment);
    }

    @Test
    public void onApplicationEventNoActiveProfiles() {


        assertThatCode(() -> profileValidator.onApplicationEvent(event)).doesNotThrowAnyException();
    }


    @Test
    public void onApplicationEventAllowedProfileCombo() {

        environment.setActiveProfiles("dev", "DB1");
        assertThatCode(() -> profileValidator.onApplicationEvent(event)).doesNotThrowAnyException();
    }


    @Test
    public void onApplicationEventAllowedProfileComboOfRandomProfiles() {
        environment.setActiveProfiles("production", "DB5", "DB6", "async", "whatever");
        assertThatCode(() -> profileValidator.onApplicationEvent(event)).doesNotThrowAnyException();
    }

    @Test
    public void onApplicationEventTooManyEnvironmentProfiles() {
        environment.setActiveProfiles("dev", "test", "DB1");
        assertThatCode(() -> profileValidator.onApplicationEvent(event)).isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    public void onApplicationEventTooManyDBProfiles() {


        environment.setActiveProfiles("dev", "DB1", "DB2");

        assertThatCode(() -> profileValidator.onApplicationEvent(event)).isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    public void onApplicationEventTooManyProfiles() {
        environment.setActiveProfiles("dev", "test", "DB1", "DB2");

        assertThatCode(() -> profileValidator.onApplicationEvent(event)).isInstanceOf(IllegalArgumentException.class);
    }
}