package cz.cvut.kbss.study.rest.util;

import cz.cvut.kbss.study.exception.WebServiceIntegrationException;
import cz.cvut.kbss.study.util.Constants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class RestUtils {

    private RestUtils() {
        throw new AssertionError();
    }

    /**
     * Creates HTTP headers object with a location header with the specified path appended to the current request URI.
     * <p>
     * The {@code uriVariableValues} are used to fill in possible variables specified in the {@code path}.
     *
     * @param path              Path to add to the current request URI in order to construct a resource location
     * @param uriVariableValues Values used to replace possible variables in the path
     * @return HttpHeaders with location headers set
     */
    public static HttpHeaders createLocationHeaderFromCurrentUri(String path, Object... uriVariableValues) {
        assert path != null;

        final URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path(path).buildAndExpand(
                uriVariableValues).toUri();
        final HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.LOCATION, location.toASCIIString());
        return headers;
    }

    /**
     * Creates HTTP headers object with a location header with the specified path appended to the current context path.
     * <p>
     * The {@code uriVariableValues} are used to fill in possible variables specified in the {@code path}.
     * <p>
     * This method is useful when major part of the request URI needs to be replaced and so it is easier to start from
     * base context path and append the new path to it.
     *
     * @param path              Path to add to the current request URI in order to construct a resource location
     * @param uriVariableValues Values used to replace possible variables in the path
     * @return HttpHeaders with location headers set
     */
    public static HttpHeaders createLocationHeaderFromContextPath(String path, Object... uriVariableValues) {
        assert path != null;

        final URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path(path)
                                                        .buildAndExpand(uriVariableValues).toUri();
        final HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.LOCATION, location.toASCIIString());
        return headers;
    }

    /**
     * Encodes the specifies value with an URL encoder.
     *
     * @param value The value to encode
     * @return Encoded string
     */
    public static String encodeUrl(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    public static String getCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
