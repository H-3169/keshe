package guat.lxy.bigdata.keshe.controller;

import com.github.pagehelper.PageInfo;
import guat.lxy.bigdata.keshe.entity.Product;
import guat.lxy.bigdata.keshe.service.CategoryService;
import guat.lxy.bigdata.keshe.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer catId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            Model model) {

        PageInfo<Product> pageInfo = productService.searchProducts(pageNum, pageSize,
                keyword, catId, minPrice, maxPrice);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("keyword", keyword);
        model.addAttribute("catId", catId);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/list";
    }

    @GetMapping("/add")
    public String addPage(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/add";
    }

    @PostMapping("/add")
    public String add(Product product) {
        if (product.getReleaseDate() == null) {
            product.setReleaseDate(new Date());
        }
        productService.saveProduct(product);
        return "redirect:/product/list";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Integer id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/edit";
    }

    @PostMapping("/edit")
    public String edit(Product product) {
        Product existing = productService.getProductById(product.getId());
        if (existing != null) {
            // 如果前端没传 photoUrl，保留原值
            if (product.getPhotoUrl() == null || product.getPhotoUrl().isEmpty()) {
                product.setPhotoUrl(existing.getPhotoUrl());
            }
            if (product.getReleaseDate() == null) {
                product.setReleaseDate(existing.getReleaseDate());
            }
        }
        productService.updateProductWithCache(product);
        return "redirect:/product/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        productService.deleteProductWithCache(id);
        return "redirect:/product/list";
    }
}