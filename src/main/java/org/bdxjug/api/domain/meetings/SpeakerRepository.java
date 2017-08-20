package org.bdxjug.api.domain.meetings;

import java.util.Optional;
import java.util.SortedSet;

public interface SpeakerRepository {

    Optional<Speaker> by(SpeakerID speakerID);

    SortedSet<Speaker> all();
}
