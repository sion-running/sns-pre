package com.fastcampus.sns;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Slf4j
public class SimpleTest {
    @Test
    public void getTimeTest() {
        String inputTime1 = "2024-07-14 22:00:00";
        String inputTime2 = "2024-07-15 10:20:00";
        String inputTime3 = "2024-07-15 11:00:00";

//        Assertions.assertEquals(calculateTimeDiff(inputTime1), "1일 전");
        Assertions.assertEquals(calculateTimeDiff(inputTime2), "1시간 전");
        Assertions.assertEquals(calculateTimeDiff(inputTime3), "25분 전");
    }

    private String calculateTimeDiff(String input) {
        if (Objects.isNull(input)) {
            throw new RuntimeException();
        }

        LocalDateTime givenDateTime = LocalDateTime.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        long dayDiff = getTimeDiff(givenDateTime, ChronoUnit.DAYS);
        long hourDiff = getTimeDiff(givenDateTime, ChronoUnit.HOURS);
        long minuteDiff = getTimeDiff(givenDateTime, ChronoUnit.MINUTES);

        log.info("hour {}", hourDiff);
        return getTimeDffText(dayDiff, hourDiff, minuteDiff);
    }

    private long getTimeDiff(LocalDateTime input, ChronoUnit unit) {
        return unit.between(input, LocalDateTime.now());
    }

    private String getTimeDffText(long day, long hour, long minute) {
        if (minute == 0) {
            return "1분 전";
        }

        if (minute < 60) {
            return minute + "분 전";
        }

        if (hour < 24) {
            return hour + "시간 전";
        }

        return day + "일 전";
    }
}
