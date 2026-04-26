package com.ra.personaltodoapp.Controller;

import com.ra.personaltodoapp.Repository.TodoRepository;
import com.ra.personaltodoapp.model.Entity.Todo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;


    @GetMapping
    public String listTodos(Model model) {
        model.addAttribute("todos", todoRepository.findAll());
        return "index";
    }


    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("todo", new Todo());
        return "form";
    }

    @PostMapping("/add")
    public String saveTodo(@Valid @ModelAttribute("todo") Todo todo,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            return "form";
        }
        todoRepository.save(todo);

        return "redirect:/";
    }
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {

        Todo todo = todoRepository.findById(id).orElse(null);
        if (todo == null) {
            return "redirect:/";
        }

        model.addAttribute("todo", todo);
        return "form";
    }


    @GetMapping("/delete/{id}")
    public String deleteTodo(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {

        todoRepository.deleteById(id);

        redirectAttributes.addFlashAttribute("message", "Đã xóa Ghi chú thành công!");

        return "redirect:/";
    }
}