import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;

import java.awt.print.Book;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BookServlet
 * @param <Book>
 */
@WebServlet("/BookServlet")
public class BookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

// List of variables used for database connections
	private String jdbcURL = "jdbc:mysql://localhost:3306/booksdetails";
	private String jdbcUsername = "root";
	private String jdbcPassword = "password";

// List of SQL prepared statements to prepare CRUD operations to our database (add on as needed)
	// Create new book
	private static final String INSERT_BOOKS_SQL = "INSERT INTO booksdetails" +" (bookName, bookDesc, bookAuthor, bookLikes) VALUES " + " (?,?,?,?);";
	// Select book by name (used to show specific book)
	private static final String SELECT_BOOK_BY_ID = "SELECT bookName, bookDesc, bookAuthor, bookLikes from booksdetails where bookName = ?;";
	// Select all books by that contains search input(used for search)
	private static final String SELECT_BOOKS_BY_NAME = "SELECT * FROM booksdetails where bookName like %?%;";
	// Update book (including likes)
	private static final String UPDATE_BOOKS_SQL = "UPDATE booksdetails set bookName = ?, bookDesc = ?, bookAuthor = ?, bookLikes = ? where bookName = ?;";
	// Select All books
	private static final String SELECT_ALL_BOOKS = "select * from booksdetails ";
	// Delete specific book
	private static final String DELETE_BOOKS_SQL = "delete from booksdetails where name = ?;";
	//Sort by Name Ascending
	private static final String SELECT_ALL_BOOKS_ORDER_BY_bookName_ASC = "SELECT bookName, bookDesc, bookAuthor, bookLikes from booksdetails ORDER BY bookName ASC where bookName = ?;";
	//Sort By Name Descending
	private static final String SELECT_ALL_BOOKS_ORDER_BY_bookName_DESC = "SELECT bookName, bookDesc, bookAuthor, bookLikes from booksdetails ORDER BY bookName DESC where bookName = ?;";
	//Sort By Likes
	private static final String SELECT_ALL_BOOKS_ORDER_BY_bookLikes= "SELECT bookName, bookDesc, bookAuthor, bookLikes from booksdetails ORDER BY bookLikes DESC where bookName = ?;";


// getConnection for connection to SQL db via JDBC 
	protected Connection getConnection() {
		Connection connection = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return connection;
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
// Routing
	// *Insert switch case block here
	// Which will, depending on the request servlet path, determine the function to
	// invoke using the switch statements.
	String action = request.getServletPath();
	
	try
	{
		switch (action) {
		case "/BookServlet/dashboard":
			listBooks(request, response);
			break;
		case "/BookServlet/edit":
			showEditForm(request, response);
			break;
		case "/BookServlet/update":
			updateBook(request, response);
			break;
		case "/BookServlet/delete":
			deleteBook(request, response);
			break;
		case "/BookServlet/search":
			searchBooks(request, response);
			break;
		case "/BookServlet/Ascending":
			listBooksByNameAsc(request, response);
			break;
		case "/BookServlet/Descending":
			listBooksByNameDesc(request, response);
			break;
		}
	}
	catch(SQLException ex)
	{
		throw new ServletException(ex);
	}
	
	// TODO Auto-generated method stub

	response.getWriter().append("Served at: ").append(request.getContextPath());
}

// Edit Form method (to populate the values for the edit form)
	private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		
		
		// get parameter passed in the URL 
		String bookName = request.getParameter("bookName");
		
		Book existingBook = new Book("","","",1);
		
		try (
				Connection connection = getConnection(); 
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOK_BY_ID);){
			
			preparedStatement.setString(1, bookName);
			
			ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				bookName = rs.getString("bookName");
				String bookDesc = rs.getString("bookDesc");
				String bookAuthor = rs.getString("bookAuthor");
				int bookLikes = rs.getInt("bookLikes");
				existingBook = new Book(bookName, bookDesc, bookAuthor, bookLikes);
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		request.setAttribute("book", existingBook);
		request.getRequestDispatcher("/bookEdit.jsp").forward(request, response); // This line will depend on the name of the Book edit file name.
	}

// Update the book table based on the Edit Form data
	private void updateBook(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		// get value from form
		String oriName = request.getParameter("oriName"); // Original book name
		String bookName = request.getParameter("bookName"); // New book name
		String bookDesc = request.getParameter("bookDesc"); // New Description
		String bookAuthor = request.getParameter("bookAuthor"); // New Author
		String bookLikes = request.getParameter("bookLikes"); // New Likes
		
		// Connect to database
		try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_BOOKS_SQL);) {
			statement.setString(1, bookName);
			statement.setString(2, bookDesc);
			statement.setString(3, bookAuthor);
			statement.setString(4, bookLikes);
			
			int i = statement.executeUpdate();
		}
		response.sendRedirect("http://localhost:8090/BookServlet/dashboard"); // This part would be affected by the routing guy
	}

