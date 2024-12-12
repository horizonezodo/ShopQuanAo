package vn.horizonezodo.core.Output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.horizonezodo.core.Entity.LogAPIType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogAPIOutput {
    private String id;
    private LogAPIType type;
    private String url;
    private String request;
    private String response;
    private long beginTime;
    private long endTime;
}
