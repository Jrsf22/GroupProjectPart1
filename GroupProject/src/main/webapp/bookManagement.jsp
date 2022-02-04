<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<div class="row">
		<div class="container">
			<h3 class="text-center">List of Books</h3>
			<hr>
			<div class="container text-left">
				<!-- Add new Book button redirects to the ???.jsp page -->
		 <!-- Replace the register.jsp when the add new book jsp page is created --><a href="<%=request.getContextPath()%>/register.jsp"
					class="btn btn-success">Add New Book</a>
			</div>
			<br>
			<!-- Create a table to list out all current book information -->
			<table class="table">
				<thead>
					<tr>
						<th>Book Name</th>
						<th>Book Description</th>
						<th>Book Author</th>
						<th>Book Likes</th>
						<th>Actions</th>
					</tr>
				</thead>
				<!-- Pass in the list of books receive via the Servlets response to a loop-->
				<tbody>
					<c:forEach var="book" items="${listBooks}">
						<!-- For each book in the database, display their information accordingly -->
						<tr>
							<td><c:out value="${book.bookName}" /></td>
							<td><c:out value="${book.bookDesc}" /></td>
							<td><c:out value="${book.bookAuthor}" /></td>
							<td><c:out value="${book.bookLikes}" /></td>
							<!-- For each book in the database, Edit/Delete buttons which invokes the edit/delete functions -->
							<td><a href="edit?name=<c:out value='${book.bookName}'/>">Edit</a>
								&nbsp;&nbsp;&nbsp;&nbsp; <a
								href="delete?name=<c:out value='${book.bookName}' />">Delete</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>

</body>
</html>
