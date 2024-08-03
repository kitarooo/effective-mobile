package com.backend.effectivemobile.repository;

import com.backend.effectivemobile.entity.Task;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByPerformerUsername(String performerUsername);
    Optional<Task> findByTitle(String title);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM users_tasks WHERE user_id= :userId AND tasks_id= :taskId", nativeQuery = true)
    void delete(@Param("taskId") Long taskId, @Param("userId") Long userId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "SELECT *.t FROM t.users_tasks WHERE t.user_id= :userId", nativeQuery = true)
    List<Task> findAllByUserId(@Param("userId") Long userId);
}
