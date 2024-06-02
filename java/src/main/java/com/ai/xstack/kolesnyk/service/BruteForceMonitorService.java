package com.ai.xstack.kolesnyk.service;

public interface BruteForceMonitorService {
    void trackFailedAttempt(String ip);

    boolean isSuspiciousIp(String ip);

    void clearIp(String ip);

    void clearAll();
}
