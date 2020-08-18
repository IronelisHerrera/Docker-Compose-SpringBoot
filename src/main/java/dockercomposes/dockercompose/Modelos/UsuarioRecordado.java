package dockercomposes.dockercompose.Modelos;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class UsuarioRecordado {

    @Id
    @GeneratedValue
    private String id;

    private String username;

    public UsuarioRecordado() { }

    public UsuarioRecordado(String id, String usuario){
        this.id = id;
        this.username = usuario;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }
}
