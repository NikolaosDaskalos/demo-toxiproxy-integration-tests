package org.example.demo.toxiproxy.service;

import lombok.AllArgsConstructor;
import org.example.demo.toxiproxy.dto.BasicResponseDto;
import org.example.demo.toxiproxy.dto.OrderDto;
import org.example.demo.toxiproxy.dto.OrdersDto;
import org.example.demo.toxiproxy.dto.UpdateOrderDto;
import org.example.demo.toxiproxy.mapper.OrderMapper;
import org.example.demo.toxiproxy.model.Order;
import org.example.demo.toxiproxy.model.OrderEvent;
import org.example.demo.toxiproxy.repository.OrderRepository;
import org.example.demo.toxiproxy.service.exception.BadRequestException;
import org.example.demo.toxiproxy.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    private final OrderMapper orderMapper;

    private final Clock clock;

    @Value("${kafka.orders.topic.name}")
    private final String ordersTopicName;

    public ResponseEntity<OrdersDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        if (!CollectionUtils.isEmpty(orders)) {
            OrdersDto ordersDto = new OrdersDto(orders.stream().map(orderMapper::mapToOrderDto).toList());
            return new ResponseEntity<>(ordersDto, HttpStatus.OK);
        }

        return new ResponseEntity<>(new OrdersDto(), HttpStatus.OK);
    }

    public Order getOrder(Long orderId) throws NotFoundException {
        return orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException(orderNotFoundMessage(orderId)));
    }

    public Order createOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        OrderEvent orderEvent = OrderEvent.builder()
                .order(savedOrder)
                .timeCreated(LocalDateTime.now(clock))
                .build();
        kafkaTemplate.send(ordersTopicName, orderEvent);
        return savedOrder;
    }

    public OrderDto updateOrder(Long orderId, UpdateOrderDto updateOrderDto) throws NotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(orderNotFoundMessage(orderId)));

        if (updateOrderDto.getQuantity() != null) {
            order.setQuantity(updateOrderDto.getQuantity());
        }

        if (updateOrderDto.getItem() != null) {
            order.setItem(updateOrderDto.getItem());
        }

        if (updateOrderDto.getUserInfo() != null) {
            order.setUserInfo(updateOrderDto.getUserInfo());
        }

        return orderMapper.mapToOrderDto(orderRepository.save(order));
    }

    public BasicResponseDto deleteOrder(Long orderId) throws BadRequestException, NotFoundException {
        if (orderId == null) {
            throw new BadRequestException("Bad request");
        }

        if (!orderRepository.existsById(orderId)) {
            throw new NotFoundException(orderNotFoundMessage(orderId));
        }
        orderRepository.deleteById(orderId);

        return new BasicResponseDto("Order with id " + orderId + "deleted successfully");
    }

    private String orderNotFoundMessage(Long orderId) {
        return String.format("Order with id %s not found", orderId);
    }
}
