package technited.dota2central;


public class SlidingMenuItems {

	private String[] itemName = {"GENERAL OPTIONS","Dashboard","SETTINGS AND CUSTOMIZATIONS","Settings","SignOut"};
	private String[] itemImage = {"","dashboard_icon_selector","","settings_icon_selector","sign_out_icon_selector"};
	
	public String getItemName(int index)
	{
		return itemName[index];
	}
	
	public String getImageName(int index)
	{
		return itemImage[index];
	}
	
}
