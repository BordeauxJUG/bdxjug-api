package org.bdxjug.api.infrastructure.sheet;

import org.bdxjug.api.domain.sponsors.Sponsor;
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
public class SheetSponsorRepositoryTest {

    @Autowired
    private SheetSponsorRepository repository;

    @Test
    public void should_return_devcoop() {
        Optional<Sponsor> devcoop = repository.all().stream().filter(s -> s.getName().equals("DEVCOOP")).findFirst();

        assertThat(devcoop.isPresent()).isTrue();
    }
}