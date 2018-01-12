package org.bdxjug.api.infrastructure.sheet;

import org.bdxjug.api.domain.meetings.Location;
import org.bdxjug.api.domain.meetings.LocationID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class SheetLocationRepositoryTest {

    @Autowired
    private SheetLocationRepository repository;

    @Test
    public void should_find_enseirb() {
        Optional<Location> enseirb = repository.all().stream().filter(l -> l.getId().equals(new LocationID("ENSEIRB_A"))).findFirst();

        assertThat(enseirb.isPresent()).isTrue();
        assertThat(enseirb.get().getName()).isEqualTo("ENSEIRB");
        assertThat(enseirb.get().getAddress()).startsWith("1");
        assertThat(enseirb.get().getRoom()).isEqualTo("Amphi A");
        assertThat(enseirb.get().getGeo()).isNotNull();
    }
}