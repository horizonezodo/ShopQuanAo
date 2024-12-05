package vn.horizonezodo.core.Entity;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document(collection = "sizes")
public class Size {
    @Id
    private String id;
    private String name;
    private String description;
}
