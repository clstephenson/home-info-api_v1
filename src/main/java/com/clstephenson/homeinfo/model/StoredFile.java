package com.clstephenson.homeinfo.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity(name = "files")
public class StoredFile extends AuditModel {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "native")
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "property_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Property property;

    @Column(name = "original_file_name")
    private String originalFileName;

    private String uuid;

    @Column(name = "content_type")
    private String contentType;

    @Enumerated(value = EnumType.STRING)
    private FileCategory category;

    private Long categoryItemId;

    public StoredFile() {
    }

    public StoredFile(Property property, String originalFileName, String uuid, String contentType, FileCategory category, Long categoryItemId) {
        this.property = property;
        this.originalFileName = originalFileName;
        this.uuid = uuid;
        this.contentType = contentType;
        this.category = category;
        this.categoryItemId = categoryItemId;
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

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public FileCategory getCategory() {
        return category;
    }

    public void setCategory(FileCategory category) {
        this.category = category;
    }

    public Long getCategoryItemId() {
        return categoryItemId;
    }

    public void setCategoryItemId(Long categoryItemId) {
        this.categoryItemId = categoryItemId;
    }

    public enum FileCategory {
        PROPERTY,
        TASK,
        LOCATION,
        FEATURE,
        IDEA
    }
}
