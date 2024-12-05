package vn.horizonezodo.core.Entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String phone;
    @OneToOne(fetch = FetchType.EAGER)
    private Role role;
    private boolean isActivate;
    private boolean isLock;
    private long timeLock;
    private long brithDay;
    private long dayCreate;
    private long dayUpdate;
    private boolean emailVerifired;
    private boolean phoneVerifired;
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Orders> orderList;
    private String address;
    private String avatarImg;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "id",name = "wallet_id")
    private Wallet wallet;
    private String note;
    private long noteCreate;
    private long noteUpdate;
}
