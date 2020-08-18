package dockercomposes.dockercompose.Repositorios;

import dockercomposes.dockercompose.Modelos.Encuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EncuestaRepositorio extends JpaRepository<Encuesta, Long> {

    Encuesta findEncuestaById(Long id);

}
