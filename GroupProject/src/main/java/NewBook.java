import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class NewBook
 */
@WebServlet("/NewBook")
public class NewBook extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewBook() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Create new book
        PrintWriter out = response.getWriter();

        // Book parameters from front-end form (params MUST match with form inputs name)
        String bn = request.getParameter("bookName"); // Book name
        String bd = request.getParameter("bookDesc"); // Book description
        String ba = request.getParameter("bookAuthor"); // Book author
        int bl = 0; // Initial book likes is 0.

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookdetails", "root", "password");

            PreparedStatement ps = con.prepareStatement("INSERT into USERDETAILS values(?,?,?,?)");

            ps.setString(1, bn); // Book name
            ps.setString(2, bd); // Book description
            ps.setString(3, ba); // Book author
            ps.setInt(4, bl); // Book likes

            int i = ps.executeUpdate();

            if (i > 0) {
                PrintWriter writer = response.getWriter();
                writer.println("<h1>" + "You have successfully added a book. 80085 are cool!!!" + "</h1>");
                writer.close();
            }
        } catch (Exception exception) {
            System.out.println(exception);
            out.close();
        }

        // TODO Auto-generated method stub
        doGet(request, response);
    }

}