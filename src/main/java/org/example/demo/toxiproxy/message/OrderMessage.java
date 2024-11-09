package org.example.demo.toxiproxy.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderMessage {
    @NotNull
    private Long id;

    @NotNull
    private String item;

    @NotNull
    private Integer quantity;

    @NotNull
    private String userInfo;

    private String status;
}
