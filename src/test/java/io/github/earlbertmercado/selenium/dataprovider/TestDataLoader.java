package io.github.earlbertmercado.selenium.dataprovider;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.earlbertmercado.selenium.exceptions.FrameworkException;

/**
 * Loads test user data from JSON files under the configured environment folder.
 *
 * Uses a cache to avoid reloading the same data multiple times during the test run.
 */
public final class TestDataLoader {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final TypeReference<Map<String, TestDataUsers>> USERS_TYPE = new TypeReference<>() {};

    private static final String TESTDATA_DIR = System.getProperty("testdata.dir", "testdata").trim();
    private static final String ENV = System.getProperty("env", "test").trim().toLowerCase();

    private static Map<String, TestDataUsers> users;

    private TestDataLoader() {}

    public static TestDataUsers getUser(String key) {
        ensureUsersLoaded();
        TestDataUsers user = users.get(key);
        if (user == null) {
            throw new FrameworkException("No test data found for key: " + key);
        }
        return user;
    }

    private static synchronized void ensureUsersLoaded() {
        if (users != null) return;
        users = loadJsonFile("users.json", USERS_TYPE);
    }

    private static <T> T loadJsonFile(String fileName, TypeReference<T> type) {
        Path path = resolveTestDataPath(fileName);
        if (!Files.exists(path)) {
            throw new FrameworkException("Test data file not found: " + path);
        }
        try (InputStream is = Files.newInputStream(path)) {
            return MAPPER.readValue(is, type);
        } catch (IOException e) {
            throw new FrameworkException("Failed to load test data from: " + path, e);
        }
    }

    private static Path resolveTestDataPath(String fileName) {
        Path base = Paths.get(TESTDATA_DIR);
        if (!base.isAbsolute()) {
            base = Paths.get(System.getProperty("user.dir"), TESTDATA_DIR);
        }
        return base.resolve(ENV).resolve(fileName).normalize();
    }
}