package com.selfxdsd.core;

import com.selfxdsd.api.Collaborators;
import com.selfxdsd.api.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import java.net.HttpURLConnection;
import java.net.URI;

/**
 * Gitlab repo collaborators.
 * @author criske
 * @version $Id$
 * @since 0.0.13
 */
final class GitlabCollaborators implements Collaborators {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(
        GitlabCollaborators.class
    );

    /**
     * Gitlab repo Collaborators base uri.
     */
    private final URI collaboratorsUri;

    /**
     * Gitlab's JSON Resources.
     */
    private final JsonResources resources;

    /**
     * Self storage, in case we want to store something.
     */
    private final Storage storage;

    /**
     * Ctor.
     *
     * @param resources Gitlab's JSON Resources.
     * @param collaboratorsUri Collaborators base URI.
     * @param storage Storage.
     */
    GitlabCollaborators(
        final JsonResources resources,
        final URI collaboratorsUri,
        final Storage storage
    ) {
        this.resources = resources;
        this.collaboratorsUri = collaboratorsUri;
        this.storage = storage;
    }

    /**
     * Invite a user to be a collaborator.
     * <br><br>
     * Note that for Gitlab the username must be the user ID
     * and permission is the access level with following
     * <a href="https://docs.gitlab.com/ee/api/members.html#valid-access-levels">
     * values </a>
     * @param username User ID.
     * @return True or false, whether the invitations was successful or not.
     */
    @Override
    public boolean invite(final String username) {
        final boolean result;
        LOG.debug(
            "Inviting user " + username
            + " to [" + this.collaboratorsUri.toString() + "]."
        );
        final Resource response = this.resources.post(
                this.collaboratorsUri,
                Json.createObjectBuilder()
                        .add("user_id", username)
                        .add("access_level", 30)
                        .build()
        );
        if(response.statusCode() == HttpURLConnection.HTTP_CREATED) {
            result = true;
            LOG.debug("Invitation successfully created!");
        } else if (response.statusCode() == HttpURLConnection.HTTP_CONFLICT) {
            result = true;
            LOG.debug("User was already invited, everything is ok.");
        } else {
            result = false;
            LOG.error(
                "Unexpected status when inviting user " + username
                    + " to [" + this.collaboratorsUri.toString() + "]. "
                    + "Expected 201 CREATED or 409 CONFLICT, but got "
                    + response.statusCode()
            );
        }
        return result;
    }

    @Override
    public boolean remove(final String username) {
        final boolean result;
        LOG.debug(
            "Removing user " + username
            + " from [" + this.collaboratorsUri.toString() + "]... "
        );
        final Resource response = this.resources.delete(
            URI.create(
                this.collaboratorsUri.toString() + "/" + username
            ),
            Json.createObjectBuilder().build()
        );
        final int status = response.statusCode();
        if(status == HttpURLConnection.HTTP_NO_CONTENT) {
            result = true;
            LOG.debug("User successfully removed!");
        } else {
            result = false;
            LOG.error(
                "Problem while removing user. Expected 204 NO CONTENT, "
                + "but got " + status + ". "
            );
        }
        return result;
    }

}
