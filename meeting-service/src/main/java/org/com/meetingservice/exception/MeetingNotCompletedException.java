package org.com.meetingservice.exception;

public class MeetingNotCompletedException extends RuntimeException {
    public MeetingNotCompletedException(String message) {
        super(message);
    }
}
