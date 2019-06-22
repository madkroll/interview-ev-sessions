package com.madkroll.inteview.evsessions.service;

import com.madkroll.inteview.evsessions.web.SubmitSessionResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.time.LocalDateTime.now;

@Service
public class SessionService {

    public SubmitSessionResponse submit(final String stationId) {
        return new SubmitSessionResponse(
                UUID.randomUUID().toString(),
                stationId,
                now().toString()
        );
    }
}