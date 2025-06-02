package com.doctorcare.PD_project.respository;

import com.doctorcare.PD_project.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NewsRepositoryCustom {
    Page<News> searchByKeywordWhole(
            String keyword,
            Pageable pageable
    );
}
