package com.example.taskmanagerapi.controllers;

import org.springframework.web.util.UriBuilder;

public abstract class GenericController {
    protected final String CONTROLLER = "Controller";
    protected final String ID = "/{id}";
    protected final UriBuilder uriBuilder;

    public GenericController(UriBuilder uriBuilder) {
        this.uriBuilder = uriBuilder;
    }

    protected String getByIdPath(){
        return this.getPath() + ID;
    }
    /**
     * Get the base path of the controller.
     *
     * @return String
     */
    protected String getPath() {
        String controllerName = this.inferControllerName();
        controllerName = controllerName.replaceAll("([a-z])([A-Z]+)", "$1-$2");
        return "/api/v1/" + controllerName.toLowerCase();
    }
    /**
     * Get the controller name
     *
     * @return String
     */
    private String inferControllerName() {
        String controllerName = this.getClass().getSimpleName();
        return controllerName.replaceAll(CONTROLLER, "");
    }
}
