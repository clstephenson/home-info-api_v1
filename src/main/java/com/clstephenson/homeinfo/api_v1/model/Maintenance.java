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
}
