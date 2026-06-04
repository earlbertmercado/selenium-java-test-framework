package io.github.earlbertmercado.selenium.dataprovider;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.earlbertmercado.selenium.exceptions.FrameworkException;

public class TestDataLoader {

    private static final String USERS_TEST_DATA = "/testdata/users.json";
    private static Map<String, TestDataUsers> users;

    private TestDataLoader() {}

    private static synchronized void ensureLoaded() {
        if (users != null) {
            return;
        }

        ObjectMapper mapper = new ObjectMapper();

        try (InputStream is = TestDataLoader.class.getResourceAsStream(USERS_TEST_DATA)) {
            if (is == null) {
                users = Collections.emptyMap();
            } else {
                users = mapper.readValue(is, new TypeReference<Map<String, TestDataUsers>>() {});
            }
        } catch (IOException e) {
            throw new FrameworkException("Failed to load test data: " + USERS_TEST_DATA, e);
        }
    }

    public static TestDataUsers getUser(String key) {
        ensureLoaded();
        TestDataUsers user = users.get(key);
        if (user == null) throw new FrameworkException("No test data found for key: " + key);
        return user;
    }

    public static Map<String, TestDataUsers> getAllUsers() {
        ensureLoaded();
        return Collections.unmodifiableMap(users);
    }
}