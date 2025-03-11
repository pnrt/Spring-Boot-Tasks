package com.pankajkumarrout.tasks.tasks.controller

import com.pankajkumarrout.tasks.tasks.entity.Task
import com.pankajkumarrout.tasks.tasks.service.TaskService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tasks")
class TaskController(private val taskService: TaskService) {

    @GetMapping
    fun getAllTasks() = taskService.getAllTasks()

    @GetMapping("/{id}")
    fun getTaskById(@PathVariable id: Long) = taskService.getTaskById(id)

    @PostMapping
    fun createTask(@RequestBody task: Task) = taskService.createTask(task)

    @PutMapping("/{id}")
    fun updateTask(@PathVariable id: Long, @RequestBody updatedTask: Task) =
        taskService.updateTask(id, updatedTask)

    @DeleteMapping("/{id}")
    fun deleteTask(@PathVariable id: Long) = taskService.deleteTask(id)
}
