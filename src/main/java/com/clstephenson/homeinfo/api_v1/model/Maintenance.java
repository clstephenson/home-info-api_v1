package com.clstephenson.homeinfo.api_v1.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "property_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Property property;

    @Column(nullable = false)
    private String task;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "last_completion_date")
    private Date lastCompletionDate;

    @Column(name = "frequency_in_days")
    private Integer frequencyInDays;

    private Boolean isRecurring;

    @ManyToOne
    @JoinColumn(name = "vendor_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Vendor vendor;

    public Maintenance() {
    }

    public Maintenance(Property property, String task, Date lastCompletionDate, Integer frequencyInDays, Boolean isRecurring,
                       Vendor vendor) {
        this.property = property;
        this.task = task;
        this.lastCompletionDate = lastCompletionDate;
        this.frequencyInDays = frequencyInDays;
        this.isRecurring = isRecurring;
        this.vendor = vendor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Date getLastCompletionDate() {
        return lastCompletionDate;
    }

    public void setLastCompletionDate(Date lastCompletionDate) {
        this.lastCompletionDate = lastCompletionDate;
    }

    public Integer getFrequencyInDays() {
        return frequencyInDays;
    }

    public void setFrequencyInDays(Integer frequencyInDays) {
        this.frequencyInDays = frequencyInDays;
    }

    public Boolean getRecurring() {
        return isRecurring;
    }

    public void setRecurring(Boolean recurring) {
        isRecurring = recurring;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }
}
