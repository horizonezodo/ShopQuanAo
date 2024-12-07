package vn.horizonezodo.core.Output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.horizonezodo.core.Entity.Category;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CateOutput {
    private String id;
    private String cateName;
    private long createAt;
    private long updateAt;
    private boolean isActivate;
    private List<Category> childCates;
}
