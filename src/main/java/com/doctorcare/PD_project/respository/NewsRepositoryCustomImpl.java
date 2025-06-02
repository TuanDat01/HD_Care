package com.doctorcare.PD_project.respository;

import com.doctorcare.PD_project.entity.News;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class NewsRepositoryCustomImpl implements NewsRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    @SuppressWarnings("JpaQlInspection")
    public Page<News> searchByKeywordWhole(String keyword, Pageable pageable) {
        // chuẩn hóa pattern (lower + wildcard)
        String pattern = "%" + keyword.trim().toLowerCase() + "%";

        // JPQL động (chỉ một param :kw)
        String jpqlBase = ""
                + "FROM News n "
                + "WHERE n.isApproved = true "
                + "  AND n.isDraft    = false "
                + "  AND ("
                + "    LOWER(n.title)       LIKE :kw "
                + "    OR LOWER(n.content)   LIKE :kw "
                + "    OR LOWER(n.category)  LIKE :kw "
                + "    OR LOWER(n.author.name) LIKE :kw "
                + ") ";

        String select     = "SELECT n "        + jpqlBase + "ORDER BY n.createdAt DESC";
        String countQuery = "SELECT COUNT(n) " + jpqlBase;

        TypedQuery<News> q        = em.createQuery(select, News.class);
        TypedQuery<Long> countQ   = em.createQuery(countQuery, Long.class);

        // set param once
        q.setParameter("kw", pattern);
        countQ.setParameter("kw", pattern);

        // pagination
        q.setFirstResult((int) pageable.getOffset());
        q.setMaxResults(pageable.getPageSize());

        List<News> content = q.getResultList();
        long total         = countQ.getSingleResult();

        return new PageImpl<>(content, pageable, total);
    }
}
