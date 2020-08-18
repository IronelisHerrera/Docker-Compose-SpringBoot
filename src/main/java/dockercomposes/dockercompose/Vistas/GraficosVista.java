package dockercomposes.dockercompose.Vistas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import dockercomposes.dockercompose.Modelos.Encuesta;
import dockercomposes.dockercompose.Modelos.Usuario;
import dockercomposes.dockercompose.Servicios.EncuestaServicio;
import dockercomposes.dockercompose.Servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;

@Route("graficos")
public class GraficosVista extends VerticalLayout implements BeforeEnterObserver {
    @Autowired
    UsuarioServicio usuarioService;


    public GraficosVista(@Autowired EncuestaServicio encuestaService) {

        //graficos Pyramid
        Chart chart = new Chart(ChartType.PYRAMID);
        Configuration conf = chart.getConfiguration();
        conf.setTitle("Promedio de respuestas por pregunta");

        Tooltip tooltip = new Tooltip();
        tooltip.setValueDecimals(2);
        conf.setTooltip(tooltip);

        PlotOptionsPyramid plotOptions = new PlotOptionsPyramid();
        plotOptions.setAllowPointSelect(true);
        plotOptions.setCursor(Cursor.POINTER);
        plotOptions.setShowInLegend(true);
        conf.setPlotOptions(plotOptions);
        int pregunta1 = 0, pregunta2 = 0, pregunta3 = 0;
        double promedio1 = 0, promedio2 = 0, promedio3 = 0; String comentarios = " ";
        for (Encuesta encuesta :
                encuestaService.listarEncuestas()) {
            pregunta1 += encuesta.getCumplieronExpectativas();
            pregunta2 += encuesta.getDominioDelTema();
            pregunta3 += encuesta.getInstalacionesConfortables();
            comentarios = encuesta.getComentario();
        }
        if (encuestaService.listarEncuestas().size() > 0) {
            promedio1 = (double) (pregunta1 / encuestaService.listarEncuestas().size());
            promedio2 = (double) (pregunta2 / encuestaService.listarEncuestas().size());
            promedio3 = (double) (pregunta3 / encuestaService.listarEncuestas().size());
            comentarios = (comentarios + " "+ encuestaService.listarEncuestas().size());
        }

        DataSeries series = new DataSeries("Preguntas");
        DataSeriesItem chrome = new DataSeriesItem("Las charlas cumplieron sus expectativas?", promedio1);
        chrome.setSliced(true);
        chrome.setSelected(true);
        series.add(chrome);
        series.add(new DataSeriesItem("Los Expositores demostraron dominio del tema?", promedio2));
        series.add(new DataSeriesItem("Las instalaciones eran confortables:", promedio3));
        conf.setSeries(series);
        chart.setVisibilityTogglingDisabled(true);
        chart.setWidth("40%");
        chart.setHeight("40%");
        setAlignItems(Alignment.CENTER);


        //graficos de barras
        Chart barra = new Chart();
        Configuration configuration = barra.getConfiguration();
        configuration.setTitle("Suma total  preguntas");
        barra.getConfiguration().getChart().setType(ChartType.BAR);

        configuration.addSeries(new ListSeries("Pregunta 1", pregunta1));
        configuration.addSeries(new ListSeries("Pregunta 2", pregunta2));
        configuration.addSeries(new ListSeries("Pregunta 3", pregunta3));

        XAxis x = new XAxis();
        x.setCategories("Pregunta 1", "Pregunta 2", "Pregunta 3");
        configuration.addxAxis(x);

        YAxis y = new YAxis();
        y.setMin(0);
        AxisTitle yTitle = new AxisTitle();
        yTitle.setText("Puntuación");
        yTitle.setAlign(VerticalAlign.HIGH);
        y.setTitle(yTitle);
        configuration.addyAxis(y);

        Tooltip tooltip2 = new Tooltip();
        configuration.setTooltip(tooltip2);

        PlotOptionsBar plotOptions2 = new PlotOptionsBar();
        DataLabels dataLabels = new DataLabels();
        dataLabels.setEnabled(true);
        plotOptions2.setDataLabels(dataLabels);
        configuration.setPlotOptions(plotOptions2);

        //Tabla datos generados de las encuestas

        Grid<Encuesta> encuestasValores = new Grid<>();
        encuestasValores.setItems(encuestaService.listarEncuestas());
        encuestasValores.addColumn(Encuesta::getId).setHeader("ID");
        encuestasValores.addColumn(Encuesta::getCumplieronExpectativas).setHeader("Pregunta 1");
        encuestasValores.addColumn(Encuesta::getDominioDelTema).setHeader("Pregunta 2");
        encuestasValores.addColumn(Encuesta::getInstalacionesConfortables).setHeader("Pregunta 3");
        encuestasValores.addColumn(Encuesta::getComentario).setHeader("Comentario");
        encuestasValores.setSelectionMode(Grid.SelectionMode.SINGLE);
        /*
        Dialog dialog = new Dialog();
        encuestasValores.addItemClickListener(event -> {
            //dialog.removeAll();
            dialog.add(new H1("Comentario:"));
            dialog.add(new H3(event.getItem().getComentario()));
            dialog.open();
            dialog.close();
            dialog.remove();
        }); */

        Button irEncuesta = new Button("Crear encuesta");
        irEncuesta.setVisible(false);
        irEncuesta.addClickListener(event -> getUI().get().navigate("encuesta"));

        Button logout = new Button("Logout");
        logout.setIcon(VaadinIcon.SIGN_OUT.create());
        logout.addClickListener(event -> {
            usuarioService.borrarCookieRecordarme();
            getUI().get().navigate("");
        });

        irEncuesta.addClickListener(event -> getUI().get().navigate(" "));
        add(new H1("ESTADÍSTICAS GENERADAS"), new H3(" \n Encuestas respondidas:" + encuestaService.listarEncuestas().size()),irEncuesta, chart, barra, encuestasValores, logout);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Usuario usuario = usuarioService.encontrarUsuarioPorUsername(VaadinSession.getCurrent().getAttribute("usuario").toString());
        if (usuario == null)
            event.forwardTo("");
        else if (!usuario.isAdmin()) {
            event.forwardTo("encuesta");
        }
    }
}


