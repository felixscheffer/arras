package com.github.fscheffer.arras.services;

import org.apache.tapestry5.json.JSONObject;

public interface SubmissionProcessor {

    void process(JSONObject object);
}
