package sk.branislavremen.universityapp.vo;

import java.util.Date;



public class PostData {
	private String postThumbUrl;
	private String postTitle;
	private Date postDate;
	
	public PostData(String postThumbUrl, String postTitle, Date postDate) {
		super();
		this.postThumbUrl = postThumbUrl;
		this.postTitle = postTitle;
		this.postDate = postDate;
	}

	public String getPostThumbUrl() {
		return postThumbUrl;
	}

	public void setPostThumbUrl(String postThumbUrl) {
		this.postThumbUrl = postThumbUrl;
	}

	public String getPostTitle() {
		return postTitle;
	}

	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}

	public Date getPostDate() {
		return postDate;
	}

	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}
	
	

}
