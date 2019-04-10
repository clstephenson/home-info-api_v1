package com.clstephenson.homeinfo.api_v1.testutils;

import com.clstephenson.homeinfo.api_v1.model.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Date;

public class TestDataHelper {

    public static User getTestUser() {
        return new User("Chris", "Stephenson", "clstephenson@gmail.com", "password");
    }

    public static Property getTestProperty(User user) {
        return new Property(
                "My House",
                new Address("123 Main St.", "Scottsdale", "Arizona", "85255"),
                2000, 1824, user);
    }

    public static Color getTestColor(Property property, Location location) {
        return new Color(property, "red", "Ben Moore", "C4089", location);
    }

    public static Idea getTestIdea(Property property) {
        return new Idea(property, "Pictures for walls", "some idea notes.");
    }

    public static Location getTestLocation(Property property) {
        return new Location(property, "Living Room", "20 x 15", "notes about room");
    }

    public static Task getTestTask(Property property, Vendor vendor) {
        LocalDate localDate = LocalDate.of(2019, Month.FEBRUARY, 15);
        Date lastCompleted = Date.from(localDate.atStartOfDay().atOffset(ZoneOffset.UTC).toInstant());
        return new Task(property, "tree trimming", lastCompleted, 60, true, vendor);
    }

    public static Material getTestMaterial(Property property, Location location) {
        return new Material(property, "Stone Veneer on Retaining Wall", "Pagosa Springs", location,
                "Some Brand");
    }

    public static Plant getTestPlant(Property property, Location location) {
        return new Plant(property, "Soap Aloe", "Notes about the plant.", location);
    }

    public static StoredFile getTestFile(Property property) {
        return new StoredFile(property, "some file path", StoredFile.FileCategory.DOCUMENT);
    }

    public static Vendor getTestVendor(User user) {
        return new Vendor("Heartwood Tree Care", "5555555555", "someemail@example.com", "heartwoodtreecare.com",
                "some note about the vendor here", user);
    }

    public static Window getTestWindow(Location location) {
        return new Window("west wall window", "36 x 42", location);
    }
}
