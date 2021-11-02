package fiscultivofamiliar;

import interfaz.UI;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;

/**
 *
 * @author nclsc
 */
public class FISFamilyFarming {

    public String calcularFarming(int tamano_familia, double altura_planta, double capacidad_adquisitiva, double fertilidad_suelo) {
        // Carga el archivo de lenguaje de control difuso 'FCL'
        String fileName = "src/fiscultivoFamiliar/FISFamilyFarming.fcl";
        FIS fis = FIS.load(fileName, true);
        // En caso de error
        if (fis == null) {
            System.err.println("No se puede cargar el archivo: '" + fileName + "'");
            return "";
        }

        // Establecer las entradas del sistema
        fis.setVariable("tamano_familia", tamano_familia);
        fis.setVariable("altura_planta", altura_planta);
        fis.setVariable("capacidad_adquisitiva", capacidad_adquisitiva);
        fis.setVariable("fertilidad_suelo", fertilidad_suelo);

        // Inicia el funcionamiento del sistema
        fis.evaluate();

        // Muestra los gráficos de las variables de entrada y salida
        JFuzzyChart.get().chart(fis.getFunctionBlock("family_farming"));

        // Imprime el valor concreto de salida del sistema
        double cantidad_terreno = fis.getVariable("cantidad_terreno").getLatestDefuzzifiedValue();

        // Muestra cuanto peso tiene la variable de salida en cada CD de salida
        double pertenenciaPequeno = fis.getVariable("cantidad_terreno").getMembership("pequeno");
        double pertenenciaMediano = fis.getVariable("cantidad_terreno").getMembership("mediano");
        double pertenenciaGrande = fis.getVariable("cantidad_terreno").getMembership("grande");

        String recomendacion_terreno = "";

        if (pertenenciaPequeno >= pertenenciaMediano && pertenenciaPequeno >= pertenenciaGrande) {
            recomendacion_terreno = "pequeno";
        } else if (pertenenciaMediano >= pertenenciaPequeno && pertenenciaMediano >= pertenenciaGrande) {
            recomendacion_terreno = "mediano";
        } else if (pertenenciaGrande >= pertenenciaPequeno && pertenenciaGrande >= pertenenciaMediano) {
            recomendacion_terreno = "grande";
        }
        
          // Imprime el valor concreto de salida del sistema
        double rendimiento_cultivo = fis.getVariable("rendimiento_cultivo").getLatestDefuzzifiedValue();

        // Muestra cuanto peso tiene la variable de salida en cada CD de salida
        double pertenenciaBajoCultivo = fis.getVariable("rendimiento_cultivo").getMembership("bajo");
        double pertenenciaMedianoCultivo = fis.getVariable("rendimiento_cultivo").getMembership("mediano");
        double pertenenciaAltoCultivo = fis.getVariable("rendimiento_cultivo").getMembership("alto");

        String recomendacion_cultivo = "";
        
         if (pertenenciaBajoCultivo >= pertenenciaMedianoCultivo && pertenenciaBajoCultivo >= pertenenciaAltoCultivo) {
            recomendacion_cultivo = "bajo";
        } else if (pertenenciaMedianoCultivo >= pertenenciaBajoCultivo && pertenenciaMedianoCultivo >= pertenenciaAltoCultivo) {
            recomendacion_cultivo = "mediano";
        } else if (pertenenciaAltoCultivo >= pertenenciaMedianoCultivo && pertenenciaAltoCultivo >= pertenenciaMedianoCultivo) {
            recomendacion_cultivo = "alto";
        }

        // Muestra las reglas activadas y el valor de salida de cada una despues de aplicar las operaciones lógicas
        StringBuilder reglasUsadas = new StringBuilder();
        reglasUsadas.append("Reglas Usadas:\n");
        fis.getFunctionBlock("family_farming").getFuzzyRuleBlock("No1").getRules().stream().filter(r -> (r.getDegreeOfSupport() > 0)).forEachOrdered(r -> {
            reglasUsadas.append(r.toString()).append("\n");
        });

        return ("Cantidad terreno: " + String.format("%.1f", cantidad_terreno)
                + "\n\n" + "Se recomienda una cantidad de terreno " + recomendacion_terreno
                + "\n\n" + "Rendimiento Cultivo:  " + rendimiento_cultivo
                + "\n\n" + "Se recomienda un rendimiento de cultivo " + recomendacion_cultivo
                + "\n\n" + reglasUsadas.toString());

    }
}
