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
    private boolean top;
    private String categoryId;
    private boolean activate;
    private List<VariantInput> variantInputs;
}
