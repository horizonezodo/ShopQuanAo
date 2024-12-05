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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.horizonezodo.core.Entity.RefreshToken;
import vn.horizonezodo.core.Exception.MessageException;
import vn.horizonezodo.core.Input.UserInput;
import vn.horizonezodo.core.Jwt.JwtUntil;
import vn.horizonezodo.core.Output.Message;
import vn.horizonezodo.core.Service.RefreshTokenService;
import vn.horizonezodo.core.Service.UserDetailImpl;
import vn.horizonezodo.core.Service.UserService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService service;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtUntil until;

    @PostMapping("/")
    public ResponseEntity<?> login(UserInput input){
        Authentication authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = until.generateJwtCookie(userDetail);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetail.getId());
        ResponseCookie jwtRefreshCookie = until.generateRefreshJwtCookie(refreshToken.getToken());
        return new ResponseEntity<>(new Message("Đăng nhập thành công"), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(UserInput input){
        if(service.checkUser(input.getEmail(), input.getPhone())){
            return new ResponseEntity<>(new Message("Email | số điện thoại đã được đăng ký"), HttpStatus.BAD_REQUEST);
        }

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
}
