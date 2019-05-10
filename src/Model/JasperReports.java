package Model;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.*;

public class JasperReports implements IReports{
    Connection con;


    JasperReports() throws JRException, SQLException, ClassNotFoundException {
          con = ConnectionHandler.getInstance().getConnection();
//        JasperReportBuilder
//        JasperReportBuilder report = DynamicReports.report();
//        Columns.column("Customer Id", "id", DataTypes.integerType());

        // Compile jrxml file.
        JasperReport jasperReport = JasperCompileManager
                .compileReport("../../../src/reports/Top10SellingBooks.jrxml");

        // Parameters for report
        Map<String, Object> parameters = new HashMap<String, Object>();

        JasperPrint print = JasperFillManager.fillReport(jasperReport,
                parameters, con);


        // Make sure the output directory exists.
        File outDir = new File("C:/jasperoutput");
        outDir.mkdirs();

        // PDF Exportor.
        JRPdfExporter exporter = new JRPdfExporter();

        ExporterInput exporterInput = new SimpleExporterInput(print);
        // ExporterInput
        exporter.setExporterInput(exporterInput);

        // ExporterOutput
        OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                "FirstJasperReport.pdf");
        // Output
        exporter.setExporterOutput(exporterOutput);

        //
        SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
        exporter.setConfiguration(configuration);
        exporter.exportReport();

        System.out.println("Done!");
    }

    @Override
    public void getTotalSales() {
        //TODO implement
    }

    @Override
    public void getTop5Customers() {
        //TODO implement
    }

    @Override
    public void getTop10SellingBooks() {
        //TODOD implement
    }
}
