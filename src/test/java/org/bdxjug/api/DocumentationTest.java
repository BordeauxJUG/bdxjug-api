package org.bdxjug.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bdxjug.api.meetings.Geo;
import org.bdxjug.api.meetings.Location;
import org.bdxjug.api.meetings.Meeting;
import org.bdxjug.api.members.Member;
import org.bdxjug.api.speakers.Speaker;
import org.bdxjug.api.sponsors.Sponsor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class DocumentationTest {

    private static final String PROJECT_DIR = System.getProperty("user.dir");
    private static Gson gson;

    @BeforeClass
    public static void init() {
        gson = configureGson(true);
        Paths.get(PROJECT_DIR, "target", "generated-docs").toFile().mkdirs();
    }

    private static Gson configureGson(boolean pretty) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        if (pretty) {
            gsonBuilder.setPrettyPrinting();
        }
        return gsonBuilder.create();
    }


    private Path writeJson(String file, String json) throws IOException {
        return Files.write(Paths.get(PROJECT_DIR, "target", "generated-docs", file), json.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void meetings() throws IOException, InterruptedException {
        Meeting meeting1 = new Meeting("228184140", "Quickie Party", LocalDate.of(2016, 1, 21));
        meeting1.setAttendance(36);
        meeting1.setDescription("La soirée sera composée par trois quickies....");
        meeting1.setRegistrationLink("https://www.meetup.com/fr-FR/BordeauxJUG/events/228184140/");
        meeting1.setLocation(new Location("ENSEIRB", "1 Avenue du Docteur Albert Schweitzer, Talence", new Geo(44.80613708496094, -0.6075440049171448)));
        Meeting meeting2 = new Meeting("228464263", "De Zéro à Héros avec Spring Boot", LocalDate.of(2016, 2, 10));
        meeting2.setAttendance(52);
        meeting2.setDescription("Jeudi 31 mars 2016 à 19h00 dans l'amphi A de l'ENSEIRB MATMECA, Stéphane NICOLL ...");
        meeting2.setRegistrationLink("https://www.meetup.com/fr-FR/BordeauxJUG/events/182201392/");
        meeting2.setLocation(new Location("ENSEIRB", "1 Avenue du Docteur Albert Schweitzer, Talence", new Geo(44.80613708496094, -0.6075440049171448)));

        writeJson("meetings.json", gson.toJson(Stream.of(meeting1, meeting2).sorted(Comparator.comparing(Meeting::date).reversed()).collect(Collectors.toList())));
    }

    @Test
    public void members() throws IOException, InterruptedException {
        Member member1 = new Member("John", "Doe");
        member1.setFirstRegistration(LocalDate.of(2016, 4, 16));
        member1.setEndOfValidity(LocalDate.of(2017, 4, 16));
        Member member2 = new Member("Jean", "Dupont");
        member2.setFirstRegistration(LocalDate.of(2014, 9, 23));
        member2.setEndOfValidity(LocalDate.of(2018, 9, 23));
        Member member3 = new Member("Michel", "Durand");
        member3.setFirstRegistration(LocalDate.of(2015, 8, 4));
        member3.setEndOfValidity(LocalDate.of(2016, 8, 4));

        writeJson("members.json", gson.toJson(Arrays.asList(member1, member2, member3)));
    }

    @Test
    public void speakers() throws IOException, InterruptedException {
        Speaker speaker1 = new Speaker("Antonio Goncalves",
                "Antonio is a senior software architect specialized in Java/Java EE. As a consultant he advises customers in France and help them in defining their software architecture. Antonio is also the founder of the very successful Paris JUG and independent JCP member on JEE6 (JSR 316), JPA 2.0 (JSR 317) and EJB 3.1 (JSR 318). Based on all his experiences, he has written two books about Java EE 5 and recently on Java EE 6 with Glassfish. It explains, in a practical way, how to develop an application using most of the Java EE specifications.",
                "http://lh3.ggpht.com/_apLykarZbI4/StG6i1ESRkI/AAAAAAAAAVo/NcQUT_iz6Yg/s800/antoniogoncalves.gif");
        speaker1.setTwitter("@agoncal");
        Speaker speaker2 = new Speaker("Nicolas Martignole",
                "Nicolas est indépendant depuis 2008, auteur du blog le Touilleur Express et\\nmembre de l'équipe du Paris JUG depuis 2 ans.\\nIl travaille plutôt dans la Finance, en tant que technical team leader.\\nUn peu d'Agilité, pas mal de développements et de bonnes pratiques et surtout, du fun.\\nEn quelques mots, son parcours\\n5 ans dans une agence Web puis une Startup Java\\n6 ans chez Thomson-Reuters dans la Finance, développeur sénior puis technical team leader.\\ndepuis 2008 consultant indépendant.",
                "https://lh6.googleusercontent.com/_apLykarZbI4/TWLOWg9d5wI/AAAAAAAAAlo/ooEPmIxCHtc/s144/nicolas_martignole_big.jpg");
        Speaker speaker3 = new Speaker("David Gageot",
                "Bonjour, je suis David Gageot, développeur indépendant. Ma passion ? L'écriture de logiciels pointus mais simples. J'ai pour leitmotiv d'être un facilitateur qui, par ma créativité et mon expertise, aide les équipes à être plus innovantes et plus efficaces.\\nJava Champion, je participe à des projets Java depuis 1998. Google Developer Expert, j'utilise mes compétences Devops sur des projets cloud.\\n\\nVous suivez peut-être mon blog JavaBien! depuis 2004.",
                "http://www.gravatar.com/avatar/f0887bf6175ba40dca795eb37883a8cf?s=128");

        writeJson("speakers.json", gson.toJson(Arrays.asList(speaker1, speaker2, speaker3)));
    }

    @Test
    public void sponsors() throws IOException, InterruptedException {
        Sponsor sponsor1 = new Sponsor("Arca", "http://www.arca-computing.fr/", "https://lh3.googleusercontent.com/NukZwnl7xNhTUQoriXPDXFmwEBqPmnarQbxOwNmpOk4=w300-h117-no");
        Sponsor sponsor2 = new Sponsor("DevCoop", "http://dev.coop", "http://www.devcoop.fr/img/logo.png");

        writeJson("sponsors.json", gson.toJson(Arrays.asList(sponsor1, sponsor2)));
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