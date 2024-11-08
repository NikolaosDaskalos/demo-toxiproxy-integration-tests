package org.example.demo.toxiproxy.controller;

import org.example.demo.toxiproxy.dto.BasicResponseDto;
import org.example.demo.toxiproxy.dto.OrderDto;
import org.example.demo.toxiproxy.dto.OrdersDto;
import org.example.demo.toxiproxy.dto.UpdateOrderDto;
import org.example.demo.toxiproxy.model.Order;
import org.example.demo.toxiproxy.service.OrderService;
import org.example.demo.toxiproxy.service.exception.BadRequestException;
import org.example.demo.toxiproxy.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<OrdersDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable Long orderId) throws NotFoundException {
        Order order = orderService.getOrder(orderId);
        return ResponseEntity.ok(order);
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order createdOrder = orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDto> updateOrder(
            @PathVariable Long orderId,
            @RequestBody UpdateOrderDto updateOrderDto) throws NotFoundException {
        return new ResponseEntity<>(orderService.updateOrder(orderId, updateOrderDto), HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<BasicResponseDto> deleteOrder(@PathVariable Long orderId) throws BadRequestException, NotFoundException {
        return new ResponseEntity<>(orderService.deleteOrder(orderId), HttpStatus.OK);
    }
}
