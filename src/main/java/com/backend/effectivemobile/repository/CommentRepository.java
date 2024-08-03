package com.backend.effectivemobile.repository;

import com.backend.effectivemobile.entity.Comment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM tasks_comments WHERE task_id= :taskId AND comments_id= :commentId", nativeQuery = true)
    void delete(@Param("commentId") Long commentId, @Param("taskId") Long taskId);

}
