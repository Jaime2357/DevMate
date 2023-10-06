package application;

import javafx.scene.layout.HBox;

/**
 *  This class is for storing the mainBox reference and any other objects that are shared between all of the controllers.
 */
public class CommonObjs {
	
	// one instance of CommonObjs
	private static CommonObjs commonObjs = new CommonObjs();
	
	// for storing mainBox reference
	private HBox mainBox;
	
	private CommonObjs() {}
	
	/**
	 * Returns CommonObjs instance.
	 * @return instance of CommonObj
	 */
	public static CommonObjs getInstance() {
		return commonObjs;
	}

	/**
	 * Gets mainBox reference.
	 * @return mainBox reference
	 */
	public HBox getMainBox() {
		return mainBox;
	}

	/**
	 * Sets the reference for mainBox.
	 * @param mainBox - the reference for mainBox
	 */
	public void setMainBox(HBox mainBox) {
		this.mainBox = mainBox;
	}

	
	
}
