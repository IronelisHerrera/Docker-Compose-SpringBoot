package dockercomposes.dockercompose.Repositorios;

import dockercomposes.dockercompose.Modelos.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

    Usuario findUsuarioById(Long id);

    Usuario findByUsername(String username);

    Usuario findByUsernameAndPassword(String usuario, String password);

}

