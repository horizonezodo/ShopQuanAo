package vn.horizonezodo.core.Input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
