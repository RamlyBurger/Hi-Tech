package com.hitech.commerce.web.form;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.hitech.commerce.domain.StoreEvent;

public class EventForm {

    private Long id;

    @NotBlank
    @Size(max = 160)
    private String name;

    @NotBlank
    @Size(max = 120)
    private String venue;

    @NotNull
    private LocalDate eventDate;

    @NotNull
    private LocalTime eventTime;

    private boolean active = true;

    public static EventForm from(StoreEvent event) {
        EventForm form = new EventForm();
        form.setId(event.getId());
        form.setName(event.getName());
        form.setVenue(event.getVenue());
        form.setEventDate(event.getEventDate());
        form.setEventTime(event.getEventTime());
        form.setActive(event.isActive());
        return form;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public LocalTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalTime eventTime) {
        this.eventTime = eventTime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
