package com.wewoo.in.repository;


import com.wewoo.in.model.UrlMapping;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MappingRepo extends CrudRepository<UrlMapping, Integer> {
    boolean existsByIndex(String url);

    Optional<UrlMapping> findByIndex(String index);
}
