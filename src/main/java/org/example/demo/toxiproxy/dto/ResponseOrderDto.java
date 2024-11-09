package org.example.demo.toxiproxy.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseOrderDto {
    private Long id;
    private String item;
    private Integer quantity;
    private String userInfo;
    private String status;
}
