package vn.horizonezodo.core.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import vn.horizonezodo.core.Entity.RefreshToken;
import vn.horizonezodo.core.Entity.User;
import vn.horizonezodo.core.Exception.MessageException;
import vn.horizonezodo.core.Input.UserInput;
import vn.horizonezodo.core.Jwt.JwtUntil;
import vn.horizonezodo.core.Output.Message;
import vn.horizonezodo.core.Service.MailService;
import vn.horizonezodo.core.Service.RefreshTokenService;
import vn.horizonezodo.core.Service.UserDetailImpl;
import vn.horizonezodo.core.Service.UserService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.Option;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private UserService service;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtUntil until;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private MailService mailService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserInput input){
        Optional<User> user = service.getUserByInfo(input.getEmail());
        if(user.isPresent()){
            if(user.get().isLock()){
                return new ResponseEntity<>( new Message("Tài khoản của bạn đã bị khóa, mời contact với nhân viên để mở khóa"), HttpStatus.BAD_REQUEST);
            }else if(!user.get().isActivate()){
                return new ResponseEntity<>(new Message("Tài khoản của bạn chưa activate"), HttpStatus.BAD_REQUEST);
            }
            else{
                Authentication authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();
                ResponseCookie jwtCookie = until.generateJwtCookie(userDetail);
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetail.getId());
                ResponseCookie jwtRefreshCookie = until.generateRefreshJwtCookie(refreshToken.getToken());
                return new ResponseEntity<>(new Message("Đăng nhập thành công"), HttpStatus.OK);
            }
        }
        else{
            return new ResponseEntity<>(new Message("Email chưa đăng ký"), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserInput input){
        if(service.checkUser(input.getEmail(), input.getPhone())){
            return new ResponseEntity<>(new Message("Email | số điện thoại đã được đăng ký"), HttpStatus.BAD_REQUEST);
        }
        input.setPassword(encoder.encode(input.getPassword()));
        service.saveUser(input);
        return new ResponseEntity<>(new Message("Đăng ký thành công"), HttpStatus.CREATED);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(){
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principle.toString() != "anonymousUser") {
            Long userId = ((UserDetailImpl) principle).getId();
            refreshTokenService.deleteAllUserRefreshToken(userId);
        }
        ResponseCookie jwtCookie = until.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = until.getCleanJwtRefreshCookie();
        return new ResponseEntity<>(new Message("Logout thành công"), HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request){
        String refreshToken = until.getJwtRefreshFromCookies(request);
        if ((refreshToken != null) && (refreshToken.length() > 0)) {
            return refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        ResponseCookie jwtCookie = until.generateJwtCookie(user);

                        return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                                .body(new Message("Token is refreshed successfully!"));
                    })
                    .orElseThrow(() -> new MessageException("Refresh token is not in database!"));
        }
        return ResponseEntity.badRequest().body(new Message("Refresh Token is empty!"));
    }

    @PostMapping("/send-verify-email")
    public ResponseEntity<?> sendActivateEmail(@RequestBody UserInput input) throws MessagingException {
        String emailToken = until.generateTokenFromData(input.getEmail());
        mailService.SendMail(input.getEmail(), emailToken);
        return new ResponseEntity<>(new Message("Mail xác thực đã được gửi tới email của bạn"), HttpStatus.OK);
    }

    @PostMapping("/check-verify-email")
    public ResponseEntity<?> checkVerifyEmail(@RequestBody UserInput input){
        String emailGot = until.getInfoFromToken(input.getVerifyEmailToken());
        if(emailGot.equals(input.getEmail())){
            service.ActivateUser(input);
            service.VerifyUserEmail(input);
            return new ResponseEntity<>(new Message("Thành công"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new Message("Email không khớp"), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/change-pass")
    public ResponseEntity<?> changePassword(@RequestBody UserInput input){
        Optional<User> user = service.getUserByInfo(input.getEmail());
        if(user.isPresent()){
            if(encoder.matches(input.getPassword(), user.get().getPassword())){
                return new ResponseEntity<>(new Message("Mật khẩu không được khớp với mật khẩu cũ"), HttpStatus.BAD_REQUEST);
            }
            else{
                input.setPassword(encoder.encode(input.getPassword()));
                service.changePassword(input);
                return new ResponseEntity<>(new Message("Mật khẩu của bạn đã được đổi"), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new Message("Không tồn tại user này"), HttpStatus.BAD_REQUEST);
    }

}
