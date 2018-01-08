package org.bdxjug.api.domain.banner;

import java.time.LocalDate;
import lombok.Data;

@Data
public class Banner {
    private final String name;
    private final String imageUrl;
    private final String targetUrl;
    private LocalDate endOfValidity;
}
