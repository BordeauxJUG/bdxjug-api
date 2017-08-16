package org.bdxjug.api.domain.meetings;

import java.util.List;
import java.util.Optional;

public interface LocationRepository {

    Optional<Location> by(LocationID locationID);

    List<Location> all();

}
