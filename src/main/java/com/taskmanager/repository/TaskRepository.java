package com.taskmanager.repository;

import com.taskmanager.model.Task;
import com.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserOrderByDueDateAsc(User user);
    List<Task> findByUserAndCompletedOrderByDueDateAsc(User user, boolean completed);
    Long countByUserAndCompleted(User user, boolean completed);
}
