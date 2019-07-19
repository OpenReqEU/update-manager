package it.eng.openreq.updatemanager.management;

public class TrainingResponse {
	
	private Integer toUpdate = 0;
	private String newModelPath = "";
	public Integer getToUpdate() {
		return toUpdate;
	}
	public void setToUpdate(Integer toUpdate) {
		this.toUpdate = toUpdate;
	}
	public String getNewModelPath() {
		return newModelPath;
	}
	public void setNewModelPath(String newModelPath) {
		this.newModelPath = newModelPath;
	}

}
