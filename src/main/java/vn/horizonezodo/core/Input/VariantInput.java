package vn.horizonezodo.core.Input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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
    private boolean stock;
    private String colorId;

    public VariantInput(String colorName, String colorImg, String sizeName, String sizeDes, double price, int saleQuantity, boolean stock) {
        this.colorName = colorName;
        this.colorImg = colorImg;
        this.sizeName = sizeName;
        this.sizeDes = sizeDes;
        this.price = price;
        this.saleQuantity = saleQuantity;
        this.stock = stock;
    }
}
