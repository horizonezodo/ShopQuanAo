package vn.horizonezodo.core.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.math.BigDecimal;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Variant {
    @Id
    private String id;
    private String productId;
    private String colorId;
    private String sizeId;
    private BigDecimal price;
    private int saleQuantity;
    private boolean isStock;
}
