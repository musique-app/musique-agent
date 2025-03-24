package com.projeto_musique.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Result of the login endpoint.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResult {

    @JsonProperty("user")
    private User user;

    @JsonProperty("token")
    private Token token;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User {

        private int id;

        private String username;

        private String email;

        private List<Role> role;

        private Company company;

        private String firstName;

        private String lastName;

        private String location;

        private boolean canCreateUsers;

        private boolean canAccessPanel;

        private boolean canAccessSpeech;

        private boolean isFullTime;

        private String timezone;

        private String lastUpdate;

        private String lastLogin;

        private String phone;

        private String created;

        private String personalStream;

        private String city;

        private String state;

    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Role {

        private int id;

        private String name;

        private String value;

    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Company {

        private int id;

        private String name;

        private String address;

        private String postcode;

        private String phone;

        private String image;

        private String category;

        private String logoImage;

        private boolean active;

        private String stream;

        private int maxFranchise;

        private String lastUpdate;

        private String created;

    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Token {

        private String expiresIn;

        private String accessToken;

    }

}

