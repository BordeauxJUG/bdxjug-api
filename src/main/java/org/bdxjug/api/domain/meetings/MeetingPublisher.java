package org.bdxjug.api.domain.meetings;

public interface MeetingPublisher {

    RegistrationID publish(Meeting meeting);

    String registerLink(RegistrationID registrationID);
}
