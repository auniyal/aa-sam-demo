package com.aa.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.Set;

/**
 * @author Ashish Uniyal
 *
 * Model for input
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "input"
})
public class Request {
    @JsonProperty("input")
    private Set<String> input = null;


}