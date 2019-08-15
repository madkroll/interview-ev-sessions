package com.madkroll.inteview.evsessions.web.list;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Data object describing single stored session.
 * */
@Getter
@AllArgsConstructor
public class ListSessionsResponseItem {

    private final String id;
    private final String stationId;
    private final String updatedAt;
    private final String status;
}