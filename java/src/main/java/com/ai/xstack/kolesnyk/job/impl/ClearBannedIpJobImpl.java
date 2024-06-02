package com.ai.xstack.kolesnyk.job.impl;

import com.ai.xstack.kolesnyk.job.ClearBannedIpJob;
import com.ai.xstack.kolesnyk.service.BruteForceMonitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClearBannedIpJobImpl implements ClearBannedIpJob {
    private static final int RELOAD_BANNED_IP_IN_MILLIS = 300_000;

    private final BruteForceMonitorService bruteForceMonitorService;

    @Override
    @Scheduled(fixedRate = RELOAD_BANNED_IP_IN_MILLIS)
    public void clearBannedIpList() {
        bruteForceMonitorService.clearAll();
    }
}
