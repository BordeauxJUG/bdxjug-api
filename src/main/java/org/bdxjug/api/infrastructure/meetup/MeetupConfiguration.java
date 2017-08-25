package org.bdxjug.api.infrastructure.meetup;

public interface MeetupConfiguration {

    MeetupClient.Reader reader();

    MeetupClient.Admin admin(String authorizationCode);
}
