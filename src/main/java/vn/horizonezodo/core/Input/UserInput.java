package vn.horizonezodo.core.Input;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInput {
    private Long id;
    private String userName;
    private String email;
    private String phone;
    private String password;
    private long birthDay;
    private String address;
    private String image;
    private String note;
    private String img;
    private String role;
    private boolean activate;
    private String verifyEmailToken;
}
