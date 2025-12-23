package com.projeto_musique.agent.core.connectivity;

import com.projeto_musique.agent.Properties;
import com.projeto_musique.agent.http.Client;
import com.projeto_musique.agent.http.Method;
import com.projeto_musique.agent.models.GetStreamResult;
import com.projeto_musique.agent.models.exceptions.RequestException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * This class is responsible for getting the stream URL.
 */
@Slf4j
public class Stream {

    /**
     * Request the stream URL given the accessToken.
     *
     * @param accessToken of the user to get the stream
     * @return GetStreamResult with the stream URL
     * @throws RequestException if the request fails
     */
    public GetStreamResult request(String accessToken) throws RequestException {
        log.info("Getting stream for access token: {}", accessToken);

        return Client.send(
                Properties.BASE_URL + "/api/company/stream",
                Map.of("Authorization", "Bearer " + accessToken),
                Method.GET,
                GetStreamResult.class
        );
    }

}
