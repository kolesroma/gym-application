package com.ai.xstack.kolesnyk.service.impl;

import com.ai.xstack.kolesnyk.service.BruteForceMonitorService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BruteForceMonitorServiceInMemory implements BruteForceMonitorService {
    private static final int THRESHOLD = 5;

    private final Map<String, Integer> failedAttemptsIpMap = new ConcurrentHashMap<>();

    @Override
    public void trackFailedAttempt(String ip) {
        failedAttemptsIpMap.put(ip, failedAttemptsIpMap.getOrDefault(ip, 0) + 1);
    }

    @Override
    public boolean isSuspiciousIp(String ip) {
        int failedAttempts = failedAttemptsIpMap.getOrDefault(ip, 0);
        return failedAttempts >= THRESHOLD;
    }

    @Override
    public void clearIp(String ip) {
        failedAttemptsIpMap.remove(ip);
    }

    @Override
    public void clearAll() {
        failedAttemptsIpMap.clear();
    }
}
