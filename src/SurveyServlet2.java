/**
 * Created by g00284823 on 08/12/2015.
 */
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.sql.Result;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class SurveyServlet2 extends HttpServlet {
    // set up database connection and prepare SQL statements
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set the MIME type for the response message
        response.setContentType("text/html");
        // Get a output writer to write the response message into the network socket
        PrintWriter out = response.getWriter();

        Connection conn = null;
        Statement stmt = null;


        try {
            String driver = "com.mysql.jdbc.Driver";
            try {
                Class.forName(driver);
                String url ="jdbc:mysql://localhost:3306/database"; //Database name here
                conn = DriverManager.getConnection(url, "root", "Indantar_123");  //URL, user and password
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Step 3: Execute a SQL SELECT query
            stmt = conn.createStatement();
            String sqlStr = "SELECT * FROM test";
            // Print an HTML page as output of query
            out.println("<html><head><title>Query Results</title></head><body>");
            out.println("<h2>Thank you for your query.</h2>");
            out.println("<p>You query is: " + sqlStr + "</p>"); // Echo for debugging
            /*********************************************/
            ResultSet rset = stmt.executeQuery(sqlStr); // Send the query to the server

            // Step 4: Process the query result
            int count = 0;
            while(rset.next()) {
                // Print a paragraph <p>...</p> for each row
                out.println("<p>" + rset.getString("firstName") + " , " + rset.getString("lastName") + " , " + rset.getInt("IDNumber") + " , " + rset.getString("emailAddress") + " , " + rset.getInt("phoneNumber") + " , " + rset.getString("comments") +"</p>");
                ++count;
            }
            out.println("<p>==== " + count + " records found ====</p>");
            out.println("</body></html>");
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally
        {
            out.close();
            try {
                // Step 5: Close the Statement and Connection
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    // process survey response
    /*
    protected void doPost( HttpServletRequest request,
                           HttpServletResponse response )
            throws ServletException, IOException
    {
        // set up response to client
        response.setContentType( "text/html" );
        PrintWriter out = response.getWriter();
        // start XHTML document
        out.println( "<?xml version = \"1.0\"?>" );
        out.println( "<!DOCTYPE html PUBLIC \"-//W3C//DTD " +
                "XHTML 1.0 Strict//EN\" \"http://www.w3.org" +
                "/TR/xhtml1/DTD/xhtml1-strict.dtd\">" );
        out.println(
                "<html xmlns = \"http://www.w3.org/1999/xhtml\">" );
        // head section of document
        out.println( "<head>" );
        // attempt to process a vote and display current results
        try {
            ResultSet res = getDatabase.executeQuery();
            //while (res.next()) {
                String firstName = res.getString(2);
                String lastName = res.getString(3);
                int IDNumber = res.getInt(4);
                String emailAddress = res.getString(5);
                int phoneNumber = res.getInt(6);
                String comments = res.getString(7);
            //}
            request.setAttribute(firstName,"first_name");
            request.setAttribute(lastName,"last_name");
            request.setAttribute(String.valueOf(IDNumber),"id");
            request.setAttribute(emailAddress,"email");
            request.setAttribute(String.valueOf(phoneNumber),"telephone");
            request.setAttribute(comments,"comments");
        }
        // if database exception occurs, return error page
        catch ( SQLException sqlException ) {
            sqlException.printStackTrace();
            out.println( "<title>Error</title>" );
            out.println( "</head>" );
            out.println( "<body><p>Database error occurred. " );
            out.println( "Try again later.</p></body></html>" );
            out.close();
        }

    }  // end of doPost method
    // close SQL statements and database when servlet terminates*/
}
