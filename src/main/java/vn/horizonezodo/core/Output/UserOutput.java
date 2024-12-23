package vn.horizonezodo.core.Output;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserOutput {
    private Long id;
    private String userName;
    private String role;
    private String image;
    private String phone;
    private long brithDay;
    private String address;
    private String email;
    private String note;
}
