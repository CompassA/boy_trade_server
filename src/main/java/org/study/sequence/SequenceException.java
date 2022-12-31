package org.study.sequence;

import lombok.Getter;

/**
 * @author Tomato
 * Created on 2022.12.31
 */
public class SequenceException extends Exception {

    @Getter
    private final String message;

    public SequenceException(String message) {
        super();
        this.message = message;
    }
}
