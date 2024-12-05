package vn.horizonezodo.core.Input;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class VariantInput {
    private String id;
    private String productId;
    private String colorName;
    private String colorImg;
    private String sizeName;
    private String sizeDes;
    private String sizeId;
    private BigDecimal price;
    private int saleQuantity;
    private boolean isStock;
    private String colorId;
}
