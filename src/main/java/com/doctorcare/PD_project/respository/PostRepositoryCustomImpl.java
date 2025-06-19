package com.doctorcare.PD_project.respository;

import com.doctorcare.PD_project.entity.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("JpaQlInspection")
    public Page<Post> searchByMultipleKeywords(String keyword, String currentUserId, Pageable pageable) {
        String[] keywords = keyword.trim().toLowerCase().split("\\s+");

        // 1. Xây dựng phần FROM…WHERE… chung
        StringBuilder base = new StringBuilder()
                .append("FROM Post p ")
                .append("WHERE p.isHidden = false ")
                .append("AND (");
        for (int i = 0; i < keywords.length; i++) {
            if (i > 0) base.append(" OR ");
            base.append("(")
                    .append("LOWER(p.content) LIKE :kw").append(i)
                    .append(" OR LOWER(p.user.name) LIKE :kw").append(i)
                    .append(" OR (p.doctor IS NOT NULL AND LOWER(p.doctor.name) LIKE :kw").append(i).append("))");
        }
        base.append(") ")
                // Lọc privacy: public hoặc đã follow
                .append("AND (")
                .append("p.user.isPrivate = false ")
                .append("OR EXISTS (")
                .append("  SELECT uf FROM UserFollow uf ")
                .append("  WHERE uf.follower.id = :currentUserId ")
                .append("    AND uf.following.id = p.user.id")
                .append(")")
                .append(")");

        // 2. JPQL cho data & count
        String select      = "SELECT p "       + base.toString() + " ORDER BY p.createdAt DESC";
        String countSelect = "SELECT COUNT(p) " + base.toString();

        TypedQuery<Post> query      = entityManager.createQuery(select, Post.class);
        TypedQuery<Long>   countQ   = entityManager.createQuery(countSelect, Long.class);

        // 3. Set các keyword params
        for (int i = 0; i < keywords.length; i++) {
            String val = "%" + keywords[i] + "%";
            query.setParameter("kw" + i, val);
            countQ.setParameter("kw" + i, val);
        }
        // 4. Set currentUserId
        query.setParameter("currentUserId", currentUserId);
        countQ.setParameter("currentUserId", currentUserId);

        // 5. Phân trang
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Post> posts = query.getResultList();
        Long total       = countQ.getSingleResult();

        return new PageImpl<>(posts, pageable, total);
    }
}

