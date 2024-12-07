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
public class VariantInput {
    private String id;
    private String productId;
    private String colorName;
    private String colorImg;
    private String sizeName;
    private String sizeDes;
    private String sizeId;
    private double price;
    private int saleQuantity;
    private boolean isStock;
    private String colorId;
}
