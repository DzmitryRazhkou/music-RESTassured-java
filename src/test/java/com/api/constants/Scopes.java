package com.api.constants;

/**
 * This class contains the API scopes for endpoints.
 * @author Muana Kimba
 */
public final class Scopes {

	// CONSTRUCTOR
	private Scopes() throws Exception {
		throw new Exception();
	}
	
	// SCOPES
	/**
	 * Contains the following User scopes:<br><br>
	 * user-read-playback-position&nbsp;&nbsp;&nbsp;&nbsp;user-read-private&nbsp;&nbsp;&nbsp;&nbsp;user-read-email<br>
	 * user-read-playback-state&nbsp;&nbsp;&nbsp;&nbsp;user-read-currently-playing&nbsp;&nbsp;&nbsp;&nbsp;user-read-recently-played<br>
	 * user-library-read&nbsp;&nbsp;&nbsp;&nbsp;user-library-modify&nbsp;&nbsp;&nbsp;&nbsp;user-top-read<br>
	 * user-follow-read&nbsp;&nbsp;&nbsp;&nbsp;user-follow-modify&nbsp;&nbsp;&nbsp;&nbsp;user-modify-playback-state
	 */
	public static final String USER = "user-read-playback-position user-read-private user-read-email"
			+ " user-read-playback-state user-read-currently-playing user-read-recently-played"
			+ " user-library-read user-library-modify user-top-read"
			+ " user-follow-read user-follow-modify user-modify-playback-state";
	
	/**
	 * Contains the following Playlist scopes:<br><br>
	 * playlist-read-private&nbsp;&nbsp;&nbsp;&nbsp;playlist-read-collaborative<br>
	 * playlist-modify-public&nbsp;&nbsp;&nbsp;&nbsp;playlist-modify-private
	 */
	public static final String PLAYLIST = "playlist-read-private playlist-read-collaborative"
			+ " playlist-modify-public playlist-modify-private";
	
	/**
	 * Contains the following UGC scopes:<br><br>
	 * ugc-image-upload
	 */
	public static final String UGC = "ugc-image-upload";
	
	/**
	 * Contains all available scopes.
	 */
	public static final String ALL = String.format("%s %s %s", USER, PLAYLIST, UGC);
}