<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">

</head>
<!-- Form for creating new book -->
<body>

    <form action="NewBook" method="post">
        Book Name: <input type="text" name="bookName">
        Description: <input type="text" name="bookDesc">
        Author: <input type="text" name="bookAuthor">
     
        <input type="submit" value="Call Servlet">
    </form>

</body>
</html>