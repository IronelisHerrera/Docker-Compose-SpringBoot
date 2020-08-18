package dockercomposes.dockercompose.Repositorios;

import dockercomposes.dockercompose.Modelos.UsuarioRecordado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRecordadoRepositorio extends JpaRepository<UsuarioRecordado, String> {
    UsuarioRecordado findUsuarioRecordadosById(String id);
    UsuarioRecordado findUsuarioRecordadosByUsername(String username);
}
