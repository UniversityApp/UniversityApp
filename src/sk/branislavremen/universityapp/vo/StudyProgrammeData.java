package sk.branislavremen.universityapp.vo;

public class StudyProgrammeData {
	
	String objectId;
	String shortName;
	String title;
	String faculty;
	String studyType; //G,H,J,...
	String studyKind; // bc, mgr, dok, ...
	String studyForm; // denna,externa
	String studyDegree; //1,2,3,N
	
	public StudyProgrammeData(String objectId, String shortName, String title,
			String faculty, String studyType, String studyKind,
			String studyForm, String studyDegree) {
	
		super();
		this.objectId = objectId;
		this.shortName = shortName;
		this.title = title;
		this.faculty = faculty;
		this.studyType = studyType;
		this.studyKind = studyKind;
		this.studyForm = studyForm;
		this.studyDegree = studyDegree;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFaculty() {
		return faculty;
	}

	public void setFaculty(String faculty) {
		this.faculty = faculty;
	}

	public String getStudyType() {
		return studyType;
	}

	public void setStudyType(String studyType) {
		this.studyType = studyType;
	}

	public String getStudyKind() {
		return studyKind;
	}

	public void setStudyKind(String studyKind) {
		this.studyKind = studyKind;
	}

	public String getStudyForm() {
		return studyForm;
	}

	public void setStudyForm(String studyForm) {
		this.studyForm = studyForm;
	}

	public String getStudyDegree() {
		return studyDegree;
	}

	public void setStudyDegree(String studyDegree) {
		this.studyDegree = studyDegree;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getTitle() + " - " + getStudyKind();
	}
	
	

}
