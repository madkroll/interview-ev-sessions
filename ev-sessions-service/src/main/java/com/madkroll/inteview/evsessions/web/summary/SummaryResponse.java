package com.madkroll.inteview.evsessions.web.summary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Data object containing summary of stored sessions.
 */
@Getter
@ToString
@AllArgsConstructor
public class SummaryResponse {

    private final int totalCount;
    private final int startedCount;
    private final int stoppedCount;
}