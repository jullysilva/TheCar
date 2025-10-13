package com.example.thecar.controller;

import com.example.thecar.model.Car;
import com.example.thecar.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarRepository carRepository;

    @GetMapping
    public String listar(Model model) {
        List<Car> cars = carRepository.findAll();
        model.addAttribute("cars", cars);
        return "index";
    }

    @PostMapping("/add")
    public String adicionar(@ModelAttribute Car car) {
        carRepository.save(car);
        return "redirect:/cars";
    }

    @GetMapping("/delete/{id}")
    public String deletar(@PathVariable Long id) {
        carRepository.deleteById(id);
        return "redirect:/cars";
    }
}
