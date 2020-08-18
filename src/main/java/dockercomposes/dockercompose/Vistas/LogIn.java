package dockercomposes.dockercompose.Vistas;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.textfield.EmailField;
import dockercomposes.dockercompose.Servicios.UsuarioServicio;
import dockercomposes.dockercompose.Modelos.Usuario;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;


@Route("")
public class LogIn extends VerticalLayout implements BeforeEnterObserver {

    TextField textField;
    PasswordField passwordField;
    com.vaadin.flow.component.checkbox.Checkbox rememberMe;
    Button button;

    @Autowired
    UsuarioServicio servicio;

    public LogIn(@Autowired UsuarioServicio usuarioServices) {

        if (usuarioServices.listarUsuarios().size() == 0) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword("admin");
            admin.setAdmin(true);
            usuarioServices.crearUsuario(admin);
            Usuario usuario = new Usuario();
            usuario.setUsername("usuario");
            usuario.setPassword("1234");
            usuario.setAdmin(false);
            usuarioServices.crearUsuario(usuario);
        }
        H1 title = new H1();
        title.getStyle().set("color", "purple");
        Icon icon = VaadinIcon.ABACUS.create();
        icon.setSize("30px");
        icon.getStyle().set("top", "-4px");
        title.add(icon);
        title.add(new Text(" Encuestas"));


        textField = new TextField("Usuario");
        //textField.setIcon(VaadinIcon.USER);
        passwordField = new PasswordField("Contraseña");
        rememberMe = new Checkbox("Recordarme", false);
        button = new Button("Iniciar Sesión");

        textField.setRequired(true);
        passwordField.setRequired(true);
        passwordField.setRevealButtonVisible(true);

        button.addClickListener(event -> {

            if (textField.getValue().isEmpty()) {
                textField.setRequiredIndicatorVisible(true);
                textField.setInvalid(true);
            }

            if (passwordField.getValue().isEmpty()) {
                passwordField.setInvalid(true);
                passwordField.setRequiredIndicatorVisible(true);
            }

            if (!textField.isEmpty() && !passwordField.isEmpty()) {
                Usuario usrLogin = usuarioServices.login(textField.getValue(), passwordField.getValue());
                if (usrLogin != null) {
                    VaadinSession.getCurrent().setAttribute("usuario", usrLogin.getUsername());
                    if (getUI().isPresent() && usrLogin.isAdmin())
                        getUI().get().navigate(GraficosVista.class);
                    else
                        getUI().get().navigate(EncuestasVista.class);
                    // Si el checkbox esta marcado se recuerda al usuario que se esta loggeando
                    if(rememberMe.getValue()){
                        usuarioServices.recordarUsuario(usrLogin.getUsername());
                    }
                } else {

                    Dialog dialog = new Dialog();

                    dialog.add(new VerticalLayout(new H3("El usuario no fue encontrado!")));
                    dialog.open();

                    textField.setRequiredIndicatorVisible(true);
                    passwordField.setRequiredIndicatorVisible(true);

                }
            }

        });

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setAlignItems(Alignment.CENTER);
        horizontalLayout.setSizeFull();

        VerticalLayout verticalLayout = new VerticalLayout();

        verticalLayout.setAlignItems(Alignment.CENTER);
        verticalLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        verticalLayout.setAlignSelf(Alignment.CENTER, textField, passwordField);
        verticalLayout.setSizeFull();

        //Bloque intento de registro - Just ignore
        Dialog dialog = new Dialog();
        VerticalLayout verticalDialog = new VerticalLayout();
        verticalDialog.setWidthFull();

        TextField usuarioField = new TextField();
        usuarioField.setVisible(false);
        usuarioField.setLabel("Usuario");

        EmailField emailField = new EmailField();
        emailField.setVisible(false);
        emailField.setLabel("Email");

        Button registrarse = new Button("Registrarse");
        registrarse.setVisible(false);
        verticalDialog.add(usuarioField, emailField, registrarse);
        dialog.add(verticalDialog);

        Button registro = new Button("Registrar Nuevo Usuario");
        registrarse.setVisible(false);
        registro.addClickListener(event -> dialog.open());

        verticalLayout.add(title, textField, passwordField, button);
        horizontalLayout.add(verticalLayout);
        add(horizontalLayout);
        //end block - intento registro
    }


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(servicio.isLoggeado()){
            Usuario usuario = servicio.encontrarUsuarioPorUsername(VaadinSession.getCurrent().getAttribute("usuario").toString());
            if(usuario != null){
                if(usuario.isAdmin()){
                    event.forwardTo("graficos");
                } else {
                    event.forwardTo("encuesta");
                }
            }
        }
    }
}

