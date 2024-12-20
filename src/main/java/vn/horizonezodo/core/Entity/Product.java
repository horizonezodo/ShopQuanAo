package vn.horizonezodo.core.Entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;

@Document(collection = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {
    @Id
    private String id;
    private String productName;
    private String productDescription;
    private long createAt;
    private long updateAt;
    private String thumbImg;
    private List<String> listImg;
    private int viewCount;
    private boolean isTop;
    private String categoryId;
    private boolean isActivate;
    private double minPrice;
    private double maxPrice;
}
