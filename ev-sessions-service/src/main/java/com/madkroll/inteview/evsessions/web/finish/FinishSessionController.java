package com.madkroll.inteview.evsessions.web.finish;

import com.madkroll.inteview.evsessions.service.ChargingSession;
import com.madkroll.inteview.evsessions.service.SessionStatefulService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class FinishSessionController {

    private final SessionStatefulService sessionService;

    public FinishSessionController(final SessionStatefulService sessionService) {
        this.sessionService = sessionService;
    }

    @RequestMapping("/chargingSessions/{sessionId}")
    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<FinishSessionResponse> finishSession(@PathVariable final String sessionId) {
        final ChargingSession session = sessionService.finish(sessionId);

        log.info("Finished session {} at station {}", session.getId(), session.getStationId());

        return ResponseEntity
                .ok()
                .body(new FinishSessionResponse(
                        session.getId().toString(),
                        session.getStationId(),
                        session.getUpdatedAt().toString(),
                        session.getStatus().name()
                ));
    }
}