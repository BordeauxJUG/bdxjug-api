package org.bdxjug.api.infrastructure.sheet;

import org.bdxjug.api.domain.members.Member;
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
public class SheetMemberRepositoryTest {

    @Autowired
    private SheetMemberRepository repository;

    @Test
    public void should_find_laurent() {
        Optional<Member> laurent = repository.all().stream().filter(m -> m.getFirstName().equals("Laurent") && m.getLastName().equals("Forêt")).findFirst();

        assertThat(laurent.isPresent()).isTrue();
        assertThat(laurent.get().getFirstRegistration()).isEqualByComparingTo(LocalDate.of(2011,11,2));
        assertThat(laurent.get().getEndOfValidity()).isEqualByComparingTo(LocalDate.of(2017,10,30));
    }
}