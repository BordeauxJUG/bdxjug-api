package org.bdxjug.api.domain.meetings;

public interface MeetingPublisher {

    RegistrationID publish(String accessToken, Meeting meeting);

    String registerLink(RegistrationID registrationID);
}
