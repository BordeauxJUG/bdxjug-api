package org.bdxjug.dashboard.meetings;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assume.assumeThat;

public class MeetupAPITest {

    @Test
    public void test_api() {
        assumeThat(MeetupAPI.apiKey(), notNullValue());
        MeetupAPI.api().pastEvents("BordeauxJUG").stream().map(e -> e.name).forEach(System.out::println);
    }

}