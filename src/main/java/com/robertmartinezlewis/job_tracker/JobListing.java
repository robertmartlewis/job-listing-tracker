package com.robertmartinezlewis.job_tracker;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class JobListing {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, unique = true, length = 1080)
    private String url;
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Modality modality;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Source source;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.NEW;
    @Column(nullable = false)
    private String company;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    @Column(nullable = true)
    private String salaryText;

    @Column(nullable = true)
    private Integer salaryMin;
    @Column(nullable = true)
    private Integer salaryMax;
    @Column(nullable = true)
    private LocalDate postedDate;
    @Column(nullable = false)
    private Instant firstSeenDate;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Modality getModality() {
        return modality;
    }

    public void setModality(Modality modality) {
        this.modality = modality;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSalaryText() {
        return salaryText;
    }

    public void setSalaryText(String salaryText) {
        this.salaryText = salaryText;
    }

    public Integer getSalaryMin() {
        return salaryMin;
    }

    public void setSalaryMin(Integer salaryMin) {
        this.salaryMin = salaryMin;
    }

    public Integer getSalaryMax() {
        return salaryMax;
    }

    public void setSalaryMax(Integer salaryMax) {
        this.salaryMax = salaryMax;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public Instant getFirstSeenDate() {
        return firstSeenDate;
    }

    public void setFirstSeenDate(Instant firstSeenDate) {
        this.firstSeenDate = firstSeenDate;
    }
}
