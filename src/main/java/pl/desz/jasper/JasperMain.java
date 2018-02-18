package pl.desz.jasper;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class JasperMain {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(JasperConfig.class);
        ctx.refresh();

        JasperUtil jasperUtil = ctx.getBean(JasperUtil.class);
        // JRXML file
        InputStream employeeReportStream
                = JasperMain.class.getClass().getResourceAsStream("/employeeReport.jrxml");
        // parameters
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", "Employee Report");
        parameters.put("minSalary", 15000.0);
        parameters.put("condition", " LAST_NAME ='Smith' ORDER BY FIRST_NAME");

        jasperUtil.setParameters(parameters);
        jasperUtil.setReport(employeeReportStream);

        JasperReport jr = jasperUtil.compile();
        JasperPrint jp = jasperUtil.fill(jr);

        jasperUtil.exportTo(jp, JasperUtil.FileType.PDF);
    }
}
