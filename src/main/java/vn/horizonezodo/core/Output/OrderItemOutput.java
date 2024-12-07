package vn.horizonezodo.core.Output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.horizonezodo.core.Entity.Orders;
import vn.horizonezodo.core.Entity.Product;
import vn.horizonezodo.core.Entity.Variant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemOutput {
    private Long id;
    private ProductOutput product;
    private Variant variant;
    private int quantity;
    private double price;
    private double totalPrice;

}
