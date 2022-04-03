package model.handler.controller;

import java.time.LocalDateTime;

public class CookiePeriod {

    private static final long BASIC_EXPIRED_TIME = 30L;
    private LocalDateTime createAt;
    private LocalDateTime expiredAt;

    public CookiePeriod() {
        LocalDateTime now = LocalDateTime.now();
        this.createAt = now;
        this.expiredAt = getExpiredTimeFrom(now);
    }

    private LocalDateTime getExpiredTimeFrom(LocalDateTime createAt) {
        return createAt.plusMinutes(BASIC_EXPIRED_TIME);
    }

    public boolean isExpired(LocalDateTime now) {
        return expiredAt.isBefore(now);
    }

    public void setMaxAge(long time) {
        this.expiredAt = createAt.plusMinutes(time);
    }
}
