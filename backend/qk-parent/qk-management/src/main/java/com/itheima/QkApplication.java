package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@SpringBootApplication
public class QkApplication {
    public static void main(String[] args) {
        loadDotenv();
        SpringApplication.run(QkApplication.class, args);
    }

    private static void loadDotenv() {
        File envFile = findEnvFile();
        if (envFile == null) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(envFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                int idx = line.indexOf('=');
                if (idx <= 0) continue;
                String key = line.substring(0, idx).trim();
                String value = line.substring(idx + 1).trim();
                if (value.length() >= 2
                        && ((value.startsWith("\"") && value.endsWith("\""))
                        || (value.startsWith("'") && value.endsWith("'")))) {
                    value = value.substring(1, value.length() - 1);
                }
                if (System.getenv(key) == null) {
                    System.setProperty(key, value);
                }
            }
        } catch (Exception ignored) {
        }
    }

    private static File findEnvFile() {
        File current = new File(System.getProperty("user.dir"));
        while (current != null) {
            File candidate = new File(current, ".env");
            if (candidate.exists()) return candidate;
            current = current.getParentFile();
        }
        return null;
    }
}
