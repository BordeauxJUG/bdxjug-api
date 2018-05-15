package org.bdxjug.api.infrastructure.sheet;

import java.time.LocalDate;
import java.util.Optional;
import org.bdxjug.api.domain.banner.Banner;
import org.bdxjug.api.domain.banner.BannerRepository;
import static org.bdxjug.api.infrastructure.sheet.Sheet.parseDate;
import static org.bdxjug.api.infrastructure.sheet.Sheet.setValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SheetBannerRepository implements BannerRepository {

    private static final int ID = 0;
    private static final int IMAGE_URL = 1;
    private static final int TARGET_URL = 2;
    private static final int END_OF_VALIDITY = 3;
    private static final int TAILLE = 4;

    private final Sheet sheet;

    @Autowired
    public SheetBannerRepository(Sheet sheet) {
        this.sheet = sheet;
    }

    @Override
    public Optional<Banner> getPetite() {
        return sheet.readLines(this::toBanner, "Banner").stream()
                .filter(s -> s.getTaille().equals(BannerRepository.TAILLE_PETITE))
                .filter(s -> s.getEndOfValidity() != null && s.getEndOfValidity().isAfter(LocalDate.now()))
                .findFirst();
    }

    public Optional<Banner> getGrande() {
        return sheet.readLines(this::toBanner, "Banner").stream()
                .filter(s -> s.getTaille().equals(BannerRepository.TAILLE_GRANDE))
                .filter(s -> s.getEndOfValidity() != null && s.getEndOfValidity().isAfter(LocalDate.now()))
                .findFirst();
    }

    private Banner toBanner(String[] values) {
        Banner banner = new Banner(values[ID], values[IMAGE_URL], values[TARGET_URL], values[TAILLE]);
        setValue(values, END_OF_VALIDITY, value -> banner.setEndOfValidity(parseDate(value)));
        return banner;
    }
}
