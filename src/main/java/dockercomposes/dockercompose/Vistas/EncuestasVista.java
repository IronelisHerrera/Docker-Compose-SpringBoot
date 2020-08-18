package dockercomposes.dockercompose.Vistas;

import com.vaadin.flow.component.icon.Icon;
import dockercomposes.dockercompose.Servicios.EncuestaServicio;
import dockercomposes.dockercompose.Modelos.Encuesta;
import dockercomposes.dockercompose.Modelos.Usuario;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import dockercomposes.dockercompose.Servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;


@Route("encuesta")
public class EncuestasVista extends VerticalLayout implements BeforeEnterObserver {
    public EncuestasVista(@Autowired EncuestaServicio encuestaService, @Autowired UsuarioServicio usuarioService) {
        NumberField pregunta1 = new NumberField("Las charlas cumplieron sus expectativas?");
        pregunta1.setValue(1d);
        pregunta1.setMin(1);
        pregunta1.setMax(5);
        pregunta1.setHasControls(true);
        pregunta1.setStep(1);
        pregunta1.setWidth("30%");

        NumberField pregunta2 = new NumberField("Los Expositores demostraron dominio del tema?");
        pregunta2.setValue(1d);
        pregunta2.setMin(1);
        pregunta2.setMax(5);
        pregunta2.setHasControls(true);
        pregunta2.setStep(1);
        pregunta2.setWidth("30%");

        NumberField pregunta3 = new NumberField("Las instalaciones eran confortables:");
        pregunta3.setValue(1d);
        pregunta3.setMin(1);
        pregunta3.setMax(5);
        pregunta3.setHasControls(true);
        pregunta3.setStep(1);
        pregunta3.setWidth("30%");

        Icon iconOk = VaadinIcon.CHECK.create();
        TextField comentario = new TextField();
        comentario.setLabel("Algun comentario");
        comentario.setPlaceholder("Escriba aqui..");
        comentario.setWidth("30%");
        comentario.setHeight("10%");
        Notification notification = new Notification(
                "Encuesta registrada", 3000);
        Button enviar = new Button("Enviar puntuación", iconOk);
        enviar.addClickListener(event -> {
            Encuesta encuesta = new Encuesta();
            encuesta.setCumplieronExpectativas(pregunta1.getValue().intValue());
            encuesta.setDominioDelTema(pregunta2.getValue().intValue());
            encuesta.setInstalacionesConfortables(pregunta3.getValue().intValue());
            encuesta.setComentario(comentario.getValue());
            encuestaService.crearEncuesta(encuesta);
            notification.open();
        });


        setAlignItems(Alignment.CENTER);
        Button logout = new Button("Logout");
        logout.setIcon(VaadinIcon.SIGN_OUT.create());
        logout.addClickListener(event -> {
            usuarioService.borrarCookieRecordarme();
            getUI().get().navigate("");
        });
        add(new H1("Encuesta"), pregunta1, pregunta2, pregunta3, comentario, enviar, logout);

        //panel de admin
        Button adminPanel = new Button("Panel de Admin");
        adminPanel.addClickListener(event -> getUI().get().navigate("graficos"));
        if (VaadinSession.getCurrent().getSession().getAttribute("usuario") != null) {
            Usuario usuario = (Usuario) VaadinSession.getCurrent().getSession().getAttribute("usuario");
            if (usuario.isAdmin()) {
                add(adminPanel);
            }
        }

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String usuario = VaadinSession.getCurrent().getAttribute("usuario").toString();
        if (usuario == null)
            event.forwardTo("");
    }
}

