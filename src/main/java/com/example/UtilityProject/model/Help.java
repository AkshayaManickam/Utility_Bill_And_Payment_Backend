package com.example.UtilityProject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Help")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Help {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)  // ✅ Make userMail unique
    private String userMail;

    @Column(nullable = false)
    private String query;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status; // SENT, RECEIVED, IN PROGRESS, COMPLETED

    @Column(nullable = true)
    private String oldValue;  // Stores old value (if applicable)

    @Column(nullable = true)
    private String newValue;

    @Column(name = "assigned_to")
    private String assignedTo;

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }


    public Help(Long id, String userMail, String query, Status status, String oldValue, String newValue) {
        this.id = id;
        this.userMail = userMail;
        this.query = query;
        this.status = status;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public Help() {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
