package com.wewoo.in.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;



@Table("url_mapping")
public record UrlMapping(
        @Id
        Integer id,
        String index,
        String url,
        LocalDateTime date
) {}