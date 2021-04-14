package com.api.constants;

/**
 * This class contains the API endpoints (base paths) in the testing scope.
 * @author Muana Kimba
 */
public final class Endpoints {

	// CONSTRUCTOR
	private Endpoints() throws Exception {
		throw new Exception();
	}
	
	// ENDPOINTS
	// Spotify account service
	/**
	 * Get an authorization code that can be exchanged for an access token.<br>
	 * <a href="https://developer.spotify.com/documentation/general/guides/authorization-guide/">Reference</a>
	 */
	public static final String AUTHORIZE = "/authorize";
	
	/**
	 * Get access and refresh tokens.<br>
	 */
	public static final String ACCESS = "/api/token";
	
	// Artists
	/**
	 * Get Spotify catalog information for a single artist identified by their unique Spotify ID.<br>
	 * <a href="https://developer.spotify.com/documentation/web-api/reference/#endpoint-get-an-artist">Reference</a>
	 */
	public static final String GET_AN_ARTIST = "/v1/artists/{id}";
	
	/**
	 * Get Spotify catalog information for several artists based on their Spotify IDs.<br>
	 * <a href="https://developer.spotify.com/documentation/web-api/reference/#endpoint-get-multiple-artists">Reference</a>
	 */
	public static final String GET_MULTIPLE_ARTISTS = "/v1/artists";
	
	// Playlists
	/**
	 * Create a playlist for a Spotify user. (The playlist will be empty until you
	 * <a href="https://developer.spotify.com/documentation/web-api/reference/#endpoint-add-tracks-to-playlist">add tracks</a>.)<br>
	 * <a href="https://developer.spotify.com/documentation/web-api/reference/#endpoint-create-playlist">Reference</a>
	 */
	public static final String CREATE_A_PLAYLIST = "/v1/users/{user_id}/playlists";
	
	/**
	 * Add one or more items to a user’s playlist.<br>
	 * <a href="https://developer.spotify.com/documentation/web-api/reference/#endpoint-add-tracks-to-playlist">Reference</a>
	 */
	public static final String ADD_ITEMS_TO_A_PLAYLIST = "/v1/playlists/{playlist_id}/tracks";
	
	/**
	 * Unfollow a Playlist.<br>
	 * 
	 */
	public static final String UNFOLLOW_A_PLAYLIST = "/v1/playlists/{playlist_id}/followers";
	
	/**
	 * Upload a Custom Playlist Cover Image<br>
	 * 
	 */
	public static final String UPLOAD_A_CUSTOM_PLAYLIST_COVER_IMAGE = "/v1/playlists/{playlist_id}/images";
}