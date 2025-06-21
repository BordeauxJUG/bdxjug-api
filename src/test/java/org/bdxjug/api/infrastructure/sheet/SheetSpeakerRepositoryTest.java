package org.bdxjug.api.infrastructure.sheet;

import org.bdxjug.api.domain.meetings.Speaker;
import org.bdxjug.api.domain.meetings.SpeakerID;
import org.bdxjug.api.domain.meetings.SpeakerRepository;
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
public class SheetSpeakerRepositoryTest {

    @Autowired
    private SpeakerRepository speakerRepository;

    @Test
    public void should_return_juliendubois() {
        Optional<Speaker> juliendubois =
                speakerRepository.all().stream().filter(s -> s.getId().equals(new SpeakerID("juliendubois")))
                        .findFirst();

        assertThat(juliendubois.isPresent()).isTrue();
        assertThat(juliendubois.get().getFirstName()).isEqualTo("Julien");
        assertThat(juliendubois.get().getLastName()).isEqualTo("Dubois");
        assertThat(juliendubois.get().getTwitter()).isEqualTo("@juliendubois");
        assertThat(juliendubois.get().getUrlAvatar()).startsWith("http");
        assertThat(juliendubois.get().getBio()).startsWith("Julien").endsWith("</a>");
    }
}