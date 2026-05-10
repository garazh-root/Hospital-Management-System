package org.com.patientservice.messages;

public enum PatientMessages {

    REQUEST_EMPTY("Request is empty"),
    MODEL_EMPTY("Model is empty"),
    RESPONSE_EMPTY("Response is empty"),
    EMAIL_ALREADY_EXISTS("Email already exists"),
    PATIENT_NOT_FOUND("Patient not found"),
    API_FOR_MANAGING_PATIENTS("API for managing patients"),
    PATIENT_CREATED("Patient created"),
    PATIENT_UPDATED("Patient updated"),
    PATIENT_FOUND("Patient found"),
    ROLE_NOT_MATCHING("Role not matching");

    private final String message;

    private PatientMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
