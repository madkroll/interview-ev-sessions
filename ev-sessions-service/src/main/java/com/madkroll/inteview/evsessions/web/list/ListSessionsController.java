package com.madkroll.inteview.evsessions.web.list;

import com.google.common.collect.ImmutableList;
import com.madkroll.inteview.evsessions.service.ChargingSession;
import com.madkroll.inteview.evsessions.service.SessionStatefulService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@RestController
@Log4j2
public class ListSessionsController {

    private final SessionStatefulService sessionService;

    public ListSessionsController(final SessionStatefulService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping(path = "/chargingSessions", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<ListSessionsResponseItem>> list() {
        final List<ChargingSession> sessions = sessionService.list();

        log.info("Total sessions: {}", sessions.size());

        return ResponseEntity
                .ok()
                .body(sessions.stream()
                        .map(chargingSession -> new ListSessionsResponseItem(
                                chargingSession.getId().toString(),
                                chargingSession.getStationId(),
                                chargingSession.getUpdatedAt().toString(),
                                chargingSession.getStatus().name()
                        ))
                        .collect(collectingAndThen(toList(), ImmutableList::copyOf))
                );
    }
}