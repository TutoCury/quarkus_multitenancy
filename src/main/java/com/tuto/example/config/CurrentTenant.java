package com.tuto.example.config;

import java.util.Optional;

public class CurrentTenant
{

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void set(String tenant)
    {
        threadLocal.set(tenant);
    }

    public static Optional<String> get()
    {
        return Optional.ofNullable(threadLocal.get());
    }

    public static void clear()
    {
        threadLocal.remove();
    }

}
