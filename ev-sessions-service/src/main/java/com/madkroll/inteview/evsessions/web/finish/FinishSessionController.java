package com.madkroll.inteview.evsessions.web.finish;

import com.madkroll.inteview.evsessions.service.ChargingSession;
import com.madkroll.inteview.evsessions.service.SessionStatefulService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring MVC REST controller providing endpoint to finish stored session in progress.
 * */
@RestController
@Log4j2
public class FinishSessionController {

    private final SessionStatefulService sessionService;

    public FinishSessionController(final SessionStatefulService sessionService) {
        this.sessionService = sessionService;
    }

    /**
     * Finishes stored target session in progress by given session ID.
     * @param sessionId path variable containing session ID to finish.
     * @return successful response entity object containing finishing session operation result
     * */
    @PutMapping(path = "/chargingSessions/{sessionId}", produces = {MediaType.APPLICATION_JSON_VALUE})
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