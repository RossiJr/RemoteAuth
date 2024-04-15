package com.rossijr.remoteauth.config.messages;

import java.util.HashMap;

/**
 * Parameter builder for messages replacement, basically used to simplify and transform to an inline way to build messages
 * For future implementations, it will be a more complete component
 */
public class ParameterBuilder {
    /**
     * Hashmap to store the parameters and their values
     */
    private final HashMap<Parameters, String> parameters;

    public ParameterBuilder() {
        this.parameters = new HashMap<>();
    }

    /**
     * Add a parameter to the hashmap with its value
     * @param parameter parameter to be added
     * @param value value of the parameter
     * @return the builder itself
     */
    public ParameterBuilder addParameter(Parameters parameter, String value){
        parameters.put(parameter, value);
        return this;
    }

    /**
     * Build the hashmap with the parameters provided
     * @return hashmap with the parameters
     */
    public HashMap<Parameters, String> build(){
        return parameters;
    }

    public static ParameterBuilder create(){
        return new ParameterBuilder();
    }
}
