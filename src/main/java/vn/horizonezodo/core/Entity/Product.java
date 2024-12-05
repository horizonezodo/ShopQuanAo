package vn.horizonezodo.core.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;

@Document(collection = "products")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    @Id
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
}
