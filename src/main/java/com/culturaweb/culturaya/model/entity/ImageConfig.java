package com.culturaweb.culturaya.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "image_config")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "section_name", nullable = false, unique = true, length = 50)
    private String sectionName;
    
    @Column(name = "display_name")
    private String displayName;
    
    @Column(name = "image_path")
    private String imagePath;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Lob
    @Column(name = "description")
    private String description;

    public ImageConfig(String sectionName, String displayName, String description) {
        this.sectionName = sectionName;
        this.displayName = displayName;
        this.description = description;
        this.imagePath = null;
        this.imageUrl = null;
    }
    
}
