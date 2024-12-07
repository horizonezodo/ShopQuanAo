package vn.horizonezodo.core.Output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.horizonezodo.core.Entity.ORDERSTATUS;
import vn.horizonezodo.core.Entity.OrderItem;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderOutput {
    private Long id;
    private List<OrderItemOutput> orderItemList;
    private long orderDate;
    private double totalPrice;
    private ORDERSTATUS orderstatus;
    private String shippingAddress;
    private String userNote;
}
