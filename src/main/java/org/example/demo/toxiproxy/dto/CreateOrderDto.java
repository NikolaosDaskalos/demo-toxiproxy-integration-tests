package org.example.demo.toxiproxy.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateOrderDto {
    @NotNull
    private String item;

    @NotNull
    private Integer quantity;

    @NotNull
    private String userInfo;
}
