package com.learnleadgrow.springauth.userauthservice.service;

import com.learnleadgrow.springauth.userauthservice.dto.JwtValidationResponseDto;
import com.learnleadgrow.springauth.userauthservice.dto.SessionResponseDto;
import com.learnleadgrow.springauth.userauthservice.dto.SignUpRequestDto;
import com.learnleadgrow.springauth.userauthservice.dto.UserDto;
import com.learnleadgrow.springauth.userauthservice.exception.LoginSessionsLimitException;
import com.learnleadgrow.springauth.userauthservice.mapper.ModelMapper;
import com.learnleadgrow.springauth.userauthservice.model.Role;
import com.learnleadgrow.springauth.userauthservice.model.Session;
import com.learnleadgrow.springauth.userauthservice.model.SessionStatus;
import com.learnleadgrow.springauth.userauthservice.model.User;
import com.learnleadgrow.springauth.userauthservice.repository.RoleRepository;
import com.learnleadgrow.springauth.userauthservice.repository.SessionRepository;
import com.learnleadgrow.springauth.userauthservice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class AuthService {
    private UserRepository userRepository;
    private SessionRepository sessionRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder bcryptPasswordEncoder;
    private ModelMapper modelMapper;
    //private static byte[] secretKeyBytes;

    public AuthService(UserRepository userRepository, SessionRepository sessionRepository, RoleRepository roleRepository, PasswordEncoder bcryptPasswordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.roleRepository = roleRepository;
        this.bcryptPasswordEncoder = bcryptPasswordEncoder;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity login(String email, String password) {
        // Get the UserName and Password
        // Verify the Username and password in the DB
        // 1. UserName validation
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()) {
            // Exception Handling -> UserNotFoundException or UserNameOrPasswordNotValidException
            return null;
        }
        User savedUser = optionalUser.get();
        // Verify User Active Sessions, Limit -> Max. 2 Active sessions else block User from login
        Integer userActiveSessionCount = getUserActiveSessionsCount(savedUser.getId());
        if(userActiveSessionCount >= 2) {
            throw new LoginSessionsLimitException("Reached maximum limit for login Sessions!");
        }
        //2. Password Validation
        //if(!password.equals(savedUser.getPassword())){
        if(!bcryptPasswordEncoder.matches(password, savedUser.getPassword())) {
            // Exception Handling -> UserNameOrPasswordNotValidException
            System.out.println("Username/ Password is Invalid!");
            return null;
        }

        // If exists & matches then Generate token
        // String token = RandomStringUtils.randomAlphanumeric(30);
        // JWT (Token) Generation
        // 1. Token Header (algorithm)
        MacAlgorithm algorithm = Jwts.SIG.HS256;
        // 2. Secret Key for Signing
        SecretKey secretKey = algorithm.key().build();
        // Printing secretkey value
        // Convert SecretKey to a byte array
        byte[] secretKeyBytes = secretKey.getEncoded();
        // Convert byte array to a hexadecimal string
        String hexString = bytesToHex(secretKeyBytes);
        // Print the hexadecimal string representation of the SecretKey
        System.out.println("SecretKey as Hexadecimal String: " + hexString);
        System.out.println("*************");
        System.out.println(secretKey.toString());
        System.out.println("*************");
        // 3. Add Claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", savedUser.getId());
        claims.put("userName", savedUser.getEmail());
        claims.put("roles", savedUser.getRoles());
        claims.put("createdAt", new Date());
        claims.put("expiryAt", new Date(LocalDateTime.now().plusMinutes(2).toEpochSecond(ZoneOffset.of("+05:30"))));

        // 4. Token Builder
        String token = Jwts.builder()
                .claims(claims)
                .signWith(secretKey, algorithm)
                .compact();

        System.out.println("JWT Token: " + token);
        System.out.println("*************");

        // Create Session for the user
        Session session = new Session();
        session.setUser(savedUser);
        session.setToken(token);
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setLoginAt(LocalDateTime.now());
        session.setExpiresAt(LocalDateTime.now().plusMinutes(30));
        session.setSecretKey(secretKey);

        sessionRepository.save(session);
        UserDto userDto = modelMapper.userToUserDtoMapper(savedUser);
        //Create Header and send the responseEntity Object
        MultiValueMapAdapter<String, String> headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add(HttpHeaders.SET_COOKIE, token);
        return new ResponseEntity(userDto, headers, HttpStatus.OK);
    }

    public ResponseEntity<UserDto> signUp(SignUpRequestDto signUpRequestDto) {
        // User Registration
        User user = modelMapper.signUpRequestDtoToUserMapper(signUpRequestDto);
        user.setEmail(signUpRequestDto.getEmail());
        user.setPassword(bcryptPasswordEncoder.encode(signUpRequestDto.getPassword()));
        User createdUser = userRepository.save(user);
        UserDto createdUserDto = modelMapper.userToUserDtoMapper(createdUser);
        return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);
    }

    public ResponseEntity<SessionResponseDto> logout(Long id, String token) {
        Optional<Session> optionalSession = sessionRepository.findByTokenAndUser_Id(token, id);
        if(optionalSession.isEmpty()) {
            //through Exception -> NoActiveSession
            System.out.println("Invalid User Credentials!");
            return null;
        }
        else {
            Session savedSession = optionalSession.get();
            // User token Validation
            //if(!token.equals(savedSession.getToken())) {
            String jwtStatus = validateJwt(id, token).getMessage();
            if(!jwtStatus.equals("Valid JWT!")) {
                // Exception
                System.out.println("Invalid User Credentials");
                return null;
            }else {
                savedSession.setSessionStatus(SessionStatus.ENDED);
                savedSession = sessionRepository.save(savedSession);
                SessionResponseDto sessionResponse = modelMapper.sessionToSessionResponseDto(savedSession);
                return new ResponseEntity<>(sessionResponse, HttpStatus.OK);
            }
        }
    }

    public List<Session> getAllUserSessions(Long id) {
        List<Session> userSessions = sessionRepository.findByUser_Id(id);
        if(userSessions.isEmpty()) {
            //Exception -> NoSessionFound
            return null;
        }
        return userSessions;
    }

    public List<Session> getUserActiveSessions(Long id) {
        List<Session> userActiveSessions = sessionRepository.findByUser_IdAndSessionStatus(id, SessionStatus.ACTIVE);
        System.out.println("********************");
        System.out.println(userActiveSessions); // Sessions List Object
        System.out.println(String.format("User Id: %d, Active Sessions: %d", id, userActiveSessions.size()));
        System.out.println("********************");
        return userActiveSessions;
    }

    public Integer getUserActiveSessionsCount(Long id) {
        List<Session> userActiveSessions = sessionRepository.findByUser_IdAndSessionStatus(id, SessionStatus.ACTIVE);
        System.out.println("Active login Sessions for Id: " + id + " is: ," + userActiveSessions.size());
        return userActiveSessions.size();
    }

    public JwtValidationResponseDto validateJwt(Long userId, String token) {
        JwtValidationResponseDto jwtValidationResponseDto = new JwtValidationResponseDto();
        jwtValidationResponseDto.setMessage("Invalid JWT!");
        // Get Session details from DB
        // we will just compare the token String in DB
        Optional<Session> session = sessionRepository.findByTokenAndUser_Id(token, userId);
        if(session.isEmpty() || session.get().getSessionStatus().equals(SessionStatus.ENDED)
                || !session.get().getExpiresAt().isAfter(LocalDateTime.now())
                || !token.equals(session.get().getToken())) {
            return jwtValidationResponseDto;
        }

        // 1. Get Token and parse it to read the claims (using JwtParser)
        // Convert the secret key string to a SecretKey object
        SecretKey secretKey = Keys.hmacShaKeyFor(session.get().getSecretKey().getEncoded());
        // Parse and decode the JWT token
        Jws<Claims> jws;
        try {
            jws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (RuntimeException e) {
            // Handle exception (e.g., token has expired, or signature is invalid)
            e.printStackTrace();
            return jwtValidationResponseDto;
        }

        // Get the claims from the token
        Claims claims = jws.getBody();
        Map<String, Object> claimsData = new HashMap<>();
        // Print out the claims
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            claimsData.put(entry.getKey(), entry.getValue());
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("-------*****--------");
        Set<Map.Entry<String, Object>> claimsDataSet = (Set<Map.Entry<String, Object>>) claims.entrySet();
        System.out.println("Claims :" + claims.entrySet());
        System.out.println("ClaimsData Map: " + claimsData);
        System.out.println("ClaimsData Set: " + claimsDataSet);
        System.out.println("-------*****--------");
        // 2. Verify the Expiry time with the current time
        // 3. Match the token with the token stored in the session table of the database
        // 4. If matches, return true else false

        System.out.println("-------------------------------------");
        System.out.println("UserId : " + claimsData.get("userId").getClass());
        System.out.println("UserName: " + claimsData.get("userName").getClass());
        System.out.println("Roles: " + claimsData.get("roles").getClass());
        System.out.println("CreatedAt: " + claimsData.get("createdAt").getClass());
        System.out.println("ExpiryAt: " + claimsData.get("expiryAt").getClass());
        System.out.println("-------------------------------------");

        // Map claims data to the JwtValidationResponseDto
        jwtValidationResponseDto.setUserId((Integer) claimsData.get("userId"));
        jwtValidationResponseDto.setUsername((String) claimsData.get("userName"));
        jwtValidationResponseDto.setRoles((List<Role>) claimsData.get("roles"));
        jwtValidationResponseDto.setCreatedAt((Long) claimsData.get("createdAt"));
        jwtValidationResponseDto.setExpiryAt((Integer) claimsData.get("expiryAt"));
        jwtValidationResponseDto.setMessage("Valid JWT!");

        return jwtValidationResponseDto;
    }

    // Helper method to convert byte array to hexadecimal string
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X", b));
        }
        return result.toString();
    }
}
