package dto;

public class DataTransferrer {

	private static GameDto info;
	
	public DataTransferrer(){}
	
	public void setInfo(GameDto input){
		info = input;
	}
	
	public static GameDto getInfo(){
		return info;
	}

}
