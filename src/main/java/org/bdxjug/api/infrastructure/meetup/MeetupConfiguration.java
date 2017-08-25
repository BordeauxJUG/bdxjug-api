package org.bdxjug.api.infrastructure.meetup;

public interface MeetupConfiguration {

    String authorizeUri();

    MeetupClient.Reader reader();

    MeetupClient.Admin admin(String authorizationCode);
}
