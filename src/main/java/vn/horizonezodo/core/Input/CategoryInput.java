package vn.horizonezodo.core.Input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryInput {
    private String parentId;
    private String childId;
    private String name;
    private boolean isActivate;
    private List<String> listChildId;
    private int level;
}
