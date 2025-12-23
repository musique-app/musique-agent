package com.projeto_musique.agent.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Result of the login endpoint.
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResult {

    @JsonProperty("user")
    private User user;

    @JsonProperty("accessToken")
    private AccessToken accessToken;

    @JsonProperty("refreshToken")
    private AccessToken refreshToken;

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
    public static class AccessToken {

        private String token;

        private String expiresIn;

    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RefreshToken {

        private String token;

        private String expiresIn;

    }

}

