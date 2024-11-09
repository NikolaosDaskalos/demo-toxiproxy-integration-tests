package org.example.demo.toxiproxy.service;

import java.time.Clock;
import java.util.List;
import lombok.AllArgsConstructor;
import org.example.demo.toxiproxy.dto.BasicResponseDto;
import org.example.demo.toxiproxy.dto.CreateOrderDto;
import org.example.demo.toxiproxy.dto.OrdersDto;
import org.example.demo.toxiproxy.dto.ResponseOrderDto;
import org.example.demo.toxiproxy.dto.UpdateOrderDto;
import org.example.demo.toxiproxy.mapper.OrderMapper;
import org.example.demo.toxiproxy.model.Order;
import org.example.demo.toxiproxy.repository.OrderRepository;
import org.example.demo.toxiproxy.service.exception.BadRequestException;
import org.example.demo.toxiproxy.service.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

//    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    private final OrderMapper orderMapper;

    private final Clock clock;

//    @Value("${kafka.orders.topic.name}")
//    private final String ordersTopicName;

    public OrdersDto getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        if (!CollectionUtils.isEmpty(orders)) {
            return new OrdersDto(orders.stream().map(orderMapper::mapToResponseOrderDto).toList());
        }

        return new OrdersDto();
    }

    public CreateOrderDto getOrder(Long orderId) throws NotFoundException {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException(orderNotFoundMessage(orderId)));
        return orderMapper.mapToOrderDto(order);
    }

    public ResponseOrderDto createOrder(CreateOrderDto orderDto) throws BadRequestException {
        return orderMapper.mapToResponseOrderDto(orderRepository.save(orderMapper.mapToOrder(orderDto)));
    }

    public CreateOrderDto updateOrder(Long orderId, UpdateOrderDto updateOrderDto) throws NotFoundException {
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

        return new BasicResponseDto(String.format("Order with id %d deleted successfully", orderId));
    }

    private String orderNotFoundMessage(Long orderId) {
        return String.format("Order with id %s not found", orderId);
    }
}