// Delete book from table according to book name
	private void deleteBook(HttpServletRequest request, HttpServletResponse response)
	throws SQLException, IOException{
		String bookName = request.getParameter("bookName");
		try (Connection connection = getConnection();
				
				PreparedStatement statement = connection.prepareStatement(DELETE_BOOKS_SQL);){
				
				statement.setString(1, bookName);
				
				int i = statement.executeUpdate();
	}
		response.sendRedirect("http://localhost:8090/BookServlet/dashboard");
	}

// List all books 
	private void listBooks(HttpServletRequest request, HttpServletResponse response)
	throws SQLException, IOException, ServletException
	{
		List <Book> books = new ArrayList <>();
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_ALL_BOOKS);) {
			
					ResultSet rs = statement.executeQuery();
					
					while (rs.next()) {
						String bookName = rs.getString("bookName");
						String bookDesc = rs.getString("bookDesc");
						String bookAuthor = rs.getString("bookAuthor");
						int bookLikes = rs.getInt("bookLikes");
						books.add (Book(bookName, bookDesc, bookAuthor, bookLikes));
					}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		request.setAttribute("listBooks", books);
		request.getRequestDispatcher("/bookManagement.jsp").forward(request, response);
	}
	
	//Sort By Name Ascending
	private void listBooksByNameAsc(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException
			{
				List <Book> books = new ArrayList <>();
				try (Connection connection = getConnection();
						PreparedStatement statement = connection.prepareStatement(SELECT_ALL_BOOKS_ORDER_BY_bookName_ASC);) {
					
							ResultSet rs = statement.executeQuery();
							
							while (rs.next()) {
								String bookName = rs.getString("bookName");
								String bookDesc = rs.getString("bookDesc");
								String bookAuthor = rs.getString("bookAuthor");
								int bookLikes = rs.getInt("bookLikes");
								books.add (Book(bookName, bookDesc, bookAuthor, bookLikes));
							}
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
				request.setAttribute("listBooks", books);
				request.getRequestDispatcher("/bookManagement.jsp").forward(request, response);
			}
	
	//Sort By Name Descending
	private void listBooksByNameDesc(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException
			{
				List <Book> books = new ArrayList <>();
				try (Connection connection = getConnection();
						PreparedStatement statement = connection.prepareStatement(SELECT_ALL_BOOKS_ORDER_BY_bookName_Desc);) {
					
							ResultSet rs = statement.executeQuery();
							
							while (rs.next()) {
								String bookName = rs.getString("bookName");
								String bookDesc = rs.getString("bookDesc");
								String bookAuthor = rs.getString("bookAuthor");
								int bookLikes = rs.getInt("bookLikes");
								books.add (Book(bookName, bookDesc, bookAuthor, bookLikes));
							}
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
				request.setAttribute("listBooks", books);
				request.getRequestDispatcher("/bookManagement.jsp").forward(request, response);
			}
	//Sort By likes
	private void listBooksByLikes(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException
			{
				List <Book> books = new ArrayList <>();
				try (Connection connection = getConnection();
						PreparedStatement statement = connection.prepareStatement(SELECT_ALL_BOOKS_ORDER_BY_bookLikes);) {
					
							ResultSet rs = statement.executeQuery();
							
							while (rs.next()) {
								String bookName = rs.getString("bookName");
								String bookDesc = rs.getString("bookDesc");
								String bookAuthor = rs.getString("bookAuthor");
								int bookLikes = rs.getInt("bookLikes");
								books.add (Book(bookName, bookDesc, bookAuthor, bookLikes));
							}
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
				request.setAttribute("listBooks", books);
				request.getRequestDispatcher("/bookManagement.jsp").forward(request, response);
			}


	
	
	
// Search for books
	private void searchBooks(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
		// Initialize array to store all books retrieved
		List<Book> books = new ArrayList<>(); 
		
		// Try/catch block to attempt connection and query database
		try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(SELECT_BOOKS_BY_NAME);) {
			Result rs = statement.executeQuery(); // Execute query
			
			// Assign the retrieved data to temporary variables to store incrementally into the books array.
			while (rs.next()) {
				String bookName = rs.getString("bookName");
				String bookDesc = rs.getString("bookDesc");
				String bookAuthor = rs.getString("bookAuthor");
				int bookLikes = rs.getInt("bookLikes");
				
				books.add(Book(bookName, bookDesc, bookAuthor, bookLikes));
			}
		}
		catch (SQLExeption e) {
			System.out.println(e.getMessage()); // Print error message
		}
		// Send the data back to the requesting .jsp
		request.setAttribute("searchBooks", books);
		request.getRequestDispatcher("/bookManagement.jsp").forward(request, response)''
	}
	

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BookServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Create new book
    	response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        

        // Book parameters from front-end form (params MUST match with form inputs name)
        String bn = request.getParameter("bookName"); // Book name
        String bd = request.getParameter("bookDesc"); // Book description
        String ba = request.getParameter("bookAuthor"); // Book author
        int bl = 0; // Initial book likes is 0.

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/booksdetails", "root", "password");

            PreparedStatement ps = con.prepareStatement("INSERT into booksdetails values(?,?,?,?)");

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