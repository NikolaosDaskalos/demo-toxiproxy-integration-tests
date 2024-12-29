package org.demo.toxiproxy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateOrderDto {
    private String item;
    private Integer quantity;
    private String userInfo;
}
