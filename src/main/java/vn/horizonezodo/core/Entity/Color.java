package vn.horizonezodo.core.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document(collection = "colors")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Color {
    @Id
    private String id;
    private String name;
    private String img;
}
