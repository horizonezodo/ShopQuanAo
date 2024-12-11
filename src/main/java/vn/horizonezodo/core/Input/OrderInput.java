package vn.horizonezodo.core.Input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderInput {
    private String orderId;
    private String userId;
    private String productId;
    private String variantId;
    private int quantity;
    private BigDecimal price;
    private String shipingAddress;
    private String note;
    private String orderStatus;
}
