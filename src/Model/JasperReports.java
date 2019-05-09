package Model;

import java.sql.Connection;
import java.sql.SQLException;

public class JasperReports implements IReports{
    Connection con;

    JasperReports() {
        try {
            con = ConnectionHandler.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        JasperReportBuilder
        JasperReportBuilder report = DynamicReports.report();
        Columns.column("Customer Id", "id", DataTypes.integerType());


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
