package api.basecamp;

/***
 * 
 * Object used to Authenticate with BaseCamp
 * 
 * @author jon
 *
 */
public class BCAuth {

	private String username;
	private String password;
	private String company;
	private boolean useSsl;
	private String tokenKey;
	private boolean hasToken;
	private String baseUrl;

	/***
	 * Init Empty BCAuth Object
	 * 
	 * Note: properties are public, so you can init them one by one if needed
	 */
	public BCAuth() {

		this.username = "";
		this.password = "";
		this.company = "";
		this.useSsl = false;
		this.tokenKey = "";
		this.hasToken = false;
		this.baseUrl = "";
		init();
	}

	/***
	 * Initialize BaseCamp Authentication Object
	 *
	 * @param un
	 *            BaseCamp Username
	 * @param pw
	 *            BaseCamp Password
	 * @param cp
	 *            BaseCamp Company Name (for url purposes)
	 * @param ssl
	 *            if ssl is enabled on your account
	 */
	public BCAuth(String un, String pw, String cp, boolean ssl) {

		this.username = un;
		this.password = pw;
		this.company = cp;
		this.useSsl = ssl;
		this.tokenKey = "";
		this.hasToken = false;
		init();
	}

	/**
	 * Initialize BaseCamp Authentication Object
	 * 
	 * @author devang.shah
	 * 
	 * @param company
	 * @param useSsl
	 * @param tokenKey
	 */
	public BCAuth(String cp, boolean ssl, String tk) {

		this.username = "";
		this.password = "";
		this.company = cp;
		this.useSsl = ssl;
		this.tokenKey = tk;
		this.hasToken = true;
		init();
	}

	protected void init() {
		if (company != null && !company.isEmpty()) {
			baseUrl = company + ".basecamphq.com";
			baseUrl = useSsl ? "https://" + baseUrl : "http://" + baseUrl;
		}
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public boolean hasToken() {
		return hasToken;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getCompany() {
		return company;
	}

	public boolean isUseSsl() {
		return useSsl;
	}

	public String getTokenKey() {
		return tokenKey;
	}

}
