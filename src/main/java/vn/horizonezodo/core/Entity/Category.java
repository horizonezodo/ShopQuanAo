package vn.horizonezodo.core.Entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;

@Document(collection = "categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    private String id;
    private String cateName;
    private long createAt;
    private long updateAt;
    private int level;
    private boolean isActivate;
    private List<String> childIds;
}
