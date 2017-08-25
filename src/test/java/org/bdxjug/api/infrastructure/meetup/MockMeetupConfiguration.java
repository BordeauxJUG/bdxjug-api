package org.bdxjug.api.infrastructure.meetup;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class MockMeetupConfiguration implements MeetupConfiguration {

    @Override
    public MeetupClient.Reader reader() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MeetupClient.Admin admin(String authorizationCode) {
        throw new UnsupportedOperationException();
    }
}