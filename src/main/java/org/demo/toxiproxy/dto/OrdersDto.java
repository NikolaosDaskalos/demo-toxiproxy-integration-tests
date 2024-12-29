package org.demo.toxiproxy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrdersDto {
    private List<ResponseOrderDto> orders = new ArrayList<>();
}

