package com.pankajkumarrout.tasks.tasks.repository

import com.pankajkumarrout.tasks.tasks.entity.Task
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : JpaRepository<Task, Long>
