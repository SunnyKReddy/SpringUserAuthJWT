package com.learnleadgrow.springauth.userauthservice.mapper;

import com.learnleadgrow.springauth.userauthservice.dto.SessionResponseDto;
import com.learnleadgrow.springauth.userauthservice.dto.SignUpRequestDto;
import com.learnleadgrow.springauth.userauthservice.dto.UserDto;
import com.learnleadgrow.springauth.userauthservice.model.Session;
import com.learnleadgrow.springauth.userauthservice.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ModelMapper {
    public UserDto userToUserDtoMapper(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());

        return userDto;
    }

    public User signUpRequestDtoToUserMapper(SignUpRequestDto signUpRequestDto) {
        User user = new User();
        user.setEmail(signUpRequestDto.getEmail());
        user.setPassword(signUpRequestDto.getPassword());
        return user;
    }

    public SessionResponseDto sessionToSessionResponseDto(Session session) {
        SessionResponseDto sessionResponseDto = new SessionResponseDto();
        sessionResponseDto.setUserId(session.getUser().getId());
        sessionResponseDto.setSessionId(session.getId());
        sessionResponseDto.setToken(session.getToken());
        sessionResponseDto.setExpiresAt(session.getExpiresAt());
        sessionResponseDto.setLoginAt(session.getLoginAt());
        sessionResponseDto.setSessionStatus(session.getSessionStatus());
        return sessionResponseDto;
    }

    public List<SessionResponseDto> sessionListToSessionResponseDtoList(List<Session> sessions) {
        List<SessionResponseDto> sessionResponses = new ArrayList<>();
        for(Session session : sessions) {
            SessionResponseDto sessionResponseDto = new SessionResponseDto();
            sessionResponses.add(sessionToSessionResponseDto(session));
        }
        return sessionResponses;
    }
}
