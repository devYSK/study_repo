package com.ys.webfluxexample1image.service;

import org.springframework.stereotype.Service;

import com.ys.webfluxexample1image.entity.common.Image;
import com.ys.webfluxexample1image.repository.ImageReactorRepository;

import reactor.core.publisher.Mono;

@Service
public class ImageService {
    private ImageReactorRepository imageRepository = new ImageReactorRepository();

    public Mono<Image> getImageById(String imageId) {
        return imageRepository.findById(imageId)
                .map(imageEntity ->
                        new Image(
                                imageEntity.getId(), imageEntity.getName(), imageEntity.getUrl()
                        )
                );
    }
}
