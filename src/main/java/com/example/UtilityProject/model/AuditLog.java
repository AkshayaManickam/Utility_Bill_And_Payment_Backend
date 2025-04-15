package com.example.UtilityProject.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String actor;            // Who performed the action
    private String action;           // e.g. LOGIN, ADD_USER, EDIT_EMPLOYEE
    private String target;           // Who or what is being affected

    @Column(columnDefinition = "TEXT")
    private String oldValue;         // Before the change

    @Column(columnDefinition = "TEXT")
    private String newValue;         // After the change

    @Column(columnDefinition = "TEXT")
    private String details;          // Extra info

    private LocalDateTime timestamp;

    public AuditLog() {
    }

    public AuditLog(Long id, String actor, String action, String target, String oldValue, String newValue, String details, LocalDateTime timestamp) {
        this.id = id;
        this.actor = actor;
        this.action = action;
        this.target = target;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.details = details;
        this.timestamp = timestamp;
    }

    // Builder pattern (manual)
    public static class Builder {
        private Long id;
        private String actor;
        private String action;
        private String target;
        private String oldValue;
        private String newValue;
        private String details;
        private LocalDateTime timestamp;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder actor(String actor) {
            this.actor = actor;
            return this;
        }

        public Builder action(String action) {
            this.action = action;
            return this;
        }

        public Builder target(String target) {
            this.target = target;
            return this;
        }

        public Builder oldValue(String oldValue) {
            this.oldValue = oldValue;
            return this;
        }

        public Builder newValue(String newValue) {
            this.newValue = newValue;
            return this;
        }

        public Builder details(String details) {
            this.details = details;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public AuditLog build() {
            return new AuditLog(id, actor, action, target, oldValue, newValue, details, timestamp);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
