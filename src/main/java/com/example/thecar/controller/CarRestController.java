package com.example.thecar.controller;

import com.example.thecar.model.Car;
import com.example.thecar.repository.CarRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@Tag(name = "Carros", description = "Endpoints para gerenciamento de carros")
public class CarRestController {

    @Autowired
    private CarRepository carRepository;

    @GetMapping
    @Operation(summary = "Lista todos os carros")
    public List<Car> listar() {
        return carRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um carro pelo ID")
    public Car buscarPorId(@PathVariable Long id) {
        return carRepository.findById(id).orElse(null);
    }

    @PostMapping
    @Operation(summary = "Adiciona um novo carro")
    public Car adicionar(@RequestBody Car car) {
        return carRepository.save(car);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um carro existente")
    public Car atualizar(@PathVariable Long id, @RequestBody Car carAtualizado) {
        return carRepository.findById(id)
                .map(car -> {
                    car.setMarca(carAtualizado.getMarca());
                    car.setModelo(carAtualizado.getModelo());
                    car.setAno(carAtualizado.getAno());
                    return carRepository.save(car);
                }).orElse(null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um carro pelo ID")
    public void deletar(@PathVariable Long id) {
        carRepository.deleteById(id);
    }
}
