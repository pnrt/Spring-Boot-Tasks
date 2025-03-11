package com.pankajkumarrout.tasks.tasks.service

import com.pankajkumarrout.tasks.tasks.entity.Task
import com.pankajkumarrout.tasks.tasks.repository.TaskRepository
import org.springframework.stereotype.Service

@Service
class TaskService(private val taskRepository: TaskRepository) {

    fun getAllTasks(): List<Task> = taskRepository.findAll()

    fun getTaskById(id: Long): Task? = taskRepository.findById(id).orElse(null)

    fun createTask(task: Task): Task = taskRepository.save(task)

    fun updateTask(id: Long, updatedTask: Task): Task? {
        return if (taskRepository.existsById(id)) {
            taskRepository.save(updatedTask.copy(id = id))
        } else {
            null
        }
    }

    fun deleteTask(id: Long) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id)
        }
    }
}
