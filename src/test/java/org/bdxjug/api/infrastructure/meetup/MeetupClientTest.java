package org.bdxjug.api.infrastructure.meetup;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Ignore // need meetup api key
@SpringBootTest
@RunWith(SpringRunner.class)
public class MeetupClientTest {

    @Autowired
    MeetupClient meetupClient;

    @Test
    public void test_api() {
        meetupClient.events("BordeauxJUG","past").stream().map(e -> e.name).forEach(System.out::println);
    }

}