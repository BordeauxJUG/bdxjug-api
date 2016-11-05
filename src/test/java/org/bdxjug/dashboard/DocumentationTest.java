package org.bdxjug.dashboard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bdxjug.dashboard.meetings.Meeting;
import org.bdxjug.dashboard.members.Member;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DocumentationTest {

    private static final String PROJECT_DIR = System.getProperty("user.dir");
    private static Gson gson;

    @BeforeClass
    public static void init() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        Paths.get(PROJECT_DIR, "target", "generated-docs").toFile().mkdirs();
    }

    private Path writeJson(String file, String json) throws IOException {
        return Files.write(Paths.get(PROJECT_DIR, "target", "generated-docs", file), json.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void meetings() throws IOException, InterruptedException {
        Meeting meeting1 = new Meeting("228184140", "Quickie Party", LocalDate.of(2016, 1, 21));
        meeting1.setAttendance(36);
        Meeting meeting2 = new Meeting("228464263", "De Zéro à Héros avec Spring Boot", LocalDate.of(2016, 2, 10));
        meeting2.setAttendance(52);

        writeJson("meetings.json", gson.toJson(Arrays.asList(meeting1, meeting2)));
    }

    @Test
    public void members() throws IOException, InterruptedException {
        Member member1 = new Member("John", "Doe");
        member1.setEndOfValidity(LocalDate.of(2017, 4, 16));
        Member member2 = new Member("Jean", "Dupont");
        member2.setEndOfValidity(LocalDate.of(2018, 9, 23));
        Member member3 = new Member("Michel", "Durand");
        member3.setEndOfValidity(LocalDate.of(2016, 8, 4));

        writeJson("members.json", gson.toJson(Arrays.asList(member1, member2, member3)));
    }

    @Test
    public void attendees() throws IOException, InterruptedException {
        Map<String, Long> map = new LinkedHashMap<>();
        map.put("John Doe", 23L);
        map.put("Jean Dupont", 15L);
        map.put("Michel Durand", 10L);

        writeJson("attendees.json", gson.toJson(map));
    }
}