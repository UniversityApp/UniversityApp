package sk.branislavremen.universityapp.vo;

public class StudyProgrammeData {

	private String objectId;
	private String skratka;
	private String popis;
	private String fakulta;
	private String forma;
	private String stupen;
	private String druhStudia;
	
	public StudyProgrammeData(String objectId, String skratka, String popis,
			String fakulta, String forma, String stupen, String druhStudia) {
		super();
		this.objectId = objectId;
		this.skratka = skratka;
		this.popis = popis;
		this.fakulta = fakulta;
		this.forma = forma;
		this.stupen = stupen;
		this.druhStudia = druhStudia;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getSkratka() {
		return skratka;
	}

	public void setSkratka(String skratka) {
		this.skratka = skratka;
	}

	public String getPopis() {
		return popis;
	}

	public void setPopis(String popis) {
		this.popis = popis;
	}

	public String getFakulta() {
		return fakulta;
	}

	public void setFakulta(String fakulta) {
		this.fakulta = fakulta;
	}

	public String getForma() {
		return forma;
	}

	public void setForma(String forma) {
		this.forma = forma;
	}

	public String getStupen() {
		return stupen;
	}

	public void setStupen(String stupen) {
		this.stupen = stupen;
	}

	public String getDruhStudia() {
		return druhStudia;
	}

	public void setDruhStudia(String druhStudia) {
		this.druhStudia = druhStudia;
	}
	
	
	
	
}
