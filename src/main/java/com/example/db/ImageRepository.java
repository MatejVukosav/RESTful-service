package com.example.db;

import com.example.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

/**
 * Created by mvukosav on 4.12.2016..
 */
public interface ImageRepository extends JpaRepository<Image, Long> {
    Collection<Image> findByAccountUsername(String username);
}
