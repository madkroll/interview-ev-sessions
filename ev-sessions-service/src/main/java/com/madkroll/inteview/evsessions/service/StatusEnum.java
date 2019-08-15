package com.madkroll.inteview.evsessions.service;

/**
 * All supported charging session states.
 * */
public enum  StatusEnum {
    /**
     * Session is already created but not finished yet.
     * */
    IN_PROGRESS,
    /**
     * Session is already finished, no more updates are possible.
     * */
    FINISHED;
}