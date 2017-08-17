package org.bdxjug.api.infrastructure.sheet;

import org.bdxjug.api.domain.meetings.LocationID;
import org.bdxjug.api.domain.meetings.Meeting;
import org.bdxjug.api.domain.meetings.MeetingID;
import org.bdxjug.api.domain.meetings.SpeakerID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class SheetMeetingRepositoryTest {

    @Autowired
    private SheetMeetingRepository repository;

    @Test
    public void should_find_jhipster() {
        Optional<Meeting> jhipster = repository.all().stream().filter(m -> m.getId().equals(new MeetingID("20141204"))).findFirst();

        assertThat(jhipster.isPresent()).isTrue();
        assertThat(jhipster.get().getLocationID()).isEqualTo(new LocationID("ENSEIRB_A"));
        assertThat(jhipster.get().getSpeakerID()).isEqualTo(new SpeakerID("juliendubois"));
        assertThat(jhipster.get().getTitle()).isEqualTo("Soirée JHipster");
        assertThat(jhipster.get().getDate()).isEqualTo(LocalDate.of(2014, 12,4));
        assertThat(jhipster.get().getDescription()).startsWith("JHipster");
    }
}