package vn.horizonezodo.core.Input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemInput {
    private Long orderId;
    private Long orderItemId;
    private int quantity;
    private BigDecimal price;
}