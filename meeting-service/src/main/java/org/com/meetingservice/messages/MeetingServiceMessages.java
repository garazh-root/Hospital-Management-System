package org.com.meetingservice.messages;

public enum MeetingServiceMessages {

    SERVICE_UNAVAILABLE("Service unavailable"),
    SCHEDULE_NOT_AVAILABLE("Schedule not available"),
    MEETING_NOT_FOUND("Meetings not found"),
    INVALID_STATUS("Invalid status"),
    SCHEDULE_NOT_MATCHING("Schedule not matching"),
    INVALID_MEETING_EXCEPTION("Invalid meeting"),
    MEETING_CONFLICT("Meeting conflict occurred"),
    SLOT_NOT_AVAILABLE("Meeting slot not available");

    private final String message;

    MeetingServiceMessages(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
