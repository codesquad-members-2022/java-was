package model.http.session;

import java.time.LocalDateTime;

public class CookiePeriod {

    private static final long BASIC_EXPIRED_TIME = 3600L;
    private LocalDateTime createAt;
    private LocalDateTime expiredAt;
    private long maxAge = 3600;

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

    public long getMaxAge() {
        return maxAge;
    }
}
