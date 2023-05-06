package capstone.elsv2.controller;

import capstone.elsv2.dto.auth.*;
import capstone.elsv2.dto.common.ResponseDTO;
import capstone.elsv2.emunCode.ErrorCode;
import capstone.elsv2.emunCode.StatusCode;
import capstone.elsv2.emunCode.SuccessCode;
import capstone.elsv2.entities.Account;
import capstone.elsv2.entities.CustomerProfile;
import capstone.elsv2.entities.SitterProfile;
import capstone.elsv2.jwt.JwtConfig;
import capstone.elsv2.repositories.AccountRepository;
import capstone.elsv2.repositories.CustomerProfileRepository;
import capstone.elsv2.repositories.RoleRepository;
import capstone.elsv2.repositories.SitterProfileRepository;
import capstone.elsv2.services.CommonService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/v1/common/auth")
public class AuthenController {
    private AuthenticationManager authenticationManager;
    private CommonService commonService;
    private JwtConfig jwtConfig;
    private PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final CustomerProfileRepository customerProfileRepository;
    private final SitterProfileRepository sitterProfileRepository;

    @Autowired
    @Lazy
    public AuthenController(AuthenticationManager authenticationManager, CommonService commonService, JwtConfig jwtConfig, PasswordEncoder passwordEncoder, RoleRepository roleRepository,
                            AccountRepository accountRepository,
                            CustomerProfileRepository customerProfileRepository,
                            SitterProfileRepository sitterProfileRepository) {
        this.authenticationManager = authenticationManager;
        this.commonService = commonService;
        this.jwtConfig = jwtConfig;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.accountRepository = accountRepository;
        this.customerProfileRepository = customerProfileRepository;
        this.sitterProfileRepository = sitterProfileRepository;
    }

