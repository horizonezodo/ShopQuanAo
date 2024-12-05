package vn.horizonezodo.core.Input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryInput {
    private String parentId;
    private String childId;
    private String name;
}
