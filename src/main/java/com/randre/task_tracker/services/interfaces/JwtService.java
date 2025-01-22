package com.randre.task_tracker.services.interfaces;

import com.randre.task_tracker.models.UserModel;

import java.time.Instant;

public interface JwtService {

    String generateAccessToken(UserModel user);

    String generateRefreshToken(UserModel user);

    String generateToken(String username, Instant expirationDate);

    String validateToken(String token);
}
