package org.example.demo.toxiproxy.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrdersDto {
    private List<ResponseOrderDto> OrdersDto = new ArrayList<>();
}

