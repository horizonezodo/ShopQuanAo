package vn.horizonezodo.core.Input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.horizonezodo.core.Entity.LogAPIType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogAPIInput {
    private String id;
    private String type;
    private String url;
    private String request;
    private String response;
    private long beginTime;
    private long endTime;
}
