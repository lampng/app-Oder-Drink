package poly.edu.duan1_lampngpk02586.model;

public class role_model {
    private int id_role;
    private String name_role;

    public role_model() {

    }

    public role_model(int id_role, String name_role) {
        this.id_role = id_role;
        this.name_role = name_role;
    }

    public int getId_role() {
        return id_role;
    }

    public void setId_role(int id_role) {
        this.id_role = id_role;
    }

    public String getName_role() {
        return name_role;
    }

    public void setName_role(String name_role) {
        this.name_role = name_role;
    }
}
