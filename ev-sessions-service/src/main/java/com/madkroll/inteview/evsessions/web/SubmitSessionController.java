package com.madkroll.inteview.evsessions.web;

import com.madkroll.inteview.evsessions.service.SessionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class SubmitSessionController {

    private final SessionService sessionService;

    public SubmitSessionController(final SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @RequestMapping("/chargingSessions")
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<SubmitSessionResponse> submit(@RequestBody final SubmitSessionRequest submitSessionRequest) {
        return ResponseEntity
                .ok()
                .body(sessionService.submit(submitSessionRequest.getStationId()));
    }
}