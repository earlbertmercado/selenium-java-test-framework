package io.github.earlbertmercado.selenium.dataprovider;

import java.io.IOException;
import java.io.InputStream;
// import java.util.Collections;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.earlbertmercado.selenium.exceptions.FrameworkException;

public final class TestDataLoader {

    private static final String TESTDATA_DIR_KEY = "testdata.dir";
    private static final String DEFAULT_TESTDATA_DIR = "testdata";
    private static final String ENV_KEY = "env";
    private static final String DEFAULT_ENV = "test";
    private static final String USERS_FILE = "users.json";

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static Map<String, TestDataUsers> users;

    private TestDataLoader() {}

    private static synchronized void ensureUsersLoaded() {
        if (users != null) {
            return;
        }

        Path usersPath = resolveTestDataPath(USERS_FILE);
        if (!Files.exists(usersPath)) {
            throw new FrameworkException("Test data file not found: " + usersPath);
        }

        try (InputStream is = Files.newInputStream(usersPath)) {
            users = MAPPER.readValue(is, new TypeReference<Map<String, TestDataUsers>>() {});
        } catch (IOException e) {
            throw new FrameworkException("Failed to load users test data from: " + usersPath, e);
        }
    }

    public static TestDataUsers getUser(String key) {
        ensureUsersLoaded();
        TestDataUsers user = users.get(key);
        if (user == null) {
            throw new FrameworkException("No test data found for key: " + key);
        }
        return user;
    }

    // public static Map<String, TestDataUsers> getAllUsers() {
    //     ensureUsersLoaded();
    //     return Collections.unmodifiableMap(users);
    // }

    private static Path resolveTestDataPath(String fileName) {
        String testDataDir = System.getProperty(TESTDATA_DIR_KEY, DEFAULT_TESTDATA_DIR).trim();
        String env = System.getProperty(ENV_KEY, DEFAULT_ENV).trim().toLowerCase();
        Path configuredPath = Paths.get(testDataDir);
        if (!configuredPath.isAbsolute()) {
            configuredPath = Paths.get(System.getProperty("user.dir"), testDataDir);
        }
        return configuredPath.resolve(env).resolve(fileName).normalize();
    }
}