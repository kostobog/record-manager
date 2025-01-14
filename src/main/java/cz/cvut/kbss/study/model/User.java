package cz.cvut.kbss.study.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.cvut.kbss.jopa.model.annotations.*;
import cz.cvut.kbss.study.model.util.HasDerivableUri;
import cz.cvut.kbss.study.util.Constants;
import cz.cvut.kbss.study.util.IdentificationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@OWLClass(iri = Vocabulary.s_c_user)
public class User implements HasDerivableUri, Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(User.class);

    @Id
    private URI uri;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.s_p_firstName)
    private String firstName;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.s_p_lastName)
    private String lastName;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.s_p_accountName)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OWLDataProperty(iri = Vocabulary.s_p_password)
    private String password;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.s_p_mbox)
    private String emailAddress;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OWLDataProperty(iri = Vocabulary.s_p_token)
    private String token;

    @OWLDataProperty(iri = Vocabulary.s_p_isInvited)
    private Boolean isInvited;

    @OWLDataProperty(iri = Vocabulary.s_p_created)
    private Date dateCreated;

    @ParticipationConstraints(nonEmpty = true)
    @OWLObjectProperty(iri = Vocabulary.s_p_is_member_of, fetch = FetchType.EAGER)
    private Institution institution;

    @Types
    private Set<String> types;

    public User() {
        this.types = new HashSet<>();
        types.add(Vocabulary.s_c_doctor);
    }

    @Override
    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Gets date when this user's account was created.
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    public void addType(String type) {
        assert types != null;
        getTypes().add(type);
    }

    public String getToken() { return token; }

    public void setToken(String token) { this.token = token; }

    public Boolean getIsInvited() { return isInvited; }

    public void setIsInvited(Boolean isInvited) { this.isInvited = isInvited; }

    /**
     * Encodes password of this person.
     *
     * @param encoder Encoder to user to encode the password
     */
    public void encodePassword(PasswordEncoder encoder) {
        if (password == null || password.isEmpty()) {
            throw new IllegalStateException("Cannot encode an empty password.");
        }
        this.password = encoder.encode(password);
    }

    /**
     * Erases the password.
     * <p>
     * Handy for example before sending the instance outside the application.
     */
    public void erasePassword() {
        this.password = null;
    }

    @Override
    public void generateUri() {
        if (uri != null) {
            return;
        }
        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalStateException("Cannot generate Person URI without first name.");
        }
        if (lastName == null || lastName.isEmpty()) {
            throw new IllegalStateException("Cannot generate Person URI without last name.");
        }
        try {
            this.uri = URI.create(Constants.BASE_URI +
                    URLEncoder.encode(firstName + "-" + lastName + "-" + IdentificationUtils.generateRandomURINumber(),
                    StandardCharsets.UTF_8.toString()));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Cannot generate Person URI due to unsupported encoding.", e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return uri != null ? uri.equals(user.uri) : user.uri == null;

    }

    @Override
    public int hashCode() {
        return uri != null ? uri.hashCode() : 0;
    }

    @Override
    public String toString() {
        return firstName + ' ' + lastName + '<' + uri + '>';
    }
}
