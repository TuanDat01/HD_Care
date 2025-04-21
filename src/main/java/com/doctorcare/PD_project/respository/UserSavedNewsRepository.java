package com.doctorcare.PD_project.respository;


import com.doctorcare.PD_project.entity.News;
import com.doctorcare.PD_project.entity.User;
import com.doctorcare.PD_project.entity.UserSavedNews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSavedNewsRepository extends JpaRepository<UserSavedNews, String> {

    boolean existsByUserAndNews(User user, News news);

    Page<UserSavedNews> findAllByUser(User user, Pageable pageable);

    Optional<UserSavedNews> findByUserAndNews(User user, News news);

    List<UserSavedNews> findAllByUser(User user);

    void deleteAllByNews(News news);
}
