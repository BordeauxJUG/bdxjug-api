/*
 * Copyright 2016 Benoît Prioux
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bdxjug.api.infrastructure.sheet;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.bdxjug.api.domain.team.TeamMate;
import org.bdxjug.api.domain.team.TeamMateID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.bdxjug.api.domain.team.TeamMateRepository;
import static org.bdxjug.api.infrastructure.sheet.Sheet.setValue;


@Component
public class SheetTeamMateRepository implements TeamMateRepository {

    private static final int ID = 0;
    private static final int FIRST_NAME = 1;
    private static final int LAST_NAME = 2;
    private static final int ROLE = 3;
    private static final int YEAR = 4;
    private static final int URL_AVATAR = 5;

    private final Sheet sheet;

    @Autowired
    public SheetTeamMateRepository(Sheet sheet) {
        this.sheet = sheet;
    }


    @Override
    public SortedSet<TeamMate> all() {
        return new TreeSet<>(sheet.readLines(this::toTeamMate, "TeamMates"));
    }
    
    @Override
    public SortedSet<TeamMate> byYear(final Integer year) {
        return new TreeSet<>(sheet.readLines(this::toTeamMate, "TeamMates").stream().filter(tm -> tm.getYear() == year).collect(Collectors.toList()));
    }

    private TeamMate toTeamMate(String[] value) {
        TeamMate teamMate = new TeamMate(new TeamMateID(value[ID]), value[FIRST_NAME], value[LAST_NAME], Integer.valueOf(value[YEAR]));
        setValue(value, ROLE, teamMate::setRole);
        setValue(value, URL_AVATAR, teamMate::setUrlAvatar);
        return teamMate;
    }

}
