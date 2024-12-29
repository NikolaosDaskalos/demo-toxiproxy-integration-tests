package org.demo.toxiproxy.service;

import lombok.AllArgsConstructor;
import org.demo.toxiproxy.dto.BasicResponseDto;
import org.demo.toxiproxy.dto.CreateOrderDto;
import org.demo.toxiproxy.dto.OrdersDto;
import org.demo.toxiproxy.dto.ResponseOrderDto;
import org.demo.toxiproxy.dto.UpdateOrderDto;
import org.demo.toxiproxy.mapper.OrderMapper;
import org.demo.toxiproxy.model.Order;
import org.demo.toxiproxy.repository.OrderRepository;
import org.demo.toxiproxy.service.exception.BadRequestException;
import org.demo.toxiproxy.service.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

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

        if (updateOrderDto.getQuantity() != null && updateOrderDto.getQuantity() != order.getQuantity()) {
            order.setQuantity(updateOrderDto.getQuantity());
            order.setPublishedToKafka(null);
        }

        if (updateOrderDto.getItem() != null && updateOrderDto.getItem() != order.getItem()) {
            order.setItem(updateOrderDto.getItem());
            order.setPublishedToKafka(null);
        }

        if (updateOrderDto.getUserInfo() != null && updateOrderDto.getUserInfo() != order.getUserInfo()) {
            order.setUserInfo(updateOrderDto.getUserInfo());
            order.setPublishedToKafka(null);
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
