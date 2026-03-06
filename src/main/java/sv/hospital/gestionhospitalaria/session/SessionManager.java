package sv.hospital.gestionhospitalaria.session;

public class SessionManager {
    private static SessionManager instance;
    private int userId;
    private String userName;
    private String rolNombre;
    private String token;
    private Integer medicoId;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) instance = new SessionManager();
        return instance;
    }

    public void login(int userId, String userName, String rolNombre, String token, Integer medicoId) {
        this.userId = userId;
        this.userName = userName;
        this.rolNombre = rolNombre;
        this.token = token;
        this.medicoId = medicoId;
    }

    public void logout() {
        userId = 0; userName = null; rolNombre = null; token = null; medicoId = null;
    }

    public int getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getRolNombre() { return rolNombre; }
    public String getToken() { return token; }
    public Integer getMedicoId() { return medicoId; }
}