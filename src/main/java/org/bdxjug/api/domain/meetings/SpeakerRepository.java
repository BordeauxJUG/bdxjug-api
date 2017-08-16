package org.bdxjug.api.domain.meetings;

import java.util.List;
import java.util.Optional;

public interface SpeakerRepository {

    Optional<Speaker> by(SpeakerID speakerID);

    List<Speaker> all();
}
