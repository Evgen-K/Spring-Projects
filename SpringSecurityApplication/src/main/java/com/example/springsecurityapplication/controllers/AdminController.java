package com.example.springsecurityapplication.controllers;

import com.example.springsecurityapplication.enumm.Status;
import com.example.springsecurityapplication.models.*;
import com.example.springsecurityapplication.repositories.CartRepository;
import com.example.springsecurityapplication.repositories.CategoryRepository;
import com.example.springsecurityapplication.repositories.OrderRepository;
import com.example.springsecurityapplication.services.OrderService;
import com.example.springsecurityapplication.services.PersonService;
import com.example.springsecurityapplication.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class AdminController {

    private final ProductService productService;

    @Value("${upload.path}")
    private String uploadPath;

    private final CategoryRepository categoryRepository;

    private final OrderService orderService;

    private final PersonService personService;

    private final CartRepository cartRepository;

//    =============================================================================================

    private final OrderRepository orderRepository;

    public AdminController(ProductService productService, CategoryRepository categoryRepository, OrderService orderService, PersonService personService, CartRepository cartRepository, OrderRepository orderRepository) {
        this.productService = productService;
        this.categoryRepository = categoryRepository;
        this.orderService = orderService;
        this.personService = personService;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/admin/product/add")
    public String addProduct(Model model){
        model.addAttribute("product", new Product());
        model.addAttribute("category", categoryRepository.findAll());
        return "product/addProduct";
    }

    @PostMapping("/admin/product/add")
    public String addProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @RequestParam("file_one")MultipartFile file_one, @RequestParam("file_two")MultipartFile file_two, @RequestParam("file_three")MultipartFile file_three, @RequestParam("file_four")MultipartFile file_four, @RequestParam("file_five")MultipartFile file_five, @RequestParam("category") int category, Model model) throws IOException {
        Category category_db = (Category) categoryRepository.findById(category).orElseThrow();
        System.out.println(category_db.getName());
        if(bindingResult.hasErrors()){
            model.addAttribute("category", categoryRepository.findAll());
            return "product/addProduct";
        }

        if(file_one != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_one.getOriginalFilename();
            file_one.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);

        }

        if(file_two != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_two.getOriginalFilename();
            file_two.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }

        if(file_three != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_three.getOriginalFilename();
            file_three.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }

        if(file_four != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_four.getOriginalFilename();
            file_four.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }

        if(file_five != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_five .getOriginalFilename();
            file_five .transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }
        productService.saveProduct(product, category_db);
        return "redirect:/admin";
    }


    @GetMapping("/admin")
    public String admin(Model model)
    {
        model.addAttribute("products", productService.getAllProduct());
        return "/admin/admin";
    }

    @GetMapping("/admin/products")
    public String infoProduct(Model model){
        model.addAttribute("products", productService.getAllProduct());
        return "admin/products";
    }

    @GetMapping("admin/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id){
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

    @GetMapping("admin/product/edit/{id}")
    public String editProduct(Model model, @PathVariable("id") int id){
        model.addAttribute("product", productService.getProductId(id));
        model.addAttribute("category", categoryRepository.findAll());
        return "product/editProduct";
    }

    @PostMapping("admin/product/edit/{id}")
    public String editProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @PathVariable("id") int id, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("category", categoryRepository.findAll());
            return "product/editProduct";
        }
        productService.updateProduct(id, product);
        return "redirect:/admin/products";
    }
//    =============================================================================================
    // 4.1. Просмотр заказа: список всех заказов
    @GetMapping("/admin/orders")
    public String orderAdmin(Model model){
        model.addAttribute("orders", orderService.getAllOrderOrderByDateTimeDesc());
        return "admin/orders";
    }

    // 4.1. Просмотр заказа: информация о конкретном заказе
    @GetMapping("/admin/order/info/{id}")
    public String orderStatusAdmin(@PathVariable("id") int id, Model model){
        model.addAttribute("order", orderService.getOrderId(id));
        return "admin/orderStatus";
    }

    // 4.2. Изменение статуса заказа.
    @PostMapping("/admin/order/edit_status/{id}")
    public String setOrderStatusAdmin(@PathVariable("id") int id, @RequestParam("status") Status status, Model model){
        Order editOrder = orderService.getOrderId(id);
        editOrder.setStatus(status);
        orderService.editOrder(id, editOrder);
        model.addAttribute("order", editOrder);
        return "/admin/orderStatus";
    }

    // 4.3. Поиск заказа по последним 4-м символам в номере заказа
    @PostMapping("/admin/order/search")
    public String orderAdmin(@RequestParam("search") String search, Model model){
        if (search != null && !search.trim().isEmpty())
            model.addAttribute("search_order", orderService.findByEndNumberOrderByDataDesc(search));
        model.addAttribute("orders", orderService.getAllOrderOrderByDateTimeDesc());
        model.addAttribute("value_search", search);
        return "admin/orders";
    }
//    =============================================================================================

    // 5.1. Просмотр пользователй: список всех пользователей
    @GetMapping("/admin/persons")
    public String personAdmin(Model model){
        model.addAttribute("persons", personService.getAllPerson());
        return "admin/persons";
    }

    // 5.1. Просмотр пользователя: информация о конкретном пользователе
    @GetMapping("/admin/person/info/{id}")
    public String personRoleAdmin(@PathVariable("id") int id, Model model){
        model.addAttribute("person", personService.getPersonId(id));
        Person editPerson = personService.getPersonId(id);
        //============================== ORDERS ==============================>
        List<Order> orderList = orderRepository.findByPerson(editPerson);
        model.addAttribute("orders", orderList);

        //============================== CART ==============================>
        List<Cart> cartList = cartRepository.findByPersonId(id);
        List<Product> productList = new ArrayList<>();

        // Получаем продукты из корзины по id товара
        for (Cart cart: cartList) {
            productList.add(productService.getProductId(cart.getProductId()));
        }

        // Вычисление итоговой цены
        float price = 0;
        for (Product product: productList) {
            price += product.getPrice();
        }
        model.addAttribute("price", price);
        model.addAttribute("cart_product", productList);
        //<============================== CART ==============================
        return "admin/personRole";
    }

    // 5.2. Изменение роли пользователя.
    @PostMapping("/admin/person/info/edit_role/{id}")
    public String setPersonRoleAdmin(@PathVariable("id") int id, @RequestParam("role") String role){    // , Model model
        Person editPerson = personService.getPersonId(id);
        editPerson.setRole(role);
        personService.editPerson(id, editPerson);
        return "redirect:/admin/person/info/{id}";
    }

    @PostMapping("/logout")
    public String performLogout() {
        return "redirect:/product";
    }

    @GetMapping("/")
    public String getAllProduct(Model model){
        model.addAttribute("products", productService.getAllProduct());
        return "/product/product";
    }

}

