package platform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
@Table(name = "snippet")
public class CodeSnippet {

    @Id
    private String uuid;
    private String code;
    private LocalDateTime date;
    private boolean secret = false;
    private LocalDateTime timeLimit = LocalDateTime.MIN;
    private Long viewsLeft = 0L;
    private boolean originalTime = true;
    private boolean originalViews = true;

    public CodeSnippet() {

    }

    public CodeSnippet(@JsonProperty("code") String code,
                       @JsonProperty("time") Long secondsLimit,
                       @JsonProperty("views") Long viewsLeft) {
        this.uuid = UUID.randomUUID().toString();
        this.code = code;
        this.date = LocalDateTime.now();
        if (secondsLimit > 0) {
            this.secret = true;
            this.originalTime = false;
            this.timeLimit = date.plusSeconds(secondsLimit);
        }
        if (viewsLeft > 0) {
            this.secret = true;
            this.originalViews = false;
            this.viewsLeft = viewsLeft;
        }
    }

    public String getCode() {
        return this.code;
    }

    public String getDate() {
        DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return this.date.format(DATE_FORMATTER);
    }

    public String getId() {
        return this.uuid;
    }

    public boolean isNotSecret() {
        return !this.secret;
    }

    public int getViewsLeft() {
        return this.viewsLeft.intValue();
    }

    public int getTimeLeft() {
        return originalTime ? 0 : (int) LocalDateTime.now().until(this.timeLimit, ChronoUnit.SECONDS);
    }

    public boolean isAvailable() {
        // checking if init values were changed by user request
        if (!originalViews && !originalTime) {
            return LocalDateTime.now().isBefore(timeLimit) && viewsLeft > 0;
        } else if (!originalTime) {
            return LocalDateTime.now().isBefore(timeLimit);
        } else return this.viewsLeft >= 0;
    }

    public void decrementViews() {
        this.viewsLeft--;
    }

    public boolean isNotOriginalViews() {
        return !this.originalViews;
    }
}