// Fig. 9.27: SurveyServlet.java
// A Web-based survey that uses JDBC from a servlet.

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.DecimalFormat;

public class SurveyServlet extends HttpServlet {
   private Connection connection;
   private PreparedStatement addEntry;

   // set up database connection and prepare SQL statements
   public void init( ServletConfig config ) 
      throws ServletException
   {
      // attempt database connection and create PreparedStatements
      try {
        // Connection connect;

            String driver = "com.mysql.jdbc.Driver";
            try {
               Class.forName(driver);
               String url ="jdbc:mysql://localhost:3306/database"; //Database name here
               connection = DriverManager.getConnection(url, "root", "Indantar_123");  //URL, user and password
            } catch (ClassNotFoundException e) {
               e.printStackTrace();
            } catch (SQLException e) {
               e.printStackTrace();
            }

         addEntry = connection.prepareStatement(
                 "INSERT INTO test ( firstName, " +
                         "lastName, IDNumber, emailAddress, phoneNumber, comments) " +
                         "VALUES ( ? , ? , ? , ? , ? , ?)");
      }
      
      // for any exception throw an UnavailableException to 
      // indicate that the servlet is not currently available
      catch ( Exception exception ) {
         exception.printStackTrace();
         throw new UnavailableException( exception.getMessage() );
      }

   }  // end of init method

   // process survey response
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

      String firstName;
      String lastName;
      int IDNumber;
      String emailAddress;
      int phoneNumber;
      String comments;
      // read current survey response
      firstName = request.getParameter("first_name");
      lastName = request.getParameter("last_name");
      IDNumber = Integer.parseInt(request.getParameter("id"));
      emailAddress = request.getParameter("email");
      phoneNumber = Integer.parseInt(request.getParameter("telephone"));
      comments = request.getParameter("comments");

      // attempt to process a vote and display current results
      try {
         int result;
         // update total for current survey response
         addEntry.setString( 1, firstName);
         addEntry.setString( 2, lastName);
         addEntry.setInt( 3, IDNumber);
         addEntry.setString( 4, emailAddress);
         addEntry.setInt( 5, phoneNumber);
         addEntry.setString( 6, comments);
         result = addEntry.executeUpdate();
         if(result  == 1)
            out.print("Entry Successful");
         else{
            out.print("Entry Unsuccessful");
            connection.rollback();
         }
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
   // close SQL statements and database when servlet terminates
   public void destroy()
   {
      // attempt to close statements and database connection
      try {
         addEntry.close();
         connection.close();
      }

      // handle database exceptions by returning error to client
      catch( SQLException sqlException ) {
         sqlException.printStackTrace();
      }
   }  // end of destroy method
}

/***************************************************************
 * (C) Copyright 2002 by Deitel & Associates, Inc. and         *
 * Prentice Hall. All Rights Reserved.                         *
 *                                                             *
 * DISCLAIMER: The authors and publisher of this book have     *
 * used their best efforts in preparing the book. These        *
 * efforts include the development, research, and testing of   *
 * the theories and programs to determine their effectiveness. *
 * The authors and publisher make no warranty of any kind,     *
 * expressed or implied, with regard to these programs or to   *
 * the documentation contained in these books. The authors     *
 * and publisher shall not be liable in any event for          *
 * incidental or consequential damages in connection with, or  *
 * arising out of, the furnishing, performance, or use of      *
 * these programs.                                             *
 ***************************************************************/
