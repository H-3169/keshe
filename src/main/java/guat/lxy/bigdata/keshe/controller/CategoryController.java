package guat.lxy.bigdata.keshe.controller;

import guat.lxy.bigdata.keshe.entity.Category;
import guat.lxy.bigdata.keshe.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "category/list";
    }

    @GetMapping("/add")
    public String addPage() {
        return "category/add";
    }

    @PostMapping("/add")
    public String add(Category category) {
        categoryService.addCategory(category);
        return "redirect:/category/list";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Integer id, Model model) {
        Category category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        return "category/edit";
    }

    @PostMapping("/edit")
    public String edit(Category category) {
        categoryService.updateCategory(category);
        return "redirect:/category/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return "redirect:/category/list";
    }
}