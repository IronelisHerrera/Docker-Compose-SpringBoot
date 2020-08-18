package dockercomposes.dockercompose.Servicios;
import dockercomposes.dockercompose.Modelos.Encuesta;
import dockercomposes.dockercompose.Repositorios.EncuestaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EncuestaServicio {

    @Autowired
    private EncuestaRepositorio encuestaRepo;


    public void crearEncuesta(Encuesta encuesta){

        encuestaRepo.save(encuesta);
    }

    public List<Encuesta> listarEncuestas(){

        return encuestaRepo.findAll();
    }

    public Encuesta encontrarEncuestaPorId(long id){

        return encuestaRepo.findEncuestaById(id);
    }

    public void eliminarEncuesta(long id){

        Encuesta encuestaToDelete = encuestaRepo.findEncuestaById(id);
        encuestaRepo.delete(encuestaToDelete);
    }

    public void  borrarTodasLasEncuestas(){

        encuestaRepo.deleteAll();
    }

}