    @PostMapping("login")
    @PermitAll
    public ResponseEntity<ResponseDTO> login(@Validated @RequestBody LoginDTO loginDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());
        try {
            Authentication authenticate = authenticationManager.authenticate(authentication);
            if (authenticate.isAuthenticated()) {
                Account userAuthenticated = accountRepository.findByEmail(authenticate.getName());

                // lấy full name nếu cus bị null thì lấy sitter
                String token = Jwts.builder().setSubject(authenticate.getName())
                        .claim(("authorities"), authenticate.getAuthorities()).claim("id", userAuthenticated.getId())
                        .claim("role",userAuthenticated.getRole().getName())
                        .claim("status", userAuthenticated.getStatus())
                        .claim("fullName", userAuthenticated.getFullName())
                        .setIssuedAt((new Date())).setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
                        .signWith(jwtConfig.secretKey()).compact();

                LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
                        .token(jwtConfig.getTokenPrefix() + token).build();

                // save token device
                userAuthenticated.setDeviceId(loginDTO.getToken());
                accountRepository.save(userAuthenticated);

                responseDTO.setData(loginResponseDTO);
                responseDTO.setSuccessCode(SuccessCode.LOGIN_SUCCESS);
                return ResponseEntity.ok().body(responseDTO);
            } else {
                responseDTO.setErrorCode(ErrorCode.LOGIN_FAIL);
                throw  new ResponseStatusException(HttpStatus.valueOf(400),"sai tài khoảng hoặc mật khẩu");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Sai tài khoản hoặc mật khẩu");
        }
    }
    @PostMapping("login-customer-gmail")
    @PermitAll
    public ResponseEntity<ResponseDTO> loginByCUSGmail(@RequestBody @Valid LoginGmailDTO loginGmailRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,bindingResult.getFieldError().getDefaultMessage());
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("CUSTOMER");
            simpleGrantedAuthorities.add(simpleGrantedAuthority);
            Account account = accountRepository.findByEmail(loginGmailRequestDTO.getEmail());
            Account userAuthenticated = null;
            String token = null;
            if (account != null) {
                Authentication authenticate = new UsernamePasswordAuthenticationToken(account.getEmail(), null);
                userAuthenticated = accountRepository.findByEmail(authenticate.getName());


                token = Jwts.builder().setSubject(authenticate.getName())
                        .claim(("authorities"), simpleGrantedAuthorities)
                        .claim("id", userAuthenticated.getId())
                        .claim("fullName", userAuthenticated.getFullName())
                        .claim("role", userAuthenticated.getRole().getName())
                        .claim("status", userAuthenticated.getStatus())
                        .setIssuedAt((new Date())).setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
                        .signWith(jwtConfig.secretKey()).compact();

                // save token device
                userAuthenticated.setDeviceId(loginGmailRequestDTO.getToken());
                accountRepository.save(userAuthenticated);

                LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
                        .token(jwtConfig.getTokenPrefix() + token).build();
                responseDTO.setData(loginResponseDTO);
                responseDTO.setSuccessCode(SuccessCode.LOGIN_SUCCESS);
            } else {
                userAuthenticated = account.builder()
                        .email(loginGmailRequestDTO.getEmail())
                        .deviceId(loginGmailRequestDTO.getToken())
                        .role(roleRepository.findAllByName("CUSTOMER"))
                        .fullName(loginGmailRequestDTO.getFullName())
                        .status(StatusCode.ACTIVATE.toString())
                        .createDate(LocalDate.now())
                        .build();
                userAuthenticated = accountRepository.save(userAuthenticated);
                CustomerProfile customerProfile = CustomerProfile.builder()
                        .dob(loginGmailRequestDTO.getDob())
                        .gender(loginGmailRequestDTO.getGender())
                        .account(userAuthenticated)
                        .build();
                customerProfileRepository.save(customerProfile);
                userAuthenticated = accountRepository.findByEmail(userAuthenticated.getEmail());
                Authentication authenticate = new UsernamePasswordAuthenticationToken(userAuthenticated.getEmail(), null);
                token = Jwts.builder().setSubject(authenticate.getName())
                        .claim(("authorities"), simpleGrantedAuthorities)
                        .claim("id", userAuthenticated.getId())
                        .claim("fullName", userAuthenticated.getFullName())
                        .claim("role", userAuthenticated.getRole().getName())
                        .claim("status", userAuthenticated.getStatus())
                        .setIssuedAt((new Date())).setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
                        .signWith(jwtConfig.secretKey()).compact();
                LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
                        .token(jwtConfig.getTokenPrefix() + token).build();
                responseDTO.setData(loginResponseDTO);
                responseDTO.setSuccessCode(SuccessCode.CREATED);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Sai tài khoản hoặc mật khẩu", e);
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("login-sitter-gmail")
    @PermitAll
    public ResponseEntity<ResponseDTO> loginBySITGmail(@RequestBody @Valid LoginGmailDTO loginGmailRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,bindingResult.getFieldError().getDefaultMessage());
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("SITTER");
            simpleGrantedAuthorities.add(simpleGrantedAuthority);
            Account account = accountRepository.findByEmail(loginGmailRequestDTO.getEmail());
            Account userAuthenticated = null;
            String token = null;
            if (account != null) {
                Authentication authenticate = new UsernamePasswordAuthenticationToken(account.getEmail(), null);
                userAuthenticated = accountRepository.findByEmail(authenticate.getName());


                token = Jwts.builder().setSubject(authenticate.getName())
                        .claim(("authorities"), simpleGrantedAuthorities)
                        .claim("id", userAuthenticated.getId())
                        .claim("fullName", userAuthenticated.getFullName())
                        .claim("role", userAuthenticated.getRole().getName())
                        .claim("status", userAuthenticated.getStatus())
                        .setIssuedAt((new Date())).setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
                        .signWith(jwtConfig.secretKey()).compact();

                // save token device
                userAuthenticated.setDeviceId(loginGmailRequestDTO.getToken());
                accountRepository.save(userAuthenticated);

                LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
                        .token(jwtConfig.getTokenPrefix() + token).build();
                responseDTO.setData(loginResponseDTO);
                responseDTO.setSuccessCode(SuccessCode.LOGIN_SUCCESS);
            } else {
                userAuthenticated = account.builder()
                        .email(loginGmailRequestDTO.getEmail())
                        .deviceId(loginGmailRequestDTO.getToken())
                        .role(roleRepository.findAllByName("SITTER"))
                        .fullName(loginGmailRequestDTO.getFullName())
                        .status(StatusCode.CREATED.toString())
                        .createDate(LocalDate.now())
                        .build();
                userAuthenticated = accountRepository.save(userAuthenticated);
                SitterProfile sitterProfile = SitterProfile.builder()
                        .dob(loginGmailRequestDTO.getDob())
                        .gender(loginGmailRequestDTO.getGender())
                        .account(userAuthenticated)
                        .build();
                sitterProfileRepository.save(sitterProfile);
                userAuthenticated = accountRepository.findByEmail(userAuthenticated.getEmail());
                Authentication authenticate = new UsernamePasswordAuthenticationToken(userAuthenticated.getEmail(), null);
                token = Jwts.builder().setSubject(authenticate.getName())
                        .claim(("authorities"), simpleGrantedAuthorities)
                        .claim("id", userAuthenticated.getId())
                        .claim("fullName", userAuthenticated.getFullName())
                        .claim("role", userAuthenticated.getRole().getName())
                        .claim("status", userAuthenticated.getStatus())
                        .setIssuedAt((new Date())).setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
                        .signWith(jwtConfig.secretKey()).compact();
                LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
                        .token(jwtConfig.getTokenPrefix() + token).build();
                responseDTO.setData(loginResponseDTO);
                responseDTO.setSuccessCode(SuccessCode.CREATED);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Sai tài khoản hoặc mật khẩu", e);
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("logout/{email}")
    @PreAuthorize("hasAnyRole('CUSTOMER','SITTER')")
    public ResponseEntity<ResponseDTO> logout(@PathVariable String email) {
        ResponseDTO responseDTO = new ResponseDTO();
        Account user = null;
        try {
            user = accountRepository.findByEmail(email);
            if (user != null) {
                user.setDeviceId("");
                accountRepository.save(user);
                responseDTO.setSuccessCode(SuccessCode.LOGOUT_SUCCESS);
            } else {
                responseDTO.setErrorCode(ErrorCode.LOGOUT_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseDTO.setErrorCode(ErrorCode.LOGOUT_ERROR);
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("send-otp")
    @PermitAll
    public ResponseEntity<ResponseDTO> sendOTP(@RequestBody SendOTP sendOTP){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(commonService.sendOTP(sendOTP));
        responseDTO.setSuccessCode(SuccessCode.SEND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PatchMapping("forgot-password")
    @PermitAll
    public ResponseEntity<ResponseDTO> forgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(commonService.forgetPassword(forgotPasswordDTO));
        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PatchMapping("change-password")
    @PreAuthorize("hasAnyRole('CUSTOMER','SITTER')")
    public ResponseEntity<ResponseDTO> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(commonService.changePassword(changePasswordDTO));
        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("check-otp")
    @PermitAll
    public ResponseEntity<ResponseDTO> checkOTP(@RequestBody CheckOTPDTO checkOTPDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(commonService.checkOTP(checkOTPDTO));
        responseDTO.setSuccessCode(SuccessCode.CHECK_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }




}
