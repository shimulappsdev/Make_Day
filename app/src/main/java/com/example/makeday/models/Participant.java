package com.example.makeday.models;

public class Participant {

    String participant_id;
    String participant_name;
    String participant_role;
    String participant_image;

    public Participant() {
    }

    public Participant(String participant_id, String participant_name, String participant_role, String participant_image) {
        this.participant_id = participant_id;
        this.participant_name = participant_name;
        this.participant_role = participant_role;
        this.participant_image = participant_image;
    }

    public String getParticipant_id() {
        return participant_id;
    }

    public void setParticipant_id(String participant_id) {
        this.participant_id = participant_id;
    }

    public String getParticipant_name() {
        return participant_name;
    }

    public void setParticipant_name(String participant_name) {
        this.participant_name = participant_name;
    }

    public String getParticipant_role() {
        return participant_role;
    }

    public void setParticipant_role(String participant_role) {
        this.participant_role = participant_role;
    }

    public String getParticipant_image() {
        return participant_image;
    }

    public void setParticipant_image(String participant_image) {
        this.participant_image = participant_image;
    }
}
