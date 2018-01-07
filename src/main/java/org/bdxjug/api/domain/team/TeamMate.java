package org.bdxjug.api.domain.team;

import lombok.Data;

/**
 *
 * @author lfo
 */
@Data
public class TeamMate implements Comparable<TeamMate> {

    private final TeamMateID Id;
    private final String firstName;
    private final String lastName;
    private final int year;
    private String role;
    private String urlAvatar;

    @Override
    public int compareTo(TeamMate other) {
        final int compareRole = compareRole(role, other.role);
        if (compareRole == 0) {
            return lastName.compareTo(other.lastName);
        }
        return compareRole;
    }

    // président then non null role first.
    private int compareRole(String role, String otherRole) {
        if ((role == null || role.isEmpty()) && (otherRole == null || otherRole.isEmpty())) {
            return 0;
        }
        if (role == null || role.isEmpty()) {
            return 1;
        }
        if (otherRole == null || otherRole.isEmpty()) {
            return -1;
        }
        if (PRESIDENT.equalsIgnoreCase(role)) {
            return -1;
        }
        if (PRESIDENT.equalsIgnoreCase(otherRole)) {
            return 1;
        }
        return 0;
    }
    private static final String PRESIDENT = "Président";

}
