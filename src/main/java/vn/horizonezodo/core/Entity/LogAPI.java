package vn.horizonezodo.core.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;


@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogAPI {
    @Id
    private String id;
    private LogAPIType type;
    private String url;
    private String request;
    private String response;
    private long beginTime;
    private long endTime;
}
