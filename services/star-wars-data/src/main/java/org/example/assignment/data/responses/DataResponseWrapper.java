package org.example.assignment.data.responses;

import lombok.Data;

@Data
public class DataResponseWrapper<T> {
    private int count;
    private String next;
    private String previous;
    private T results;
}
