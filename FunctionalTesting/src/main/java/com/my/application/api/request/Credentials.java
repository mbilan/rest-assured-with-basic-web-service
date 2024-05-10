package com.my.application.api.request;

import lombok.NonNull;

public record Credentials(@NonNull String login, @NonNull String password) {}
