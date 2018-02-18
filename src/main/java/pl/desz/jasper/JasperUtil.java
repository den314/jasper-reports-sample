package pl.desz.jasper;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Map;

@Component
public class JasperUtil {

    public enum FileType { PDF, XLSX, CSV, HTML }

    @Autowired
    private DataSource dataSource;
    private InputStream reportInputStream;
    private Map<String, Object> parameters;

    public void setReport(InputStream is) {
        reportInputStream = is;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public JasperReport compile() {
        JasperReport jasperReport;
        try {
            jasperReport = JasperCompileManager.compileReport(reportInputStream);
        } catch (JRException e) {
            e.printStackTrace();
            throw new RuntimeException("Report could not be compiled.");
        }
        return jasperReport;
    }

    public JasperPrint fill(JasperReport jr) {
        JasperPrint jasperPrint = null;
        try {
            jasperPrint = JasperFillManager.fillReport(jr, this.parameters, dataSource.getConnection());
        } catch (SQLException | JRException e) {
            e.printStackTrace();
        }
        return jasperPrint;
    }

    public void exportTo(JasperPrint jp, FileType fileType) {
        JRPdfExporter pdfExporter = new JRPdfExporter();
        pdfExporter.setExporterInput(new SimpleExporterInput(jp));
        pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput("employee.pdf"));
        try {
            pdfExporter.exportReport();
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
