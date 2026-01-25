package com.qualique.repository;

import com.qualique.entity.GalleryImage;
import com.qualique.entity.GalleryImage.GalleryCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GalleryImageRepository extends JpaRepository<GalleryImage, Long> {
    
    List<GalleryImage> findByActiveTrueOrderByDisplayOrderAsc();
    
    List<GalleryImage> findByGalleryCategoryAndActiveTrueOrderByDisplayOrderAsc(GalleryCategory category);
    
    List<GalleryImage> findAllByOrderByDisplayOrderAsc();
}
