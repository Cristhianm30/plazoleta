package com.plaza.plazoleta.domain.port;

import com.plaza.plazoleta.domain.model.Category;


import java.util.Optional;

public interface CategoryRepository {

    Category save(Category category);
    Optional<Category> findByName(String name);
    Optional<Category> findById(Long userId);
}
