package dockercomposes.dockercompose.Modelos;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Encuesta {

    @Id
    @GeneratedValue
    private Long id;

    private int cumplieronExpectativas;
    private int dominioDelTema;
    private int instalacionesConfortables;
    private String comentario;


    public Encuesta(int cumplieronExpectativas, int dominioDelTema, int instalacionesConfortables, String comentario) {
        this.cumplieronExpectativas = cumplieronExpectativas;
        this.dominioDelTema = dominioDelTema;
        this.instalacionesConfortables = instalacionesConfortables;
        this.comentario = comentario;
    }

    public Encuesta() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCumplieronExpectativas() {
        return cumplieronExpectativas;
    }

    public void setCumplieronExpectativas(int cumplieronExpectativas) {
        this.cumplieronExpectativas = cumplieronExpectativas;
    }

    public int getDominioDelTema() {
        return dominioDelTema;
    }

    public void setDominioDelTema(int dominioDelTema) {
        this.dominioDelTema = dominioDelTema;
    }

    public int getInstalacionesConfortables() {
        return instalacionesConfortables;
    }

    public void setInstalacionesConfortables(int instalacionesConfortables) {
        this.instalacionesConfortables = instalacionesConfortables;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
