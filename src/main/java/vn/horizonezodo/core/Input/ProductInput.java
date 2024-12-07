package vn.horizonezodo.core.Input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductInput {
    private String productId;
    private String productName;
    private String productDescription;
    private long createAt;
    private long updateAt;
    private String thumbImg;
    private List<String> listImg;
    private int saleCount;
    private boolean isTop;
    private String categoryId;
    private boolean isActivate;
    private String id;
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
