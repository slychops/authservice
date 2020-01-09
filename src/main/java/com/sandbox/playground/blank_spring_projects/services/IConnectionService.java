package com.sandbox.playground.blank_spring_projects.services;

import org.springframework.util.MultiValueMap;

public interface IConnectionService {

    MultiValueMap<String, String> prepareHeader();
}
