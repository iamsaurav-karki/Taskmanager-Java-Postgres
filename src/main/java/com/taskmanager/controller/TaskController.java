package com.taskmanager.controller;

import com.taskmanager.model.Task;
import com.taskmanager.model.User;
import com.taskmanager.service.TaskService;
import com.taskmanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String getAllTasks(Model model, Authentication authentication) {
        User currentUser = userService.getCurrentUser(authentication.getName());
        
        model.addAttribute("tasks", taskService.getTasksByUserAndCompleted(currentUser, false));
        model.addAttribute("completedTasks", taskService.getTasksByUserAndCompleted(currentUser, true));
        model.addAttribute("pendingCount", taskService.countTasksByUserAndCompleted(currentUser, false));
        model.addAttribute("completedCount", taskService.countTasksByUserAndCompleted(currentUser, true));
        model.addAttribute("newTask", new Task());
        
        return "tasks";
    }

    @GetMapping("/{id}")
    public String getTaskDetail(@PathVariable Long id, Model model, Authentication authentication) {
        User currentUser = userService.getCurrentUser(authentication.getName());
        Task task = taskService.getTaskById(id);
        
        // Verify ownership
        if (!task.getUser().getId().equals(currentUser.getId())) {
            return "redirect:/tasks?error=unauthorized";
        }
        
        model.addAttribute("task", task);
        return "task-detail";
    }

    @GetMapping("/new")
    public String newTaskForm(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("priorities", Task.Priority.values());
        return "task-form";
    }

    @PostMapping("/new")
    public String createTask(@Valid @ModelAttribute("task") Task task, 
                           BindingResult result, 
                           Authentication authentication) {
        if (result.hasErrors()) {
            return "task-form";
        }
        
        User currentUser = userService.getCurrentUser(authentication.getName());
        taskService.createTask(task, currentUser);
        
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/edit")
    public String editTaskForm(@PathVariable Long id, Model model, Authentication authentication) {
        User currentUser = userService.getCurrentUser(authentication.getName());
        Task task = taskService.getTaskById(id);
        
        // Verify ownership
        if (!task.getUser().getId().equals(currentUser.getId())) {
            return "redirect:/tasks?error=unauthorized";
        }
        
        model.addAttribute("task", task);
        model.addAttribute("priorities", Task.Priority.values());
        
        return "task-form";
    }

    @PostMapping("/{id}/edit")
    public String updateTask(@PathVariable Long id, 
                           @Valid @ModelAttribute("task") Task task, 
                           BindingResult result,
                           Authentication authentication) {
        if (result.hasErrors()) {
            return "task-form";
        }
        
        User currentUser = userService.getCurrentUser(authentication.getName());
        Task existingTask = taskService.getTaskById(id);
        
        // Verify ownership
        if (!existingTask.getUser().getId().equals(currentUser.getId())) {
            return "redirect:/tasks?error=unauthorized";
        }
        
        task.setId(id);
        task.setUser(currentUser);
        task.setCompleted(existingTask.isCompleted());
        taskService.updateTask(task);
        
        return "redirect:/tasks";
    }

    @PostMapping("/{id}/toggle")
    public String toggleTaskCompletion(@PathVariable Long id, Authentication authentication) {
        User currentUser = userService.getCurrentUser(authentication.getName());
        Task task = taskService.getTaskById(id);
        
        // Verify ownership
        if (!task.getUser().getId().equals(currentUser.getId())) {
            return "redirect:/tasks?error=unauthorized";
        }
        
        taskService.toggleTaskCompletion(id);
        return "redirect:/tasks";
    }

    @PostMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id, Authentication authentication) {
        User currentUser = userService.getCurrentUser(authentication.getName());
        Task task = taskService.getTaskById(id);
        
        // Verify ownership
        if (!task.getUser().getId().equals(currentUser.getId())) {
            return "redirect:/tasks?error=unauthorized";
        }
        
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }
}
