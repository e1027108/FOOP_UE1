package dto;

public class DataTransferrer {

	private static GameDto info;
	
	public DataTransferrer(){}
	
	public void setInfo(GameDto info){
		this.info = info;
	}
	
	public static GameDto getInfo(){
		return info;
	}

}
