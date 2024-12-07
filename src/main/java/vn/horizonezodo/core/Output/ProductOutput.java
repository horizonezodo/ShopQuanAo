package vn.horizonezodo.core.Output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.horizonezodo.core.Entity.Variant;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductOutput {

    private String id;
    private String productName;
    private long createAt;
    private String thumbImg;
    private List<String> listImage;
    private int viewCount;
    private boolean isTop;
    private List<Variant> listVariants;
}
