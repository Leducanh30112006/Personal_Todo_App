package com.ra.personaltodoapp.Controller;

import com.ra.personaltodoapp.Repository.TodoRepository;
import com.ra.personaltodoapp.model.Entity.Todo;
import jakarta.servlet.http.HttpSession;
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

    @GetMapping("/welcome")
    public String showWelcomePage() {
        return "welcome";
    }

    @PostMapping("/welcome")
    public String saveSession(@RequestParam("ownerName") String ownerName,
                              HttpSession session, Model model) {
        if (ownerName == null || ownerName.trim().isEmpty()) {
            model.addAttribute("error", "Tên không được để trống!");
            return "welcome";
        }
        session.setAttribute("ownerName", ownerName);
        return "redirect:/";
    }

    @GetMapping
    public String listTodos(Model model, HttpSession session) {
        if (session.getAttribute("ownerName") == null) {
            return "redirect:/welcome";
        }
        model.addAttribute("todos", todoRepository.findAll());
        return "index";
    }

    @GetMapping("/add")
    public String showAddForm(Model model, HttpSession session) {
        if (session.getAttribute("ownerName") == null) return "redirect:/welcome";
        model.addAttribute("todo", new Todo());
        return "form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, HttpSession session) {
        if (session.getAttribute("ownerName") == null) return "redirect:/welcome";
        Todo todo = todoRepository.findById(id).orElse(null);
        if (todo == null) return "redirect:/";

        model.addAttribute("todo", todo);
        return "form";
    }

    @PostMapping("/add")
    public String saveTodo(@Valid @ModelAttribute("todo") Todo todo,
                           BindingResult bindingResult,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        if (session.getAttribute("ownerName") == null) return "redirect:/welcome";

        if (bindingResult.hasErrors()) {
            return "form";
        }

        todoRepository.save(todo);
        redirectAttributes.addFlashAttribute("message", "Lưu ghi chú thành công!");
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteTodo(@PathVariable("id") Long id,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        if (session.getAttribute("ownerName") == null) return "redirect:/welcome";

        todoRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Đã xóa ghi chú!");
        return "redirect:/";
    }
}