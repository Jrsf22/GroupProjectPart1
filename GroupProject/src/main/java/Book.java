/* Book attributes
 * bookName (Name of book)
 * bookDesc (Description of book)
 * bookAuthor (Author of book)
 * bookLikes (Number of likes)
 */


public class Book {
	protected String bookName;
	protected String bookDesc;
	protected String bookAuthor;
	protected int bookLikes;
	
	// Book constructor
	public Book(String bookName, String bookDesc, String bookAuthor, int bookLikes) {
		super();
		this.bookName = bookName;
		this.bookDesc = bookDesc;
		this.bookAuthor = bookAuthor;
		this.bookLikes = bookLikes;
	}
	
	//Book getters and setters
	// bookName
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	
	// bookDesc
	public String getBookDesc() {
		return bookDesc;
	}
	public void setBookDesc(String bookDesc) {
		this.bookDesc = bookDesc;
	}
	
	// bookAuthor
	public String getBookAuthor() {
		return bookAuthor;
	}
	public void setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
	}
	
	// bookLikes
	public int getBookLikes() {
		return bookLikes;
	}
	public void setBookLikes(int bookLikes) {
		this.bookLikes = booklikes;
	}
}

