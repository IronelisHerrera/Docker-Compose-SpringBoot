package dockercomposes.dockercompose.Servicios;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import  dockercomposes.dockercompose.Modelos.Usuario;
import dockercomposes.dockercompose.Modelos.UsuarioRecordado;
import dockercomposes.dockercompose.Repositorios.UsuarioRecordadoRepositorio;
import  dockercomposes.dockercompose.Repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepo;
    @Autowired
    private UsuarioRecordadoRepositorio usuariosRecordados;


    public void crearUsuario(Usuario usuario) {

        usuarioRepo.save(usuario);
    }

    public boolean existe(Usuario usuario) {
        return !usuarioRepo.findByUsername(usuario.getUsername()).toString().isEmpty();
    }


    public List<Usuario> listarUsuarios() {

        return usuarioRepo.findAll();
    }

    public Usuario encontrarUsuarioPorId(long id) {
        return usuarioRepo.findUsuarioById(id);
    }

    public Usuario encontrarUsuarioPorUsername(String username){
        return usuarioRepo.findByUsername(username);
    }

    public void eliminarUsuario(long id) {

        // Igualo  el cliente al cliente que buscamos mediante el id
        Usuario usuarioToDelete = usuarioRepo.findUsuarioById(id);

        // y aqui lo borro
        usuarioRepo.delete(usuarioToDelete);
    }

    public void borrarTodosLosUsuarios() {

        usuarioRepo.deleteAll();
    }

    // ============
    // Auth & Login
    // ============

    public boolean isLoggeado(){
        return VaadinSession.getCurrent().getAttribute("usuario") != null || loginRecordado();
    }

    public Usuario login(String usuario, String password) {
        return usuarioRepo.findByUsernameAndPassword(usuario, password);
    }

    private boolean loginRecordado(){
        Optional<Cookie> cookie = getCookieRecordarme();

        if(cookie.isPresent() && !cookie.get().getValue().equals("")){
            String id = cookie.get().getValue();
            String usuario = null;
            try{
                usuario = getUsuarioRecordado(id);
            } catch (NullPointerException e){
                System.out.println("Usuario nulo");
            }


            if(usuario != null){
                VaadinSession.getCurrent().setAttribute("usuario", usuario);
                return true;
            } else return false;
        }
        return false;
    }

    private Optional<Cookie> getCookieRecordarme(){
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
        if(cookies != null){
            return Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("recordarme")).findFirst();
        }
        return Optional.empty();
    }

    public void borrarCookieRecordarme(){
        Optional<Cookie> cookieVieja = getCookieRecordarme();
        if(cookieVieja.isPresent()){
            String idCookie = cookieVieja.get().getValue();
            UsuarioRecordado borrar = usuariosRecordados.findUsuarioRecordadosById(idCookie);
            if(borrar != null){
                usuariosRecordados.delete(borrar);
            }
        }
        Cookie cookie = new Cookie("recordarme", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        VaadinService.getCurrentResponse().addCookie(cookie);
        VaadinSession.getCurrent().setAttribute("usuario", null);
    }

    public void recordarUsuario(String usuario){
        String id = idRecordar(usuario);
        Cookie cookie = new Cookie("recordarme", id);
        // Recordar por 1 semana
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 7);
        VaadinService.getCurrentResponse().addCookie(cookie);
    }

    private String getUsuarioRecordado(String id){
        return usuariosRecordados.findUsuarioRecordadosById(id).getUsername();
    }

    private String idRecordar(String usuario){
        SecureRandom random = new SecureRandom();
        String id = new BigInteger(130, random).toString(16);
        UsuarioRecordado tmp = new UsuarioRecordado(id, usuario);
        usuariosRecordados.save(tmp);
        return id;
    }

}
