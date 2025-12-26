package org.demo.service;

// import java.time.Clock;
// import java.time.Instant;
// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.time.ZoneId;
// import java.time.ZonedDateTime;
// import java.time.temporal.ChronoUnit;

import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.inject.Inject;

/**
 * Service class responsible for providing time-related functionality throughout the application. This service acts as a
 * single source of truth for getting the current time and supports time zone conversions and testing via a configurable
 * clock.
 */
@ApplicationScoped
public class TimeService {

    // @Inject
    // Clock clock;

    // /**
    //  * Gets the current instant in UTC.
    //  *
    //  * @return The current instant
    //  */
    // public Instant now() {
    //     return clock.instant();
    // }

    // /**
    //  * Gets the current date in UTC.
    //  *
    //  * @return The current date
    //  */
    // public LocalDate today() {
    //     return LocalDate.now(clock);
    // }

    // /**
    //  * Gets the current date and time in UTC.
    //  *
    //  * @return The current date and time
    //  */
    // public LocalDateTime nowLocalDateTime() {
    //     return LocalDateTime.now(clock);
    // }

    // /**
    //  * Gets the current date and time in UTC as a ZonedDateTime.
    //  *
    //  * @return The current date and time in UTC
    //  */
    // public ZonedDateTime nowZonedDateTime() {
    //     return ZonedDateTime.now(clock);
    // }

    // /**
    //  * Gets the current date and time in UTC as a ZonedDateTime.
    //  *
    //  * @return The current date and time in UTC
    //  */
    // public ZonedDateTime nowZonedDateTimeTruncated() {
    //     return ZonedDateTime.now(clock).truncatedTo(ChronoUnit.SECONDS);
    // }

    // /**
    //  * Gets the current date and time in the specified time zone.
    //  *
    //  * @param zoneId The time zone ID
    //  * @return The current date and time in the specified time zone
    //  */
    // public ZonedDateTime nowInZone(ZoneId zoneId) {
    //     return ZonedDateTime.now(clock.withZone(zoneId));
    // }

    // /**
    //  * Gets the current date and time in the specified time zone.
    //  *
    //  * @param zoneId The time zone ID string (e.g., "America/New_York")
    //  * @return The current date and time in the specified time zone
    //  * @throws IllegalArgumentException if the zone ID is invalid
    //  */
    // public ZonedDateTime nowInZone(String zoneId) {
    //     return nowInZone(ZoneId.of(zoneId));
    // }

    // /**
    //  * Converts a zoned datetime into a server local datetime
    //  *
    //  * @param zonedTime a ZonedDateTime
    //  * @return the given date time given in server local date time
    //  */
    // public LocalDateTime zonedTimeToLocalTime(ZonedDateTime zonedTime) {
    //     if (zonedTime == null) return null;

    //     ZonedDateTime utcTime = zonedTime.withZoneSameInstant(ZoneId.systemDefault());
    //     return utcTime.toLocalDateTime();
    // }

    // /**
    //  * Converts a local date time into a zoned datetime with the server's default zone
    //  *
    //  * @param localTime a LocalDateTime
    //  * @return the given date time given in server local date time
    //  */
    // public ZonedDateTime localTimeToZonedTime(LocalDateTime localTime) {
    //     if (localTime == null) return null;
    //     return ZonedDateTime.of(localTime, ZoneId.systemDefault());
    // }
}
